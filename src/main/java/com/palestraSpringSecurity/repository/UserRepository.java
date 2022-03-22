/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.palestraSpringSecurity.repository;

import com.palestraSpringSecurity.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author tiagolopes
 */

public interface UserRepository extends JpaRepository<User, Integer>{
    
//    Optional<User> findByUsername(String username);
    User findByUsername(String username);
        
//    @Query("SELECT e FROM User e JOIN FETCH e.roles WHERE e.username= (:username)")
//    List<User> findByUsernameAndRoles(@Param ("username") String username);
//     
}
