package com.example.mangkyuproject.domain.service;

import com.example.mangkyuproject.domain.membership.Membership;
import com.example.mangkyuproject.domain.membership.MembershipType;
import com.example.mangkyuproject.domain.repository.MembershipRepository;
import com.example.mangkyuproject.domain.membership.dto.MembershipResponse;
import com.example.mangkyuproject.utils.exception.MembershipErrorResult;
import com.example.mangkyuproject.utils.exception.MembershipException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;

    public MembershipResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        Membership savedMembership = membershipRepository.save(Membership.builder()
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER)
                .build());

        return MembershipResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }
}
