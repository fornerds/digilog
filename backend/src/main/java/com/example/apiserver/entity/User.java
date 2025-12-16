package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true, length = 100)
    private String email;

    @Column(nullable = true, length = 255)
    private String password;

    @Column(nullable = true, length = 50)
    private String name;

    @Column(nullable = true, length = 20)
    private String phone;

    @Column(name = "birth_date", nullable = true)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Provider provider = Provider.LOCAL;

    @Column(name = "provider_id", length = 100)
    private String providerId; // 네이버/카카오 고유 ID

    @Column(length = 500)
    private String profileImageUrl;

    @Builder
    public User(String email, String password, String name, String phone, LocalDate birthDate, 
                Gender gender, UserRole role, Provider provider, String providerId, String profileImageUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role != null ? role : UserRole.USER;
        this.provider = provider != null ? provider : Provider.LOCAL;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updatePhone(String phone) {
        this.phone = phone;
    }

    public void updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public void updateProviderId(String providerId) {
        this.providerId = providerId;
    }
}

