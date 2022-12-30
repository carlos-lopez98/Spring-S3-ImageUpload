package com.carloslopez98.awsimageupload.datastore;


import com.carloslopez98.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository
public class FakeUserProfileDataStore {

    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static{
        USER_PROFILES.add(new UserProfile(UUID.fromString("96b2f3bd-ada3-45c1-a085-dec74505bebc"), "Janetjones", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("c74c1bea-95bf-4f02-a4e7-1f389738e0b8"), "antoniojunior", null));
    }

    public List<UserProfile> getUserProfiles(){
        return USER_PROFILES;
    }
}
