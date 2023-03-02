package com.example.mangkyuproject.domain.repository;

import static com.example.mangkyuproject.domain.membership.MembershipType.NAVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.mangkyuproject.domain.membership.Membership;
import com.example.mangkyuproject.domain.membership.MembershipType;
import com.example.mangkyuproject.domain.repository.MembershipRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MembershipRepositoryTest {
    @Autowired
    private MembershipRepository membershipRepository;

    @Test
    public void 멤버십등록() {
        // given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(NAVER)
                .point(10000)
                .build();

        // when
        final Membership result = membershipRepository.save(membership); // 등록

        // then
        assertNotNull(result.getId());
        assertEquals("userId", result.getUserId());
        assertEquals(NAVER, result.getMembershipType());
        assertEquals(10000, result.getPoint());
    }

    @Test
    public void 멤버십이존재하는지테스트() {
        // given
        final Membership membership = Membership.builder()
                .userId("userId")
                .membershipType(NAVER)
                .point(10000)
                .build();

        // when
        membershipRepository.save(membership); // 저장하고,
        // 저장한 멤버십 정보를 가져오기.
        final Membership findResult = membershipRepository.findByUserIdAndMembershipType("userId", NAVER);

        // then
        assertNotNull(findResult);
        assertNotNull(findResult.getId());
        assertEquals("userId", findResult.getUserId());
        assertEquals(NAVER, findResult.getMembershipType());
        assertEquals(10000, findResult.getPoint());
    }

    @Test
    public void 멤버십조회_사이즈가0() {
        // given

        // when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void 멤버십조회_사이즈가2() {
        // given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership kakaoMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.KAKAO)
                .point(10000)
                .build();

        membershipRepository.save(naverMembership);
        membershipRepository.save(kakaoMembership);

        // when
        List<Membership> result = membershipRepository.findAllByUserId("userId");

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void 멤버십추가후삭제() {
        // given
        final Membership naverMembership = Membership.builder()
                .userId("userId")
                .membershipType(MembershipType.NAVER)
                .point(10000)
                .build();

        final Membership savedMembership = membershipRepository.save(naverMembership);

        // when
        membershipRepository.deleteById(savedMembership.getId());

        // then
    }
}
