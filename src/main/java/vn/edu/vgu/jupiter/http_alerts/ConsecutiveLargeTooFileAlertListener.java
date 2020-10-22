package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsecutiveLargeTooFileAlertListener implements UpdateListener {
    private static final Logger log = LoggerFactory.getLogger(ConsecutiveLargeTooFileAlertListener.class);

    private long highPriorityThreshold;

    public ConsecutiveLargeTooFileAlertListener(long highPriorityThreshold) {
        this.highPriorityThreshold = highPriorityThreshold;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Long count = (Long) newEvents[0].get("failuresCount");
        if (count < highPriorityThreshold) {
            log.info("LOW PRIORITY: Large files are consecutively being sent");
        } else {
            log.warn("HIGH PRIORITY: Large files are consecutively being sent");
        }
    }
}
