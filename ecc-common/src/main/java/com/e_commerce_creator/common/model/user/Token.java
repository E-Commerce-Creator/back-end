package com.e_commerce_creator.common.model.user;

import com.e_commerce_creator.common.enums.user.TokenType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tokens")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;
    @Enumerated(EnumType.STRING)
    TokenType tokenType;

    boolean isExpired;
    boolean isRevoked;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user_tokens")
    User user;
}
