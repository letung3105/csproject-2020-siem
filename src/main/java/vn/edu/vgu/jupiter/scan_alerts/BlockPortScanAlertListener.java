package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class BlockPortScanAlertListener implements UpdateListener {
    private final Logger logger = LoggerFactory.getLogger(BlockPortScanAlertListener.class);

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }
        Instant ts = (Instant) newEvents[0].get("timestamp");
        logger.info("[ts={}] multiple hosts received multiple connections to different closed ports", ts.getEpochSecond());
    }
}
