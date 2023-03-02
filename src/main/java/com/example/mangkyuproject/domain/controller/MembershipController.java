package com.example.mangkyuproject.domain.controller;

import static com.example.mangkyuproject.domain.membership.MembershipConstants.USER_ID_HEADER;

import com.example.mangkyuproject.domain.membership.dto.MembershipDetailResponse;
import com.example.mangkyuproject.domain.membership.dto.MembershipRequest;
import com.example.mangkyuproject.domain.membership.dto.MembershipAddResponse;
import com.example.mangkyuproject.domain.service.MembershipService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;

    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershipList(
            @RequestHeader(USER_ID_HEADER) final String userId) {

        return ResponseEntity.ok(membershipService.getMembershipList(userId));
    }

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest) {

        MembershipAddResponse response = membershipService.addMembership(userId,
                membershipRequest.getMembershipType(), membershipRequest.getPoint());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @DeleteMapping("/api/v1/memberships/{membershipId}")
    public ResponseEntity<Object> removeMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable("membershipId") Long membershipId) {
        membershipService.removeMembership(membershipId, userId);

        return ResponseEntity.noContent().build();
    }
}
