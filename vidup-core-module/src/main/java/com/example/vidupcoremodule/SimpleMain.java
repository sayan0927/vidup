package com.example.vidupcoremodule;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;

import java.io.*;
import java.util.*;

public class SimpleMain {

    private static final int QUOTE_CHARACTER = '\'';
    private static final int DOUBLE_QUOTE_CHARACTER = '"';
    public static void main(String[] args) throws Exception {






    }

    public static <M> M test(M item, M nd) {
        System.out.println(item + " " + nd + item.getClass().getName()+" "+nd.getClass().getName());

        return item;
    }

    public static <E> void mergeArray(E ...list) {
        List<E> result = new ArrayList<E>();


    }


    



    static void lucene() throws IOException, ParseException {
        Directory index = new ByteBuffersDirectory();
        // Create an index writer
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(index, config);


        Document document = new Document();
        document.add(new TextField("name", "Watch kernel deveoper do Linux kernel development _-)(720P_HD)", Field.Store.YES));
        document.add(new StringField("id", "1", Field.Store.YES));
        indexWriter.addDocument(document);

        document = new Document();
        document.add(new TextField("name", "The Day Oil Went Negative_ These Unlikely Traders Made _660M(720P_HD)", Field.Store.YES));
        document.add(new StringField("id", "2", Field.Store.YES));
        indexWriter.addDocument(document);

        document = new Document();
        document.add(new TextField("name", "Watch Citadel_s high-speed trading in action(360P)", Field.Store.YES));
        document.add(new StringField("id", "3", Field.Store.YES));
        indexWriter.addDocument(document);



        document = new Document();
        document.add(new TextField("name", "Using My Python Skills To Punish Credit Card Scammers(720P_HD)", Field.Store.YES));
        document.add(new StringField("id", "4", Field.Store.YES));
        indexWriter.addDocument(document);

        document = new Document();
        document.add(new TextField("name", "Old Gods of Asgard - Herald of Darkness (Alan Wake 2 _ Official Music Video)(720P_HD)", Field.Store.YES));
        document.add(new StringField("id", "5", Field.Store.YES));
        indexWriter.addDocument(document);

        document = new Document();
        document.add(new TextField("name", "Nvidia CUDA in 100 Seconds(720P_HD)", Field.Store.YES));
        document.add(new StringField("id", "6", Field.Store.YES));
        indexWriter.addDocument(document);

        document = new Document();
        document.add(new TextField("name", "Atomic Heart Hedgie", Field.Store.YES));
        document.add(new StringField("id", "7", Field.Store.YES));
        indexWriter.addDocument(document);

        document = new Document();
        document.add(new TextField("name", "20 Minutes Of Funniest Animals 2023 -- Best Dogs Videos --(720P_HD)", Field.Store.YES));
        document.add(new StringField("id", "8", Field.Store.YES));
        indexWriter.addDocument(document);



        indexWriter.close();
        testSearch(1,index);

        IndexWriterConfig c = new IndexWriterConfig(analyzer);
        IndexWriter i = new IndexWriter(index, c);


        Query query = new TermQuery(new Term("id","6"));
        Query query1 = new TermQuery(new Term("name", "Nvidia CUDA in 100 Seconds(720P_HD)"));



        i.deleteDocuments(query);
        i.forceMergeDeletes();
        i.commit();


        testSearch(1000,index);
    }

    static void testSearch(int times,Directory index) throws IOException, ParseException {

        for(int i=0;i<times;i++)
        {
            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser queryParser = new QueryParser("name", new StandardAnalyzer());
            queryParser.setAllowLeadingWildcard(true);
            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
            Scanner scanner = new Scanner(System.in);
            String[] in = scanner.next().split(" ");

            for (String s:in)
            {
                Query termQuery = new FuzzyQuery(new Term("name", s),2,0);
                booleanQueryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
            }

            PhraseQuery.Builder phraseQueryBuilder = new PhraseQuery.Builder();
            phraseQueryBuilder.setSlop(2); // Adjust slop value as needed
            for (String token : in) {
                phraseQueryBuilder.add(new Term("content", token));
            }
            booleanQueryBuilder.add(phraseQueryBuilder.build(), BooleanClause.Occur.SHOULD);




            Query combinedQuery = booleanQueryBuilder.build();
            TopDocs topDocs = searcher.search(combinedQuery, 10);

            System.out.println("found "+topDocs.scoreDocs.length);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = searcher.doc(scoreDoc.doc);
                System.out.println(document.get("id"));

            }
        }
    }




        public static void test(String text) throws IOException {


            System.out.println(tokenize(text));

    }

    public static List<String> tokenize(String text) {
        StringBuilder stringBuilder = new StringBuilder(text);


        Set<Character> invalidChars = new HashSet<>();

        invalidChars.add(',');
        invalidChars.add('.');
        invalidChars.add('<');
        invalidChars.add('>');
        invalidChars.add('/');
        invalidChars.add('?');
        invalidChars.add(';');
        invalidChars.add(':');
        invalidChars.add('\''); //single quote
        invalidChars.add('"');
        invalidChars.add('[');
        invalidChars.add('{');
        invalidChars.add(']');
        invalidChars.add('}');
        invalidChars.add('\\');
        invalidChars.add('|');
        invalidChars.add('-');
        invalidChars.add('_');
        invalidChars.add('+');
        invalidChars.add('=');
        invalidChars.add(')');
        invalidChars.add('(');
        invalidChars.add('!');
        invalidChars.add('@');
        invalidChars.add('#');
        invalidChars.add('$');
        invalidChars.add('%');
        invalidChars.add('^');
        invalidChars.add('&');
        invalidChars.add('*');
        invalidChars.add('`');

        for(int i=0;i<stringBuilder.length();i++)
        {
            if(invalidChars.contains(stringBuilder.charAt(i)))
                stringBuilder.setCharAt(i,' ');

        }


        String[] toks = stringBuilder.toString().split(" ");
        List<String> finalToks = new ArrayList<>();

        for (String s:toks)
        {
            if(!(s.isEmpty() || s.contains(" ")))
                finalToks.add(s.toLowerCase());
        }

        return finalToks;
    }



}