package com.example.vidupcoremodule.storage.internal;

import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * This class represents location of a container in Azure Files
 * Required field to identify a container is Storage Account and Container ,Sas Token is required to access the container.
 */
@Data
@NoArgsConstructor
public class AzureLocation extends CloudLocation{


    String storageAccount;
    String sasToken;
    String containerName;

    public AzureLocation(String storageAccount, String sasToken, String containerName) {
        this.storageAccount = storageAccount;
        this.sasToken = sasToken;
        this.containerName = containerName;

    }




}
