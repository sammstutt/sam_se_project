package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {

    public PriorityAlertDecorator(Alert alert) {
        super(alert);
    }

    @Override
    public String getCondition() {
        // Add a "[PRIORITY]" tag to the original alert's description
        return "[PRIORITY] " + wrappedAlert.getCondition();
    }
}
