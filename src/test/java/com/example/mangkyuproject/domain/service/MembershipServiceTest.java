package com.example.mangkyuproject.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.mangkyuproject.domain.membership.Membership;
import com.example.mangkyuproject.domain.membership.MembershipType;
import com.example.mangkyuproject.domain.repository.MembershipRepository;
import com.example.mangkyuproject.domain.membership.dto.MembershipResponse;
import com.example.mangkyuproject.utils.exception.MembershipErrorResult;
import com.example.mangkyuproject.utils.exception.MembershipException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MembershipServiceTest {
    private final String userId = "userId";
    private final MembershipType membershipType = MembershipType.NAVER;
    private final Integer point = 10000;

    @InjectMocks
    MembershipService membershipService;
    
    @Mock
    MembershipRepository membershipRepository;
    
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
        final MembershipResponse actual = membershipService.addMembership(userId, membershipType, point);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMembershipType()).isEqualTo(MembershipType.NAVER);

        // verify
        verify(membershipRepository, times(1)).findByUserIdAndMembershipType(userId, membershipType);
        verify(membershipRepository, times(1)).save(any(Membership.class));
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
