package org.example.vidupstreaming.storage;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StorageProperties {


    @Autowired(required = false)
    LocalStorageService localStorageService;
    @Autowired(required = false)
    AzureStorageService azureStorageService;
    private String finalStoreLocation;

    @PostConstruct
    public void init() throws IOException {

    }

    public StorageService<?> getStorageService(String protocol) {
        if (protocol.contains(("http"))) {
            return azureStorageService;
        } else return localStorageService;
    }


}
