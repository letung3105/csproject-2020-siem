package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPUndeployException;
import com.espertech.esper.runtime.client.plugin.PluginLoader;
import com.espertech.esper.runtime.client.plugin.PluginLoaderInitContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PluginLoader for added this example as part of an Esper configuration file and therefore execute it during startup.
 *
 * @author Vo Le Tung
 */
public class HTTPAlertsPlugin implements PluginLoader {
    public static final String RUNTIME_URI_KEY = "runtimeURI";
    public static final String LOG_PATH_KEY = "logPath";

    public static final String FAILED_LOGIN_ATTEMPTS_THRESHOLD_KEY = "failedLoginAttemptsThreshold";
    public static final String FAILED_LOGIN_TIME_WINDOW_KEY = "failedLoginTimeWindow";
    public static final String FAILED_LOGIN_ALERT_INTERVAL_KEY = "failedLoginAlertInterval";
    public static final String FAILED_LOGIN_HIGH_PRIORITY_THRESHOLD_KEY = "failedLoginHighPriorityThreshold";

    public static final String SAME_IP_FAILED_LOGIN_ATTEMPTS_THRESHOLD_KEY = "sameIpFailedLoginAttemptsThreshold";
    public static final String SAME_IP_FAILED_LOGIN_TIME_WINDOW_KEY = "sameIpFailedLoginTimeWindow";
    public static final String SAME_IP_FAILED_LOGIN_ALERT_INTERVAL_KEY = "sameIpFailedLoginAlertInterval";
    public static final String SAME_IP_FAILED_LOGIN_HIGH_PRIORITY_THRESHOLD_KEY = "sameIpFailedLoginHighPriorityThreshold";

    public static final String SAME_USER_FAILED_LOGIN_ATTEMPTS_THRESHOLD_KEY = "sameUserFailedLoginAttemptsThreshold";
    public static final String SAME_USER_FAILED_LOGIN_TIME_WINDOW_KEY = "sameUserFailedLoginTimeWindow";
    public static final String SAME_USER_FAILED_LOGIN_ALERT_INTERVAL_KEY = "sameUserFailedLoginAlertInterval";
    public static final String SAME_USER_FAILED_LOGIN_HIGH_PRIORITY_THRESHOLD_KEY = "sameUserFailedLoginHighPriorityThreshold";

    private static final Logger log = LoggerFactory.getLogger(HTTPAlertsPlugin.class);

    private String runtimeURI;
    private String logPath;
    private Thread httpAlertsThread;
    private HTTPAlertsMain main;
    private HTTPAlertsConfigurations configs;

    public void init(PluginLoaderInitContext context) {
        logPath = context.getProperties().getProperty(LOG_PATH_KEY, "/var/log/apache2/access.log");
        runtimeURI = context.getProperties().getProperty(RUNTIME_URI_KEY, context.getRuntime().getURI());
        configs = new HTTPAlertsConfigurations(
                new HTTPAlertsConfigurations.FailedLogin(
                        Integer.parseInt(context.getProperties().getProperty(FAILED_LOGIN_ATTEMPTS_THRESHOLD_KEY, "15")),
                        Integer.parseInt(context.getProperties().getProperty(FAILED_LOGIN_TIME_WINDOW_KEY, "6")),
                        Integer.parseInt(context.getProperties().getProperty(FAILED_LOGIN_ALERT_INTERVAL_KEY, "3")),
                        Integer.parseInt(context.getProperties().getProperty(FAILED_LOGIN_HIGH_PRIORITY_THRESHOLD_KEY, "20"))
                ),
                new HTTPAlertsConfigurations.FailedLoginFromSameIP(
                        Integer.parseInt(context.getProperties().getProperty(SAME_IP_FAILED_LOGIN_ATTEMPTS_THRESHOLD_KEY, "12")),
                        Integer.parseInt(context.getProperties().getProperty(SAME_IP_FAILED_LOGIN_TIME_WINDOW_KEY, "2")),
                        Integer.parseInt(context.getProperties().getProperty(SAME_IP_FAILED_LOGIN_ALERT_INTERVAL_KEY, "1")),
                        Integer.parseInt(context.getProperties().getProperty(SAME_IP_FAILED_LOGIN_HIGH_PRIORITY_THRESHOLD_KEY, "15"))
                ),
                new HTTPAlertsConfigurations.FailedLoginSameUserID(
                        Integer.parseInt(context.getProperties().getProperty(SAME_USER_FAILED_LOGIN_ATTEMPTS_THRESHOLD_KEY, "3")),
                        Integer.parseInt(context.getProperties().getProperty(SAME_USER_FAILED_LOGIN_TIME_WINDOW_KEY, "2")),
                        Integer.parseInt(context.getProperties().getProperty(SAME_USER_FAILED_LOGIN_ALERT_INTERVAL_KEY, "1")),
                        Integer.parseInt(context.getProperties().getProperty(SAME_USER_FAILED_LOGIN_HIGH_PRIORITY_THRESHOLD_KEY, "5"))
                )
        );
    }

    public void postInitialize() {
        log.info("Starting HTTPAlerts for runtime URI '" + runtimeURI + "'.");

        try {
            main = new HTTPAlertsMain(logPath);
            main.deploy(configs);
            httpAlertsThread = new Thread(main, this.getClass().getName());
            httpAlertsThread.setDaemon(true);
            httpAlertsThread.start();
        } catch (Exception e) {
            log.error("Error starting HTTPAlerts example: " + e.getMessage());
        }

        log.info("HTTPAlerts started.");
    }

    public void deploy(HTTPAlertsConfigurations configs) {
        main.deploy(configs);
    }

    public void destroy() {
        if (main != null) {
            try {
                EPRuntimeProvider.getDefaultRuntime().getDeploymentService().undeployAll();
            } catch (EPUndeployException e) {
                log.warn("Failed to undeploy: " + e.getMessage(), e);
            }
        }
        try {
            httpAlertsThread.interrupt();
            httpAlertsThread.join();
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
        }
        main = null;
        log.info("HTTPAlerts stopped.");
    }
}
