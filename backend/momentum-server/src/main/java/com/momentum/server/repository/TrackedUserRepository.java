package com.momentum.server.repository;

import com.momentum.server.domain.journal.TrackedUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TrackedUserRepository extends JpaRepository<TrackedUser, UUID> {
}
