package org.example.vidupmediaprocessing.FFMPEG;


import lombok.Data;
import org.example.vidupmediaprocessing.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component("FFMpegWrapper")
public class FFMpegWrapper {

    String ffmpegMessage;
    @Autowired
    private StorageProperties storageProperties;

    public FFMpegWrapper(StorageProperties properties) {

        this.ffmpegMessage = null;
    }

    String getVideoCodec(String ffmpegMessage) {
        String[] tokens = ffmpegMessage.split(" ");

        for (int ptr = 0; ptr < tokens.length; ptr++) {
            if (tokens[ptr].equals("Video:") && ptr + 1 < tokens.length) return tokens[ptr + 1];
        }

        return null;
    }

    List<String> getAudioCodec(String ffmpegMessage) {
        List<String> codecs = new ArrayList<>();
        String[] tokens = ffmpegMessage.split(" ");

        for (int ptr = 0; ptr < tokens.length; ptr++) {
            if (tokens[ptr].equals("Audio:") && ptr + 1 < tokens.length) codecs.add(tokens[ptr + 1]);
        }

        return codecs;
    }

    public StreamCounts getStreamCounts(String inputFileName, Path inputFileLocation) {


        String cmd = "ffprobe " + (inputFileLocation.resolve(inputFileName)).toString();
        String[] commands = cmd.split(" ");
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectErrorStream(true);

        Process process = null;


        try {
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        StreamCounts streamCounts = new StreamCounts();


        String line;

                /*
                 sample lines produced by ffmpeg for each stream type
                 Stream #0:0(ger): Video: h264 (High),xxxxxxxxxxxx
                 Stream #0:1(ger): Audio: aac (LC), 48000 Hz, 5.1, fltp (default)
                 Stream #0:3(cze): Subtitle: subrip (srt)
                */
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = reader.readLine()) != null) {

            //    System.out.println(line);
                if (line.indexOf("Stream") == -1) continue;


             //   System.out.println("is stream");
                if (line.indexOf("Audio") != -1) {
                    streamCounts.audioStreams++;
                } else if (line.indexOf("Video") != -1) {
                    streamCounts.videoStreams++;
                } else if (line.indexOf("Subtitle") != -1) {
                    streamCounts.subtitleStreams++;
                }
            }


            return streamCounts;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public boolean compatibleCodecs(String fileName, Path inputFilePath) throws Exception {
        Path originalStorePath = inputFilePath.resolve(fileName);

        // // System.out.println("checking compatible codecs");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", originalStorePath.toString());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output of ffmpeg command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder message = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                message.append(line);
            }

            // // System.out.println(message.toString());

            return compatibleVcodec(message.toString()) && compatibleAcodecs(message.toString());
        } catch (Exception e) {
            throw e;
        }


    }

    public boolean splitVideoIntoPNG(Path videoLocation, String videoFileName, Path pngOutputPath, String fps, String outputFormat) {
        String inputFile = videoLocation.resolve(videoFileName).toString();

        //  // System.out.println(outputFormat+"  ffmt");

        String outputFiles = pngOutputPath.resolve(outputFormat).toString();


        //ffmpeg -i  C:\Users\sayan\Desktop\SEM6-PROJECT\videostore\transcoded\146-1-t.mp4 -vf fps=1 %04d.png

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", inputFile, "-vf", "fps=" + fps, outputFiles);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            StringBuilder processMessage = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                processMessage.append(line);
                processMessage.append("\n");
            }

            //  // System.out.println(processMessage);


            if (processMessage.toString().contains("Error")) return false;

            return !process.isAlive();

        } catch (IOException e) {
            // System.out.println(e);
            // System.out.println("\n Could Not Split Video Into PNG's");
            return false;
        }

    }

    public void splitVideoFileIntoChunksOfDuration(Path inputLocation, String inputFileName, Path outputPathLocation, Integer chunkDurationInSeconds, String outputPrefix, String outputSuffix) throws Exception {

        // System.out.println("got "+inputLocation+" "+inputFileName+" "+outputPathLocation);
        String ffInput = inputLocation.resolve(inputFileName).toString();


        String inputFormat = inputFileName.split("\\.")[1];
        String inputFileNameWithoutFormat = inputFileName.split("\\.")[0];


        String ffOutput = outputPrefix + inputFileNameWithoutFormat + outputSuffix + "." + inputFormat;
        ffOutput = outputPathLocation.resolve(ffOutput).toString();

        // System.out.println(ffInput+" a");
        // System.out.println(ffOutput+" b");

        //setting duration parameter
        int minutes = chunkDurationInSeconds / 60;
        int seconds = chunkDurationInSeconds % 60;

        String durationParam = minutes + ":" + seconds;


        try {
            ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", ffInput, "-threads", "3", "-map", "0:v:0", "-map", "0:a", "-vcodec", "copy", "-f", "segment", "-segment_time", durationParam, "-reset_timestamps", "1", ffOutput);

            pb.redirectErrorStream(true);
            Process splittingProcess = pb.start();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(splittingProcess.getInputStream()));

            StringBuilder message = new StringBuilder();
            String line;

            while (true) {
                line = bufferedReader.readLine();
                if (line == null) break;

                message.append(line);
                // System.out.println(line);

                if (line.contains("Opening")) {
                    String[] tokens = line.split(" ");

                    int filePtrIdx = -1;
                    for (int ptr = 0; ptr < tokens.length; ptr++) {
                        if (tokens[ptr].equals("Opening")) {
                            filePtrIdx = ptr + 1;
                            break;
                        }
                    }

                    // System.out.println(tokens[filePtrIdx]+" alubaba\n\n\n");
                }


            }

            // System.out.println(message.toString());

        } catch (IOException e) {
            throw e;
        }
    }

    public List<String> splitVideoFileIntoChunksOfDurationAndGetSplitFileNames(Path inputLocation, String inputFileName, Path outputPathLocation, Integer chunkDurationInSeconds, String outputPrefix, String outputSuffix) throws Exception {
        // System.out.println("got "+inputLocation+" "+inputFileName+" "+outputPathLocation);
        String ffInput = inputLocation.resolve(inputFileName).toString();


        String inputFormat = inputFileName.split("\\.")[1];
        String inputFileNameWithoutFormat = inputFileName.split("\\.")[0];


        String ffOutput = outputPrefix + inputFileNameWithoutFormat + outputSuffix + "." + inputFormat;
        ffOutput = outputPathLocation.resolve(ffOutput).toString();

        // System.out.println(ffInput+" a");
        // System.out.println(ffOutput+" b");

        //setting duration parameter
        int minutes = chunkDurationInSeconds / 60;
        int seconds = chunkDurationInSeconds % 60;

        String durationParam = minutes + ":" + seconds;


        try {
            ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", ffInput, "-threads", "3", "-map", "0:v:0", "-map", "0:a", "-vcodec", "copy", "-f", "segment", "-segment_time", durationParam, "-reset_timestamps", "1", ffOutput);

            pb.redirectErrorStream(true);
            Process splittingProcess = pb.start();


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(splittingProcess.getInputStream()));

            StringBuilder message = new StringBuilder();
            String line;
            List<String> fileNames = new ArrayList<>();

            while (true) {
                line = bufferedReader.readLine();
                if (line == null) break;

                message.append(line);
                // System.out.println(line);

                if (line.contains("Opening")) {
                    String[] tokens = line.split(" ");

                    int filePtrIdx = -1;
                    for (int ptr = 0; ptr < tokens.length; ptr++) {
                        if (tokens[ptr].equals("Opening")) {
                            filePtrIdx = ptr + 1;
                            break;
                        }
                    }

                    StringBuilder token = new StringBuilder(tokens[filePtrIdx]);
                    token.deleteCharAt(token.length() - 1);
                    token.deleteCharAt(0);

                    Path path = Paths.get(token.toString());

                    fileNames.add(path.getFileName().toString());
                }


            }


            return fileNames;

        } catch (IOException e) {
            throw e;

        }
    }

    /**
     * Copies the audio and video streams to MP4 container without Transcoding
     *
     * @param inputFileName the File Name to Transcode
     * @return The File Name after transcoding and saving
     * <p>
     * Configure the below paths if needed
     * The root directory for input file is fetched from
     * application.properties
     * storage.local.original-store-path = ....
     * The root directory for storing output file is fetched from
     * application.properties
     * storage.local.transcoded-store-path = .....
     */

    public String copyToMP4(String inputFileName, Path inputFilePath, Path outputFilePath) throws Exception {


        String fileNameWithoutFormat = inputFileName.split("\\.")[0];

        // change format to mp4
        String outputFileName = fileNameWithoutFormat + ".mp4";

        String inputFile = inputFilePath.resolve(inputFileName).toString();
        String outputFile = outputFilePath.resolve(outputFileName).toString();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", inputFile, "-map", "0:v:0",         // first video stream
                    "-map", "0:a", "-codec", "copy",
                    "-movflags", "+faststart",
                    outputFile);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            // Read the output of ffmpeg command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder message = new StringBuilder();
            while ((line = reader.readLine()) != null) message.append(line);

            this.ffmpegMessage = message.toString();

            if (ffmpegMessage.length() < 10) return null;

            // if (process.isAlive()) return null;

            return outputFileName;
        } catch (Exception e) {
            throw e;
        }


    }

    /**
     * Transcodes the file with file name as inputFileName to provided videoCodec and audioCodec and store in MP4 container
     *
     * @param inputFileName the File to Transcode
     * @param videoCodec    The target videoCodec
     * @param audioCodec    The target audioCodec
     * @return The File Name after transcoding and saving
     * <p>
     * Configure the below paths if needed
     * The root directory for input file is fetched from
     * application.properties
     * storage.local.original-store-path = ....
     * The root directory for storing output file is fetched from
     * application.properties
     * storage.local.transcoded-store-path = .....
     */

    public String transcodeToGivenVcodecAndAcodecWithMP4Container(String inputFileName, Path inputFilePath, Path outputfilePath, String videoCodec, String audioCodec) throws Exception {

        String fileNameWithoutFormat = inputFileName.split("\\.")[0];
        String outputFileName = fileNameWithoutFormat + ".mp4";  // change format to mp4
        String inputFile = inputFilePath.resolve(inputFileName).toString();
        String outputFile = outputfilePath.resolve(outputFileName).toString();

        // System.out.println(inputFile);
        // System.out.println(outputFile);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", inputFile, "-map", "0:v:0",         // first video stream
                    "-map", "0:a",         // all audio streams
                    "-c:v", videoCodec, "-c:a", audioCodec,
                    //   "-preset", "ultrafast",
                    //  "-tune","zerolatency",
                    // "-crf","28",
                    "-strict", "-2", "-sn",                  // exclude subtitles
                    outputFile);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            // Read the output of ffmpeg command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder message = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                message.append(line).append("\n");
                System.out.println(line + " \n");
                if (message.toString().contains("Error")) {
                    throw new Exception("Transcoding Error");
                }
            }

            this.ffmpegMessage = message.toString();

            System.out.println(ffmpegMessage);


            if (ffmpegMessage.length() < 10) throw new Exception("Transcoding Error");

            // if (process.isAlive()) throw new Exception("Transcoding Error");

            return outputFileName;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }


    }

    private boolean compatibleVcodec(String metadata) throws Exception {

        String vcodec = getVideoCodec(metadata);
        if (vcodec == null) throw new Exception("No video codec found");
        return vcodec.contains("h264") || vcodec.contains("libx264");
    }

    private boolean compatibleAcodecs(String metadata) throws Exception {
        List<String> acodecs = getAudioCodec(metadata);

        if (acodecs == null) throw new Exception("No audio codec found");

        for (String codec : acodecs) {
            if (!codec.contains("aac")) return false;
        }

        return true;
    }

    @Data
    public class StreamCounts {
        int audioStreams;
        int videoStreams;
        int subtitleStreams;
    }


}
