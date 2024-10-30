package org.example.vidupstreaming.storage.internal;



/**
 * This class represents location of a blob in Azure Files
 * Required field to identify a blob is Storage Account,Container and blobName
 * Sas Token is required to access the container.
 */


public class AzureFileLocation extends AzureLocation {



    String blobName;



    public AzureFileLocation(String storageAccount, String sasToken, String containerName, String blobName) {
        super(storageAccount, sasToken, containerName);
        this.blobName = blobName;
    }

    public String getBlobName() {
        return blobName;
    }



}
