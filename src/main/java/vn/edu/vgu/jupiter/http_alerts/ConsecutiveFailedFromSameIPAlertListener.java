package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple listener for httpConsecutiveFailedFromSameIPAlert Event
 * <p>
 * The information of the new events is logged to the system using the class's logger
 *
 * @author Bui Xuan Phuoc
 */

public class ConsecutiveFailedFromSameIPAlertListener implements UpdateListener {
    private static final Logger log = LoggerFactory.getLogger(ConsecutiveFailedLoginAlertListener.class);

    private long highPriorityThreshold;

    public ConsecutiveFailedFromSameIPAlertListener(long highPriorityThreshold) {
        this.highPriorityThreshold = highPriorityThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long count = (Long) newEvents[0].get("failuresCount");
        String IPAddress = (String) newEvents[0].get("IPAddress");
        if (count < highPriorityThreshold) {
            log.info("LOW PRIORITY: Consecutive failed logins in short time frame coming from '{}'", IPAddress);
        } else {
            log.warn("HIGH PRIORITY: Consecutive failed logins in short time frame coming from '{}'", IPAddress);
        }
    }
}
