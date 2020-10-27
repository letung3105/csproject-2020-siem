package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.metric.StatementMetric;
import com.espertech.esper.runtime.client.*;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HTTPAlertsMain implements Runnable {
    private static class MetricListener implements UpdateListener {
        private Map<String, Long> eventsCumulativeCount;
        private Set<PropertyChangeListener> propertyChangeListenerSet;

        public MetricListener() {
            this.propertyChangeListenerSet = new HashSet<>();
            this.eventsCumulativeCount = new HashMap<>();
            this.eventsCumulativeCount.put("HTTPFailedLogin", 0L);
            this.eventsCumulativeCount.put("ConsecutiveFailedLoginsAlert", 0L);
            this.eventsCumulativeCount.put("ConsecutiveFailedLoginsFromSameIPAlert", 0L);
            this.eventsCumulativeCount.put("ConsecutiveFailedLoginsSameUserIDAlert", 0L);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeListenerSet.add(listener);
        }

        @Override
        public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
            if (newEvents == null) {
                return; // ignore old events for events leaving the window
            }

            StatementMetric metric = (StatementMetric) newEvents[0].getUnderlying();
            if (eventsCumulativeCount.containsKey(metric.getStatementName())) {
                Long oldCount = eventsCumulativeCount.get(metric.getStatementName());
                Long newCount = oldCount + metric.getNumOutputIStream();
                if (!newCount.equals(oldCount)) {
                    eventsCumulativeCount.put(metric.getStatementName(), newCount);
                    for (PropertyChangeListener l : propertyChangeListenerSet) {
                        l.propertyChange(new PropertyChangeEvent(this.getClass(), metric.getStatementName(), oldCount, newCount));
                    }
                }
            }
        }
    }

    private EPRuntime runtime;
    private MetricListener metricListener;
    private HTTPFailedLoginStatement httpFailedLoginEventStmt;
    private ConsecutiveFailedLoginsAlertStatement failedLoginAlertStmt;
    private ConsecutiveFailedLoginsFromSameIPAlertStatement failedLoginFromSameIPAlertStmt;
    private ConsecutiveFailedLoginsSameUserIDAlertStatement failedLoginSameUserIDAlertStmt;
    private String logPath;

    public HTTPAlertsMain(String logPath) {
        this.runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), CEPSetupUtil.getConfiguration());
        this.metricListener = new MetricListener();
        CEPSetupUtil
                .compileDeploy(
                        "select * from com.espertech.esper.common.client.metric.StatementMetric",
                        runtime
                )
                .addListener(metricListener);
        this.logPath = logPath;
    }

    public static void main(String[] args) {
        HTTPAlertsConfigurations httpAlertConfig = new HTTPAlertsConfigurations(
                new HTTPAlertsConfigurations.FailedLogin(15, 6, 3, 20),
                new HTTPAlertsConfigurations.FailedLoginFromSameIP(12, 2, 1, 15),
                new HTTPAlertsConfigurations.FailedLoginSameUserID(3, 2, 1, 5));

        HTTPAlertsMain httpAlertsMain = new HTTPAlertsMain("/private/var/log/apache2/access.log");
        httpAlertsMain.deploy(httpAlertConfig);
        httpAlertsMain.run();
    }

    /**
     * A while loop is run to check if there are any new entries from the log file.
     * If new entries are found, they are turned into httpLogEvent and send to the CEP machine
     */
    public void run() {
        File apacheAccessLogFile = new File(logPath);
        TailerListener listener = new HTTPDLogTailer(runtime);
        Tailer tailer = Tailer.create(apacheAccessLogFile, listener, 100, true);
        tailer.run();
    }

    /**
     * Deploy all the modules related to HTTP alerts with the given configurations
     *
     * @param configs configurations for http alerts
     * @author Vo Le Tung
     */
    public void deploy(HTTPAlertsConfigurations configs) {
        httpFailedLoginEventStmt = new HTTPFailedLoginStatement(runtime);
        failedLoginAlertStmt = new ConsecutiveFailedLoginsAlertStatement(
                runtime,
                configs.getFailedLogin().getConsecutiveAttemptsThreshold(),
                configs.getFailedLogin().getTimeWindow(),
                configs.getFailedLogin().getAlertInterval(),
                configs.getFailedLogin().getHighPriorityThreshold());
        failedLoginFromSameIPAlertStmt = new ConsecutiveFailedLoginsFromSameIPAlertStatement(runtime,
                configs.getFailedLoginFromSameIP().getConsecutiveAttemptsThreshold(),
                configs.getFailedLoginFromSameIP().getTimeWindow(),
                configs.getFailedLoginFromSameIP().getAlertInterval(),
                configs.getFailedLoginFromSameIP().getHighPriorityThreshold());
        failedLoginSameUserIDAlertStmt = new ConsecutiveFailedLoginsSameUserIDAlertStatement(runtime,
                configs.getFailedLoginSameUserID().getConsecutiveAttemptsThreshold(),
                configs.getFailedLoginSameUserID().getTimeWindow(),
                configs.getFailedLoginSameUserID().getAlertInterval(),
                configs.getFailedLoginSameUserID().getHighPriorityThreshold());
    }

    /**
     * Undeploy all the modules related to HTTP service alerts.
     *
     * @author Vo Le Tung
     */
    public void undeploy() throws EPUndeployException {
        if (httpFailedLoginEventStmt != null) {
            httpFailedLoginEventStmt.undeploy();
            httpFailedLoginEventStmt = null;
        }
        if (failedLoginAlertStmt != null) {
            failedLoginAlertStmt.undeploy();
            failedLoginAlertStmt = null;
        }
        if (failedLoginFromSameIPAlertStmt != null) {
            failedLoginFromSameIPAlertStmt.undeploy();
            failedLoginFromSameIPAlertStmt = null;
        }
        if (failedLoginSameUserIDAlertStmt != null) {
            failedLoginSameUserIDAlertStmt.undeploy();
            failedLoginSameUserIDAlertStmt = null;
        }
    }

    public void addStatementMetricListener(PropertyChangeListener listener) {
        this.metricListener.addPropertyChangeListener(listener);
    }
}
