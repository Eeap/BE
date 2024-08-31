package com.demoboletto.domain;

import com.demoboletto.type.EProfile;
import com.demoboletto.type.EProvider;
import com.demoboletto.type.ERole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id", nullable = false)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "serial_id", nullable = false, unique = true)
    private String serialId;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private EProvider provider;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private ERole role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "is_frame")
    private boolean isFrame;

    @Column(name = "is_friendapply")
    private boolean isFriendApply;

    @Column(name = "is_location")
    private boolean isLocation;

    @Column(name = "friend_profile")
    @Enumerated(EnumType.STRING)
    private EProfile userProfile;


    @Builder
    public User(String serialId, String password, String email, String name, String nickname, EProvider provider, ERole role) {
        this.serialId = serialId;
        this.password = password;
        this.provider = provider;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.email =email;
        this.name = name;
        this.nickname = nickname;
        this.isFrame=true;
        this.isFriendApply=true;
        this.isLocation=true;
        this.userProfile = EProfile.Blue;
    }

    public void register(String nickname) {
        this.nickname = nickname;
        this.role = ERole.USER;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    public static User signUp(String serialId, EProvider provider) {
        return User.builder()
                .serialId(serialId)
                .provider(provider)
                .password(null)
                .role(ERole.USER)
                .build();
    }


}
