package com.carloslopez98.awsimageupload.filestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3){
        this.s3 = s3;
    }


    //Needs path -- bucketName -- fileName
    //Now this methods saves files to our Amazon s3 bucket
    public void save(String path,
                     String fileName,
                     Optional<Map<String, String>> optionalMetaData,
                     InputStream inputStream){

        //ObjectMetadata class comes included with the amazonaws dependency we added to pom.xml -- using maven
        ObjectMetadata metaData = new ObjectMetadata();


        //If metaData is present, then we'll store it in local variable and pass it into our bucket
        optionalMetaData.ifPresent( data -> {
          if(!data.isEmpty()){
              data.forEach(metaData::addUserMetadata);
          }
        });


        try{
            s3.putObject(path, fileName, inputStream, metaData);
        } catch(AmazonServiceException e){
            throw new IllegalStateException("Failed to store file to S3", e);
        }

    }


  //--Note a byte array is what is downloaded when using downloads from the cloud--

    public byte[] download(String path, String key) {
        try{
            //--S3 object is being fetched with the S3.getObject method--
             S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        }catch(AmazonServiceException | IOException e){
            throw new IllegalStateException("Failed to download file from S3", e);
        }

    }
}
