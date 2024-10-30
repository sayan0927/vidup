package com.example.vidupcoremodule.core.service.videoservices;




import com.example.vidupcoremodule.core.entity.DatabaseEntities.Reported;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.repository.ReportedRepository;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * CRUD functions for ReportedVideo Entity
 */
@Service
public class ReportedVideoService {

    static String ACTIVE_STATUS = "ACTIVE";
    static String CLOSED = "CLOSED";
    public static String DISMISSED = "DISMISSED";
    @Autowired
    UtilClass utilClass;
    @Autowired
    VideoService videoService;
    @Autowired
    private ReportedRepository reportedRepository;


    /**
     * Get all Videos that were Reported
     * @return  List of Videos that were Reported
     */
    public List<Reported> getActiveReports() {
        return reportedRepository.findAllByStatusOrderByVideo(ACTIVE_STATUS);
    }


    /**
     * Dismissed a Reported a Video
     * @param videoId   Id of Reported Video to dismiss
     */
    public String dismissReportedVideo(UUID videoId) {
        Video v = videoService.getVideoById(videoId);

        if(v==null)
            return "Video Not Found";

        List<Reported> reported = reportedRepository.findAllByVideo(v);

        if(reported==null || reported.isEmpty())
            return "Video Never Reported";

        reportedRepository.updateByVideo(v, CLOSED);
        return DISMISSED;
    }


    /**
     * Report a Video
     * @param videoId   The Video Reported
     * @param reason    Reason to Report
     * @param reportedBy    The User who Reported
     * @return          Saved Reported Entity
     */
    public Reported reportVideo(UUID videoId, String reason, User reportedBy) {
        Video reportedVideo = videoService.getVideoById(videoId);
        Reported existing = reportedRepository.findReportedByReportedByAndVideoAndStatus(reportedBy, reportedVideo, ACTIVE_STATUS);
        if (existing != null)
            return existing;

        Reported reported = new Reported();
        reported.setReportedAt(LocalDateTime.now());
        reported.setVideo(reportedVideo);
        reported.setReportedBy(reportedBy);
        reported.setStatus(ACTIVE_STATUS);
        reported.setReason(reason == null ? "" : reason);
        return reportedRepository.save(reported);

    }
}
