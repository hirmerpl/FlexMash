
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Performance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Performance.MaximumLatency.MaximumLatency;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "maximumLatency"
})
public class Performance {

    @JsonProperty("maximumLatency")
    private MaximumLatency maximumLatency;

    @JsonProperty("maximumLatency")
    public MaximumLatency getMaximumLatency() {
        return maximumLatency;
    }

    @JsonProperty("maximumLatency")
    public void setMaximumLatency(MaximumLatency maximumLatency) {
        this.maximumLatency = maximumLatency;
    }

}
