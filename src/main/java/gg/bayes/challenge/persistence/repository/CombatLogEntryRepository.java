package gg.bayes.challenge.persistence.repository;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import gg.bayes.challenge.service.model.HeroDamage;
import gg.bayes.challenge.service.model.HeroItem;
import gg.bayes.challenge.service.model.HeroKills;
import gg.bayes.challenge.service.model.HeroSpells;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombatLogEntryRepository extends JpaRepository<CombatLogEntryEntity, Long> {
    @Query("SELECT new gg.bayes.challenge.rest.model.HeroKills(cle.actor, CAST(count(cle.actor) AS Integer)) FROM CombatLogEntryEntity cle JOIN cle.match m WHERE m.id=:matchId AND cle.type = 'HERO_KILLED' GROUP BY cle.actor")
    List<HeroKills> getMatchHeroKills(Long matchId);

    @Query("SELECT new gg.bayes.challenge.rest.model.HeroItem(cle.item, cle.timestamp) FROM CombatLogEntryEntity cle JOIN cle.match m WHERE m.id=:matchId AND cle.type = 'ITEM_PURCHASED' AND cle.actor=:heroName")
    List<HeroItem> getMatchHeroItems(Long matchId, String heroName);

    @Query("SELECT new gg.bayes.challenge.rest.model.HeroSpells(cle.ability, CAST(count(cle.ability) AS Integer)) FROM CombatLogEntryEntity cle JOIN cle.match m WHERE m.id=:matchId AND cle.type = 'SPELL_CAST' AND cle.actor=:heroName GROUP BY cle.ability")
    List<HeroSpells> getMatchHeroSpells(Long matchId, String heroName);

    @Query("SELECT new gg.bayes.challenge.rest.model.HeroDamage(cle.target, CAST(count(cle.target) AS Integer), CAST(sum(cle.damage) AS Integer)) FROM CombatLogEntryEntity cle JOIN cle.match m WHERE m.id=:matchId AND cle.type = 'DAMAGE_DONE' AND cle.actor=:heroName GROUP BY cle.target")
    List<HeroDamage> getMatchHeroDamage(Long matchId, String heroName);
}
