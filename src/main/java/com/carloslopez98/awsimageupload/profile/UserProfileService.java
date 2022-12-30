package com.carloslopez98.awsimageupload.profile;

import com.carloslopez98.awsimageupload.bucket.BucketName;
import com.carloslopez98.awsimageupload.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles(){
        return userProfileDataAccessService.getUserProfiles();
    }


    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        isFileEmpty(file);

        isImage(file);

        //Gets our userProfile --Check Helper Method--
        UserProfile profile = getUserProfileorElseThrowException(userProfileId);

        //Storing metaData from the file
        Map<String, String> metadata = getMetaData(file);

        /** Store the image in our s3 Bucket and update database (userProfileImageLink) with s3 image link --Using our FileStore */

        //Creating a path for us to use {bucketName/profileId}
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), profile.getUserProfileId());

        //This will be the file name --{fileName-randomId}
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());



        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            profile.setUserProfileImageLink(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }


    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {

        UserProfile profile = getUserProfileorElseThrowException(userProfileId);

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(),
                profile.getUserProfileId());

        return profile.getUserProfileImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }

    private Map<String, String> getMetaData(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();

        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getUserProfileorElseThrowException(UUID userProfileId) {
        return userProfileDataAccessService.getUserProfiles()
                                .stream()
                                .filter( userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                                .findFirst()
                                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s nor found", userProfileId)));
    }

    private void isImage(MultipartFile file) {
        if(!Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(),
                ContentType.IMAGE_PNG.getMimeType(),
                ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("File must be an image {JPEG, PNG, GIF}  [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalStateException("Cannot upload empty file [" + file.getSize() + "]");
        }
    }


}
