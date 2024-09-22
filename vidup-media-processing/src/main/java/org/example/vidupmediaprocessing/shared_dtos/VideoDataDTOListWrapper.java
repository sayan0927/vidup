package org.example.vidupmediaprocessing.shared_dtos;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDataDTOListWrapper {


    List<? extends VideoDataDTO> videoData;
   // List<String> videoData;
}
