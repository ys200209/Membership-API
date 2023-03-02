package com.example.mangkyuproject.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mangkyuproject.domain.membership.Membership;
import com.example.mangkyuproject.domain.membership.MembershipType;
import com.example.mangkyuproject.domain.membership.dto.MembershipDetailResponse;
import com.example.mangkyuproject.domain.repository.MembershipRepository;
import com.example.mangkyuproject.domain.membership.dto.MembershipAddResponse;
import com.example.mangkyuproject.utils.exception.MembershipErrorResult;
import com.example.mangkyuproject.utils.exception.MembershipException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MembershipServiceTest {
    private final String userId = "userId";
    private final Long membershipId = -1L;
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;

    @InjectMocks
    MembershipService membershipService;
    
    @Mock
    MembershipRepository membershipRepository;
    @Mock
    private RatePointService ratePointService;
    
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void 멤버십등록실패_중복된_회원() {
        // given
        when(membershipRepository.findByUserIdAndMembershipType(userId, membershipType)).thenReturn(Membership.builder().build());

        // when
        final MembershipException membershipException = assertThrows(MembershipException.class, () ->
                membershipService.addMembership(userId, membershipType, point));

        // then
        assertEquals(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER, membershipException.getErrorResult());
    }

    @Test
    public void 멤버십_등록성공() {
        // given
        when(membershipRepository.findByUserIdAndMembershipType(userId, membershipType)).thenReturn(null);
        when(membershipRepository.save(any(Membership.class))).thenReturn(membership());

        // when
        final MembershipAddResponse actual = membershipService.addMembership(userId, membershipType, point);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
    }

    @Test
    public void 멤버십목록조회() {
        // given
        when(membershipRepository.findAllByUserId(userId)).thenReturn(
                List.of(
                        Membership.builder().build(),
                        Membership.builder().build(),
                        Membership.builder().build()
        ));

        // when
        final List<MembershipDetailResponse> result = membershipService.getMembershipList(userId);

        // then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    public void 멤버십상세조회실패_존재하지않음() {
        // given
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> membershipService.getMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십상세조회실패_본인이아님() {
        // given
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> membershipService.getMembership(membershipId, "notowner"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십상세조회성공() {
        // given
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership()));

        // when
        final MembershipDetailResponse result = membershipService.getMembership(membershipId, userId);

        // then
        assertThat(result.getMembershipType()).isEqualTo(MembershipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(point);
    }

    @Test
    public void 멤버십삭제실패_존재하지않음() {
        // given
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> membershipService.removeMembership(membershipId, userId));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십삭제실패_본인이아님() {
        // given
        final Membership membership = membership();
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));

        // when
        final MembershipException result = assertThrows(MembershipException.class, () -> membershipService.removeMembership(membershipId, "notowner"));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    public void 멤버십삭제성공() {
        // given
        final Membership membership = membership();
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));

        // when
        membershipService.removeMembership(membershipId, userId);

        // then
    }

    //

    @Test
    public void 멤버십적립실패_존재하지않음() {
        // given
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.empty());

        // when
        final MembershipException result = assertThrows(MembershipException.class, () ->
                membershipService.accumulateMembershipPoint(membershipId, userId, 10000));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십적립실패_본인이아님() {
        // given
        final Membership membership = membership();
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));

        // when
        final MembershipException result = assertThrows(MembershipException.class, () ->
                membershipService.accumulateMembershipPoint(membershipId, "notowner", 10000));

        // then
        assertThat(result.getErrorResult()).isEqualTo(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
    }

    @Test
    public void 멤버십적립성공() {
        // given
        final Membership membership = membership();
        when(membershipRepository.findById(membershipId)).thenReturn(Optional.of(membership));

        // when
        membershipService.accumulateMembershipPoint(membershipId, userId, 10000);

        // then

    }

    private Membership membership() {
        return Membership.builder()
                .id(-1L)
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER)
                .build();
    }
}
