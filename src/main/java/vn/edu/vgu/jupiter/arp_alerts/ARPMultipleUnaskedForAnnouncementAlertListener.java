package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ARPMultipleUnaskedForAnnouncementAlertListener implements UpdateListener {
    private static final Logger logger = LoggerFactory.getLogger(ARPMultipleUnaskedForAnnouncementAlertListener.class);

    private long highPriorityThreshold;

    public ARPMultipleUnaskedForAnnouncementAlertListener(long highPriorityThreshold) {
        this.highPriorityThreshold = highPriorityThreshold;
    }
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement epStatement, EPRuntime epRuntime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long count = (Long) newEvents[0].get("count");
        if (count < highPriorityThreshold) {
            logger.info("LOW PRIORITY: Consecutive gratuitous announcements in a small instance of time detected: " + newEvents[0].get("count"));
        } else {
            logger.warn("HIGH PRIORITY: Consecutive gratuitous announcements in a small instance of time detected: " + newEvents[0].get("count"));
        }
        logger.warn("Consecutive gratuitous announcements in a small instance of time detected");
    }
}
