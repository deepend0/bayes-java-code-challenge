package gg.bayes.challenge.service;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.persistence.model.MatchEntity;
import gg.bayes.challenge.persistence.repository.CombatLogEntryRepository;
import gg.bayes.challenge.persistence.repository.MatchRepository;
import gg.bayes.challenge.service.model.HeroDamage;
import gg.bayes.challenge.service.model.HeroItem;
import gg.bayes.challenge.service.model.HeroKills;
import gg.bayes.challenge.service.model.HeroSpells;
import gg.bayes.challenge.service.parser.CombatLogParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class MatchService {
    private final CombatLogParser combatLogParser;
    private final MatchRepository matchRepository;
    private final CombatLogEntryRepository combatLogEntryRepository;

    public Long ingestCombatLog(String combatLog) {
        MatchEntity matchEntity = new MatchEntity();
        Set<CombatLogEntryEntity> combatLogEntryEntities = combatLogParser.parse(combatLog);
        matchEntity.setCombatLogEntries(combatLogEntryEntities);
        combatLogEntryEntities.forEach(combatLogEntryEntity -> combatLogEntryEntity.setMatch(matchEntity));
        MatchEntity matchEntitySaved = matchRepository.save(matchEntity);
        return matchEntitySaved.getId();
    }

    public List<HeroKills> getHeroKills(Long matchId) {
        return combatLogEntryRepository.getMatchHeroKills(matchId);
    }

    public List<HeroItem> getHeroItems(Long matchId, String heroName) {
        return combatLogEntryRepository.getMatchHeroItems(matchId, heroName);
    }

    public List<HeroSpells> getHeroSpells(Long matchId, String heroName) {
        return combatLogEntryRepository.getMatchHeroSpells(matchId, heroName);
    }


    public List<HeroDamage> getHeroDamage(Long matchId, String heroName) {
        return combatLogEntryRepository.getMatchHeroDamage(matchId, heroName);
    }
}
