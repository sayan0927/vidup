package com.example.vidupcoremodule.core.service.userservices;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.Session;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.repository.SessionRepository;
import com.example.vidupcoremodule.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

/**
 * CRUD functions for Session Entity
 */
@Service
public class SessionService {


    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public void deleteSessionOfUser(User user) {
        sessionRepository.deleteByUser(user);
    }

    public void deleteAllSessions() {
        sessionRepository.deleteAll();
    }


    /**
     * Check if User's entry exists in Session Table i.e. wether User has logged In
     * @param userId    The User
     * @return          True if logged in, False otherwise
     */
    public boolean userLoggedIn(Integer userId)
    {
        User user = userRepository.findUserById(userId);
        if(user==null)
            return false;

        Session session = sessionRepository.findByUser(user);
        return session!=null;
    }


    /**
     * Creates a Session Entity from newly logged in User and save it
     * @param loggedInUser      Newly logged in User
     * @param ip                User's IP
     * @param token             User's Secret Token generated upon login
     * @return                  Saved Session Entity
     */
    public Session saveSession(LoggedInUserDetails loggedInUser, String ip, String token)
    {
        Session currentSession = new Session();

        currentSession.setUser(loggedInUser.getUser());
        currentSession.setUserName(loggedInUser.getUsername());
        currentSession.setIpAddress(ip);
        currentSession.setLoginTime(Time.valueOf(LocalTime.now()));
        currentSession.setToken(token);

        try {
            return sessionRepository.save(currentSession);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw dataIntegrityViolationException;
        }
    }


}
