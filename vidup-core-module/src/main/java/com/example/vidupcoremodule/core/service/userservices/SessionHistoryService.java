package com.example.vidupcoremodule.core.service.userservices;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.Session;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.SessionHistory;
import com.example.vidupcoremodule.core.repository.SessionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;

/**
 * CRUD functions for SessionHistory Entity
 */
@Service
public class SessionHistoryService {

    @Autowired
    private SessionHistoryRepository sessionHistoryRepository;

    /**
     * Creates Session History from a provided Session Entity
     * @param session   Provided Session History Entity
     * @return          Saved Session History Entity
     */
    public SessionHistory createHistoryFromSessionAndSave(Session session)
    {
        SessionHistory sessionHistory = new SessionHistory();
        sessionHistory.setUser(session.getUser());
        sessionHistory.setUserName(session.getUserName());
        sessionHistory.setIpAddress(session.getIpAddress());
        sessionHistory.setLoginTime(session.getLoginTime());
        sessionHistory.setLogoutTime(Time.valueOf(LocalTime.now()));
        return sessionHistoryRepository.save(sessionHistory);
    }

    /**
     * Overloaded Method
     * Creates Session History from a provided Session Entity and logoutTime
     * @param session   Provided Session History Entity
     * @param logoutTime Provided logout time
     * @return          Saved Session History Entity
     */
    public SessionHistory createHistoryFromSessionAndSave(Session session,Time logoutTime)
    {
        SessionHistory sessionHistory = new SessionHistory();
        sessionHistory.setUser(session.getUser());
        sessionHistory.setUserName(session.getUserName());
        sessionHistory.setIpAddress(session.getIpAddress());
        sessionHistory.setLoginTime(session.getLoginTime());
        sessionHistory.setLogoutTime(logoutTime);
        return sessionHistoryRepository.save(sessionHistory);
    }
}
