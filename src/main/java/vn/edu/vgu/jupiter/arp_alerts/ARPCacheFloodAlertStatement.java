package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPCacheFloodAlertStatement {
    String statement = "insert into ARPCacheFloodAlertEvent\n " +
            "select cast(count(distinct IP) as int) from ARPCacheUpdateEvent\n " +
            "having count(distinct IP) >= 240";
    private String listenStatement = "select * from ARPCacheFloodAlertEvent";

    public ARPCacheFloodAlertStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
        ARPAlertUtils.compileDeploy(listenStatement, runtime).addListener(new ARPCacheFloodAlertListener());
    }
}
