package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.runtime.client.EPRuntime;

public class ARPDuplicateIPAlertStatement {
    String statement =     "insert into ARPDuplicateIPAlertEvent\n " +
            "select srcIP, time\n " +
            "from ARPCacheUpdateEvent\n " +
            "group by srcIP\n " +
            "having count(*) > 1";

    private String listenStatement = "select * from ARPDuplicateIPAlertEvent";

    public ARPDuplicateIPAlertStatement(EPRuntime runtime) {
        ARPAlertUtils.compileDeploy(statement, runtime);
    }
}
