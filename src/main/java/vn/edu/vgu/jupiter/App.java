package vn.edu.vgu.jupiter;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import vn.edu.vgu.jupiter.dashboard.Dashboard;

import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        Properties props = new Properties();
        props.put("runtimeURI", "PortScansAlertPlugin");
        props.put("netdev", "lo0");
        config.getRuntime().addPluginLoader("PortScansAlertPlugin", "vn.edu.vgu.jupiter.scan_alerts.PortScansAlertPlugin", props);

        EPRuntime runtime = EPRuntimeProvider.getRuntime("PortScansAlertPlugin", config);

        //Run Dashboard
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(Dashboard.class);
            }
        }.start();
        Dashboard dashboard = Dashboard.waitAndGetDashboard();

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
