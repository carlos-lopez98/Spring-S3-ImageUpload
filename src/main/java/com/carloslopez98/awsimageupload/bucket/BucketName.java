package com.carloslopez98.awsimageupload.bucket;


//Enums are intended to be predefined set of values
//Java won't let you instantiate an enum
public enum BucketName {

    PROFILE_IMAGE("carlos-image-upload-123");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
