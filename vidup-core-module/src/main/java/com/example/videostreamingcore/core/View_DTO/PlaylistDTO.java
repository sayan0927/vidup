package com.example.videostreamingcore.core.View_DTO;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Playlist;

public record PlaylistDTO(Playlist playlist, Long videoCount){}
