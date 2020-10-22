package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

public class ARPDuplicateIPAlertListener implements UpdateListener {
    @Override
    public void update(EventBean[] eventBeans, EventBean[] eventBeans1, EPStatement epStatement, EPRuntime epRuntime) {
        System.out.println("Multiple MAC address for: " +eventBeans[0].get("srcIP") + "detected in the cache");
    }
}
