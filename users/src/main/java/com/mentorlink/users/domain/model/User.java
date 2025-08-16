package com.mentorlink.users.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, length = 36)
    private String externalId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Setter
    @Column(nullable = false, length = 20)
    private String firstname;

    @Setter
    @Column(nullable = false, length = 20)
    private String lastname;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Setter
    @Column(nullable = false, length = 20)
    private String country;

    @Setter
    @Column(nullable = false, length = 20)
    private String state;

    @Setter
    @Column(nullable = false, length = 100)
    private String timezone;

    @Setter
    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Builder
    public User(String firstname, String externalId, String username, String lastname, String email, String country, String state, String timezone, String imageUrl, Role role) {
        this.firstname = firstname;
        this.externalId = externalId;
        this.username = username;
        this.lastname = lastname;
        this.email = email;
        this.country = country;
        this.state = state;
        this.timezone = timezone;
        this.imageUrl = imageUrl;
        this.role = role;
    }
}
