package gg.bayes.challenge.service.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class CombatLogParserTest {
    private final CombatLogParser combatLogParser = new CombatLogParser();

    @Test
    public void shouldParseAllMatchingLines() {
        Set<CombatLogEntryEntity> entryEntities = combatLogParser.parse(
                "[00:08:41.094] game state is now 4\n" +
                "[00:08:43.460] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown\n" +
                "[00:08:48.059] npc_dota_hero_dragon_knight buys item item_circlet\n" +
                "[00:10:07.739] npc_dota_hero_mars uses item_quelling_blade\n" +
                "[00:10:42.031] npc_dota_hero_abyssal_underlord hits npc_dota_hero_bloodseeker with abyssal_underlord_firestorm for 18 damage (720->702)\n" +
                "[00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars\n"
        );
        assertEquals(4, entryEntities.size());
        Set<CombatLogEntryEntity> entryEntitiesExpected = Set.of(
                new CombatLogEntryEntity(null, null, LocalTime.parse("00:08:43.460", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), CombatLogEntryEntity.Type.SPELL_CAST, "pangolier", null, "pangolier_swashbuckle", 1, null, null),
            new CombatLogEntryEntity(null, null,LocalTime.parse("00:08:48.059", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), CombatLogEntryEntity.Type.ITEM_PURCHASED, "dragon_knight", null, null, null, "circlet", null),
            new CombatLogEntryEntity(null, null,LocalTime.parse("00:10:42.031", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), CombatLogEntryEntity.Type.DAMAGE_DONE, "abyssal_underlord", "bloodseeker", "abyssal_underlord_firestorm", null, null, 18),
            new CombatLogEntryEntity(null, null,LocalTime.parse("00:11:17.489", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), CombatLogEntryEntity.Type.HERO_KILLED, "mars", "snapfire", null, null, null, null)
        );
        assertEquals(entryEntitiesExpected, entryEntities);
    }
}
