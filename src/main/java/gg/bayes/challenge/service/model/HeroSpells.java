package gg.bayes.challenge.service.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class HeroSpells {
    String spell;
    Integer casts;
}
