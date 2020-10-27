package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ARPDuplicateIPAlertListener implements UpdateListener {
    private static final Logger logger = LoggerFactory.getLogger(ARPDuplicateIPAlertListener.class);

    @Override
    public void update(EventBean[] eventBeans, EventBean[] eventBeans1, EPStatement epStatement, EPRuntime epRuntime) {
        logger.warn("Multiple MAC address for: " +eventBeans[0].get("IP") + " detected in the cache");
    }
}
