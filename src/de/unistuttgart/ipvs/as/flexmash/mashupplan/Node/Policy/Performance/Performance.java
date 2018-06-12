
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Performance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Performance.ProcessingTime.ProcessingTime;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Performance.ResponseTime.ResponseTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "processingTime",
    "responseTime"
})
public class Performance {

    @JsonProperty("processingTime")
    private ProcessingTime processingTime;
    @JsonProperty("responseTime")
    private ResponseTime responseTime;

    @JsonProperty("processingTime")
    public ProcessingTime getProcessingTime() {
        return processingTime;
    }

    @JsonProperty("processingTime")
    public void setProcessingTime(ProcessingTime processingTime) {
        this.processingTime = processingTime;
    }

    @JsonProperty("responseTime")
    public ResponseTime getResponseTime() {
        return responseTime;
    }

    @JsonProperty("responseTime")
    public void setResponseTime(ResponseTime responseTime) {
        this.responseTime = responseTime;
    }

}
