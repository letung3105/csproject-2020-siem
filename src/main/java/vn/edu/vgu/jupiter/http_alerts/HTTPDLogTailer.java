package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.runtime.client.EPRuntime;
import org.apache.commons.io.input.TailerListenerAdapter;
import vn.edu.vgu.jupiter.eventbean_http.httpLogEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Listener for http log tailer, whenever a new line is found, a httplogevent is created
 * and sent to the esper runtime
 *
 * @author Bui Xuan Phuoc
 */
public class HTTPDLogTailer extends TailerListenerAdapter {
    EPRuntime runtime;

    public HTTPDLogTailer(EPRuntime runtime) {
        this.runtime = runtime;
    }
    public void handle(String line) {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(line);
        while (m.find()) {
            line = line.replace(m.group(1), m.group(1).replace(" ", ""));
        }
        ArrayList<String> lineComponents = new ArrayList<String>(Arrays.asList(line.split(" ")));
        while (lineComponents.size() > 10) {
            lineComponents.set(3, lineComponents.get(3) + lineComponents.get(4));
            lineComponents.remove(4);
        }
        runtime.getEventService().sendEventBean(new httpLogEvent(lineComponents), "httpLogEvent");
    }
}
