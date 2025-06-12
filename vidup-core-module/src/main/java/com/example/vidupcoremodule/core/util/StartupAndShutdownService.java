package com.example.vidupcoremodule.core.util;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Session;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.service.userservices.SessionHistoryService;
import com.example.vidupcoremodule.core.service.userservices.SessionService;
import com.example.vidupcoremodule.core.service.userservices.SignupService;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.service.videoservices.searching.SearchService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

/**
 * Service to handle startup and shutdown operations for the application.
 */
@Service
public class StartupAndShutdownService implements DisposableBean {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionHistoryService sessionHistoryService;

    @Autowired
    @Qualifier("LuceneSearch")
    private SearchService searchService;

    @Autowired
    private VideoService videoService;

    @Autowired
    SignupService signupService;


    /**
     * Executes startup SQL queries to transfer sessions to history and clear the session table.
     * * Call any other method that may be needed during App Startup
     */
    @PostConstruct
    public void executeStartupQueries() {

        System.out.println("Startup operations executing");
        executeStartupSQL();
        generateSearchIndex();
        cleanupNotReadyVideos();
        cleanupExpiredSignupRequests();

    }

    void cleanupExpiredSignupRequests()
    {
        signupService.clearExpiredSignupRequests();
    }

    void cleanupNotReadyVideos() {
        videoService.cleanupNotReadyVideos();
    }

    void generateSearchIndex() {
        List<Video> allVids = videoService.getAllVideos();

        for (Video video : allVids) {
            searchService.addToSearchIndex(video);
        }

    }

    /**
     * Transfers all current sessions to history and clears the session table.
     * This method is called during the startup of the application.
     */
    private void executeStartupSQL() {
        List<Session> sessionList = sessionService.getAllSessions();
        Time logoutTime = Time.valueOf(LocalTime.now());

        for (Session session : sessionList) {
            sessionHistoryService.createHistoryFromSessionAndSave(session, logoutTime);
        }

        sessionService.deleteAllSessions();
    }

    /**
     * Executes shutdown SQL queries to transfer sessions to history and clear the session table.
     * Call any other method that may be needed during App Shutdown
     */
    @Override
    public void destroy() throws Exception {
        executeShutdownSQL();
    }

    /**
     * Transfers all current sessions to history and clears the session table.
     * This method is called during the shutdown of the application.
     */
    private void executeShutdownSQL() {
        List<Session> sessionList = sessionService.getAllSessions();
        Time logoutTime = Time.valueOf(LocalTime.now());

        for (Session session : sessionList) {
            sessionHistoryService.createHistoryFromSessionAndSave(session, logoutTime);
        }

        sessionService.deleteAllSessions();
    }
}
