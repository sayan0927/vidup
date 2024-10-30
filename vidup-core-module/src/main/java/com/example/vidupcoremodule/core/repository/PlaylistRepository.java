package com.example.vidupcoremodule.core.repository;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Playlist;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist,Integer> {


    List<Playlist> getPlaylistsByCreator(User creator);



}
