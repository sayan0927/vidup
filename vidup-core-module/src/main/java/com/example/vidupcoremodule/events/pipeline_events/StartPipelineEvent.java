
package com.example.vidupcoremodule.events.pipeline_events;


import com.example.vidupcoremodule.events.GenericEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.modulith.ApplicationModule;
import org.springframework.modulith.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class StartPipelineEvent<T, U, V> extends GenericEvent {

    T videoId;
    U fileStoreName;
    V fileStorePath;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(videoId), ResolvableType.forInstance(fileStoreName), ResolvableType.forInstance(fileStorePath));
    }
}
