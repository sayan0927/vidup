package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Session;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {

    @Modifying
    @Transactional
    //@Query(value = "delete from Session s WHERE s.user  = :user and s.token = :token")
     void deleteByUserAndToken(User user, String token);

    @Modifying
    @Transactional
    void deleteAll();

    @Modifying
    @Transactional
    void deleteByUser(User user);

    Session findByUserAndToken(User user,String token);

    Session findByUser(User user);

    List<Session> findAll();


}