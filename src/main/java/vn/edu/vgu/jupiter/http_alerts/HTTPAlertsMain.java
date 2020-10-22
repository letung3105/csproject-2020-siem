package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import vn.edu.vgu.jupiter.eventbean_http.HTTPLog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPAlertsMain implements Runnable {

    private HTTPAlertsConfigurations defaultConfigs;
    private EPRuntime runtime;
    private HTTPFailedLoginEventStatement httpFailedLoginEventStmt;
    private FailedLoginAlertStatement failedLoginAlertStmt;
    private FailedLoginFromSameIPAlertStatement failedLoginFromSameIPAlertStmt;
    private FailedLoginSameUserIDAlertStatement failedLoginSameUserIDAlertStmt;

    public HTTPAlertsMain(HTTPAlertsConfigurations defaultConfigs) {
        this.defaultConfigs = defaultConfigs;
        this.runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), CEPSetupUtil.getConfiguration());
    }

    public static void main(String[] args) {
        HTTPAlertsConfigurations httpAlertConfig = new HTTPAlertsConfigurations(
                new HTTPAlertsConfigurations.FailedLogin(15, 6, 3, 20),
                new HTTPAlertsConfigurations.FailedLoginFromSameIP(12, 2, 1, 15),
                new HTTPAlertsConfigurations.FailedLoginSameUserID(3, 2, 1, 5));
        new HTTPAlertsMain(httpAlertConfig).run();
    }

    /**
     * A httpd access
     * log parser for linux systems
     *
     * @return ArrayList<httpLogEvent> list of events parsed from the log
     * @author Bui Xuan Phuoc
     */
    public static ArrayList<HTTPLog> getEventsFromApacheHTTPDLogs() throws IOException {
        // TODO: more efficient way to read the log?
        BufferedReader reader = new BufferedReader(new FileReader("/var/log/apache2/access.log"));
        String line = null;
        ArrayList<HTTPLog> result = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            Pattern p = Pattern.compile("\"([^\"]*)\"");

            Matcher m = p.matcher(line);
            while (m.find()) {
                line = line.replace(m.group(1), m.group(1).replace(" ", ""));
            }


            ArrayList<String> lineComponents = new ArrayList<String>(Arrays.asList(line.split(" ")));
            while (lineComponents.size() > 10) {
                lineComponents.set(3, lineComponents.get(3) + lineComponents.get(4));
                lineComponents.remove(4);
            }
            // System.out.println(lineComponents + " " + lineComponents.size());
            result.add(new HTTPLog(lineComponents));
        }
        // System.out.println(result.size());
        return result;
    }

    /**
     * A while loop is run to check if there are any new entries from the log file.
     * If new entries are found, they are turned into httpLogEvent and send to the CEP machine
     */
    public void run() {
        int recordedNumberOfLogEntries = 0;
        while (true) {
            ArrayList<HTTPLog> httpLogs = null;
            try {
                httpLogs = getEventsFromApacheHTTPDLogs();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int currentNumberOfLogEntries = httpLogs.size();
            if (recordedNumberOfLogEntries < currentNumberOfLogEntries) {
                for (int i = recordedNumberOfLogEntries; i < currentNumberOfLogEntries; i++) {
                    runtime.getEventService().sendEventBean(httpLogs.get(i), "HTTPLog");
                }
                recordedNumberOfLogEntries = currentNumberOfLogEntries;
            }
        }
    }

    public void deploy(HTTPAlertsConfigurations configs) {
        httpFailedLoginEventStmt = new HTTPFailedLoginEventStatement(runtime);
        failedLoginAlertStmt = new FailedLoginAlertStatement(
                runtime,
                configs.getFailedLogin().getConsecutiveAttemptsThreshold(),
                configs.getFailedLogin().getTimeWindow(),
                configs.getFailedLogin().getAlertInterval(),
                configs.getFailedLogin().getHighPriorityThreshold());
        failedLoginFromSameIPAlertStmt = new FailedLoginFromSameIPAlertStatement(runtime,
                configs.getFailedLoginFromSameIP().getConsecutiveAttemptsThreshold(),
                configs.getFailedLoginFromSameIP().getTimeWindow(),
                configs.getFailedLoginFromSameIP().getAlertInterval(),
                configs.getFailedLoginFromSameIP().getHighPriorityThreshold());
        failedLoginSameUserIDAlertStmt = new FailedLoginSameUserIDAlertStatement(runtime,
                configs.getFailedLoginSameUserID().getConsecutiveAttemptsThreshold(),
                configs.getFailedLoginSameUserID().getTimeWindow(),
                configs.getFailedLoginSameUserID().getAlertInterval(),
                configs.getFailedLoginSameUserID().getHighPriorityThreshold());
    }
}
