package com.momentum.notification.mapper;

import com.momentum.notification.domain.Notification;
import com.momentum.notification.dto.NotificationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
}