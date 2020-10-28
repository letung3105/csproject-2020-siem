package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPUndeployException;
import com.espertech.esper.runtime.client.plugin.PluginLoader;
import com.espertech.esper.runtime.client.plugin.PluginLoaderInitContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyChangeListener;

/**
 * PluginLoader for added this example as part of an Esper configuration file and therefore execute it during startup.
 *
 * @author Vo Le Tung
 * @author Pham Nguyen Thanh An
 */
public class ARPAlertsPlugin implements PluginLoader {
    public static final String NETDEV_KEY = "netdev";
    public static final String RUNTIME_URI_KEY = "runtimeURI";
    public static final String ARP_CACHE_FLOOD_TIME_WINDOW_KEY = "arpCacheFloodTimeWindow";
    public static final String ARP_CACHE_FLOOD_ALERT_INTERVAL_KEY = "arpCacheFloodAlertInterval";
    public static final String ARP_CACHE_FLOOD_HIGH_PRIORITY_THRESHOLD_KEY = "arpCacheFloodHighPriorityThreshold";
    public static final String ARP_CACHE_FLOOD_CONSECUTIVE_ATTEMPTS_THRESHOLD_KEY = "arpCacheFloodConsecutiveAttemptsThreshold";
    public static final String ARP_GRATUITOUS_ANNOUNCEMENT_TIME_WINDOW_KEY = "arpGratuitousAnnouncementTimeWindow";
    public static final String ARP_GRATUITOUS_ANNOUNCEMENT_ALERT_INTERVAL_KEY = "arpGratuitousAnnouncementAlertInterval";
    public static final String ARP_GRATUITOUS_ANNOUNCEMENT_HIGH_PRIORITY_THRESHOLD_KEY = "arpGratuitousAnnouncementHighPriorityThreshold";
    public static final String ARP_GRATUITOUS_ANNOUNCEMENT_CONSECUTIVE_ATTEMPTS_THRESHOLD_KEY = "arpGratuitousAnnouncementConsecutiveAttemptsThreshold";
    public static final Logger log = LoggerFactory.getLogger(ARPAlertsPlugin.class);
    public static final String ARP_DUPLICATE_IP_ALERT_INTERVAL_KEY = "arpDuplicateIPAlertInterval";
    private String netdev;
    private String runtimeURI;
    private ARPAlertsMain main;
    private Thread portScansAlertThread;
    private ARPAlertsConfigurations configs;

    public void addStatementMetricListener(PropertyChangeListener listener) {
        if (main != null) {
            main.addStatementMetricListener(listener);
        }
    }

    public void init(PluginLoaderInitContext context) {
        runtimeURI = context.getProperties().getProperty(RUNTIME_URI_KEY, context.getRuntime().getURI());
        netdev = context.getProperties().getProperty(NETDEV_KEY, "lo");
        configs = new ARPAlertsConfigurations(
                new ARPAlertsConfigurations.ARPDuplicateIP(
                        Integer.parseInt(context.getProperties().getProperty(ARP_DUPLICATE_IP_ALERT_INTERVAL_KEY, "10"))
                ),
                new ARPAlertsConfigurations.ARPCacheFlood(
                        Integer.parseInt(context.getProperties().getProperty(ARP_CACHE_FLOOD_CONSECUTIVE_ATTEMPTS_THRESHOLD_KEY, "40")),
                        Integer.parseInt(context.getProperties().getProperty(ARP_CACHE_FLOOD_TIME_WINDOW_KEY, "3")),
                        Integer.parseInt(context.getProperties().getProperty(ARP_CACHE_FLOOD_ALERT_INTERVAL_KEY, "10")),
                        Integer.parseInt(context.getProperties().getProperty(ARP_CACHE_FLOOD_HIGH_PRIORITY_THRESHOLD_KEY, "30"))
                ),
                new ARPAlertsConfigurations.ARPGratuitousAnnouncement(
                        Integer.parseInt(context.getProperties().getProperty(ARP_GRATUITOUS_ANNOUNCEMENT_CONSECUTIVE_ATTEMPTS_THRESHOLD_KEY, "4")),
                        Integer.parseInt(context.getProperties().getProperty(ARP_GRATUITOUS_ANNOUNCEMENT_TIME_WINDOW_KEY, "10")),
                        Integer.parseInt(context.getProperties().getProperty(ARP_GRATUITOUS_ANNOUNCEMENT_ALERT_INTERVAL_KEY, "10")),
                        Integer.parseInt(context.getProperties().getProperty(ARP_GRATUITOUS_ANNOUNCEMENT_HIGH_PRIORITY_THRESHOLD_KEY, "3"))
                )
        );
    }

    /**
     * Start the daemon thread that continuously receives raw events and sends it to the system.
     * <p>
     * This function is called by Esper's engine.
     */
    public void postInitialize() {
        log.info("Starting ARPAlerts for runtime URI '" + runtimeURI + "'.");

        try {
            main = new ARPAlertsMain(netdev);
            main.deploy(configs);
            portScansAlertThread = new Thread(main, this.getClass().getName());
            portScansAlertThread.setDaemon(true);
            portScansAlertThread.start();
        } catch (Exception e) {
            log.error("Error starting ARPAlerts example: " + e.getMessage());
        }

        log.info("ARPAlerts started.");
    }

    /**
     * Deploy the EPL statements that are managed by the runtime that is associated with this plugin
     * using the given configurations.
     *
     * @param configs Deployment configurations
     */
    public void deploy(ARPAlertsConfigurations configs) {
        if (main != null) {
            main.deploy(configs);
        }
    }

    /**
     * Undeploy the EPL statements that are managed by the runtime that is associated with this plugin
     * using the given configurations
     */
    public void undeploy() throws EPUndeployException {
        if (main != null) {
            main.undeploy();
        }
    }

    /**
     * Undeploy all statements and stop the daemon.
     */
    public void destroy() {
        if (main != null) {
            try {
                EPRuntimeProvider.getDefaultRuntime().getDeploymentService().undeployAll();
            } catch (EPUndeployException e) {
                log.warn("Failed to undeploy: " + e.getMessage(), e);
            }
        }
        try {
            portScansAlertThread.interrupt();
            portScansAlertThread.join();
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
        }
        main = null;
        log.info("ARPAlerts stopped.");
    }
}
