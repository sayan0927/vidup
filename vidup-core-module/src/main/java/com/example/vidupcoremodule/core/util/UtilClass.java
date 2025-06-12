package com.example.vidupcoremodule.core.util;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.VideoDataMP4;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.entity_dtos.UserMapper;
import com.example.vidupcoremodule.core.service.userservices.UserService;
import com.example.vidupcoremodule.dtos.UserDTO;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ConfigurationProperties(prefix = "util")
@Data
public class UtilClass {

    Map<String, String> tikaUtilMappings;

    @Autowired
    private UserService userService;

    private Tika tika;

    private String corePort;

    private String streamingPort;

    private String streamingIp;

    private String coreIp;

    private UserMapper userMapper;

    @Autowired
    private Environment environment;

    public String getMp4Url() {

        String address = streamingIp + ":" + streamingPort;
        String prof = getActiveProfile();

        if (prof.equals("dev")) {
            try {

                return getCurrentIp() + ":" + streamingPort + "/videos/permitted/stream";
            } catch (UnknownHostException unknownHostException) {
                return address + "/videos/permitted/stream";
            }
        } else {
            System.out.println("prof is " + prof);
            System.out.println("mp4 ip is " + streamingIp + ":" + streamingPort);
            return address + "/videos/permitted/stream";
        }
    }

    public String getDashUrl() {



        String address = streamingIp + ":" + streamingPort;
        String prof = getActiveProfile();
        if (prof.equals("dev")) {
            try {
                getCurrentIp();
                return "localhost" + ":" + streamingPort + "/videos/permitted/dash_manifest";
            } catch (UnknownHostException unknownHostException) {
                return address + "/videos/permitted/dash_manifest";
            }
        } else {
            return address + "/videos/permitted/dash_manifest";
        }

    }


    @Bean
    public Tika tika() {
        return new Tika();
    }


    public Map<String, String> getTikaUtilMappings() {
        return tikaUtilMappings;
    }

    public String getActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();

        System.out.println(Arrays.toString(activeProfiles));

        if (activeProfiles.length > 0) {
            return activeProfiles[0];  // Return the first active profile (e.g., "dev")
        }
        return "default";  // Return "default" if no profile is active
    }

    @PostConstruct
    void init() {
        tikaUtilMappings = new HashMap<>();

        //bidirectional Mapping
        tikaUtilMappings.put("application/x-matroska", "mkv");
        tikaUtilMappings.put("video/quicktime", "mp4");
        tikaUtilMappings.put("video/mp4", "mp4");

        tikaUtilMappings.put("mkv", "application/x-matroska");
        tikaUtilMappings.put("mp4", "video/quicktime");
    }

    public URL urlFromPath(Path path) throws MalformedURLException {
        try {
            URL url = path.toUri().toURL();
            return url;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public ModelAndView getHTMLofLoggedInUser(String viewName, LoggedInUserDetails loggedInUserDetails) {
        ModelAndView modelAndView = new ModelAndView(viewName);
        modelAndView.addObject("user_details", loggedInUserDetails);
        modelAndView.addObject("session_token", loggedInUserDetails.getSessionToken());
        modelAndView.addObject("source_url", getMp4Url());

        return modelAndView;
    }




    public String getCurrentIp() throws UnknownHostException {

        String ip;
        InetAddress inetAddress = InetAddress.getLocalHost();
        ip = inetAddress.toString().split("/")[1];
        return ip;

    }

    public String getMIMETypeApacheTika(String filePath) {
        return tika.detect(filePath);
    }

    public Boolean isMIMETypeImage(MultipartFile file) {
        try {
            InputStream stream = file.getInputStream();
            String mime = tika.detect(stream);
            return mime.startsWith("image/");
        } catch (IOException ioException) {
            return false;
        }
    }

    public boolean isLoggedIn(Authentication authentication) {
        return authentication != null && authentication.getPrincipal() instanceof UserDetails;
    }


    public String getClientIpAddress(HttpServletRequest httpServletRequest) {
        return standardiseIp(httpServletRequest.getRemoteAddr());

    }

    public String standardiseIp(String ip) {
        return ip.replace(':', '.');
    }

    public String determineFileNameForDefaultLanguage(List<VideoDataMP4> entities) {
        // Iterate over the entities list and check the condition to determine the fileName
        for (VideoDataMP4 entity : entities) {


            if ("eng".equals(entity.getLanguage())) {
                return entity.getMp4FileName();
            }
        }
        // If no match found, return first Streams Filename
        return entities.get(0).getMp4FileName();
    }

    public LoggedInUserDetails getUserDetailsFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("this "+userDetails);
        LoggedInUserDetails loggedInUserDetails = new LoggedInUserDetails(userService.findByUserName(userDetails.getUsername()));
        return loggedInUserDetails;
    }

    public UserDTO getUserDtoFromAuthentication(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("this "+userDetails);
        LoggedInUserDetails loggedInUserDetails = new LoggedInUserDetails(userService.findByUserName(userDetails.getUsername()));

        return userMapper.INSTANCE.userToUserDTO(loggedInUserDetails.getUser());
    }


}
