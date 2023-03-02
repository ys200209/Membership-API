package com.example.mangkyuproject.domain.membership.dto;

import com.example.mangkyuproject.domain.membership.MembershipType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MembershipDetailResponse {
    private final Long id;
    private final MembershipType membershipType;
    private final Integer point;
    private final LocalDateTime createdAt;
}
