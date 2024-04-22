package gg.bayes.challenge.service.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.*;

public class CombatLogLineParserTest {
    @Test
    public void shouldParseHeroKilled() {
        String line = "[00:11:20.322] npc_dota_hero_rubick is killed by npc_dota_hero_pangolier";
        CombatLogLineParser.HeroKilledLineParser parser = new CombatLogLineParser.HeroKilledLineParser();
        assertTrue(parser.matches(line));
        CombatLogEntryEntity combatLogEntryEntity = parser.parse(line);
        assertEquals(LocalTime.parse("00:11:20.322", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), combatLogEntryEntity.getTimestamp());
        assertEquals("pangolier", combatLogEntryEntity.getActor());
        assertEquals("rubick", combatLogEntryEntity.getTarget());
    }

    @Test
    public void shouldParseItemPurchased() {
        String line = "[00:11:23.821] npc_dota_hero_rubick buys item item_magic_stick";
        CombatLogLineParser.ItemPurchasedLineParser parser = new CombatLogLineParser.ItemPurchasedLineParser();
        assertTrue(parser.matches(line));
        CombatLogEntryEntity combatLogEntryEntity = parser.parse(line);
        assertEquals(LocalTime.parse("00:11:23.821", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), combatLogEntryEntity.getTimestamp());
        assertEquals("rubick", combatLogEntryEntity.getActor());
        assertEquals("magic_stick", combatLogEntryEntity.getItem());

    }

    @Test
    public void shouldParseSpellCastOnDotaUnknown() {
        String line = "[00:11:24.654] npc_dota_hero_puck casts ability puck_illusory_orb (lvl 1) on dota_unknown";
        CombatLogLineParser.SpellCastLineParser parser = new CombatLogLineParser.SpellCastLineParser();
        assertTrue(parser.matches(line));
        CombatLogEntryEntity combatLogEntryEntity = parser.parse(line);
        assertEquals(LocalTime.parse("00:11:24.654", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), combatLogEntryEntity.getTimestamp());
        assertEquals("puck", combatLogEntryEntity.getActor());
        assertEquals("puck_illusory_orb", combatLogEntryEntity.getAbility());
        assertEquals(1, combatLogEntryEntity.getAbilityLevel());
        assertEquals(null, combatLogEntryEntity.getTarget());
    }

    @Test
    public void shouldParseSpellCastOnTarget() {
        String line = "[00:11:28.553] npc_dota_hero_bloodseeker casts ability bloodseeker_bloodrage (lvl 1) on npc_dota_hero_bloodseeker";
        CombatLogLineParser.SpellCastLineParser parser = new CombatLogLineParser.SpellCastLineParser();
        assertTrue(parser.matches(line));
        CombatLogEntryEntity combatLogEntryEntity = parser.parse(line);
        assertEquals(LocalTime.parse("00:11:28.553", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), combatLogEntryEntity.getTimestamp());
        assertEquals("bloodseeker", combatLogEntryEntity.getActor());
        assertEquals("bloodseeker_bloodrage", combatLogEntryEntity.getAbility());
        assertEquals(1, combatLogEntryEntity.getAbilityLevel());
        assertEquals("bloodseeker", combatLogEntryEntity.getTarget());
    }

    @Test
    public void shouldParseDamageDone() {
        String line = "[00:11:28.286] npc_dota_hero_bane hits npc_dota_hero_puck with dota_unknown for 60 damage (275->215)";
        CombatLogLineParser.DamageDoneLineParser parser = new CombatLogLineParser.DamageDoneLineParser();
        assertTrue(parser.matches(line));
        CombatLogEntryEntity combatLogEntryEntity = parser.parse(line);
        assertEquals(LocalTime.parse("00:11:28.286", DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY), combatLogEntryEntity.getTimestamp());
        assertEquals("bane", combatLogEntryEntity.getActor());
        assertEquals("puck", combatLogEntryEntity.getTarget());
        assertEquals(60, combatLogEntryEntity.getDamage());
    }

    @Test
    public void shouldNotParseHeroKilled() {
        String line = "[00:11:20.322] game state is now 2";
        CombatLogLineParser.HeroKilledLineParser parser = new CombatLogLineParser.HeroKilledLineParser();
        assertFalse(parser.matches(line));
    }

    @Test
    public void shouldNotParseItemPurchased() {
        String line = "[00:11:20.322] game state is now 2";
        CombatLogLineParser.ItemPurchasedLineParser parser = new CombatLogLineParser.ItemPurchasedLineParser();
        assertFalse(parser.matches(line));
    }

    @Test
    public void shouldNotParseSpellCast() {
        String line = "[00:11:20.322] game state is now 2";
        CombatLogLineParser.SpellCastLineParser parser = new CombatLogLineParser.SpellCastLineParser();
        assertFalse(parser.matches(line));
    }

    @Test
    public void shouldNotParseDamageDone() {
        String line = "[00:11:20.322] game state is now 2";
        CombatLogLineParser.DamageDoneLineParser parser = new CombatLogLineParser.DamageDoneLineParser();
        assertFalse(parser.matches(line));
    }
}
