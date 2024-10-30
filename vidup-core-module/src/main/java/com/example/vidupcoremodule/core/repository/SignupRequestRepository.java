package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.SignupRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SignupRequestRepository extends JpaRepository<SignupRequest,String> {

    SignupRequest findByEmail(String email);

    SignupRequest findByUserName(String username);

    List<SignupRequest> findAllByValidTillBefore(LocalDateTime validTill);

    void deleteAllByValidTillBefore(LocalDateTime validTill);
}
