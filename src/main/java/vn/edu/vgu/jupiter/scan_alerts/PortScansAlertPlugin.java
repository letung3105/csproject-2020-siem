package vn.edu.vgu.jupiter.scan_alerts;

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
 * @author Pham Nguyen Thanh An
 */
public class PortScansAlertPlugin implements PluginLoader {
    public static final String NETDEV_KEY = "netdev";
    public static final String RUNTIME_URI_KEY = "runtimeURI";
    public static final String VERTICAL_TIME_WINDOW_KEY = "verticalTimeWindow";
    public static final String VERTICAL_ALERT_INTERVAL_KEY = "verticalAlertInterval";
    public static final String VERTICAL_HIGH_PRIORITY_THRESHOLD_KEY = "verticalHighPriorityThreshold";
    public static final String VERTICAL_CONNECTION_COUNT_THRESHOLD_KEY = "verticalConnectionCountThreshold";
    public static final String HORIZONTAL_TIME_WINDOW_KEY = "horizontalTimeWindow";
    public static final String HORIZONTAL_ALERT_INTERVAL_KEY = "horizontalAlertInterval";
    public static final String HORIZONTAL_HIGH_PRIORITY_THRESHOLD_KEY = "horizontalHighPriorityThreshold";
    public static final String HORIZONTAL_CONNECTION_COUNT_THRESHOLD_KEY = "horizontalConnectionCountThreshold";
    public static final String BLOCK_TIME_WINDOW_KEY = "blockTimeWindow";
    public static final String BLOCK_ALERT_INTERVAL_KEY = "blockAlertInterval";
    public static final String BLOCK_HIGH_PRIORITY_THRESHOLD_KEY = "blockHighPriorityThreshold";
    public static final String BLOCK_PORTS_COUNT_THRESHOLD_KEY = "blockPortsCountThreshold";
    public static final String BLOCK_ADDRESSES_COUNT_THRESHOLD_KEY = "blockAddressesCountThreshold";
    private static final Logger log = LoggerFactory.getLogger(PortScansAlertPlugin.class);
    private String netdev;
    private String runtimeURI;
    private PortScansAlertMain main;
    private Thread portScansAlertThread;
    private PortScansAlertConfigurations configs;

    /**
     * Set the default configurations that is used when the runtime first started.
     * <p>
     * This function is called by Esper's engine.
     *
     * @param context The context given by Esper
     */
    public void init(PluginLoaderInitContext context) {
        runtimeURI = context.getProperties().getProperty(RUNTIME_URI_KEY, context.getRuntime().getURI());
        netdev = context.getProperties().getProperty(NETDEV_KEY, "lo");
        configs = new PortScansAlertConfigurations(
                new PortScansAlertConfigurations.VerticalScan(
                        Integer.parseInt(context.getProperties().getProperty(VERTICAL_TIME_WINDOW_KEY, "60")),
                        Integer.parseInt(context.getProperties().getProperty(VERTICAL_ALERT_INTERVAL_KEY, "10")),
                        Integer.parseInt(context.getProperties().getProperty(VERTICAL_HIGH_PRIORITY_THRESHOLD_KEY, "100")),
                        Integer.parseInt(context.getProperties().getProperty(VERTICAL_CONNECTION_COUNT_THRESHOLD_KEY, "60"))
                ),
                new PortScansAlertConfigurations.HorizontalScan(
                        Integer.parseInt(context.getProperties().getProperty(HORIZONTAL_TIME_WINDOW_KEY, "60")),
                        Integer.parseInt(context.getProperties().getProperty(HORIZONTAL_ALERT_INTERVAL_KEY, "10")),
                        Integer.parseInt(context.getProperties().getProperty(HORIZONTAL_HIGH_PRIORITY_THRESHOLD_KEY, "100")),
                        Integer.parseInt(context.getProperties().getProperty(HORIZONTAL_CONNECTION_COUNT_THRESHOLD_KEY, "60"))
                ),
                new PortScansAlertConfigurations.BlockScan(
                        Integer.parseInt(context.getProperties().getProperty(BLOCK_TIME_WINDOW_KEY, "60")),
                        Integer.parseInt(context.getProperties().getProperty(BLOCK_ALERT_INTERVAL_KEY, "10")),
                        Integer.parseInt(context.getProperties().getProperty(BLOCK_HIGH_PRIORITY_THRESHOLD_KEY, "5")),
                        Integer.parseInt(context.getProperties().getProperty(BLOCK_PORTS_COUNT_THRESHOLD_KEY, "50")),
                        Integer.parseInt(context.getProperties().getProperty(BLOCK_ADDRESSES_COUNT_THRESHOLD_KEY, "2"))
                )
        );
    }

    /**
     * Start the daemon thread that continuously receives raw events and sends it to the system.
     * <p>
     * This function is called by Esper's engine.
     */
    public void postInitialize() {
        log.info("Starting PortScansAlert for runtime URI '" + runtimeURI + "'.");

        try {
            main = new PortScansAlertMain(netdev);
            main.deploy(configs);
            portScansAlertThread = new Thread(main, this.getClass().getName());
            portScansAlertThread.setDaemon(true);
            portScansAlertThread.start();
        } catch (Exception e) {
            log.error("Error starting PortScansAlert example: " + e.getMessage());
        }

        log.info("PortScansAlert started.");
    }

    /**
     * Deploy the EPL statements that are managed by the runtime that is associated with this plugin
     * using the given configurations.
     *
     * @param configs
     */
    public void deploy(PortScansAlertConfigurations configs) {
        main.deploy(configs);
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
        log.info("PortScansAlert stopped.");
    }
}
