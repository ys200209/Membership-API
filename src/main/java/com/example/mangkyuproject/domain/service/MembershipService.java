package com.example.mangkyuproject.domain.service;

import com.example.mangkyuproject.domain.membership.Membership;
import com.example.mangkyuproject.domain.membership.MembershipType;
import com.example.mangkyuproject.domain.membership.dto.MembershipDetailResponse;
import com.example.mangkyuproject.domain.repository.MembershipRepository;
import com.example.mangkyuproject.domain.membership.dto.MembershipAddResponse;
import com.example.mangkyuproject.utils.exception.MembershipErrorResult;
import com.example.mangkyuproject.utils.exception.MembershipException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        Membership savedMembership = membershipRepository.save(Membership.builder()
                .userId(userId)
                .point(point)
                .membershipType(MembershipType.NAVER)
                .build());

        return MembershipAddResponse.builder()
                .id(savedMembership.getId())
                .membershipType(savedMembership.getMembershipType())
                .build();
    }

    /**
     * @param userId
     * @return MembershipDetailResponse: 멤버십 상세 정보 Response DTO
     */
    public List<MembershipDetailResponse> getMembershipList(final String userId) {

        final List<Membership> memberships = membershipRepository.findAllByUserId(userId);

        return memberships.stream()
                .map(v -> MembershipDetailResponse.builder()
                        .id(v.getId())
                        .membershipType(v.getMembershipType())
                        .point(v.getPoint())
                        .createdAt(v.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public MembershipDetailResponse getMembership(final Long membershipId, final String userId) {
        final Membership membership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        return MembershipDetailResponse.builder()
                .id(membership.getId())
                .membershipType(membership.getMembershipType())
                .point(membership.getPoint())
                .createdAt(membership.getCreatedAt())
                .build();
    }

    public void removeMembership(Long membershipId, String userId) {
        Membership membership = membershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));

        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        membershipRepository.deleteById(membershipId);
    }
}
