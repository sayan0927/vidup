package org.example.vidupstreaming.storage.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
@Configuration
public class AzureConfig {

    @Value("${azure.storage.account}")
    public String storageAccount;

    // This SAS token gives access for whole of the storage account
    @Value("${azure.storage.account.sas.token}")
     String storageAccountSasToken;

    @Value("${azure.video.files.container}")
    public String videoFilesContainer;


    public AzureLocation getVideoFilesContainerLocation()
    {
        return new AzureLocation(storageAccount,storageAccountSasToken,videoFilesContainer);
    }

    public AzureFileLocation getVideoFileLocation(String videoFileName)
    {
        return new AzureFileLocation(storageAccount,storageAccountSasToken,videoFilesContainer,videoFileName);
    }

    public String getContainerFromUrl(URL url) throws Exception
    {
        String urlString = url.toString();

        urlString = urlString.replace("https://","");

        String[] parts = urlString.split("/")[0].split("\\.");

        return parts[0];
    }

    public AzureFileLocation getLocationFromURL(URL url) throws Exception
    {
        String urlString = url.toString();

        String protocol = "https://";

        urlString = urlString.replace(protocol,"");

        String[] parts = urlString.split("/");


        String storageAccountName = protocol + parts[0];
        String containerName = parts[1];
        String blobName = parts[2];

        return new AzureFileLocation(storageAccountName,storageAccountSasToken,containerName,blobName);

    }



}
