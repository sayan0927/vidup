package com.example.vidupcoremodule.storage.dto;

import lombok.Data;

import java.net.URL;
import java.nio.file.Path;

@Data
public class UploadFileDTO {

    public UploadFileDTO(URL fileLocation, String fileName) {
        this.fileLocation = fileLocation;
        this.fileName = fileName;
    }

    public URL fileLocation;
    public String fileName;



}
