package vn.edu.vgu.jupiter.dashboard;

import com.espertech.esper.common.client.EventBean;
import com.espertech.esper.common.client.metric.StatementMetric;
import com.espertech.esper.runtime.client.EPRuntime;
import com.espertech.esper.runtime.client.EPStatement;
import com.espertech.esper.runtime.client.UpdateListener;

public class MetricsPanel implements UpdateListener {
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        if (newEvents == null) {
            return; // ignore old events for events leaving the window
        }

        StatementMetric metric = (StatementMetric) newEvents[0].getUnderlying();
        System.out.println(
                metric.getRuntimeURI() + "-" +
                        metric.getStatementName() + ": " +
                        metric.getNumInput() + " | " +
                        metric.getNumOutputIStream() + " | " +
                        metric.getNumOutputRStream()
        );
    }
}
