
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Performance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Performance.Latency.Latency;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "latency"
})
public class Performance {

    @JsonProperty("latency")
    private Latency latency;

    @JsonProperty("latency")
    public Latency getLatency() {
        return latency;
    }

    @JsonProperty("latency")
    public void setLatency(Latency latency) {
        this.latency = latency;
    }

}
