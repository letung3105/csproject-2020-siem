package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPCacheFloodAlertStatement {
    String statement = "insert into ARPCacheFloodAlertEvent\n " +
            "select count(*) from ARPCacheUpdateEvent\n " +
            "having count(*) >= 240";
    private String listenStatement = "select * from ARPReplyEvent";

    public ARPCacheFloodAlertStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
        ARPAlertUtils.compileDeploy(listenStatement, runtime).addListener(new ARPCacheFloodAlertListener());
    }
}
