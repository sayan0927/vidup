package org.example.vidupstreaming.service;


import org.springframework.stereotype.Service;

@Service
public class VideoStreamService {

    /*
    private final Logger logger = LoggerFactory.getLogger(VideoStreamService.class);



    @Autowired
    private final StreamingApplicationConstants constants;

    @Autowired
    AzureConfig azureConfig;


    public VideoStreamService(SessionRepository sessionRepository, StreamingApplicationConstants constants) {
        this.sessionRepository = sessionRepository;
        this.constants = constants;
    }


    public Mono<ResponseEntity<byte[]>> returnVideo(String httpRangeList, String fileType, Path filePath) {

        return Mono.just(prepareContent(fileType, httpRangeList, filePath));

    }


    public Mono<ResponseEntity<byte[]>> returnVideo(URL blobURL, String httpRangeList) {

        return Mono.just(prepareContent(blobURL, httpRangeList));
    }


    public ResponseEntity<byte[]> prepareContent(URL url, String range) {
        try {

            long rangeStart = 0;
            long rangeEnd = constants.CHUNK_SIZE;


            final Long fileSize = azureConfig.getBlobSize(url);// getFileSize(fileKey,filePath);


            if (range == null) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).header(constants.CONTENT_TYPE, constants.VIDEO_CONTENT + "mp4").header(constants.ACCEPT_RANGES, constants.BYTES).header(constants.CONTENT_LENGTH, String.valueOf(rangeEnd)).header(constants.CONTENT_RANGE, constants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                        //  .header(CONTENT_LENGTH, String.valueOf(fileSize))
                        .body(azureConfig.readByteRange(url, rangeStart, rangeEnd)); // Read the object and convert it as bytes
            }
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = rangeStart + constants.CHUNK_SIZE;
            }

            rangeEnd = Math.min(rangeEnd, fileSize - 1);
            final byte[] data = azureConfig.readByteRange(url, rangeStart, rangeEnd);
            final String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
            if (rangeEnd >= fileSize) {
                httpStatus = HttpStatus.OK;
            }
            return ResponseEntity.status(httpStatus).header(constants.CONTENT_TYPE, constants.VIDEO_CONTENT + "mp4").header(constants.ACCEPT_RANGES, constants.BYTES).header(constants.CONTENT_LENGTH, contentLength).header(constants.CONTENT_RANGE, constants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize).body(data);
        } catch (Exception e) {
            logger.error("Exception while reading the file {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    public ResponseEntity<byte[]> prepareContent(final String fileType, final String range, Path filePath) {

        try {

            long rangeStart = 0;
            long rangeEnd = constants.CHUNK_SIZE;


            final Long fileSize = sizeFromFile(filePath);// getFileSize(fileKey,filePath);


            if (range == null) {
                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).header(constants.CONTENT_TYPE, constants.VIDEO_CONTENT + fileType).header(constants.ACCEPT_RANGES, constants.BYTES).header(constants.CONTENT_LENGTH, String.valueOf(rangeEnd)).header(constants.CONTENT_RANGE, constants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
                        //  .header(CONTENT_LENGTH, String.valueOf(fileSize))
                        .body(readByteRangeByChunksNew(filePath, rangeStart, rangeEnd)); // Read the object and convert it as bytes
            }
            String[] ranges = range.split("-");
            rangeStart = Long.parseLong(ranges[0].substring(6));
            if (ranges.length > 1) {
                rangeEnd = Long.parseLong(ranges[1]);
            } else {
                rangeEnd = rangeStart + constants.CHUNK_SIZE;
            }

            rangeEnd = Math.min(rangeEnd, fileSize - 1);
            final byte[] data = readByteRangeByChunksNew(filePath, rangeStart, rangeEnd);
            final String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
            HttpStatus httpStatus = HttpStatus.PARTIAL_CONTENT;
            if (rangeEnd >= fileSize) {
                httpStatus = HttpStatus.OK;
            }
            return ResponseEntity.status(httpStatus).header(constants.CONTENT_TYPE, constants.VIDEO_CONTENT + fileType).header(constants.ACCEPT_RANGES, constants.BYTES).header(constants.CONTENT_LENGTH, contentLength).header(constants.CONTENT_RANGE, constants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize).body(data);
        } catch (IOException e) {
            logger.error("Exception while reading the file {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public byte[] readByteRangeByChunksNew(Path filePath, long start, long end) throws IOException {
        Path path = filePath;
        long length = end - start + 1;
        //    System.out.println(path+" path normal");
        byte[] result = new byte[(int) length];

        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(path))) {
            // Skip to the start position
            inputStream.skip(start);

            // Read the specified range into the result array
            int bytesRead = inputStream.read(result, 0, (int) length);

            if (bytesRead < length) {
                throw new IOException("Could not read the entire range from the file");
            }
        }

        return result;
    }


    public Path getVideoStorePath() {
        return constants.VIDEO_STORE_PATH;
    }






    private String getFilePath() {
        return constants.VIDEO_STORE_LOCATION;
    }




    public Long getFileSize(String fileName, Path filePath) {
        return Optional.ofNullable(fileName).map(file -> filePath).map(this::sizeFromFile).orElse(0L);
    }


    private Long sizeFromFile(Path path) {
        try {
            return Files.size(path);
        } catch (IOException ioException) {
            logger.error("Error while getting the file size", ioException);
        }
        return 0L;
    }

    */


}