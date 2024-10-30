package com.example.vidupcoremodule.core.service.videoservices.searching.internal;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import com.example.vidupcoremodule.core.service.videoservices.VideoService;
import com.example.vidupcoremodule.core.service.videoservices.searching.SearchService;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service("LuceneSearch")
class LuceneSearch implements SearchService {




    @Autowired
    private VideoService videoService;

    private static final String WILDCARD="*";

    @Autowired
    private LuceneIndex luceneIndex;


    @Override
    public List<Video> getSearchResults(String searchText) {

        try {


            QueryParser queryParser = new QueryParser(LuceneIndex.NAME, new StandardAnalyzer());
            queryParser.setAllowLeadingWildcard(true);
            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();

            String[] searchTokens = searchText.split(" ");

            for (String tok:searchTokens) {

                Query termQuery = new FuzzyQuery(new Term(LuceneIndex.NAME, tok),2,0);
                booleanQueryBuilder.add(termQuery,BooleanClause.Occur.SHOULD);
            }


            Query combinedQuery = booleanQueryBuilder.build();

            // Search with the combined query


            // Dynamically cast the return value
            IndexSearcher indexSearcher = luceneIndex.getIndexSearcher();
            TopDocs topDocs = indexSearcher.search(combinedQuery, 10);

            List<Video> result = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs)
            {
                Document document = indexSearcher.doc(scoreDoc.doc);
                result.add(videoService.getVideoById(UUID.fromString(document.get(LuceneIndex.ID))));
            }

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addToSearchIndex(Video video) {
        luceneIndex.addVideo(video);
    }

    @Override
    public void removeFromSearchIndex(Video video) {
        luceneIndex.removeVideo(video);
    }
}
