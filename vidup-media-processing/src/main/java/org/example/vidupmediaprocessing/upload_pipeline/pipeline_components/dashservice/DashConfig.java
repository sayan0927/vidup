package org.example.vidupmediaprocessing.upload_pipeline.pipeline_components.dashservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component

public class DashConfig {

    @Value("${dash.base.url.template}")
    public String baseUrlTemplate;

    public String videoIdPlaceHolder = "VIDEO_ID";

    public  String manifestName = "manifest.mpd";


    public String[] dashCommandTemplate()
    {
        return new String[] {"ffmpeg", "-i", "INFILE", "-y",
                "-map", "v:0", "-s:0", "960x540", "-b:v:0", "2M", "-maxrate:0", "2.14M", "-bufsize:0", "3.5M",
                "-map", "v:0", "-s:1", "416x234", "-b:v:1", "145k", "-maxrate:1", "155k", "-bufsize:1", "220k",
                "-map", "v:0", "-s:7", "1920x1080", "-b:v:7", "6M", "-maxrate:7", "6.42M", "-bufsize:7", "11M",
                "-map", "0:a",
                "-use_template", "1", "-use_timeline", "1",
                "-seg_duration", "4",
                "-adaptation_sets", "ADAPTATIONSETS", "-f", "dash", "OUTFILE"};
    }

    public String[] dashCommandOriginalAnd480pTemplate()
    {
        return new String[] {"ffmpeg", "-i", "INFILE", "-y",
                "-map", "v:0",
                "-map", "v:0", "-s:1", "640x480", "-b:v:1", "1M", "-maxrate:1", "1.07M", "-bufsize:1", "1.5M",
                "-map", "0:a",
                "-use_template", "1", "-use_timeline", "1",
                "-seg_duration", "4",
                "-adaptation_sets", "ADAPTATIONSETS", "-f", "dash", "OUTFILE"};
    }



}
