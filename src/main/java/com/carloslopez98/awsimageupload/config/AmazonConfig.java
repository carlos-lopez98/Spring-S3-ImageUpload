package com.carloslopez98.awsimageupload.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.macie2.model.AwsAccount;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//This file configures and provides an s3 Bucket for Springboot to connect with
@Configuration
public class AmazonConfig {

    //This will give us the s3 Client

    @Bean //--Spring instantiates an instance of this class so we can inject into other classes
    public AmazonS3 s3(){

        //Fetches credentials from the AWS config file
        AWSCredentialsProvider credentials = new ProfileCredentialsProvider();

        AWSCredentials awsCredentials = new
                BasicAWSCredentials(credentials.getCredentials().getAWSAccessKeyId(), credentials.getCredentials().getAWSSecretKey());


        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
