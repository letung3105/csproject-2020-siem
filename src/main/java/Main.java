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
        Configuration configuration = UnauthorizedLogUtil.getConfiguration();
        new Main().run();
    }

    public void run() {
        Configuration configuration = UnauthorizedLogUtil.getConfiguration();
        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

        new UnauthorizedAlertStatement(runtime, 5, 5);
        new UnauthorizedStatement(runtime);

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
                for (int i = currentNumberOfLogEntries; i < currentNumberOfLogEntries; i++) {
                    runtime.getEventService().sendEventBean(httpLogEvents.get(i), "httpLogEvent");
                }
                recordedNumberOfLogEntries = currentNumberOfLogEntries;
            }
        }
    }

    public static ArrayList<httpLogEvent> getEventsFromApacheHTTPDLogs() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("/var/log/apache2/access.log.1"));
        String line = null;
        ArrayList<httpLogEvent> result = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            Pattern p = Pattern.compile("\"([^\"]*)\"");

            Matcher m = p.matcher(line);
            while (m.find()) {
                line = line.replace(m.group(1), m.group(1).replace(" ", ""));
            }

            ArrayList<String> lineComponents = new ArrayList<String>(Arrays.asList(line.split(" ")));
            System.out.println(lineComponents + " " + lineComponents.size());
            result.add(new httpLogEvent(lineComponents));
        }
        return result;
    }
}