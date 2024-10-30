package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.mp4handling.internal;



import org.example.vidupmediaprocessing.storage.StorageProperties;
import org.example.vidupmediaprocessing.upload_pipeline.CoreApplicationConstants;
import org.example.vidupmediaprocessing.shared_dtos.VideoDataMP4DTO;
import org.mp4parser.Container;
import org.mp4parser.boxes.iso14496.part12.SampleDescriptionBox;
import org.mp4parser.boxes.sampleentry.AudioSampleEntry;
import org.mp4parser.muxer.FileRandomAccessSourceImpl;
import org.mp4parser.muxer.Movie;
import org.mp4parser.muxer.RandomAccessSource;
import org.mp4parser.muxer.Track;
import org.mp4parser.muxer.builder.DefaultMp4Builder;
import org.mp4parser.muxer.container.mp4.MovieCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
class Mp4ParserWrapper {




    @Autowired
    private StorageProperties storageProperties;






    public List<VideoDataMP4DTO> createVideoContentMP4(UUID videoId, File file, Path outputPath) throws Exception
    {
        List<Movie> movieList = getMoviesListFromFile(file);

        if(movieList==null)
            throw new Exception("No movies found in file ");

        for (Movie movie : movieList) {
            writeMovieToDisk(movie, videoId,outputPath);
        }


        return movieList.stream().map(movie -> {



            try {
                VideoDataMP4DTO dto = movieToVideoContentMP4(movie,videoId,outputPath);
                return dto;
            }catch (Exception e) {
                throw new RuntimeException(e);
            }


        }).toList();
    }


    private Track getVideoTrack(Movie movie)
    {
        for (Track track:movie.getTracks())
        {
            if (track.getHandler().equals(CoreApplicationConstants.mp4VideoHandlerName))
                return track;
        }
        return null;
    }


    private List<Movie> getMoviesListFromFile(File uploadedFile) throws Exception
    {
        List<Movie> movieList = new ArrayList<>();
        try {


            RandomAccessFile randomAccessFile = new RandomAccessFile(uploadedFile, "rws");
            RandomAccessSource randomAccessSource = new FileRandomAccessSourceImpl(randomAccessFile);
            ReadableByteChannel fileChannel = new FileInputStream(uploadedFile).getChannel();


            Movie movie = null;
            try {
                movie = MovieCreator.build(fileChannel, randomAccessSource, uploadedFile.getName());
            }
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            Track videoTrack = getVideoTrack(movie);
            if(videoTrack==null)
                return null;


            //Extract AudioTracks and save
            Set<String> languagesAdded = new HashSet<>();

            for (Track track : movie.getTracks())
            {
                // if not audio track
                if (!track.getHandler().equals(CoreApplicationConstants.mp4AudioHandlerName))
                    continue;

                String lang = getTrackLanguage(track);
                String audioCodec = null;

                audioCodec = getAudioTrackCodec(track);
                if(audioCodec==null)
                    return null;


                if (languagesAdded.contains(lang) || CoreApplicationConstants.unsupportedCodecs.contains(audioCodec))
                    continue;

                languagesAdded.add(lang);

                Movie temp = new Movie();
                temp.addTrack(videoTrack);
                temp.addTrack(track);
                movieList.add(temp);
            }
        }
        catch (Exception exception)
        {
            throw exception;

        }

        return movieList;
    }


    private String getTrackLanguage(Track track)
    {
        String lang = track.getTrackMetaData().getLanguage();

        if(lang.equals(CoreApplicationConstants.mp4UnidentifiedLanguage))
            return "unknown";
        else
            return lang;
    }

    private VideoDataMP4DTO movieToVideoContentMP4(Movie movie, UUID videoId, Path contentLocation) throws Exception
    {
        VideoDataMP4DTO v = new VideoDataMP4DTO();

        v.setVideoID(videoId);


        String storeName = getStorageName(movie,videoId);
        v.setMp4FileName(storeName);
        v.setAudioCodec(getAudioTrackCodec(movie.getTracks().get(1)));
        String language = getTrackLanguage(movie.getTracks().get(1));
        v.setLanguage(language);


        System.out.println("loc is "+ contentLocation.resolve(storeName)+" size is "+Files.size(contentLocation.resolve(storeName)));

        v.setSize(Files.size(contentLocation.resolve(storeName)));

        try {

            URL url = contentLocation.resolve(storeName).toUri().toURL();

            v.setMp4Location(url);
            return v;
        }catch (Exception e)
        {
            throw e;
        }



    }




    private Long getMovieDuration(Movie movie)
    {
        return movie.getTracks().get(0).getDuration()/ movie.getTracks().get(0).getTrackMetaData().getTimescale();

    }







    private String getStorageName(Movie movie, UUID videoID)
    {
        String baseName = videoID.toString();



        //should have only 1 video 1 audio stream
        if(movie.getTracks().size()!=2)
            return null;

        String language = movie.getTracks().get(1).getTrackMetaData().getLanguage();
        baseName = baseName.split("\\.")[0];

        String storageName = baseName+"-"+language+".mp4";

        return storageName;
    }


    private void writeMovieToDisk(Movie movie,UUID videoId,Path outputPath) throws Exception
    {
        Container mp4file = new DefaultMp4Builder().build(movie);


        if(!Files.exists(outputPath))
            Files.createDirectory(outputPath);




        try {
            String fileStorageName;
            fileStorageName = getStorageName(movie, videoId);
            Path filePath = outputPath.resolve(fileStorageName);

            FileChannel fc = new FileOutputStream(filePath.toString()).getChannel();
            mp4file.writeContainer(fc);
            fc.close();

        }
        catch (IOException e)
        {
           throw new Exception("Failed to write movie "+movie);
        }

    }


    private String getAudioTrackCodec(Track audioTrack)
    {
        SampleDescriptionBox sampleDescriptionBox = audioTrack.getSampleDescriptionBox();
        AudioSampleEntry audioSampleEntry = sampleDescriptionBox.getBoxes(AudioSampleEntry.class).get(0);



        return audioSampleEntry.getType();

    }





    private String getVideoTrackCodec(Movie movie)
    {

        SampleDescriptionBox sampleDescriptionBox = movie.getTracks().get(0).getSampleDescriptionBox();

        try
        {
            return sampleDescriptionBox.getSampleEntry().getType();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            return "unknown";
        }

    }




}
