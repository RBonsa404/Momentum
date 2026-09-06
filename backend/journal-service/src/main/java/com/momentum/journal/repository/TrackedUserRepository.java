package com.momentum.journal.repository;

import com.momentum.journal.domain.TrackedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrackedUserRepository extends JpaRepository<TrackedUser, UUID> {
}
