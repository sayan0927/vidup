package com.example.videostreamingcore.core.service.userservices;

import com.example.videostreamingcore.core.service.videoservices.searching.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class AdminServices {


    @Autowired
    @Qualifier("LuceneSearch")
    SearchService searchService;







}
