package com.example.videostreamingcore;

import com.example.videostreamingcore.events.EventPublisher;
import com.example.videostreamingcore.events.pipeline_events.StartPipelineEvent;
import org.assertj.core.internal.Paths;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;
import java.util.UUID;

//@SpringBootTest(classes = VidUpCoreStartup.class)
//@ActiveProfiles("dev")
class VidupCoreTests {




    /*
    @Test
    void verifiesModularStructure() {
        ApplicationModules modules = ApplicationModules.of(VidUpCoreStartup.class);
        modules.verify();
    }


    @Test
    void createApplicationModuleModel() {
        ApplicationModules modules = ApplicationModules.of(VidUpCoreStartup.class);
        modules.forEach(System.out::println);
    }

    @Test
    void createModuleDocumentation() {
        ApplicationModules modules = ApplicationModules.of(VidUpCoreStartup.class);
        new Documenter(modules)
                .writeDocumentation()
                .writeIndividualModulesAsPlantUml();
    }



     */



    /*
    @Autowired
    EventPublisher eventPublisher;
    @Test
    void testEventPublish()
    {
        StartPipelineEvent<UUID,String,Path> event = new StartPipelineEvent<>(UUID.randomUUID(),"name", Path.of("C:\\Users"));
        eventPublisher.publishGenericEvent(event);
    }

     */




}

