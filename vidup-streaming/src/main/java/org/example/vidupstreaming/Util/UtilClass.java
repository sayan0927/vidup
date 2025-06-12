package org.example.vidupstreaming.Util;



import org.example.vidupstreaming.Entity.VideoDataMP4;
import org.example.vidupstreaming.constants.StreamingApplicationConstants;
import org.example.vidupstreaming.service.VideoStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilClass {

    @Autowired
    VideoStreamService videoStreamService;

    @Autowired
    StreamingApplicationConstants constants;

    public UtilClass(VideoStreamService videoStreamService,StreamingApplicationConstants constants) {
        this.videoStreamService = videoStreamService;
        this.constants=constants;
    }






    public String standardiseIp(String ip)
    {
        return ip.replace(':','.');
    }






    public String getClientIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String xForwardedForHeader = headers.getFirst("X-Forwarded-For");

        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            // The client's IP address is the first entry in the X-Forwarded-For header
            String ip = xForwardedForHeader.split(",")[0].trim();
            if(ip.equals(constants.ipV6LoopBackAddress) )
                ip = constants.localHostAddress;

            return standardiseIp(ip);
        }


        // If X-Forwarded-For header is not present, fallback to the remote address
        String ip = request.getRemoteAddress().getHostString();

        if(ip.equals(constants.ipV6LoopBackAddress))
            ip = constants.localHostAddress;

        if(ip.equals(constants.dynamicLocalHostAddress) )
            ip = constants.localHostAddress;

        return standardiseIp(ip);
    }

    public VideoDataMP4 determineEntityForDefaultLanguage(List<VideoDataMP4> entities)
    {
        for (VideoDataMP4 entity : entities)
        {
            if ("eng".equals(entity.getLanguage())) {
                return entity; // Set the desired fileName
            }
        }
        return entities.get(0);
    }

    public String determineFileNameForDefaultLanguage(List<VideoDataMP4> entities) {
        // Iterate over the entities list and check the condition to determine the fileName
        for (VideoDataMP4 entity : entities)
        {


            if ("eng".equals(entity.getLanguage())) {
                return entity.getStoreFileNameWithoutFormat(); // Set the desired fileName
            }
        }
        // If no match found, return first Streams Filename
        return entities.get(0).getStoreFileNameWithoutFormat();
    }

    public String determineFileNameForSelectedLanguage(List<VideoDataMP4> entities, String language) {

        // Iterate over the entities list and check the condition to determine the fileName
        for (VideoDataMP4 entity : entities)
        {

            if (language.equals(entity.getLanguage())) {
                return entity.getStoreFileNameWithoutFormat(); // Set the desired fileName
            }
        }
        // If no match found, return first Streams Filename
        return entities.get(0).getStoreFileNameWithoutFormat();
    }
}
