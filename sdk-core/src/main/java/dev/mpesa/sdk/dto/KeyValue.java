package dev.mpesa.sdk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KeyValue {
    @JsonProperty("Key")
    private String key;

    @JsonProperty("Value")
    private String value;

    public KeyValue() {}

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
    public void setKey(String key) { this.key = key; }
    public void setValue(String value) { this.value = value; }

    @Override
    public String toString() {
        return "{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}