package vn.edu.vgu.jupiter.arp_alerts;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.metric.StatementMetric;
import com.espertech.esper.runtime.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import vn.edu.vgu.jupiter.EPFacade;
import vn.edu.vgu.jupiter.arp_alerts.eventbean.ARPPacketEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static vn.edu.vgu.jupiter.arp_alerts.ARPAlertsConfigurations.getEPConfiguration;

public class ARPAlertsMain implements Runnable {
    private static class MetricListener implements UpdateListener {
        private Map<String, Long> eventsCumulativeCount;
        private Set<PropertyChangeListener> propertyChangeListenerSet;

        public MetricListener() {
            this.propertyChangeListenerSet = new HashSet<>();
            this.eventsCumulativeCount = new HashMap<>();
            this.eventsCumulativeCount.put("ARPAnnouncementEvent", 0L);
            this.eventsCumulativeCount.put("ARPBroadcastEvent", 0L);
            this.eventsCumulativeCount.put("ARPCacheFloodAlertEvent", 0L);
            this.eventsCumulativeCount.put("ARPCacheUpdateEvent", 0L);
            this.eventsCumulativeCount.put("ARPDuplicateIPAlertEvent", 0L);
            this.eventsCumulativeCount.put("ARPMultipleUnaskedForAnnouncementAlertEvent", 0L);
            this.eventsCumulativeCount.put("ARPReplyEvent", 0L);
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeListenerSet.add(listener);
        }

        @Override
        public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
            if (newEvents == null) {
                return; // ignore old events for events leaving the window
            }

            StatementMetric metric = (StatementMetric) newEvents[0].getUnderlying();
            if (eventsCumulativeCount.containsKey(metric.getStatementName())) {
                Long oldCount = eventsCumulativeCount.get(metric.getStatementName());
                Long newCount = oldCount + metric.getNumOutputIStream();
                if (!newCount.equals(oldCount)) {
                    eventsCumulativeCount.put(metric.getStatementName(), newCount);
                    for (PropertyChangeListener l : propertyChangeListenerSet) {
                        l.propertyChange(new PropertyChangeEvent(this.getClass(), metric.getStatementName(), oldCount, newCount));
                    }
                }
            }
        }
    }

    private static Logger log = LogManager.getLogger(ARPAlertsMain.class);

    private String netDevName;

    private EPRuntime runtime;
    private ARPAnnouncementStatement arpAnnouncementStatement;
    private ARPReplyStatement arpReplyStatement;
    private ARPCacheFloodAlertStatement arpCacheFloodAlertStatement;
    private ARPDuplicateIPAlertStatement arpDuplicateIPAlertStatement;
    private ARPCacheUpdateStatement arpCacheUpdateStatement;
    private ARPMultipleUnaskedForAnnouncementAlertStatement arpMultipleUnaskedForAnnouncementAlertStatement;
    private ARPBroadcastStatement arpBroadcastStatement;
    private MetricListener metricListener;


    public ARPAlertsMain(String netDevName) {
        this.runtime = EPRuntimeProvider.getRuntime(this.getClass().getSimpleName(), getEPConfiguration());
        this.metricListener = new ARPAlertsMain.MetricListener();
        this.netDevName = netDevName;
        EPFacade
                .compileDeploy(
                        "select * from com.espertech.esper.common.client.metric.StatementMetric",
                        runtime, getEPConfiguration()
                )
                .addListener(metricListener);
    }

    public static void main(String[] args) {
        ARPAlertsConfigurations arpAlertsConfigurations = new ARPAlertsConfigurations(
                new ARPAlertsConfigurations.ARPDuplicateIP(10),
                new ARPAlertsConfigurations.ARPCacheFlood(40, 3, 10, 30),
                new ARPAlertsConfigurations.ARPGratuitousAnnouncement(4, 10, 10, 3)
        );
        ARPAlertsMain arpAlertsMain = new ARPAlertsMain(args[0]);
        arpAlertsMain.deploy(arpAlertsConfigurations);
        arpAlertsMain.run();
    }

    public void deploy(ARPAlertsConfigurations arpAlertsConfigurations) {
        arpBroadcastStatement = new ARPBroadcastStatement(runtime);
        arpAnnouncementStatement = new ARPAnnouncementStatement(runtime);
        arpReplyStatement = new ARPReplyStatement(runtime);
        arpCacheFloodAlertStatement = new ARPCacheFloodAlertStatement(runtime,
                arpAlertsConfigurations.arpCacheFlood.consecutiveAttemptsThreshold,
                arpAlertsConfigurations.arpCacheFlood.timeWindowSeconds,
                arpAlertsConfigurations.arpCacheFlood.alertIntervalSeconds,
                arpAlertsConfigurations.arpCacheFlood.highPriorityThreshold);
        arpDuplicateIPAlertStatement = new ARPDuplicateIPAlertStatement(runtime,
                arpAlertsConfigurations.arpDuplicateIP.alertIntervalSeconds);
        arpCacheUpdateStatement = new ARPCacheUpdateStatement(runtime);
        arpMultipleUnaskedForAnnouncementAlertStatement = new ARPMultipleUnaskedForAnnouncementAlertStatement(runtime,
                arpAlertsConfigurations.arpGratuitousAnnouncement.consecutiveAttemptsThreshold,
                arpAlertsConfigurations.arpGratuitousAnnouncement.timeWindowSeconds,
                arpAlertsConfigurations.arpGratuitousAnnouncement.alertIntervalSeconds,
                arpAlertsConfigurations.arpGratuitousAnnouncement.highPriorityThreshold);
    }

    public void run() {
        try {
            // getting the network interface
            PcapNetworkInterface nif = Pcaps.getDevByName(netDevName);
            log.info(nif.getName() + "(" + nif.getDescription() + ")");

            //OPEN PCAP HANDLE FROM NETWORK INTERFACE
            int snapLen = 65536; //bytes
            PcapNetworkInterface.PromiscuousMode mode = PcapNetworkInterface.PromiscuousMode.PROMISCUOUS;
            int timeout = 10; //milliseconds
            PcapHandle handle = nif.openLive(snapLen, mode, timeout);
            PacketListener listener = packet -> {
                if (packet.contains(ArpPacket.class)) {
                    ArpPacket arp = packet.get(ArpPacket.class);
                    ARPPacketEvent arpPacketEvent = new ARPPacketEvent(arp.getHeader());
                    runtime.getEventService().sendEventBean(arpPacketEvent, "ARPPacketEvent");
                }
            };

            try {
                handle.loop(-1, listener);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handle.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void undeploy() throws EPUndeployException {
        if (arpBroadcastStatement != null) {
            arpBroadcastStatement.undeploy();
            arpBroadcastStatement = null;
        }
        if (arpAnnouncementStatement != null) {
            arpAnnouncementStatement.undeploy();
            arpAnnouncementStatement = null;
        }
        if (arpReplyStatement != null) {
            arpReplyStatement.undeploy();
            arpReplyStatement = null;
        }
        if (arpCacheFloodAlertStatement != null) {
            arpCacheFloodAlertStatement.undeploy();
            arpCacheFloodAlertStatement = null;
        }
        if (arpDuplicateIPAlertStatement != null) {
            arpDuplicateIPAlertStatement.undeploy();
            arpDuplicateIPAlertStatement = null;
        }
        if (arpMultipleUnaskedForAnnouncementAlertStatement != null) {
            arpMultipleUnaskedForAnnouncementAlertStatement.undeploy();
            arpMultipleUnaskedForAnnouncementAlertStatement = null;
        }
        if (arpCacheUpdateStatement != null) {
            arpCacheUpdateStatement.undeploy();
            arpCacheUpdateStatement = null;
        }
    }

    public void addStatementMetricListener(PropertyChangeListener listener) {
        this.metricListener.addPropertyChangeListener(listener);
    }
}
