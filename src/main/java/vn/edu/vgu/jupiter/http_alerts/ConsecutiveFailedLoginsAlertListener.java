package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple listener for httpConsecutiveFailedLoginAlert Event
 * <p>
 * The information of the new events is logged to the system using the class's logger
 *
 * @author Dang Chi Cong
 */
public class ConsecutiveFailedLoginsAlertListener implements UpdateListener {
    private static final Logger log = LoggerFactory.getLogger(ConsecutiveFailedLoginsAlertListener.class);

    private long highPriorityThreshold;

    public ConsecutiveFailedLoginsAlertListener(long highPriorityThreshold) {
        this.highPriorityThreshold = highPriorityThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long count = (Long) newEvents[0].get("failuresCount");
        if (count < highPriorityThreshold) {
            log.info("LOW PRIORITY: Consecutive failed logins in short time frame detected");
        } else {
            log.warn("HIGH PRIORITY: Consecutive failed logins in short time frame detected");
        }
    }
}
