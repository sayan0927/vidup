package com.example.vidupcoremodule.core.controller;


import com.example.vidupcoremodule.core.View_DTO.DTOService;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Reported;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.service.videoservices.ReportedVideoService;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
class AdminController {


    @Autowired
    UtilClass utilClass;

    @Autowired
    ReportedVideoService reportedVideoService;


    @Autowired
    VideoService videoService;

    @Autowired
    DTOService dtoService;

    /**
     * Returns the admin page view.
     *
     * @param auth Authenticated User
     * @return ModelAndView object for the admin page
     */
    @RequestMapping("/page")
    public ModelAndView adminPage(Authentication auth) {
        LoggedInUserDetails loggedInUser = utilClass.getUserDetailsFromAuthentication(auth);

        ModelAndView m = utilClass.getHTMLofLoggedInUser("admin", loggedInUser);

        List<Reported> allReported = reportedVideoService.getActiveReports();
        List<Video> videosUploaded = videoService.getUploadVideosOfUser(loggedInUser.getUser());
        //  List<VideoPageDTO> containers = dtoService.getContainersForVideos(videosUploaded);

        m.addObject("reported_list", allReported);
        //  m.addObject("current_user_videos_container", containers);
        return m;
    }

    /**
     * Dismisses a reported video by video ID.
     *
     * @param videoId Video ID to dismiss
     * @param auth    Authenticated User
     * @return ResponseEntity with the dismissal status
     */
    @DeleteMapping("/reported/{videoId}/dismiss")
    public ResponseEntity<?> dismissReportedVideo(@PathVariable(name = "videoId") String videoId, Authentication auth) {
        String status = reportedVideoService.dismissReportedVideo(UUID.fromString(videoId));

        if (status.equals(ReportedVideoService.DISMISSED))
            return new ResponseEntity<>(status, HttpStatus.OK);

        else return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
    }


}
