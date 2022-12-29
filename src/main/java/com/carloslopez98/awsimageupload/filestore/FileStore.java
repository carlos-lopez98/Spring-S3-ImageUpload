package com.carloslopez98.awsimageupload.filestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.s3.model.ObjectMetadata;

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

}
