package com.example.mangkyuproject.study;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@Import(testConfig.class)
public class StudyTest {
    @InjectMocks
    private StudyController controller;

    private Gson gson;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        gson = new Gson();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testApiRequestBody() throws Exception {
        // given
        RequestDto RequestDto = new RequestDto("req", 1, "pass", "email");
        String jsonObject =
        "{"
                + "\"name\": \"req\""
                + "\"age\": 1"
        + "}";

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/requestBody/")
                .content(gson.toJson(RequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("req"))
                .andExpect(jsonPath("age").value("1"))
                .andExpect(jsonPath("password").value("pass"))
                .andExpect(jsonPath("email").value("email"))
                .andDo(print());

        // then
    }

    @Test
    void testApiModelAttribute() throws Exception {
        // given
        RequestDto requestDto = new RequestDto("req", 1, "pass", "email");

        // when // flashAttr()
        /*mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/ModelAttribute/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .flashAttr("requestDto", requestDto))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("req"))
                .andExpect(jsonPath("age").value("1"))
                .andExpect(jsonPath("password").value("pass"))
                .andExpect(jsonPath("email").value("email"))
                .andDo(print());*/

        // when // param(): FAIL
        /*mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/ModelAttribute/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", requestDto.getName())
                        .param("age", String.valueOf(requestDto.getAge()))
                        .param("password", "pass")
                        .param("email", "email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("req"))
                .andExpect(jsonPath("age").value("1"))
                .andExpect(jsonPath("password").value("pass"))
                .andExpect(jsonPath("email").value("email"))
                .andDo(print());*/

        // when // content(String query)
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/ModelAttribute/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("name=req&age=1&password=pass&email=email")
                        /*.param("name", requestDto.getName())
                        .param("age", String.valueOf(requestDto.getAge()))
                        .param("password", "pass")
                        .param("email", "email")*/)
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("req"))
                .andExpect(jsonPath("age").value("1"))
                .andExpect(jsonPath("password").value("pass"))
                .andExpect(jsonPath("email").value("email"))
                .andDo(print());

        // then
    }
}

/**
 * - 직접 생각한 @RequestBody 와 @ModelAttribute 의 차이점.
 * @RequestBody: setter 없이 getter만 있어도 파라미터 바인딩이 가능하므로, 좀 더 데이터를 안전하게 전달받을 수 있음.
 * @ModelAttribute: 얘는 파라미터 바인딩을 위해선 생성자가 필요한데, 생성자가 없다면 setter로만 파라미터 바인딩을 할 수 있다.
 * 즉, AllArgsConstructor 가 없는 채로 getter만 있으면 파라미터 바인딩이 충족되지 않음. 또한 ContentType이 폼 형식으로 강제됨.
 *
 * 이렇게만 보면 @ModelAttribute 가 유연하지 못해 보이는데도 불구하고 애플리케이션을 개발할 때 대부분을 @ModelAttribute로 사용한다. 왜지. 더 공부.
 */

@Controller
class StudyController {
    @GetMapping("/api/requestBody/")
    public ResponseEntity<RequestDto> testApiRequestBody(@RequestBody RequestDto requestDto) {
        System.out.println("\nRequestBody = " + requestDto + "\n");
        return ResponseEntity.ok(requestDto);
    }

    @PostMapping("/api/ModelAttribute/")
    public ResponseEntity<RequestDto> testApiModelAttribute(@ModelAttribute RequestDto requestDto) {
        System.out.println("\nModelAttribute = " + requestDto + "\n");
        return ResponseEntity.ok(requestDto);
    }
}

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ToString
class RequestDto { // DTO 클래스도 Bean으로 등록해주어야 하는지?
    private final String name;
    private final long age;
    private final String password;
    private final String email;
}

