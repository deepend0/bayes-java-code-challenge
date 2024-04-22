package gg.bayes.challenge.service.parser;

import gg.bayes.challenge.persistence.model.CombatLogEntryEntity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract sealed class CombatLogLineParser {
    private final String patternStr;
    private final Pattern pattern;

    public CombatLogLineParser(String patternStr) {
        this.patternStr = patternStr;
        this.pattern = Pattern.compile(patternStr);
    }

    public boolean matches(String line) {
        Matcher m = pattern.matcher(line);
        return m.matches();
    }

    public CombatLogEntryEntity parse(String line) {
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(line);
        m.matches();
        return map(m);
    }

    public abstract CombatLogEntryEntity map(Matcher m);

    public static final class ItemPurchasedLineParser extends CombatLogLineParser {

        private static final String PATTERN = "\\[(.*)\\] npc_dota_hero_(.*) buys item item_(.*)";

        public ItemPurchasedLineParser() {
            super(PATTERN);
        }

        @Override
        public CombatLogEntryEntity map(Matcher m) {
            CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
            combatLogEntryEntity.setTimestamp(LocalTime.parse(m.group(1), DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY));
            combatLogEntryEntity.setActor(m.group(2));
            combatLogEntryEntity.setItem(m.group(3));
            combatLogEntryEntity.setType(CombatLogEntryEntity.Type.ITEM_PURCHASED);
            return combatLogEntryEntity;
        }
    }

    public static final class HeroKilledLineParser extends CombatLogLineParser {

        private static final String PATTERN = "\\[(.*)\\] npc_dota_hero_(.*) is killed by npc_dota_hero_(.*)";

        public HeroKilledLineParser() {
            super(PATTERN);
        }

        @Override
        public CombatLogEntryEntity map(Matcher m) {
            CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
            combatLogEntryEntity.setTimestamp(LocalTime.parse(m.group(1), DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY));
            combatLogEntryEntity.setTarget(m.group(2));
            combatLogEntryEntity.setActor(m.group(3));
            combatLogEntryEntity.setType(CombatLogEntryEntity.Type.HERO_KILLED);
            return combatLogEntryEntity;
        }
    }

    public static final class SpellCastLineParser extends CombatLogLineParser {

        private static final String PATTERN = "\\[(.*)\\] npc_dota_hero_(.*) casts ability (.*) \\(lvl (.*)\\) on (dota_unknown|npc_dota_hero_(.*))";

        public SpellCastLineParser() {
            super(PATTERN);
        }

        @Override
        public CombatLogEntryEntity map(Matcher m) {
            CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
            combatLogEntryEntity.setTimestamp(LocalTime.parse(m.group(1), DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY));
            combatLogEntryEntity.setActor(m.group(2));
            combatLogEntryEntity.setAbility(m.group(3));
            combatLogEntryEntity.setAbilityLevel(Integer.parseInt(m.group(4)));
            if(!m.group(5).equals("dota_unknown")) {
                combatLogEntryEntity.setTarget(m.group(6));
            }
            combatLogEntryEntity.setType(CombatLogEntryEntity.Type.SPELL_CAST);
            return combatLogEntryEntity;
        }
    }

    public static final class DamageDoneLineParser extends CombatLogLineParser {

        private static final String PATTERN = "\\[(.*)\\] npc_dota_hero_(.*) hits npc_dota_hero_(.*) with (.*) for (.*) damage \\((.*)->(.*)\\)";

        public DamageDoneLineParser() {
            super(PATTERN);
        }

        @Override
        public CombatLogEntryEntity map(Matcher m) {
            CombatLogEntryEntity combatLogEntryEntity = new CombatLogEntryEntity();
            combatLogEntryEntity.setTimestamp(LocalTime.parse(m.group(1), DateTimeFormatter.ISO_LOCAL_TIME).getLong(ChronoField.MILLI_OF_DAY));
            combatLogEntryEntity.setActor(m.group(2));
            combatLogEntryEntity.setTarget(m.group(3));
            if(m.group(4).equals("dota_unknown")) {

            } else if(m.group(4).startsWith("item")) {
                combatLogEntryEntity.setItem(m.group(4));
            } else {
                combatLogEntryEntity.setAbility(m.group(4));
            }
            combatLogEntryEntity.setDamage(Integer.parseInt(m.group(5)));
            combatLogEntryEntity.setType(CombatLogEntryEntity.Type.DAMAGE_DONE);
            return combatLogEntryEntity;
        }
    }
}
