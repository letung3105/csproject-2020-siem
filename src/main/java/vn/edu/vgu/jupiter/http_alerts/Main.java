package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.configuration.Configuration;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPRuntimeProvider;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import vn.edu.vgu.jupiter.eventbean_http.httpLogEvent;

import java.io.BufferedReader;
import java.io.File;
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

        new ConsecutiveFailedLoginAlertStatement(runtime, 15, 5);
        new ConsecutiveFailedFromSameIPAlertStatement(runtime, 12, 1);
        new ConsecutiveFailedLoginSameUserIDStatement(runtime, 3, 1);
        new FailedLoginStatement(runtime);
        new FileTooLargeFromSameIPAlertStatement(runtime, 5);
        new FileTooLargeStatement(runtime);

        File accessLog = new File("/var/log/apache2/access.log");
        TailerListener listener = new HTTPDLogTailer(runtime);
        Tailer tailer = Tailer.create(accessLog, listener, 100);
        tailer.run();
    }

//    /**
//     * A httpd access log parser for linux systems
//     *
//     * @author Bui Xuan Phuoc
//     * @return ArrayList<httpLogEvent> list of events parsed from the log
//     */
//    public static ArrayList<httpLogEvent> getEventsFromApacheHTTPDLogs() throws IOException {
//        // TODO: more efficient way to read the log?
//
//        BufferedReader reader = new BufferedReader(new FileReader("/var/log/apache2/access.log"));
//        String line = null;
//        ArrayList<httpLogEvent> result = new ArrayList<>();
//
//        while ((line = reader.readLine()) != null) {
//            Pattern p = Pattern.compile("\"([^\"]*)\"");
//
//            Matcher m = p.matcher(line);
//            while (m.find()) {
//                line = line.replace(m.group(1), m.group(1).replace(" ", ""));
//            }
//
//
//            ArrayList<String> lineComponents = new ArrayList<String>(Arrays.asList(line.split(" ")));
//            while (lineComponents.size() > 10) {
//                lineComponents.set(3, lineComponents.get(3) + lineComponents.get(4));
//                lineComponents.remove(4);
//            }
//            // System.out.println(lineComponents + " " + lineComponents.size());
//            result.add(new httpLogEvent(lineComponents));
//        }
//        // System.out.println(result.size());
//        return result;
//    }
}
