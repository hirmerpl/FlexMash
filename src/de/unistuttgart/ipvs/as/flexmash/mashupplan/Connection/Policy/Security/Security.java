
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "encryption"
})
public class Security {

    @JsonProperty("encryption")
    private Boolean encryption;

    @JsonProperty("encryption")
    public Boolean getEncryption() {
        return encryption;
    }

    @JsonProperty("encryption")
    public void setEncryption(Boolean encryption) {
        this.encryption = encryption;
    }

}
