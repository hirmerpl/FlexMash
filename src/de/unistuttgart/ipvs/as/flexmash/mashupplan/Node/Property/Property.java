
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Property;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "key",
    "value",
    "nodePropertyCard",
    "nodePropertyCardMin",
    "nodePropertyCardMax",
    "nodePropertyType"
})
public class Property {

    @JsonProperty("key")
    private String key;
    @JsonProperty("value")
    private List<Object> value = null;
    @JsonProperty("nodePropertyCard")
    private String nodePropertyCard;
    @JsonProperty("nodePropertyCardMin")
    private Integer nodePropertyCardMin;
    @JsonProperty("nodePropertyCardMax")
    private Integer nodePropertyCardMax;
    @JsonProperty("nodePropertyType")
    private String nodePropertyType;

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("value")
    public List<Object> getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(List<Object> value) {
        this.value = value;
    }

    @JsonProperty("nodePropertyCard")
    public String getNodePropertyCard() {
        return nodePropertyCard;
    }

    @JsonProperty("nodePropertyCard")
    public void setNodePropertyCard(String nodePropertyCard) {
        this.nodePropertyCard = nodePropertyCard;
    }

    @JsonProperty("nodePropertyCardMin")
    public Integer getNodePropertyCardMin() {
        return nodePropertyCardMin;
    }

    @JsonProperty("nodePropertyCardMin")
    public void setNodePropertyCardMin(Integer nodePropertyCardMin) {
        this.nodePropertyCardMin = nodePropertyCardMin;
    }

    @JsonProperty("nodePropertyCardMax")
    public Integer getNodePropertyCardMax() {
        return nodePropertyCardMax;
    }

    @JsonProperty("nodePropertyCardMax")
    public void setNodePropertyCardMax(Integer nodePropertyCardMax) {
        this.nodePropertyCardMax = nodePropertyCardMax;
    }

    @JsonProperty("nodePropertyType")
    public String getNodePropertyType() {
        return nodePropertyType;
    }

    @JsonProperty("nodePropertyType")
    public void setNodePropertyType(String nodePropertyType) {
        this.nodePropertyType = nodePropertyType;
    }

}
