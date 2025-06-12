package com.example.vidupcoremodule.core.service.userservices;


import com.example.vidupcoremodule.core.View_DTO.PlaylistDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Playlist;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.PlaylistVideo;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.composite_ids.PlaylistVideoPrimaryKey;
import com.example.vidupcoremodule.core.repository.PlaylistRepository;
import com.example.vidupcoremodule.core.repository.PlaylistVideoRepository;
import com.example.vidupcoremodule.core.repository.UserRepository;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * CRUD functions for Playlist Entity
 */
@Service
public class PlaylistService {


    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoService videoService;

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;


    public Playlist save(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist getPlaylistById(String id) {
        return playlistRepository.findById(Integer.parseInt(id)).orElse(null);
    }

    public String deleteVideoFromPlayList(UUID videoId, String playlistId) {
        Video video = videoService.getVideoById(videoId);

        if (video == null) return "Video Not Found";

        Playlist playlist = playlistRepository.findById(Integer.parseInt(playlistId)).orElse(null);

        if (playlist == null) return "Playlist Not Found";

        PlaylistVideoPrimaryKey pk = new PlaylistVideoPrimaryKey();
        pk.setPlaylist(playlist);
        pk.setVideo(video);

        PlaylistVideo record = playlistVideoRepository.findById(pk).orElse(null);

        if (record == null) return "Video Does Not Exist In Playlist";

        playlistVideoRepository.delete(record);

        return "Deleted";
    }

    /**
     * Create New Playlist with provided name and Creator
     *
     * @param playlistName Name of Playlist
     * @param creator      User who created the playlist
     * @return Saved Playlist Entity
     */
    public Playlist createPlaylist(String playlistName, User creator) {

        Playlist playlist = new Playlist();
        playlist.setCreator(creator);
        playlist.setName(playlistName);
        playlist.setCreationDate(LocalDateTime.now());
        playlist.setLastModified(LocalDateTime.now());
        return playlistRepository.save(playlist);
    }

    public void deleteById(String playlistId) {
        playlistRepository.deleteById(Integer.parseInt(playlistId));
    }


    /**
     * Adds a Video to a Playlist
     *
     * @param videoId    The Video to Add
     * @param playlistId The Playlist to Add to
     * @return Updated Playlist entity
     */
    public PlaylistVideo addToPlaylist(UUID videoId, String playlistId) {
        Video video = videoService.getVideoById(videoId);
        Playlist playlist = playlistRepository.findById(Integer.parseInt(playlistId)).orElse(null);

        if (playlist == null) {
            return null;
        }

        PlaylistVideoPrimaryKey primaryKey = new PlaylistVideoPrimaryKey();
        primaryKey.setPlaylist(playlist);
        primaryKey.setVideo(video);

        PlaylistVideo existingRecord = playlistVideoRepository.findByPrimaryKey(primaryKey);

        if (existingRecord != null) return existingRecord;


        PlaylistVideo newRecord = new PlaylistVideo();
        newRecord.setPrimaryKey(primaryKey);
        newRecord.setAddedDate(LocalDateTime.now());

        return playlistVideoRepository.save(newRecord);

    }

    /**
     * Get the Videos that are in a Playlist
     *
     * @param playlist The Playlist
     * @return List of Videos which are in the Playlist
     */
    public List<Video> getVideosOfPlaylist(Playlist playlist) {
        List<PlaylistVideo> playlistVideoList = playlistVideoRepository.findByPrimaryKeyPlaylist(playlist);

        List<Video> videoList = new ArrayList<>();

        for (PlaylistVideo p : playlistVideoList) {

            videoList.add(p.getPrimaryKey().getVideo());
        }

        return videoList;
    }

    /**
     * Remove a Video from a Playlist
     *
     * @param videoId    The Video to Remove
     * @param playlistId The Playlist from which to Remove
     * @return The updated Playlist Entity
     */
    public Playlist removeVideoFromPlaylist(UUID videoId, String playlistId) {
        Video video = videoService.getVideoById(videoId);
        Playlist playlist = this.getPlaylistById(playlistId);

        PlaylistVideoPrimaryKey primaryKey = new PlaylistVideoPrimaryKey();

        primaryKey.setPlaylist(playlist);
        primaryKey.setVideo(video);

        playlistVideoRepository.deleteById(primaryKey);

        return playlist;
    }

    /**
     * Get the Videos that are in a Playlist in Sorted Order
     * Returns Videos Sorted By Date Added ( Low to High)
     *
     * @param playlist The Playlist
     * @return List of Videos which are in the Playlist
     */
    public List<Video> getVideosOfPlaylistSortedByAddedDate(Playlist playlist) {
        List<PlaylistVideo> playlistVideoList = playlistVideoRepository.findByPrimaryKeyPlaylistOrderByAddedDate(playlist);

        List<Video> videoList = new ArrayList<>();

        for (PlaylistVideo p : playlistVideoList) {
            videoList.add(p.getPrimaryKey().getVideo());
        }

        return videoList;
    }

    /**
     * Get the number of Videos in a Playlist
     *
     * @param playlist The Playlist whose Video count to return
     * @return The number of Videos in playlist
     */
    public int countVideosInPlaylist(Playlist playlist) {
        return playlistVideoRepository.countByPrimaryKeyPlaylist(playlist);
    }


    /**
     * Returns number of videos of each playlist in the list
     *
     * @param playlists The list of playlists
     * @return List of PlaylistDTO
     * PlaylistDTO has 2 fields playlist and videoCount
     */
    public List<PlaylistDTO> getVideoCountsOfPlaylists(List<Playlist> playlists) {
        return playlistVideoRepository.getVideoCountsOfPlaylists(playlists);
    }


    /**
     * Returns the Playlists which contain a Video
     *
     * @param video The Video
     * @return List of Playlists which contain video
     */
    public List<Playlist> getVPlaylistsHavingVideo(Video video) {
        List<PlaylistVideo> playlistVideoList = playlistVideoRepository.findByPrimaryKeyVideo(video);

        List<Playlist> playlists = new ArrayList<>();

        for (PlaylistVideo p : playlistVideoList) {
            playlists.add(p.getPrimaryKey().getPlaylist());
        }

        return playlists;
    }


    /**
     * Overloaded Method
     * Returns Playlists created by provided User
     *
     * @param user The User
     * @return List of Playlist, created by user
     */
    public List<Playlist> getPlayListsOfUser(User user) {

        return playlistRepository.getPlaylistsByCreator(user);
    }
}
