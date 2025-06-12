package com.example.vidupcoremodule.core.repository;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);
    User findUserById(Integer id);

    User findByEmail(String email);



}




