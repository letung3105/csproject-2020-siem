package vn.edu.vgu.jupiter.scan_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.pcap4j.packet.namednumber.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorizontalPortScanAlertListener implements UpdateListener {
    private final Logger logger = LoggerFactory.getLogger(HorizontalPortScanAlertListener.class);

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return;
        }
        Port hostPort = (Port) newEvents[0].get("hostPort");
        logger.info(hostPort + " IS UNDER HORIZONTAL ATTACK.");
    }
}
