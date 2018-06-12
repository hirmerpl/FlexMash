
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Performance.Performance;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Security.Security;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "performance",
        "security"
})
public class Policy {

    @JsonProperty("performance")
    private Performance performance;
    @JsonProperty("security")
    private Security security;

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

}
