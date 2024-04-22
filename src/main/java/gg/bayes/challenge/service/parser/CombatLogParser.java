package gg.bayes.challenge.service.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CombatLogParser {
    private final List<CombatLogLineParser> combatLogLineParsers = List.of(
            new CombatLogLineParser.ItemPurchasedLineParser(),
            new CombatLogLineParser.HeroKilledLineParser(),
            new CombatLogLineParser.SpellCastLineParser(),
            new CombatLogLineParser.DamageDoneLineParser()
    );

    public Set<CombatLogEntryEntity> parse(String combatLog) {
        return combatLog
                .lines()
                .map(
                        line -> combatLogLineParsers.stream()
                                .filter(p -> p.matches(line))
                                .findFirst().map(p->p.parse(line)))
                .filter(o->o.isPresent())
                .map(Optional::get)
                .collect(Collectors.toSet());
    }
}
