package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * A simple listener for VerticalPortScanAlert events
 * <p>
 * The information of the new events is logged to the system using the class's logger
 *
 * @author Tung Le Vo
 */
public class VerticalPortScanAlertListener implements UpdateListener {
    private final Logger logger = LoggerFactory.getLogger(VerticalPortScanAlertListener.class);

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        InetAddress hostAddr = (InetAddress) newEvents[0].get("hostAddr");
        logger.info(hostAddr + " IS UNDER ATTACKS");
    }
}
