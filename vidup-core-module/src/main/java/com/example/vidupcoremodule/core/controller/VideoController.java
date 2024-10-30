package com.example.vidupcoremodule.core.controller;


import com.example.vidupcoremodule.core.View_DTO.*;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.*;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.entity_dtos.UserMapper;
import com.example.vidupcoremodule.core.service.userservices.UserService;
import com.example.vidupcoremodule.core.service.videoservices.*;
import com.example.vidupcoremodule.core.service.videoservices.searching.SearchService;
import com.example.vidupcoremodule.core.util.EventUtil;
import com.example.vidupcoremodule.core.util.UtilClass;
import com.example.vidupcoremodule.dtos.UserDTO;
import com.example.vidupcoremodule.storage.AzureStorageService;
import com.example.vidupcoremodule.storage.LocalStorageService;
import com.example.vidupcoremodule.storage.StorageUtil;
import com.example.vidupcoremodule.storage.internal.AzureConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * URLs prefixed with /permitted are allowed to be accessed without login
 * Put all /permitted URLs on top
 */
@Controller
@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/videos")
public class VideoController {


    private final ExecutorService fileReadExecutor = newFixedThreadPool(1000);

    UserMapper userMapper;

    @Autowired
    StorageUtil storageProperties;
    @Autowired
    VideoReactionService videoReactionService;
    @Autowired
    UtilClass utilClass;
    @Autowired
    ReportedVideoService reportedVideoService;

    @Autowired
    VideoCommentService videoCommentService;


    @Autowired
    DTOService dtoService;
    @Autowired
    TagService tagService;
    @Autowired
    ViewsHistoryService viewsHistoryService;
    @Autowired
    @Qualifier("LuceneSearch")
    SearchService searchService;


    @Autowired
    LocalStorageService localStorageService;

    @Autowired
    AzureStorageService azureStorageService;

    @Autowired
    VideoService videoService;
    @Autowired
    UserService userService;
    @Autowired
    EventUtil eventUtil;


    @Autowired
    AzureConfig azureConfig;


    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }


    @GetMapping("/permitted/dash_manifest/{videoId}")
    public ResponseEntity<?> getDashManifest(@PathVariable("videoId") String videoId) {
        VideoDataDashManifest manifest = videoService.getManifest(UUID.fromString(videoId));
        URL manifestLocation = manifest.getLocation();
        try {
            String protocol = manifestLocation.getProtocol();
            System.out.println("\nprotocol is\n" + protocol);

            byte[] bytes = storageProperties.getStorageService(protocol).readFileAsBytes(manifestLocation);

            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Manifest Not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/permitted/stream/dash/{videoId}/{chunkName}")
    public CompletableFuture<ResponseEntity<?>> getDashChunk(@PathVariable("videoId") String videoId, @PathVariable(value = "chunkName", required = false) String chunkName) {


        CompletableFuture<VideoDataDashSegment> dashFuture = videoService.getDashSegmentOfVideoFuture(UUID.fromString(videoId), chunkName);

        CompletableFuture<ResponseEntity<?>> responseFuture = dashFuture.thenApplyAsync(dashSeg -> {

            try {
                URL dashSegUrl = dashSeg.getLocation();
                String protocol = dashSegUrl.getProtocol();
                System.out.println("\nprotocol is\n" + protocol);
                byte[] bytes = storageProperties.getStorageService(protocol).readFileAsBytes(dashSegUrl);

                return new ResponseEntity<>(bytes, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Dash Segment Not found", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });


        return responseFuture;


    }




    @GetMapping("/permitted/all")
    public ResponseEntity<?> allVideosJSON(Authentication authentication) {

        List<VideoDTO> videoDTOS = dtoService.getVideoDTOsFromVideos();
        Map<String, Object> dto = new HashMap<>();
        dto.put("dto", videoDTOS);
        dto.put("source_url", utilClass.getMp4Url());

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/permitted/all/page")
    public ModelAndView allVideosHTML(Authentication authentication) {
        LoggedInUserDetails currentUser;
        Boolean loggedIn;
        if (utilClass.isLoggedIn(authentication)) {
            currentUser = utilClass.getUserDetailsFromAuthentication(authentication);
            loggedIn = Boolean.TRUE;
        } else {
            currentUser = new LoggedInUserDetails(userService.getGuestUser());
            loggedIn = Boolean.FALSE;
        }

        List<VideoDTO> dto = dtoService.getVideoDTOsFromVideos();
        List<Tag> tags = tagService.findAll();

        System.out.println("current user"+currentUser);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user_details", currentUser);
        modelAndView.addObject("logged_in", loggedIn);
        modelAndView.addObject("session_token", "abc");
        modelAndView.addObject("source_url", utilClass.getMp4Url());

        // System.out.println("url is " + utilClass.getMp4Url());
        modelAndView.addObject("dto", dto);
        modelAndView.addObject("alltags", tags);
        modelAndView.setStatus(HttpStatusCode.valueOf(200));
        return modelAndView;
    }

    @GetMapping("/permitted/{video_id}/page/dash")
    public ModelAndView getVideoHTMLDashPlayer(@PathVariable("video_id") String videoId, Authentication authentication) {

        if (utilClass.isLoggedIn(authentication))
            viewsHistoryService.handleVideoWatched(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        Boolean loggedIn;
        LoggedInUserDetails currentUser;
        if (utilClass.isLoggedIn(authentication)) {
            currentUser = utilClass.getUserDetailsFromAuthentication(authentication);
            loggedIn = Boolean.TRUE;
        } else {
            currentUser = new LoggedInUserDetails(userService.getGuestUser());
            loggedIn = Boolean.FALSE;
        }

        ModelAndView modelAndView = new ModelAndView("video_page");

        DashVideoPageDTO dto = dtoService.getDashPageDTO(UUID.fromString(videoId));

        modelAndView.addObject("user_details", currentUser);
        modelAndView.addObject("logged_in", loggedIn);
        modelAndView.addObject("session_token", "abc");
        modelAndView.addObject("mp4_url", utilClass.getMp4Url());
        modelAndView.addObject("dash_url", utilClass.getDashUrl());
        modelAndView.addObject("other_videos", videoService.getAllVideos());
        modelAndView.addObject("dto", dto);
        modelAndView.setStatus(HttpStatusCode.valueOf(200));
        return modelAndView;
    }


    @GetMapping("/permitted/{video_id}/page")
    public ModelAndView getVideoHTML(@PathVariable("video_id") String videoId, Authentication authentication) {

        if (utilClass.isLoggedIn(authentication))
            viewsHistoryService.handleVideoWatched(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        Boolean loggedIn;
        LoggedInUserDetails currentUser;
        if (utilClass.isLoggedIn(authentication)) {
            currentUser = utilClass.getUserDetailsFromAuthentication(authentication);
            loggedIn = Boolean.TRUE;
        } else {
            currentUser = new LoggedInUserDetails(userService.getGuestUser());
            loggedIn = Boolean.FALSE;
        }

        ModelAndView modelAndView = new ModelAndView("video_page_old");

        VideoPageDTO videoPageDTO = dtoService.getVideoDataContainerFromVideoId(UUID.fromString(videoId));

        modelAndView.addObject("user_details", currentUser);
        modelAndView.addObject("logged_in", loggedIn);
        modelAndView.addObject("session_token", "abc");
        modelAndView.addObject("source_url", utilClass.getMp4Url());
        modelAndView.addObject("other_videos", videoService.getAllVideos());
        modelAndView.addObject("dto", videoPageDTO);
        modelAndView.setStatus(HttpStatusCode.valueOf(200));
        return modelAndView;
    }


    @GetMapping("/permitted/{video_id}/page/vjs_dash")
    public ModelAndView getVideoHTMLVJSDASH(@PathVariable("video_id") String videoId, Authentication authentication) {

        if (utilClass.isLoggedIn(authentication))
            viewsHistoryService.handleVideoWatched(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        Boolean loggedIn;
        LoggedInUserDetails currentUser;
        if (utilClass.isLoggedIn(authentication)) {
            currentUser = utilClass.getUserDetailsFromAuthentication(authentication);
            loggedIn = Boolean.TRUE;
        } else {
            currentUser = new LoggedInUserDetails(userService.getGuestUser());
            loggedIn = Boolean.FALSE;
        }

        ModelAndView modelAndView = new ModelAndView("video_page_vjsdash");

        DashVideoPageDTO dto = dtoService.getDashPageDTO(UUID.fromString(videoId));

        modelAndView.addObject("user_details", currentUser);
        modelAndView.addObject("logged_in", loggedIn);
        modelAndView.addObject("session_token", "abc");
        modelAndView.addObject("source_url", utilClass.getMp4Url());
        modelAndView.addObject("other_videos", videoService.getAllVideos());
        modelAndView.addObject("dto", dto);
        modelAndView.setStatus(HttpStatusCode.valueOf(200));
        return modelAndView;
    }


    @GetMapping("/permitted/{video_id}")
    public ResponseEntity<?> getVideoJSON(@PathVariable("video_id") String videoId, Authentication authentication) {

        if (!videoService.videoExists(UUID.fromString(videoId)))
            return new ResponseEntity<>("Invalid Video Id", HttpStatus.NOT_FOUND);

        if (utilClass.isLoggedIn(authentication))
            viewsHistoryService.handleVideoWatched(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        VideoPageDTO videoPageDTO = dtoService.getVideoDataContainerFromVideoId(UUID.fromString(videoId));

        Map<String, Object> dto = new HashMap<>();
        boolean multiLang = videoPageDTO.availableLanguages().size() > 1;
        dto.put("video_data", videoPageDTO);
        dto.put("source_url", utilClass.getMp4Url());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @GetMapping("/creative_centre/page")
    public ModelAndView creativeCentre(Authentication authentication) {

        List<Tag> tags = tagService.findAll();
        ModelAndView modelAndView = utilClass.getHTMLofLoggedInUser("creative_centre", utilClass.getUserDetailsFromAuthentication(authentication));
        modelAndView.addObject("alltags", tags);
        modelAndView.setStatus(HttpStatusCode.valueOf(200));
        return modelAndView;
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchVideos(@RequestParam(value = "search_key") String searchKey, Authentication authentication) {


        if (searchKey == null || searchKey.isEmpty()) {
            return new ResponseEntity<>("NO SEARCH KEY", HttpStatus.NO_CONTENT);
        }


        List<Video> results = searchService.getSearchResults(searchKey);

        if (results == null || results.isEmpty()) {
            return new ResponseEntity<>("NO RESULTS", HttpStatus.NO_CONTENT);
        }

        Map<String, Object> dto = new HashMap<>();
        dto.put("results", results);
        dto.put("source_url", utilClass.getMp4Url());
        List<VideoDTO> resultsDTO = dtoService.getVideoDTOsFromVideos(results);
        return new ResponseEntity<>(resultsDTO, HttpStatus.OK);
    }

    @GetMapping("/search/page")
    public ModelAndView searchVideosPage(@RequestParam(value = "search_key") String searchKey, Authentication authentication) {

        ModelAndView modelAndView = utilClass.getHTMLofLoggedInUser("all_videos", utilClass.getUserDetailsFromAuthentication(authentication));
        List<Tag> tags = tagService.findAll();

        List<Video> foundVideos = searchService.getSearchResults(searchKey);
        List<VideoDTO> foundVideosDTO;

        if (searchKey == null || searchKey.isEmpty()) {
            foundVideos = videoService.getAllVideos();
            foundVideosDTO = dtoService.getVideoDTOsFromVideos(foundVideos);
            modelAndView.addObject("dto", foundVideosDTO);
            modelAndView.addObject("msg", "No Search Key");

        } else if (foundVideos == null || foundVideos.isEmpty()) {
            foundVideos = videoService.getAllVideos();
            foundVideosDTO = dtoService.getVideoDTOsFromVideos(foundVideos);
            modelAndView.addObject("dto", foundVideosDTO);
            modelAndView.addObject("msg", "No Results");

        } else {
            foundVideos = videoService.joinWithRemaning(foundVideos);
            foundVideosDTO = dtoService.getVideoDTOsFromVideos(foundVideos);
            modelAndView.addObject("dto", foundVideosDTO);

        }

        modelAndView.addObject("alltags", tags);
        return modelAndView;
    }


    @GetMapping("/tag_filter")
    public ResponseEntity<?> tagFilter(@RequestParam(value = "tags") List<Integer> tags, @RequestParam(value = "videoIds") List<String> videoIds, Authentication authentication) {

        List<UUID> uuids = videoIds.stream().map((vid) -> UUID.fromString(vid)).toList();

        List<Video> videos = videoService.getAllByIds(uuids);
        List<Video> filteredVideos = tagService.filterTags(videos, tags);
        List<VideoDTO> filteredDTOs = dtoService.getVideoDTOsFromVideos(filteredVideos);
        return new ResponseEntity<>(filteredDTOs, HttpStatus.OK);
    }


    /**
     * Post a comment on the specified Video
     * Notifies Creator of Video about new Comment
     *
     * @param commentText    Text of the comment
     * @param videoId        The Video
     * @param authentication Authenticated user
     * @return The Generated Comment Entity
     */
    @PostMapping("/{videoId}/comment")
    public ResponseEntity<VideoComment> addComment(@RequestParam(name = "commentText") String commentText, @PathVariable String videoId, Authentication authentication) {

        VideoComment comment = videoCommentService.addComment(commentText, UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        User targetUser = comment.getParentVideo().getCreator();
        User generatingUser = comment.getCommenter();
        eventUtil.publishCommentNotificationEvent(UUID.fromString(videoId), userMapper.INSTANCE.userToUserDTO(generatingUser), comment.getCommentText(), userMapper.INSTANCE.userToUserDTO(targetUser));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * Initiates Uploading a new Video
     * Generates Video Entity
     * Messages video storeName to Upload Transcode Handler for handling the next step in the Upload Process
     * Returns Video Entity
     *
     * @param file           Video File
     * @param description    Description
     * @param visibility     Public/Private
     * @param videoName      Video Name
     * @param selectedTags   Id of the selected Tags
     * @param authentication Authenticated User
     * @return Generated Video Entity
     */
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam(name = "description") String description,

                                              @RequestParam("visibility") String visibility, @RequestParam("video_name") String videoName, @RequestParam(value = "tags", required = false) List<String> selectedTags, Authentication authentication) {


        if (file.isEmpty()) {
            return new ResponseEntity<>("Empty File", HttpStatus.BAD_REQUEST);
        }


        //create db entity
        User creator = utilClass.getUserDetailsFromAuthentication(authentication).getUser();
        String originalFilename = file.getOriginalFilename();
        Video video = videoService.createVideoEntityAndSave(originalFilename, creator, description, visibility, videoName, selectedTags);

        Path storePath = storageProperties.getLocal().getOriginalStorePath();

        //file store_name is id.format ( guaranteed unique)
        String originalFileFormat = originalFilename.split("\\.")[1];
        String storeName = video.getId().toString().concat(".").concat(originalFileFormat);

        //upload file with given name at given path
        localStorageService.upload(file.getResource(), storeName, storePath);
        eventUtil.publishStartPipelineEvent(video.getId(), storeName, storePath);
        return new ResponseEntity<>(video, HttpStatus.CREATED);

    }


    /**
     * Report a video by logged in user
     *
     * @param videoId        Reported video
     * @param reason         Reason
     * @param authentication Auth object
     * @return Created Reported Entity / Previously created Entity if same video reported twice by same user
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/{video_id}/report")
    public ResponseEntity<Reported> reportVideo(@PathVariable(name = "video_id") String videoId, @RequestParam(name = "reason", required = false) String reason, Authentication authentication) {

        System.out.println("\n\nreporting\n");

        Reported reported = reportedVideoService.reportVideo(UUID.fromString(videoId), reason, utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        System.out.println(reported);
        return new ResponseEntity<>(reported, HttpStatus.OK);


    }

    /**
     * Download a Video
     * If the Video has multiple languages, english will be downloaded , if english not present , the first detected language will be downloaded
     *
     * @param videoId        The Video
     * @param authentication Authenticated User
     * @return The Video File (HTTP 200) on success
     */
    @GetMapping("/{videoId}/download")
    public ResponseEntity<Resource> downloadVideo(@PathVariable String videoId, Authentication authentication) {

        Resource errorResource = new ByteArrayResource("Invalid Video Id".getBytes());
        if (!videoService.videoExists(UUID.fromString(videoId)))
            return new ResponseEntity<>(errorResource, HttpStatus.NOT_FOUND);

        Resource resource = videoService.downloadVideoFileAsResource(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        if (resource == null) {
            return new ResponseEntity<>(errorResource, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = utilClass.getMIMETypeApacheTika(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println(contentType);
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);


    }


    /**
     * Deletes a Video
     *
     * @param videoId The Video
     * @param auth    Authenticated User
     * @return True(HTTP 200) / False(HTTP 404) on invalid videoId
     */
    @DeleteMapping("/{video_id}/delete")
    public ResponseEntity<Boolean> deleteVideo(@PathVariable(name = "video_id") String videoId, Authentication auth) {
        System.out.println(videoId);

        Boolean deleted = videoService.deleteVideo(UUID.fromString(videoId));

        if (deleted)
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        else
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
    }


    /**
     * Like the video by logged in User
     *
     * @param videoId        The video
     * @param authentication Auth object
     * @return Created/Updated Video Reaction
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/{videoId}/reactions/react/like")
    public ResponseEntity<VideoReaction> likeVideo(@PathVariable("videoId") String videoId, Authentication authentication, HttpServletRequest request) {

        VideoReaction reaction = videoReactionService.likeVideo(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        UserDTO generatingUser = userMapper.INSTANCE.userToUserDTO(reaction.getPrimaryKey().getUser());
        UserDTO targetUser = UserMapper.INSTANCE.userToUserDTO(reaction.getPrimaryKey().getVideo().getCreator());

        if (reaction.getScore() != 0)
            eventUtil.publishReactionNotificationEvent(UUID.fromString(videoId), generatingUser, targetUser, reaction.getScore());

        return new ResponseEntity<>(reaction, HttpStatus.OK);
    }

    /**
     * Like the video by logged in User
     *
     * @param videoId        The video
     * @param authentication Auth object
     * @return Created/Updated Video Reaction
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/{videoId}/reactions/react/dislike")
    public ResponseEntity<VideoReaction> dislikeVideo(@PathVariable("videoId") String videoId, Authentication authentication) {

        VideoReaction reaction = videoReactionService.dislikeVideo(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        UserDTO generatingUser = userMapper.INSTANCE.userToUserDTO(reaction.getPrimaryKey().getUser());
        UserDTO targetUser = UserMapper.INSTANCE.userToUserDTO(reaction.getPrimaryKey().getVideo().getCreator());


        if (reaction.getScore() != 0)
            eventUtil.publishReactionNotificationEvent(UUID.fromString(videoId), generatingUser, targetUser, reaction.getScore());
        return new ResponseEntity<>(reaction, HttpStatus.OK);
    }

    /**
     * IMPORTANT Return HTTP NOT_FOUND if this was called before calling /react/like or react/dislike
     * Set Neutral Reaction ( Like after liking) / (Dislike after disliking)
     *
     * @param videoId        The video
     * @param authentication Auth object
     * @return Updated Video Reaction ( HTTP OK)
     * null ( HTTP NOT_FOUND ) , when this was called before calling /react/like or react/dislike
     */
    @CrossOrigin(origins = "*")
    @PutMapping("/{videoId}/reactions/react/neutral")
    public ResponseEntity<VideoReaction> neutralReactionOnVideo(@PathVariable("videoId") String videoId, Authentication authentication) {

        VideoReaction reaction = videoReactionService.neutralReactionOnVideo(UUID.fromString(videoId),
                utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        if (reaction != null) return new ResponseEntity<>(reaction, HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{videoId}/reactions/count")
    public ResponseEntity<ReactionDTO> getAllReactionsCount(@PathVariable("videoId") String videoId, Authentication authentication) {
        ReactionDTO reactionDTO = videoReactionService.getAllReactionsCount(UUID.fromString(videoId));
        return new ResponseEntity<>(reactionDTO, HttpStatus.OK);
    }


    /**
     * Return true if video was already Liked by logged in user
     *
     * @param videoId        The video
     * @param authentication Auth object
     * @return True if liked previously,false otherwise
     */
    @GetMapping("/{videoId}/reactions/liked_prev")
    public ResponseEntity<Boolean> videoLikedAlready(@PathVariable("videoId") String videoId, Authentication authentication) {
        Boolean liked = videoReactionService.isVideoLikedBy(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        return new ResponseEntity<>(liked, HttpStatus.OK);
    }

    /**
     * Return true if video was already Disliked by logged in user
     *
     * @param videoId        The video
     * @param authentication Auth object
     * @return True if Disliked previously,false otherwise
     */
    @GetMapping("/{videoId}/reactions/disliked_prev")
    public ResponseEntity<Boolean> videoDislikedAlready(@PathVariable("videoId") String videoId, Authentication authentication) {
        Boolean disliked = videoReactionService.isVideoDislikedBy(UUID.fromString(videoId), utilClass.getUserDetailsFromAuthentication(authentication).getUser());
        return new ResponseEntity<>(disliked, HttpStatus.OK);
    }
}
