package vn.edu.vgu.jupiter.http_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

/**
 * A simple listener for httpFailedLogin Event
 * <p>
 * The information of the new events is logged to the system using the class's logger
 *
 * @author Bui Xuan Phuoc
 */
public class FailedLoginStatementListener implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        System.out.println("New failed login detected " + newEvents[0].get("IPAddress"));
    }
}
