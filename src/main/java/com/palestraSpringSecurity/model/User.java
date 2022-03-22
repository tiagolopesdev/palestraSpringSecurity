/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.palestraSpringSecurity.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author tiagolopes
 */
@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private boolean active;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "userRoles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    private List<String> roles = new ArrayList<>();

    public User(String username, String password, boolean active) {
        this.username = username;
        this.password = password;
        this.active = active;
    }
    
    public User() {
    }

}
