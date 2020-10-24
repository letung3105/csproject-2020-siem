package vn.edu.vgu.jupiter;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;

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

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
