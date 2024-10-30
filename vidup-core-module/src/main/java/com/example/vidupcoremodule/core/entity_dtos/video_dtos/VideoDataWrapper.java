package com.example.vidupcoremodule.core.entity_dtos.video_dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDataWrapper {


    List<VideoDataDTO> videoData;

}
