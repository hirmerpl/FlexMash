
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Capacity.Capacity;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Performance.Performance;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Privacy.Privacy;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Recoverability.Recoverability;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Reliability.Reliability;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Security.Security;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "performance",
    "security",
    "privacy",
    "capacity",
    "reliability",
    "recoverability"
})
public class Policy {

    @JsonProperty("performance")
    private Performance performance;
    @JsonProperty("security")
    private Security security;
    @JsonProperty("privacy")
    private Privacy privacy;
    @JsonProperty("capacity")
    private Capacity capacity;
    @JsonProperty("reliability")
    private Reliability reliability;
    @JsonProperty("recoverability")
    private Recoverability recoverability;

    @JsonProperty("performance")
    public Performance getPerformance() {
        return performance;
    }

    @JsonProperty("performance")
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    @JsonProperty("security")
    public Security getSecurity() {
        return security;
    }

    @JsonProperty("security")
    public void setSecurity(Security security) {
        this.security = security;
    }

    @JsonProperty("privacy")
    public Privacy getPrivacy() {
        return privacy;
    }

    @JsonProperty("privacy")
    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    @JsonProperty("capacity")
    public Capacity getCapacity() {
        return capacity;
    }

    @JsonProperty("capacity")
    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    @JsonProperty("reliability")
    public Reliability getReliability() {
        return reliability;
    }

    @JsonProperty("reliability")
    public void setReliability(Reliability reliability) {
        this.reliability = reliability;
    }

    @JsonProperty("recoverability")
    public Recoverability getRecoverability() {
        return recoverability;
    }

    @JsonProperty("recoverability")
    public void setRecoverability(Recoverability recoverability) {
        this.recoverability = recoverability;
    }

}
