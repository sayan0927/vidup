package com.example.vidupcoremodule.storage.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @Value("${final.store.location}")
    public String finalStoreLocation;
}
