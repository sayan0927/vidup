package com.example.videostreamingcore.core.service.videoservices.searching;

import com.example.videostreamingcore.core.entity.DatabaseEntities.Video;

import java.util.List;

/**
 * Provides API to get Videos matching Search Key and to Add/Remove Videos to/from Search Index
 */
public interface SearchService {

    List<Video> getSearchResults(String searchText);

    void addToSearchIndex(Video video);

    void removeFromSearchIndex(Video video);

}