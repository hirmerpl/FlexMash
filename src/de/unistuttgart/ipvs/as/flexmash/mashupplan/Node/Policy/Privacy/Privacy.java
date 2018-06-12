
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Privacy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "authorizedAccess"
})
public class Privacy {

    @JsonProperty("authorizedAccess")
    private Boolean authorizedAccess;

    @JsonProperty("authorizedAccess")
    public Boolean getAuthorizedAccess() {
        return authorizedAccess;
    }

    @JsonProperty("authorizedAccess")
    public void setAuthorizedAccess(Boolean authorizedAccess) {
        this.authorizedAccess = authorizedAccess;
    }

}
