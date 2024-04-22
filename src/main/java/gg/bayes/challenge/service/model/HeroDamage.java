package gg.bayes.challenge.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class HeroDamage {
    String target;
    @JsonProperty("damage_instances")
    Integer damageInstances;
    @JsonProperty("total_damage")
    Integer totalDamage;
}
