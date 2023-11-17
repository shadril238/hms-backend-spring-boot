package com.shadril.notificationservice.entity;

import com.shadril.notificationservice.enums.PreferenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferenceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationPreferenceId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "pref_type")
    @Enumerated(EnumType.STRING)
    private PreferenceType prefType;

    @Column(name = "enabled")
    private boolean enabled;
}
