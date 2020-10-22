package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.runtime.client.EPRuntimeProvider;
import com.espertech.esper.runtime.client.EPUndeployException;
import com.espertech.esper.runtime.client.plugin.PluginLoader;
import com.espertech.esper.runtime.client.plugin.PluginLoaderInitContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.edu.vgu.jupiter.http_alerts.HTTPAlertsConfigurations;

/**
 * PluginLoader for added this example as part of an Esper configuration file and therefore execute it during startup.
 *
 * @author Vo Le Tung
 * @author Pham Nguyen Thanh An
 */
public class PortScansAlertPlugin implements PluginLoader {
    private static final Logger log = LoggerFactory.getLogger(PortScansAlertPlugin.class);
    private static final String NETDEV_KEY = "netdev";
    private static final String RUNTIME_URI_KEY = "runtimeURI";

    private String netdev;
    private String runtimeURI;
    private PortScansAlertMain main;
    private Thread portScansAlertThread;
    private PortScansAlertConfigurations configs;

    public void init(PluginLoaderInitContext context) {
        if (context.getProperties().getProperty(RUNTIME_URI_KEY) != null) {
            runtimeURI = context.getProperties().getProperty(RUNTIME_URI_KEY);
        } else {
            runtimeURI = context.getRuntime().getURI();
        }

        if (context.getProperties().getProperty(NETDEV_KEY) != null) {
            netdev = context.getProperties().getProperty(NETDEV_KEY);
        } else {
            netdev = context.getRuntime().getURI();
        }

        configs = new PortScansAlertConfigurations(
                new PortScansAlertConfigurations.VerticalScan(60, 10, 100, 60),
                new PortScansAlertConfigurations.HorizontalScan(60, 10, 100, 60),
                new PortScansAlertConfigurations.BlockScan(60, 10, 5, 50, 2));
    }

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

    public void deploy(PortScansAlertConfigurations configs) {
        main.deploy(configs);
    }

    public void undeploy() throws EPUndeployException {
        if (main != null) {
            main.undeploy();
        }
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
            portScansAlertThread.interrupt();
            portScansAlertThread.join();
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
        }
        main = null;
        log.info("PortScansAlert stopped.");
    }
}
