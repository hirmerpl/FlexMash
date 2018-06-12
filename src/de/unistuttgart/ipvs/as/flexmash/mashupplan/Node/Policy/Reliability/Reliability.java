
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Reliability;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Reliability.MeanTimeBetweenFailures.MeanTimeBetweenFailures;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Reliability.MeanTimeToRecovery.MeanTimeToRecovery;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "meanTimeBetweenFailures",
    "meanTimeToRecovery"
})
public class Reliability {

    @JsonProperty("meanTimeBetweenFailures")
    private MeanTimeBetweenFailures meanTimeBetweenFailures;
    @JsonProperty("meanTimeToRecovery")
    private MeanTimeToRecovery meanTimeToRecovery;

    @JsonProperty("meanTimeBetweenFailures")
    public MeanTimeBetweenFailures getMeanTimeBetweenFailures() {
        return meanTimeBetweenFailures;
    }

    @JsonProperty("meanTimeBetweenFailures")
    public void setMeanTimeBetweenFailures(MeanTimeBetweenFailures meanTimeBetweenFailures) {
        this.meanTimeBetweenFailures = meanTimeBetweenFailures;
    }

    @JsonProperty("meanTimeToRecovery")
    public MeanTimeToRecovery getMeanTimeToRecovery() {
        return meanTimeToRecovery;
    }

    @JsonProperty("meanTimeToRecovery")
    public void setMeanTimeToRecovery(MeanTimeToRecovery meanTimeToRecovery) {
        this.meanTimeToRecovery = meanTimeToRecovery;
    }

}
