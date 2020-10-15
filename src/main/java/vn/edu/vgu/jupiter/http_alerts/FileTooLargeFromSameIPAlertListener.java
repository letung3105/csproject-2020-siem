import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;


/**
 * A simple listener for httpFileTooLargeFromSameIPAlert Event
 * <p>
 * The information of the new events is logged to the system using the class's logger
 */

public class FileTooLargeFromSameIPAlertListener implements UpdateListener {

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        System.out.println("Attempts to send large entity from the same IP detected: " + newEvents[0].get("IPAddress"));
    }
}
