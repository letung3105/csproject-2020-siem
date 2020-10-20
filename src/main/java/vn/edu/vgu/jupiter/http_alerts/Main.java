package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import vn.edu.vgu.jupiter.eventbean_http.httpLogEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main implements Runnable {

    public static void main(String[] args) {
        new Main().run();
    }

    /**
     * A while loop is run to check if there are any new entries from the log file.
     * If new entries are found, they are turned into httpLogEvent and send to the CEP machine
     */
    public void run() {
        Configuration configuration = CEPSetupUtil.getConfiguration();
        EPRuntime runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), configuration);

        new FailedLoginStatement(runtime);
        new ConsecutiveFailedLoginAlertStatement(runtime, 15, 5, 5, 25);
        new ConsecutiveFailedFromSameIPAlertStatement(runtime, 12, 1, 1, 20);
        new ConsecutiveFailedLoginSameUserIDStatement(runtime, 3, 1, 1, 6);

        new FileTooLargeStatement(runtime);
        new FileTooLargeFromSameIPAlertStatement(runtime, 5, 20, 10, 10);

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

    /**
     * A httpd access
     * log parser for linux systems
     *
     * @return ArrayList<httpLogEvent> list of events parsed from the log
     * @author Bui Xuan Phuoc
     */
    public static ArrayList<httpLogEvent> getEventsFromApacheHTTPDLogs() throws IOException {
        // TODO: more efficient way to read the log?
        BufferedReader reader = new BufferedReader(new FileReader("/var/log/apache2/access.log"));
        String line = null;
        ArrayList<httpLogEvent> result = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            Pattern p = Pattern.compile("\"([^\"]*)\"");

            Matcher m = p.matcher(line);
            while (m.find()) {
                line = line.replace(m.group(1), m.group(1).replace(" ", ""));
            }

            ArrayList<String> lineComponents = new ArrayList<>(Arrays.asList(line.split(" ")));
            // System.out.println(lineComponents + " " + lineComponents.size());
            result.add(new httpLogEvent(lineComponents));
        }
        // System.out.println(result.size());
        return result;
    }
}
