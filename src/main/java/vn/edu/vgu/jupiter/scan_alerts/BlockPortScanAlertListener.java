package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A listener that can be attach the statement that returns events
 * of type <code>BlockPortScanAlert</code>. The received update will
 * be shown to the user in a log message.
 *
 * @author Vo Le Tung
 */
public class BlockPortScanAlertListener implements UpdateListener {
    private static final Logger logger = LoggerFactory.getLogger(BlockPortScanAlertListener.class);
    private int countThreshold;
    private int count = 0;

    public BlockPortScanAlertListener(int countThreshold){
        this.countThreshold = countThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long ts = (Long) newEvents[0].get("timestamp");
        logger.info("[ts={}] POTENTIAL BLOCK PORT SCAN", ts);
        count++;
        if(count == countThreshold){ //warning
            logger.warn("BLOCK PORT SCAN COUNT HAS REACHED {} times.", countThreshold);
            count = 0;
        }
    }
}

