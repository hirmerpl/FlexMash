
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Connection.Policy.Policy;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sourceId",
    "sourceUUID",
    "targetId",
    "targetUUID",
    "policy"
})
public class Connection {

    @JsonProperty("sourceId")
    private String sourceId;
    @JsonProperty("sourceUUID")
    private String sourceUUID;
    @JsonProperty("targetId")
    private String targetId;
    @JsonProperty("targetUUID")
    private String targetUUID;
    @JsonProperty("policy")
    private Policy policy;

    @JsonProperty("sourceId")
    public String getSourceId() {
        return sourceId;
    }

    @JsonProperty("sourceId")
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @JsonProperty("sourceUUID")
    public String getSourceUUID() {
        return sourceUUID;
    }

    @JsonProperty("sourceUUID")
    public void setSourceUUID(String sourceUUID) {
        this.sourceUUID = sourceUUID;
    }

    @JsonProperty("targetId")
    public String getTargetId() {
        return targetId;
    }

    @JsonProperty("targetId")
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    @JsonProperty("targetUUID")
    public String getTargetUUID() {
        return targetUUID;
    }

    @JsonProperty("targetUUID")
    public void setTargetUUID(String targetUUID) {
        this.targetUUID = targetUUID;
    }

    @JsonProperty("policy")
    public Policy getPolicy() {
        return policy;
    }

    @JsonProperty("policy")
    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

}
