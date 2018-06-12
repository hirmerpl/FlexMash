
package de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Recoverability;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Recoverability.BackupInterval.BackupInterval;
import de.unistuttgart.ipvs.as.flexmash.mashupplan.Node.Policy.Recoverability.RecoveryTime.RecoveryTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "backupInterval",
    "recoveryTime"
})
public class Recoverability {

    @JsonProperty("backupInterval")
    private BackupInterval backupInterval;
    @JsonProperty("recoveryTime")
    private RecoveryTime recoveryTime;

    @JsonProperty("backupInterval")
    public BackupInterval getBackupInterval() {
        return backupInterval;
    }

    @JsonProperty("backupInterval")
    public void setBackupInterval(BackupInterval backupInterval) {
        this.backupInterval = backupInterval;
    }

    @JsonProperty("recoveryTime")
    public RecoveryTime getRecoveryTime() {
        return recoveryTime;
    }

    @JsonProperty("recoveryTime")
    public void setRecoveryTime(RecoveryTime recoveryTime) {
        this.recoveryTime = recoveryTime;
    }

}
