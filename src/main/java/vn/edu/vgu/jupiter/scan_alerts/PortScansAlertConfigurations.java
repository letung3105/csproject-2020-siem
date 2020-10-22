package vn.edu.vgu.jupiter.scan_alerts;

public class PortScansAlertConfigurations {
    public static abstract class Common {
        private int timeWindow;
        private int alertInterval;
        private int highPriorityThreshold;

        public Common(int timeWindow, int alertInterval, int highPriorityThreshold) {
            this.timeWindow = timeWindow;
            this.alertInterval = alertInterval;
            this.highPriorityThreshold = highPriorityThreshold;
        }

        public int getTimeWindow() {
            return timeWindow;
        }

        public int getAlertInterval() {
            return alertInterval;
        }

        public int getHighPriorityThreshold() {
            return highPriorityThreshold;
        }
    }

    public static class VerticalScan extends Common {
        private int connectionsCountThreshold;

        public VerticalScan(int timeWindow, int alertInterval, int highPriorityThreshold, int connectionsCountThreshold) {
            super(timeWindow, alertInterval, highPriorityThreshold);
            this.connectionsCountThreshold = connectionsCountThreshold;
        }

        public int getConnectionsCountThreshold() {
            return connectionsCountThreshold;
        }
    }

    public static class HorizontalScan extends Common {
        private int connectionsCountThreshold;

        public HorizontalScan(int timeWindow, int alertInterval, int highPriorityThreshold, int connectionsCountThreshold) {
            super(timeWindow, alertInterval, highPriorityThreshold);
            this.connectionsCountThreshold = connectionsCountThreshold;
        }

        public int getConnectionsCountThreshold() {
            return connectionsCountThreshold;
        }
    }

    public static class BlockScan extends Common {
        private int portsCountThreshold;
        private int addressesCountThreshold;

        public BlockScan(int timeWindow, int alertInterval, int highPriorityThreshold, int portsCountThreshold, int addressesCountThreshold) {
            super(timeWindow, alertInterval, highPriorityThreshold);
            this.portsCountThreshold = portsCountThreshold;
            this.addressesCountThreshold = addressesCountThreshold;
        }

        public int getPortsCountThreshold() {
            return portsCountThreshold;
        }

        public int getAddressesCountThreshold() {
            return addressesCountThreshold;
        }
    }

    private VerticalScan verticalScan;
    private HorizontalScan horizontalScan;
    private BlockScan blockScan;

    public PortScansAlertConfigurations(VerticalScan verticalScan, HorizontalScan horizontalScan, BlockScan blockScan) {
        this.verticalScan = verticalScan;
        this.horizontalScan = horizontalScan;
        this.blockScan = blockScan;
    }

    public VerticalScan getVerticalScan() {
        return verticalScan;
    }

    public HorizontalScan getHorizontalScan() {
        return horizontalScan;
    }

    public BlockScan getBlockScan() {
        return blockScan;
    }
}
