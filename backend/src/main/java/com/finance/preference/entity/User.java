package com.finance.preference.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Users")
public class User {

    @Id
    @Column(name = "UserID", length = 20)
    private String userId;

    @Column(name = "UserName", nullable = false, length = 50)
    private String userName;

    @Column(name = "Email", nullable = false, length = 100)
    private String email;

    @Column(name = "Account", nullable = false, length = 50)
    private String account;
}
