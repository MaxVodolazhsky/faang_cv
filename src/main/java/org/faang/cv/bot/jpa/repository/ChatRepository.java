package org.faang.cv.bot.jpa.repository;

import org.faang.cv.bot.jpa.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
}
