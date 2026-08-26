package mapping.config;

import java.util.Map;

public class MappingDefinition {
    private String provider;
    private String eventType;
    private DetectionRule detection;
    private Map<String, String> fields;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public DetectionRule getDetection() {
        return detection;
    }

    public void setDetection(DetectionRule detection) {
        this.detection = detection;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    public static class DetectionRule {
        private String jsonPath;
        private String expectedValue;

        // Getters & Setters
        public String getJsonPath() {
            return jsonPath;
        }

        public void setJsonPath(String jsonPath) {
            this.jsonPath = jsonPath;
        }

        public String getExpectedValue() {
            return expectedValue;
        }

        public void setExpectedValue(String expectedValue) {
            this.expectedValue = expectedValue;
        }
    }
}