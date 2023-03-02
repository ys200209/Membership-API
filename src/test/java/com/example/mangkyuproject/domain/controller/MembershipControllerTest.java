package com.example.mangkyuproject.domain.controller;

import static com.example.mangkyuproject.domain.membership.MembershipConstants.USER_ID_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.mangkyuproject.domain.controller.advice.GlobalExceptionHandler;
import com.example.mangkyuproject.domain.membership.MembershipType;
import com.example.mangkyuproject.domain.membership.dto.MembershipDetailResponse;
import com.example.mangkyuproject.domain.membership.dto.MembershipRequest;
import com.example.mangkyuproject.domain.membership.dto.MembershipAddResponse;
import com.example.mangkyuproject.domain.service.MembershipService;
import com.example.mangkyuproject.utils.exception.MembershipErrorResult;
import com.example.mangkyuproject.utils.exception.MembershipException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

//@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    @InjectMocks
    private MembershipController membershipController;

    @Mock
    private MembershipService membershipService;

    private MockMvc mockMvc;
    private Gson gson;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(membershipController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void 멤버십_등록실패_사용자_식별값이_헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
//                        .content(objectMapper.writeValueAsString(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("invalidRequestParam")
    public void 멤버십등록실패_필드범위검증_실패(MembershipRequest request) throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십등록실패_MemberService에서_Duplicated_예외발생() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        when(membershipService.addMembership("12345", MembershipType.NAVER, 10000))
                .thenThrow(new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void 멤버십등록성공() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        final MembershipAddResponse membershipAddResponse = MembershipAddResponse.builder()
                .id(-1L)
                .membershipType(MembershipType.NAVER).build();

        when(membershipService.addMembership("12345", MembershipType.NAVER, 10000)).thenReturn(membershipAddResponse);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .header(USER_ID_HEADER, "12345")
                        .content(gson.toJson(membershipRequest(10000, MembershipType.NAVER)))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isCreated());

        final MembershipAddResponse response = gson.fromJson(
                resultActions
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8), MembershipAddResponse.class);

        assertThat(response.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(response.getId()).isNotNull();
    }

    @Test
    public void 멤버십목록조회실패_사용자식별값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십목록조회성공() throws Exception {
        // given
        final String url = "/api/v1/memberships";
        when(membershipService.getMembershipList("12345")).thenReturn(
                List.of(
                        MembershipDetailResponse.builder().build(),
                        MembershipDetailResponse.builder().build(),
                        MembershipDetailResponse.builder().build()
        ));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void 멤버십삭제실패_사용자식별값이헤더에없음() throws Exception {
        // given
        final String url = "/api/v1/memberships/-1";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십삭제성공() throws Exception {
        // given
        final String url = "/api/v1/memberships/-1";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.delete(url)
                        .header(USER_ID_HEADER, "12345")
        );

        // then
        resultActions.andExpect(status().isNoContent());
    }

    private static Stream<Arguments> invalidRequestParam() {
        return Stream.of(
                Arguments.of(new MembershipRequest(null, MembershipType.NAVER)),
                Arguments.of(new MembershipRequest(-1, MembershipType.NAVER)),
                Arguments.of(new MembershipRequest(10000, null))
        );
    }

    private MembershipRequest membershipRequest(final Integer point, final MembershipType membershipType) {
        return MembershipRequest.builder()
                .point(point)
                .membershipType(membershipType)
                .build();
    }
}
