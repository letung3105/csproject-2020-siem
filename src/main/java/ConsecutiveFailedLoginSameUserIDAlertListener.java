import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

/**
 * A simple listener for httpConsecutiveFailedFromSameIPAlert Event
 * <p>
 * The information of the new events is logged to the system using the class's logger
 */

public class ConsecutiveFailedLoginSameUserIDAlertListener implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        System.out.println("Consecutive failed logins targeting " + newEvents[0].get("userID") + " detected");
    }
}
