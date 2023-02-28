package com.example.mangkyuproject.domain.membership.dto;

import com.example.mangkyuproject.domain.membership.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
//@NoArgsConstructor(force = true)
public class MembershipResponse {
    private final Long id;
    private final MembershipType membershipType;
}
