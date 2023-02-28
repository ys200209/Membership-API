package com.example.mangkyuproject.domain.membership.dto;


import com.example.mangkyuproject.domain.membership.MembershipType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true) // 필드들이 final이므로 기본 생성자를 사용할 수 없음.
// 하지만 DTO 클래스는 Jackson에 의해 직렬화 과정에서 기본 생성자를 반드시 호출한다.
// 이 문제를 해결하기 위한 옵션 force.
public class MembershipRequest {
    @NotNull
    @Min(0)
    private final Integer point;

    @NotNull
    private final MembershipType membershipType;
}
