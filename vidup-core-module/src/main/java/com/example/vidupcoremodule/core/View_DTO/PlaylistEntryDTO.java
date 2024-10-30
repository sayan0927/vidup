package com.example.vidupcoremodule.core.View_DTO;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;

import java.time.LocalDateTime;

public record PlaylistEntryDTO(Video video, User creator, LocalDateTime uploaded, int views) {
}
