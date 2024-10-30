package org.example.vidupmediaprocessing.upload_pipeline.pipeline_messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Base Class for Kafka Messages in JSON format
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractPipelineMessage<T> implements Serializable {

    // this is required to correctly serialise generic types
    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_ARRAY, use = JsonTypeInfo.Id.CLASS, property = "videoIdClass")
    T videoId;

}
