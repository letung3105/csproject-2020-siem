import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main implements Runnable {

    public static void main(String args[]) {
        new Main().run();
    }

    public void run() {
        Configuration configuration = ConsecutiveFailedLoginsUtil.getConfiguration();
        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

        new ConsecutiveFailedAlertStatement(runtime, 1, 5);
        new httpFailedLoginStatement(runtime);

        int recordedNumberOfLogEntries = 0;
        while (true) {
            ArrayList<httpLogEvent> httpLogEvents = null;
            try {
                httpLogEvents = getEventsFromApacheHTTPDLogs();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int currentNumberOfLogEntries = httpLogEvents.size();
            if (recordedNumberOfLogEntries < currentNumberOfLogEntries) {
                for (int i = recordedNumberOfLogEntries; i < currentNumberOfLogEntries; i++) {
                    runtime.getEventService().sendEventBean(httpLogEvents.get(i), "httpLogEvent");
                }
                recordedNumberOfLogEntries = currentNumberOfLogEntries;
            }
        }
    }

    public static ArrayList<httpLogEvent> getEventsFromApacheHTTPDLogs() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/var/log/apache2/access.log"));
        String line = null;
        ArrayList<httpLogEvent> result = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            Pattern p = Pattern.compile("\"([^\"]*)\"");

            Matcher m = p.matcher(line);
            while (m.find()) {
                line = line.replace(m.group(1), m.group(1).replace(" ", ""));
            }

            ArrayList<String> lineComponents = new ArrayList<String>(Arrays.asList(line.split(" ")));
//            System.out.println(lineComponents + " " + lineComponents.size());
            result.add(new httpLogEvent(lineComponents));
        }
//        System.out.println(result.size());
        return result;
    }
}
