
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Performance.Latency;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "value",
    "unit",
    "operator"
})
public class Latency {

    @JsonProperty("value")
    private Integer value;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("operator")
    private String operator;

    @JsonProperty("value")
    public Integer getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(Integer value) {
        this.value = value;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("operator")
    public String getOperator() { return operator; }

    @JsonProperty("operator")
    public void setOperator(String operator) { this.operator = operator; }

}
