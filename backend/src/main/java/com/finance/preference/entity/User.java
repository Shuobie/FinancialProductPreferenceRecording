package com.finance.preference.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "UserID", length = 20)
    private String userId; // pk 使用者id

    @Column(name = "UserName", nullable = false, length = 50)
    private String userName; // 使用者名稱

    @Column(name = "Email", nullable = false, length = 100)
    private String email; // 電子郵件

    @Column(name = "Account", nullable = false, length = 50)
    private String account; // 銀行帳號
}
