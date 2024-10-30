package com.example.vidupcoremodule.core.controller;


import com.example.vidupcoremodule.core.View_DTO.DTOService;
import com.example.vidupcoremodule.core.View_DTO.PlaylistDTO;
import com.example.vidupcoremodule.core.View_DTO.PlaylistEntryDTO;
import com.example.vidupcoremodule.core.View_DTO.VideoPageDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Playlist;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.PlaylistVideo;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.LoggedInUserDetails;
import com.example.vidupcoremodule.core.service.userservices.PlaylistService;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600, methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    PlaylistService playlistService;

    @Autowired
    UtilClass utilClass;

    @Autowired
    DTOService dtoService;

    @Autowired
    VideoService videoService;




    /**
     * Returns the playlist videos in HTML format.
     *
     * @param playlistId     ID of the playlist
     * @param authentication Authenticated user
     * @return ModelAndView object containing playlist videos
     */
    @GetMapping("/{playlist_id}/videos/page")
    public ModelAndView playlistVideosHTML(@PathVariable("playlist_id") String playlistId, Authentication authentication) {
        LoggedInUserDetails loggedInUser = utilClass.getUserDetailsFromAuthentication(authentication);
        ModelAndView m = utilClass.getHTMLofLoggedInUser("playlist_page", loggedInUser);

        Playlist playlist = playlistService.getPlaylistById(playlistId);

        List<Video> viewOrder = new ArrayList<>();
        List<PlaylistEntryDTO> dto = dtoService.getPlaylistsVideosDTO(playlistId);

        for (PlaylistEntryDTO d : dto) {
            viewOrder.add(d.video());

        }

        List<Video> videosUploaded = videoService.getUploadVideosOfUser(loggedInUser.getUser());
        List<VideoPageDTO> containers = dtoService.getContainersForVideos(videosUploaded);

        m.addObject("dto", dto);
        m.addObject("playlist", playlist);
        m.addObject("view_order", viewOrder);
        m.addObject("current_user_videos_container", containers);
        m.setStatus(HttpStatusCode.valueOf(200));
        return m;

    }

    /**
     * Returns the playlist videos in JSON format.
     *
     * @param playlistId     ID of the playlist
     * @param authentication Authenticated user
     * @return ResponseEntity object containing playlist videos
     */
    @GetMapping("/{playlistId}/videos")
    public ResponseEntity<?> playlistVideosJSON(@PathVariable("playlistId") String playlistId, Authentication authentication) {

        System.out.println("hhhherre\n\n");

        Playlist playlist = playlistService.getPlaylistById(playlistId);
        if(playlist==null)
        {
            return new ResponseEntity<>("Invalid playlist id",HttpStatus.NOT_FOUND);
        }

        List<PlaylistEntryDTO> videos  = dtoService.getPlaylistsVideosDTO(playlistId);

        Map<String,Object> dto = new HashMap<>();

        dto.put("videos",videos);
        dto.put("playlist",playlist);

        return new ResponseEntity<>(dto,HttpStatus.OK);
    }




    /**
     * Deletes a Playlist
     * @param playlistId    Playlist to Delete
     * @return              True(HTTP 200) / False ( HTTP 404) on invalid playlistId
     */
    @DeleteMapping("/{playlistId}/delete")
    public ResponseEntity<?> deletePlaylist(@PathVariable("playlistId") String playlistId,Authentication authentication) {

        if (playlistService.getPlaylistById(playlistId) == null) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
        }

        User currentUser = utilClass.getUserDetailsFromAuthentication(authentication).getUser();
        if(!currentUser.getId().equals(playlistService.getPlaylistById(playlistId).getCreator().getId()))
        {
            return new ResponseEntity<>("You dont own this playlist. \n Delete Failed", HttpStatus.FORBIDDEN);
        }


        playlistService.deleteById(playlistId);
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }

    /**
     * Returns all playlists of logged-in user in JSON format.
     * To get playlists in HTML view, use endpoint /playlists/my/page.
     * @param authentication    Authenticated User
     * @return                  Playlists of current user
     */
    @GetMapping("/my")
    public ResponseEntity<?> myPlaylistsJSON(Authentication authentication) {

        List<PlaylistDTO> records = dtoService.getUsersPlaylistsDTO(utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        if (records == null || records.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    /**
     * Returns all playlists of logged in user IN HTML view
     * To get playlists in JSON format,  Use endpoint /playlists/my
     * @param authentication    Authenticated User
     * @return                  Playlists of current user
     */
    @GetMapping("/my/page")
    public ModelAndView myPlaylistsHTML(Authentication authentication) {
        LoggedInUserDetails loggedInUser = utilClass.getUserDetailsFromAuthentication(authentication);
        List<PlaylistDTO> records = dtoService.getUsersPlaylistsDTO(loggedInUser.getUser());
        ModelAndView m = utilClass.getHTMLofLoggedInUser("all_playlists", loggedInUser);
        m.addObject("table_dto", records);
        m.setStatus(HttpStatusCode.valueOf(200));
        return m;

    }

    /**
     * Creates a new playlist for the authenticated user.
     * @param playlistName      Name of the playlist to be created
     * @param authentication    Authenticated User
     * @return                  Created playlist or false if creation failed
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    public ResponseEntity<?> createPlaylist(@RequestParam(value = "pname") String playlistName, Authentication authentication) {
        Playlist playlist = playlistService.createPlaylist(playlistName, utilClass.getUserDetailsFromAuthentication(authentication).getUser());

        if (playlist == null) {
            return new ResponseEntity<>(Boolean.FALSE, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(playlist, HttpStatus.OK);
    }

    /**
     * Adds a video to a playlist.
     * @param videoId      ID of the video to be added
     * @param playlistId   ID of the playlist to which the video will be added
     * @return             PlaylistVideo record if successful, or error message if unsuccesful
     */
    @PutMapping("/{playListId}/add/{videoId}")
    public ResponseEntity<?> addToPlaylist(@PathVariable(value = "videoId") String videoId, @PathVariable(value = "playListId") String playlistId) {

        PlaylistVideo record = playlistService.addToPlaylist(UUID.fromString(videoId), playlistId);

        if (record == null)
            return new ResponseEntity<>("Error", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    /**
     * Deletes a video from a playlist.
     * @param videoId      ID of the video to be deleted
     * @param playlistId   ID of the playlist from which the video will be deleted
     * @return             Response message indicating success or failure
     */
    @DeleteMapping("/{playListId}/delete/{videoId}")
    public ResponseEntity<?> deleteVideoFromPlaylist(@PathVariable(value = "videoId") String videoId, @PathVariable(value = "playListId") String playlistId,Authentication authentication) {

        User currentUser = utilClass.getUserDetailsFromAuthentication(authentication).getUser();
        if(!currentUser.getId().equals(playlistService.getPlaylistById(playlistId).getCreator().getId()))
        {
            return new ResponseEntity<>("You dont own this playlist. \n Delete Failed", HttpStatus.FORBIDDEN);
        }

        String response = playlistService.deleteVideoFromPlayList(UUID.fromString(videoId), playlistId);

        if (response.equals("Deleted"))
            return new ResponseEntity<>(response, HttpStatus.OK);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
