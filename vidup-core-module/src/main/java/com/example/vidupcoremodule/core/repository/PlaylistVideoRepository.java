package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.View_DTO.PlaylistDTO;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Playlist;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.PlaylistVideo;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.entity.composite_ids.PlaylistVideoPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, PlaylistVideoPrimaryKey> {


    PlaylistVideo findByPrimaryKey(PlaylistVideoPrimaryKey primaryKey);

    List<PlaylistVideo> findByPrimaryKeyPlaylist(Playlist playlist);

    List<PlaylistVideo> findByPrimaryKeyPlaylistOrderByAddedDate(Playlist playlist);

    List<PlaylistVideo> findByPrimaryKeyVideo(Video video);

    int countByPrimaryKeyPlaylist(Playlist playlist);

    /**
     * Returns Count of videos of each playlist in the list
     * @param playlists        The list
     * @return                 List<Integer> counts = holding number of videos in each playlist
     *                         counts.size() == playlists.size()
     *
     * IMPORTANT               COUNTS WILL HOLD VIDEOCOUNTS IN SORTED ORDER OF PLAYLIST.ID
     *
     *                         Example
     *                         playlist.size()==3
     *                         playlist[0].id = 100,
     *                         playlist[1].id = 50 ,
     *                         playlist[2].id = 200
     *
     *                         then
     *                         counts.size() == 3
     *                         counts[0] = videoCount(playlist50)
     *                         counts[1] = videoCount(playlist100)
     *                         counts[2] = videoCount(playlist200)
     *
     *   Native Sql Query      SELECT count(record.playlist_id) from `playlist` as play
     *                         left join `playlist_video` as record on play.id=record.playlist_id
     *                         where play.id in (23,24,25)
     *                         group by play.id
     */
    @Query(" SElECT COUNT(record.primaryKey.playlist)  FROM Playlist p LEFT JOIN PlaylistVideo record ON p=record.primaryKey.playlist WHERE p in (:playlists) GROUP BY p ORDER BY p.id")
    List<Integer> getCountsOfVideosInPlaylists(List<Playlist> playlists);


    /**
     *  Returns number of videos of each playlist in the list
     * @param playlists     The list
     * @return              List of PlaylistDTO
     *                      PlaylistDTO has 2 fields playlist and videoCount
     *
     *                      Native Sql Query      SELECT count(record.playlist_id) from `playlist` as play
     *                                              left join `playlist_video` as record on play.id=record.playlist_id
     *                                              where play.id in (23,24,25)
     *                                              group by play.id
     */
    @Query(" SElECT new com.example.vidupcoremodule.core.View_DTO.PlaylistDTO(p  ,COUNT(record.primaryKey.playlist)) FROM Playlist p LEFT JOIN PlaylistVideo record ON p=record.primaryKey.playlist WHERE p in (:playlists) GROUP BY p ORDER BY p.id")
    List<PlaylistDTO> getVideoCountsOfPlaylists(List<Playlist> playlists);

}
