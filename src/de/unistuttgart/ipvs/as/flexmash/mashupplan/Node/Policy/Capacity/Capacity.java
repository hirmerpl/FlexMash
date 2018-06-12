
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Capacity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Capacity.Disk.Disk;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Capacity.MaximumProcessingPower.MaximumProcessingPower;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Capacity.Memory.Memory;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "memory",
    "disk",
    "maximumProcessingPower"
})
public class Capacity {

    @JsonProperty("memory")
    private Memory memory;
    @JsonProperty("disk")
    private Disk disk;
    @JsonProperty("maximumProcessingPower")
    private MaximumProcessingPower maximumProcessingPower;

    @JsonProperty("memory")
    public Memory getMemory() {
        return memory;
    }

    @JsonProperty("memory")
    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    @JsonProperty("disk")
    public Disk getDisk() {
        return disk;
    }

    @JsonProperty("disk")
    public void setDisk(Disk disk) {
        this.disk = disk;
    }

    @JsonProperty("maximumProcessingPower")
    public MaximumProcessingPower getMaximumProcessingPower() {
        return maximumProcessingPower;
    }

    @JsonProperty("maximumProcessingPower")
    public void setMaximumProcessingPower(MaximumProcessingPower maximumProcessingPower) {
        this.maximumProcessingPower = maximumProcessingPower;
    }

}
