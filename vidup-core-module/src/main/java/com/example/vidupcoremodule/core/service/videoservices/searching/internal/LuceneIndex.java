package com.example.vidupcoremodule.core.service.videoservices.searching.internal;


import com.example.vidupcoremodule.core.entity.DatabaseEntities.Video;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service("LuceneIndex")
class LuceneIndex{

    static String ID = "id";
    static String NAME = "name";
    Directory directory;
    StandardAnalyzer standardAnalyzer;

    LuceneIndex() {
        directory = new ByteBuffersDirectory();
        standardAnalyzer = new StandardAnalyzer();
    }


    /**
     * This method should only be executed by a single thread
     * After creating indexWriter with directory,config instances ,
     * if we attempt to create another indexWriter instance with same directory,config instances , LockObtainFailedException is thrown
     *
     * @param video The video to add to Search Index
     */
    synchronized public void addVideo(Video video) {

        try {

            IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            Document document = new Document();
            document.add(new StringField(ID, video.getId().toString(), Field.Store.YES));
            document.add(new TextField(NAME, video.getVideoName(), Field.Store.YES));
            indexWriter.addDocument(document);
            indexWriter.close();
            notify();


        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


    /**
     * This method should only be executed by a single thread
     * After creating indexWriter with directory,config instances ,
     * if we attempt to create another indexWriter instance , LockObtainFailedException is thrown
     *
     * @param video The video to remove from Search Index
     */
    synchronized public void removeVideo(Video video) {
        try {
            IndexWriterConfig config = new IndexWriterConfig(standardAnalyzer);
            IndexWriter indexWriter = new IndexWriter(directory, config);

            Query query = new TermQuery(new Term(ID, video.getId().toString()));

            indexWriter.deleteDocuments(query);
            indexWriter.forceMergeDeletes();
            indexWriter.commit();
            indexWriter.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    public IndexSearcher getIndexSearcher() {
        try {
            IndexReader reader = DirectoryReader.open(directory);
            return new IndexSearcher(reader);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


}
