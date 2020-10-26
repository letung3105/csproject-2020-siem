package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ARPCacheFloodAlertListener implements UpdateListener {
    private static final Logger logger = LoggerFactory.getLogger(ARPCacheFloodAlertListener.class);

    private long highPriorityThreshold;

    public ARPCacheFloodAlertListener(long highPriorityThreshold) {
        this.highPriorityThreshold = highPriorityThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement epStatement, EPRuntime epRuntime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long count = Long.parseLong(newEvents[0].get("count").toString());
        if (count < highPriorityThreshold) {
            logger.info("LOW PRIORITY: Large number of entries in the ARP cache " + newEvents[0].get("count"));
        } else {
            logger.warn("HIGH PRIORITY: Too many entries in the ARP cache:" + newEvents[0].get("count"));
        }
    }
}
