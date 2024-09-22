package com.example.videostreamingcore.core.View_DTO;

import com.example.videostreamingcore.core.entity.DatabaseEntities.User;
import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;

import java.time.LocalDateTime;

public record PlaylistEntryDTO(Video video, User creator, LocalDateTime uploaded, int views) {
}
