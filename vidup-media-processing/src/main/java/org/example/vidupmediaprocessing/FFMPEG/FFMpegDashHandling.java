package org.example.vidupmediaprocessing.FFMPEG;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FFMpegDashHandling {

    @Autowired
    FFMpegWrapper ffmpegWrapper;


    /**
     * Creates MPEG-DASH files from the given videofile.
     * Allows user to supply their own ffmpeg command to generate dash files, but does not check correctness of supplied command
     * User of this method is responsible for cleanup of output folder if providing an invalid command
     *
     * <p>
     * //TODO
     * CURRENTLY SUBTITLE STREAMS ARE NOT HANDLED
     * <p>
     * IF ffcommand not provided, default command is used
     * DEFAULT COMMAND AND ADAPTATION SETS IS HARDCODED FOR 3 VIDEO STREAMS AND ALL AUDIO STREAMS
     *
     * @param inputFileName     Video file name
     * @param inputFileLocation Video file location
     * @param outputLocation    Dash files output location
     * @param manifestFileName  Name of the manifest file to produce
     * @throws Exception
     */
    public void createMpegDash(String inputFileName, Path inputFileLocation, Path outputLocation, String manifestFileName, String[] ffCommandTemplate) throws Exception {


        String in = inputFileLocation.resolve(inputFileName).toAbsolutePath().toString();
        String out = outputLocation.resolve(manifestFileName).toAbsolutePath().toString();
        /** IMPORTANT !
         *  FFMpeg only support UNIX style file seperators (/) when provided with absolute path for the  mpd file
         *  So Using Window's File Seperator wont work for .mpd files and we need to change the seperator
         */
        String currentSeperator = outputLocation.getFileSystem().getSeparator();
        String supportedSeperator = "/";
        out = out.replace(currentSeperator, supportedSeperator);


        FFMpegWrapper.StreamCounts streamCounts = ffmpegWrapper.getStreamCounts(inputFileName, inputFileLocation);


        String[] commands;

        if (ffCommandTemplate == null || ffCommandTemplate.length == 0) {
            throw new RuntimeException("ffmpeg dash command template is empty");

            //  commands = dashConfig.dashCommandTemplate();


        } else {
            commands = ffCommandTemplate;

            String adaptationSets = getDashAdaptationSets(streamCounts, commands);
            //replacing template values
            commands[inFileIdx(commands)] = in;
            commands[outFileIdx(commands)] = out;
            commands[adaptationsSetsIdx(commands)] = adaptationSets;
        }

        System.out.println("command " + Arrays.toString(commands));

        ProcessBuilder processBuilder = new ProcessBuilder(commands);


        processBuilder.redirectErrorStream(true);
        Process process = null;
        process = processBuilder.start();
        String line;
        Path logFilePath = outputLocation.resolve("dash.log");
        File logFile = Files.createFile(logFilePath).toFile();

       // System.out.println("\ncommand is " + Arrays.toString(commands));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); FileWriter writer = new FileWriter(logFile, true)) {

            while ((line = reader.readLine()) != null) {
                writer.append(line).append("\n");
               // System.out.println(line + " \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  int adaptationsSetsIdx(String[] command)
    {
        for(int i=0;i<command.length;i++)
        {
            if(command[i].equals("ADAPTATIONSETS"))
                return i;
        }
        return -1;
    }

    public int inFileIdx(String[] command)
    {
        for(int i=0;i<command.length;i++)
        {
            if(command[i].equals("INFILE"))
                return i;
        }
        return -1;
    }

    public  int outFileIdx(String[] command)
    {
        for(int i=0;i<command.length;i++)
        {
            if(command[i].equals("OUTFILE"))
                return i;
        }
        return -1;
    }


    private int countDashVideoStreamOutputFromCommands(String[] commands) {

        int count = 0;


        for (int i=0; i<commands.length-1;i++) {
           if(commands[i].startsWith("-map") && commands[i+1].startsWith("v:"))
               count++;
        }

        return count;
    }


    //TODO
    //      implement subtitle handling
    private String getDashAdaptationSets(FFMpegWrapper.StreamCounts streamCounts, String[] commands) throws Exception {
        if (streamCounts.videoStreams == 0)
            throw new Exception("No Video Stream found\n Could not create DASH adaptation set");

        if (streamCounts.audioStreams == 0)
            throw new Exception("No Audio Stream found\n Could not create DASH adaptation set");

        String vStreamSet = "id=0,streams=";

        int vCount = countDashVideoStreamOutputFromCommands(commands);

      //  System.out.println("vcount is " + vCount);

        for (int i = 0; i < vCount; i++)
            vStreamSet = vStreamSet + i + ",";


        //remove last comma
        vStreamSet = vStreamSet.substring(0, vStreamSet.length() - 1);

        List<String> aStreamSets = new ArrayList<>();

        int aStreamIdStart = 1, aStreamStart = vCount;

        for (int i = 0; i < streamCounts.audioStreams; i++) {
            String set = "id=" + aStreamIdStart + ",streams=" + aStreamStart;

            aStreamSets.add(set);

            aStreamIdStart++;
            aStreamStart++;
        }

        String finalAdaptSet = vStreamSet;

        for (String set : aStreamSets) {
            finalAdaptSet = finalAdaptSet + " " + set;
        }

        return finalAdaptSet;
    }
}
