package com.example.mangkyuproject.domain.repository;

import com.example.mangkyuproject.domain.membership.Membership;
import com.example.mangkyuproject.domain.membership.MembershipType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndMembershipType(String userId, MembershipType membershipType);

    List<Membership> findAllByUserId(String userId);
}
