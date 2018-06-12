
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Policy;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Property.Property;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "guiId",
    "serviceId",
    "properties",
    "policy",
    "target"
})
public class Node {

    @JsonProperty("guiId")
    private String guiId;
    @JsonProperty("serviceId")
    private String serviceId;
    @JsonProperty("properties")
    private List<Property> properties = null;
    @JsonProperty("policy")
    private Policy policy;
    @JsonProperty("target")
    private List<String> target = null;

    @JsonProperty("guiId")
    public String getGuiId() {
        return guiId;
    }

    @JsonProperty("guiId")
    public void setGuiId(String guiId) {
        this.guiId = guiId;
    }

    @JsonProperty("serviceId")
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty("serviceId")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @JsonProperty("properties")
    public List<Property> getProperties() {
        return properties;
    }

    @JsonProperty("properties")
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @JsonProperty("policy")
    public Policy getPolicy() {
        return policy;
    }

    @JsonProperty("policy")
    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    @JsonProperty("target")
    public List<String> getTarget() {
        return target;
    }

    @JsonProperty("target")
    public void setTarget(List<String> target) {
        this.target = target;
    }

}
