package com.example.vidupcoremodule.core.service.videoservices;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Tag;
import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public Set<Tag> getTagsById(List<String> tagIds)
    {

        if(tagIds==null || tagIds.isEmpty())
            return new HashSet<>();

        List<Integer> integerIds = tagIds.stream().map(Integer::valueOf).toList();
        List<Tag> tags = tagRepository.findAllById(integerIds);

        return new HashSet<>(tags);
    }

    public List<Video> filterTags(List<Video> videos, List<Integer> tags) {

        if (tags == null || tags.isEmpty()) return videos;

        List<Video> matching = new ArrayList<>();

        for (Video video : videos) {
            for (Integer tagId : tags) {

                Tag t = findById(tagId);

                if (video.getTags().contains(t))
                    matching.add(video);
            }
        }

        return matching;
    }

    public List<Tag> findAll()
    {
        return tagRepository.findAll();
    }

    public Tag findById(Integer id)
    {
        return tagRepository.findTagById(id);
    }
}
