package com.example.mangkyuproject.domain.controller;

import static com.example.mangkyuproject.domain.membership.MembershipConstants.USER_ID_HEADER;

import com.example.mangkyuproject.domain.membership.dto.MembershipRequest;
import com.example.mangkyuproject.domain.membership.dto.MembershipResponse;
import com.example.mangkyuproject.domain.service.MembershipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest) {

        MembershipResponse response = membershipService.addMembership(userId,
                membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
