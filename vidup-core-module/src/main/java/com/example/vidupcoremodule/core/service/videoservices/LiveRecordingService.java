package com.example.vidupcoremodule.core.service.videoservices;



import com.example.vidupcoremodule.core.entity.DatabaseEntities.ContentChunk;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.LiveRecording;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.User;
import com.example.vidupcoremodule.core.repository.*;
import com.example.vidupcoremodule.storage.LocalStorageService;
import com.example.vidupcoremodule.storage.StorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LiveRecordingService {

    public static String LIVE_RECORDING_UPLOAD_FORMAT = ".webm";

    @Autowired
    private LiveRecordingRepository liveRecordingRepository;

    @Autowired
    private ContentChunkRepository contentChunkRepository;

    @Autowired
    private LocalStorageService storageService;
    @Autowired
    private StorageUtil storageProperties;

    public LiveRecording createNewRecording(String recordingName, String description, String visibility, User creator) {

        return liveRecordingRepository.save(LiveRecording.builder().
                startTime(LocalDateTime.now())
                .endTime(null)
                .creator(creator)
                .description(description.isEmpty() ? (creator.getUserName().concat(LocalDateTime.now().toString())) : recordingName)
                .name(recordingName.isEmpty() ? (creator.getUserName().concat(LocalDateTime.now().toString())) : recordingName)
                .visibility(visibility)
                .build());
    }

    public LiveRecording getRecordingById(UUID id) {
        return liveRecordingRepository.findById(id).orElseThrow();
    }

    public ContentChunk registerChunk(UUID contentId, byte[] chunk, int chunkSequence) {
        ContentChunk contentChunk = new ContentChunk();

        contentChunk.setContentId(contentId);
        contentChunk.setSequence(chunkSequence);
        contentChunk.setChunkData(chunk);
        contentChunk.setSize(chunk.length);
        return contentChunkRepository.save(contentChunk);
    }

    public void completeRecording(UUID recordingId) {
        LiveRecording liveRecording = liveRecordingRepository.findById(recordingId).orElseThrow();
        liveRecording.setEndTime(LocalDateTime.now());
        liveRecordingRepository.save(liveRecording);
    }

    public void buildFileFromChunks(UUID contentId) throws Exception {
        List<ContentChunk> chunks = contentChunkRepository.findContentChunkByContentIdOrderBySequence(contentId);

        for (ContentChunk chunk : chunks) {
            String folderName = chunk.getContentId().toString();
            String fileName = chunk.getContentId() + LIVE_RECORDING_UPLOAD_FORMAT;

            Path filePath = storageProperties.getLocal().getLiveRecordBasePath();
            storageService.appendChunks(chunk.getChunkData(), fileName, filePath);
        }
    }
}
