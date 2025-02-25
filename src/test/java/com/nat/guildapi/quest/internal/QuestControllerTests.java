package com.nat.guildapi.quest.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nat.guildapi.quest.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestController.class)
public class QuestControllerTests {
    private final UUID questUUID = UUID.fromString("39795088-8fe1-4674-af0c-6c7abd30fdc6");
    private final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private QuestService questService;
    @MockBean
    private QuestModelAssembler questModelAssembler;
    @MockBean
    private PagedResourcesAssembler<QuestDto> pagedResourcesAssembler;

    @Test
    public void testGetAllQuestsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/v1/quests"))
            .andExpect(status().isFound());
    }

    @Test
    public void testGetAllQuestsAuthenticated() throws Exception {
        mockMvc
            .perform(
                get("/api/v1/quests")
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .jwt()
                            .authorities(new SimpleGrantedAuthority("read:quests"))
                    )
            )
            .andExpect(status().isOk());
    }

    @Test
    public void testCreateQuestUnauthorized() throws Exception {
        QuestCreateRequestDto questCreateRequestDto = createQuestCreateRequestDto();
        QuestDto questDto = createQuestDtoFromCreateRequestDto(questCreateRequestDto);

        given(questService.createQuest(questCreateRequestDto))
            .willReturn(questDto);

        String json = ow.writeValueAsString(questCreateRequestDto);

        mockMvc
            .perform(
                post("/api/v1/quests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
            )
            .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateQuestBadRequest() throws Exception {
        mockMvc
            .perform(
                post("/api/v1/quests")
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .jwt()
                            .authorities(new SimpleGrantedAuthority("create:quests"))
                    )
            )
            .andExpect(status().isUnsupportedMediaType());

        mockMvc
            .perform(
                post("/api/v1/quests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .jwt()
                            .authorities(new SimpleGrantedAuthority("create:quests"))
                    )
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateQuestWithAuthority() throws Exception {
        QuestCreateRequestDto questCreateRequestDto = createQuestCreateRequestDto();
        QuestDto questDto = createQuestDtoFromCreateRequestDto(questCreateRequestDto);
        EntityModel<QuestDto> questModel = EntityModel.of(questDto);

        given(questService.createQuest(questCreateRequestDto))
            .willReturn(questDto);

        String json = ow.writeValueAsString(questCreateRequestDto);

        mockMvc
            .perform(
                post("/api/v1/quests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .jwt()
                            .authorities(new SimpleGrantedAuthority("create:quests"))
                    )
            )
            .andExpect(status().isCreated())
            .andExpect(redirectedUrl("http://localhost/api/v1/quests/" + questUUID))
            .andDo(result -> {
                String redirectUrl = result.getResponse().getRedirectedUrl();

                given(questService.getQuestById(questUUID))
                    .willReturn(questDto);

                given(questModelAssembler.toModel(questDto))
                    .willReturn(questModel);

                assert redirectUrl != null;
                mockMvc.perform(
                        get(redirectUrl)
                            .with(
                                SecurityMockMvcRequestPostProcessors
                                    .jwt()
                                    .authorities(new SimpleGrantedAuthority("read:quests"))
                            )
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
                    .andExpect(jsonPath("questId").value(questUUID.toString()))
                    .andExpect(jsonPath("questTitle").value("Quest Title"))
                    .andExpect(jsonPath("questDescription").value("Quest Description"));
            });
    }

    @Test
    public void testUpdateQuestWithAuthority() throws Exception {
        QuestUpdateRequestDto questUpdateRequestDto = createQuestUpdateRequestDto();

        QuestDto updatedQuestDto = createQuestDtoFromUpdateRequestDto(questUpdateRequestDto);
        EntityModel<QuestDto> updatedQuestModel = EntityModel.of(updatedQuestDto);

        given(questService.updateQuest(questUUID, questUpdateRequestDto))
            .willReturn(updatedQuestDto);

        given(questModelAssembler.toModel(updatedQuestDto))
            .willReturn(updatedQuestModel);

        String json = ow.writeValueAsString(questUpdateRequestDto);

        mockMvc
            .perform(
                put("/api/v1/quests/" + questUUID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .with(
                        SecurityMockMvcRequestPostProcessors
                            .jwt()
                            .authorities(new SimpleGrantedAuthority("update:quests"))
                    )
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("questId").value(questUUID.toString()))
            .andExpect(jsonPath("questTitle").value("New Quest Title"))
            .andExpect(jsonPath("questDescription").value("New Quest Description"));
    }

    private QuestCreateRequestDto createQuestCreateRequestDto() {
        QuestCreateRequestDto questCreateRequestDto = new QuestCreateRequestDto();
        questCreateRequestDto.setQuestTitle("Quest Title");
        questCreateRequestDto.setQuestDescription("Quest Description");
        return questCreateRequestDto;
    }

    private QuestUpdateRequestDto createQuestUpdateRequestDto() {
        QuestUpdateRequestDto questUpdateRequestDto = new QuestUpdateRequestDto();
        questUpdateRequestDto.setQuestTitle("New Quest Title");
        questUpdateRequestDto.setQuestDescription("New Quest Description");
        return questUpdateRequestDto;
    }

    private QuestDto createQuestDtoFromCreateRequestDto(QuestCreateRequestDto questCreateRequestDto) {
        QuestDto questDto = new QuestDto();
        questDto.setQuestId(questUUID);
        questDto.setQuestTitle(questCreateRequestDto.getQuestTitle());
        questDto.setQuestDescription(questCreateRequestDto.getQuestDescription());
        return questDto;
    }

    private QuestDto createQuestDtoFromUpdateRequestDto(QuestUpdateRequestDto questUpdateRequestDto) throws Exception {
        QuestDto questDto = new QuestDto();
        questDto.setQuestId(questUUID);
        questDto.setQuestTitle(questUpdateRequestDto.getQuestTitle());
        questDto.setQuestDescription(questUpdateRequestDto.getQuestDescription());
        return questDto;
    }
}
