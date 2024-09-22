package org.example.vidupmediaprocessing.shared_dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {


    List<VideoDataDTO> videoData;
}
