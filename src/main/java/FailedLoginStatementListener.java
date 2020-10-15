import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

/**
 * this class listen on failed log event
 * @author Dang Chi Cong
 */
public class FailedLoginStatementListener implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        System.out.println("Dictionary attack on " + newEvents[0].get("userID") + " at IP: " +newEvents[0].get("IPAddress"));
    }
}
