# Classify if an HTTP alert has high or low priority

To classify if an HTTP alert has high or low priority, a new field that keeps track of the number of raw events leading to the alert is added.

```java
class HTTPAlert {
    private long failuresCount;
}
```

Upon a new event, the listener will extract the counting field from the event, then compares the extracted number with the threshold that had been given to it. If the count exceeds the threshold, the event is marked as having HIGH priority and logged to the system with WARN level.


```java
class HTTPAlertListener implements UpdateListener {
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement statement, EPRuntime runtime) {
        Long count = (Long) newEvents[0].get("failuresCount");
        if (count < highPriorityThreshold) {
            log.info("LOW PRIORITY");
        } else {
            log.warn("HIGH PRIORITY");
        }
    }
}
```