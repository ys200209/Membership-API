package com.example.mangkyuproject.domain.membership.dto;

import com.example.mangkyuproject.domain.membership.MembershipType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
//@NoArgsConstructor(force = true)
public class MembershipAddResponse {
    private final Long id;
    private final MembershipType membershipType;
}
