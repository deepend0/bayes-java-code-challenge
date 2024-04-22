package gg.bayes.challenge.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/*
 * Integration test template to get you started. Add tests and make modifications as you see fit.
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MatchControllerIntegrationTest {

    private static final String COMBATLOG_FILE_1 = "/data/combatlog_1.log.txt";
    private static final String COMBATLOG_FILE_2 = "/data/combatlog_2.log.txt";

    @Autowired
    private MockMvc mvc;

    private Map<String, Long> matchIds;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    void setup() throws Exception {
        // Populate the database with all events from both sample data files and store the returned
        // match IDS.
        matchIds = Map.of(
                COMBATLOG_FILE_1, ingestMatch(COMBATLOG_FILE_1),
                COMBATLOG_FILE_2, ingestMatch(COMBATLOG_FILE_2));
    }

    @Test
    void shouldGetHeroKills() throws Exception {
        String jsonStr = mvc.perform(get("/api/match/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        Set heroKillsExpected = objectMapper.readValue("[{\"hero\":\"abyssal_underlord\",\"kills\":6},{\"hero\":\"bane\",\"kills\":2},{\"hero\":\"bloodseeker\",\"kills\":11},{\"hero\":\"death_prophet\",\"kills\":9},{\"hero\":\"dragon_knight\",\"kills\":3},{\"hero\":\"mars\",\"kills\":6},{\"hero\":\"pangolier\",\"kills\":5},{\"hero\":\"puck\",\"kills\":7},{\"hero\":\"rubick\",\"kills\":4},{\"hero\":\"snapfire\",\"kills\":2}]", Set.class);
        Set heroKills = objectMapper.readValue(jsonStr, Set.class);
        assertEquals(heroKillsExpected, heroKills);
    }

    @Test
    void shouldGetHeroItems() throws Exception {
        String jsonStr = mvc.perform(get("/api/match/1/abyssal_underlord/items"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        Set heroKillsExpected = objectMapper.readValue("[{\"item\":\"gauntlets\",\"timestamp\":844248},{\"item\":\"magic_stick\",\"timestamp\":652229},{\"item\":\"arcane_boots\",\"timestamp\":1324464},{\"item\":\"energy_booster\",\"timestamp\":1324464},{\"item\":\"tpscroll\",\"timestamp\":1647619},{\"item\":\"recipe_headdress\",\"timestamp\":1454966},{\"item\":\"ring_of_regen\",\"timestamp\":843915},{\"item\":\"cloak\",\"timestamp\":1173901},{\"item\":\"soul_ring\",\"timestamp\":844448},{\"item\":\"chainmail\",\"timestamp\":1806480},{\"item\":\"ring_of_regen\",\"timestamp\":1454466},{\"item\":\"recipe_magic_wand\",\"timestamp\":753604},{\"item\":\"recipe_headdress\",\"timestamp\":1806280},{\"item\":\"wind_lace\",\"timestamp\":754670},{\"item\":\"boots\",\"timestamp\":922296},{\"item\":\"hood_of_defiance\",\"timestamp\":1200328},{\"item\":\"ring_of_regen\",\"timestamp\":1806113},{\"item\":\"headdress\",\"timestamp\":1454966},{\"item\":\"ring_of_regen\",\"timestamp\":1179833},{\"item\":\"ring_of_health\",\"timestamp\":1123980},{\"item\":\"recipe_mekansm\",\"timestamp\":1807680},{\"item\":\"pipe\",\"timestamp\":1487158},{\"item\":\"magic_wand\",\"timestamp\":791461},{\"item\":\"recipe_pipe\",\"timestamp\":1455132},{\"item\":\"guardian_greaves\",\"timestamp\":2005132},{\"item\":\"gauntlets\",\"timestamp\":844082},{\"item\":\"recipe_guardian_greaves\",\"timestamp\":1994401},{\"item\":\"headdress\",\"timestamp\":1806280},{\"item\":\"mekansm\",\"timestamp\":1807946},{\"item\":\"recipe_soul_ring\",\"timestamp\":844448}]", Set.class);
        Set heroKills = objectMapper.readValue(jsonStr, Set.class);
        assertEquals(heroKillsExpected, heroKills);
    }

    @Test
    void shouldGetHeroSpells() throws Exception {
        String jsonStr = mvc.perform(get("/api/match/1/abyssal_underlord/spells"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        Set heroKillsExpected = objectMapper.readValue("[{\"spell\":\"abyssal_underlord_cancel_dark_rift\",\"casts\":1},{\"spell\":\"abyssal_underlord_dark_rift\",\"casts\":3},{\"spell\":\"abyssal_underlord_firestorm\",\"casts\":67},{\"spell\":\"abyssal_underlord_pit_of_malice\",\"casts\":14}]", Set.class);
        Set heroKills = objectMapper.readValue(jsonStr, Set.class);
        assertEquals(heroKillsExpected, heroKills);
    }

    @Test
    void shouldGetHeroDamages() throws Exception {
        String jsonStr = mvc.perform(get("/api/match/1/abyssal_underlord/damage"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        Set heroKillsExpected = objectMapper.readValue("[{\"target\":\"bane\",\"damage_instances\":67,\"total_damage\":3414},{\"target\":\"bloodseeker\",\"damage_instances\":194,\"total_damage\":6030},{\"target\":\"death_prophet\",\"damage_instances\":75,\"total_damage\":5781},{\"target\":\"mars\",\"damage_instances\":18,\"total_damage\":1205},{\"target\":\"rubick\",\"damage_instances\":27,\"total_damage\":1624}]", Set.class);
        Set heroKills = objectMapper.readValue(jsonStr, Set.class);
        assertEquals(heroKillsExpected, heroKills);
    }

    /**
     * Helper method that ingests a combat log file and returns the match id associated with all parsed events.
     *
     * @param file file path as a classpath resource, e.g.: /data/combatlog_1.log.txt.
     * @return the id of the match associated with the events parsed from the given file
     * @throws Exception if an error happens when reading or ingesting the file
     */
    private Long ingestMatch(String file) throws Exception {
        String fileContent = IOUtils.resourceToString(file, StandardCharsets.UTF_8);

        return Long.parseLong(mvc.perform(post("/api/match")
                                         .contentType(MediaType.TEXT_PLAIN)
                                         .content(fileContent))
                                 .andReturn()
                                 .getResponse()
                                 .getContentAsString());
    }
}
