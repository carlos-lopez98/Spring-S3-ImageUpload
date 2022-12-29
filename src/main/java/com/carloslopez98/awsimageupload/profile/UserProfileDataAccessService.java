package com.carloslopez98.awsimageupload.profile;

import com.carloslopez98.awsimageupload.datastore.FakeUserProfileDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


//This class will interact directly with our dataStore
@Repository //The reason we use the dataAccessService is so that if need be in the future we only change one line of code in this class and can switch to an actual database
public class UserProfileDataAccessService {

    private final FakeUserProfileDataStore fakeUserProfileDataStore;

    @Autowired
    public UserProfileDataAccessService(FakeUserProfileDataStore fakeUserProfileDataStore) {
     this.fakeUserProfileDataStore = fakeUserProfileDataStore;
    }

        List<UserProfile> getUserProfiles(){
        return fakeUserProfileDataStore.getUserProfiles();
}

}
