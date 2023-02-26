package com.example.mangkyuproject.study;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@Slf4j
public class StudyTest {
    @Mock
    private StudyController controller;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Gson gson;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        gson = new Gson();
    }

    @Test
    void testRequestBody() throws Exception {
        // given
        Study study = new Study("테스트이름", 20);

        // then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/RequestBody")
//                        .content(objectMapper.writeValueAsString(study))
                        .content(gson.toJson(study))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void testModelAttribute() throws Exception {
        // given
        Study study = new Study("테스트이름", 20);

        // when
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/ModelAttribute")
                        .content(objectMapper.writeValueAsString(study))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());


        // then

    }

    @RestController
    static class StudyController {
        @GetMapping("/api/RequestBody")
        public String RequestBody(@RequestBody Study request) {
//            log.info("name: {}", request.getName());
//            log.info("age: {}", request.getAge());
            return "OK";
        }

        @GetMapping("/api/ModelAttribute")
        public String ModelAttribute(@ModelAttribute Study request) {
//            log.info("name: {}", request.getName());
//            log.info("age: {}", request.getAge());
            return "OK";
        }
    }

    @Getter
    @RequiredArgsConstructor
    @NoArgsConstructor(force = true)
    static class Study {
        private final String name;
        private final Integer age;
    }
}
