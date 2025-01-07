package com.e_commerce_creator.common.model.users;

import com.e_commerce_creator.common.model.account.Account;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    Long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Account account;
}
