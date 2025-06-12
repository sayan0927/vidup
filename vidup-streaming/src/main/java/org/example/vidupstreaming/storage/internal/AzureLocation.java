package org.example.vidupstreaming.storage.internal;



/**
 * This class represents location of a container in Azure Files
 * Required field to identify a container is Storage Account and Container ,Sas Token is required to access the container.
 */
public class AzureLocation {


    String storageAccount;
    String sasToken;
    String containerName;

    public AzureLocation(String storageAccount, String sasToken, String containerName) {
        this.storageAccount = storageAccount;
        this.sasToken = sasToken;
        this.containerName = containerName;

    }



    public String getSasToken() {
        return sasToken;
    }

    public String getStorageAccount() {
        return storageAccount;
    }

    public String getContainerName() {
        return containerName;
    }




}
