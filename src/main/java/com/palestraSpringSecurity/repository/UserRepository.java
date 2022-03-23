/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.palestraSpringSecurity.repository;

import com.palestraSpringSecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author tiagolopes
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

//    @Query("SELECT u FROM User u JOIN FETCH u.role r WHERE r.id = :id")
//    public List<User> findByUsernameAndRoles(Integer idUser);
//     
}
