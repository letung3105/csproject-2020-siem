package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * A listener that can be attach the statement that returns events
 * of type <code>VerticalPortScanAlert</code>. The received update will
 * be shown to the user in a log message.
 *
 * @author Vo Le Tung
 */
public class VerticalPortScanAlertListener implements UpdateListener {
    private static final Logger logger = LoggerFactory.getLogger(VerticalPortScanAlertListener.class);
    private int countThreshold;
    private int count = 0;

    public VerticalPortScanAlertListener(int countThreshold){
        this.countThreshold = countThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long ts = (Long) newEvents[0].get("timestamp");
        InetAddress hostAddr = (InetAddress) newEvents[0].get("hostAddr");
        logger.info("[ts={}] POTENTIAL VERTICAL PORT SCAN ON {}", ts, hostAddr.getHostAddress()); //info
        count++;
        if(count == countThreshold){ //warning
            logger.warn("VERTICAL PORT SCAN COUNT HAS REACHED {} times.", countThreshold);
            count = 0;
        }
    }
}
