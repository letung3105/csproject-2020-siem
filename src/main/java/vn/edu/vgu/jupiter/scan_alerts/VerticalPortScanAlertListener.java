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
        int count = ((Long) newEvents[0].get("count")).intValue();
        if(count < countThreshold){
            logger.info("[ts={}] LOW PRIORITY: POTENTIAL VERTICAL PORT SCAN ON {}", ts, hostAddr.getHostAddress()); //info
        } else {
            logger.warn("[ts={}] HIGH PRIORITY: VERTICAL PORT SCAN ON {}", ts, hostAddr.getHostAddress()); //warn
        }
    }
}
