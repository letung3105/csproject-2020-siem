module vn.edu.vgu.jupiter {
    requires java.sql;
    requires java.naming;
    requires slf4j.api;
    requires org.pcap4j.core;
    requires esper.common;
    requires esper.compiler;
    requires esper.runtime;
    requires javafx.controls;
    requires javafx.fxml;

    opens vn.edu.vgu.jupiter.dashboard to javafx.fxml, javafx.graphics;
    exports vn.edu.vgu.jupiter;
    exports vn.edu.vgu.jupiter.eventbean;
    exports vn.edu.vgu.jupiter.eventbean_http;
    exports vn.edu.vgu.jupiter.scan_alerts;
    exports vn.edu.vgu.jupiter.http_alerts;
}