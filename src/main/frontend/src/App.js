import React, { useState, useEffect, useCallback } from "react";
import logo from './logo.svg';
import './App.css';
import axios from "axios";
import { useDropzone } from 'react-dropzone'

//React Component
const UserProfiles = () => {

  //--Note Use Brackets when using the useState hook--//
  const [userProfiles, setUserProfiles] = useState([]); //userProfiles is set = to an empty array, using useState[]

  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/api/v1/user-profile").then(res => {
      console.log(res);

      setUserProfiles(res.data); //This sets userProfiles array = res.data
    });
  }

  //--useEffect runs everytime the state changes and initially when the component first loads--//
  useEffect(() => {
    fetchUserProfiles();
  }, []);

  //--renders both user profiles since we're using the .map function--//
  return userProfiles?.map((userProfile, index) => {
    return (
      <div key={index}>

        {/*Either have an image or you don't*/}
        {userProfile.userProfileId ? <img src={`http://localhost:8080/api/v1/user-profile/${userProfile.userProfileId}/image/download`} /> : null}

        {/* TODO profile image */}
        < br />
        <br />
        <h1>{userProfile.userName}</h1>
        <p>{userProfile.userProfileId}</p>
        <Dropzone userProfileId={userProfile.userProfileId} />
        <br />
      </div>
    );
  });

};

function Dropzone({ userProfileId }) {
  const onDrop = useCallback(acceptedFiles => {

    const file = acceptedFiles[0];
    console.log(file);
    const formData = new FormData();

    //This name has to be the same as the backend name used in @RequestParam
    formData.append("file", file);

    axios.post(
      `http://localhost:8080/api/v1/user-profile/${userProfileId}/image/upload`,
      formData,
      {
        headers: {
          "Content-Type": "mulipart/form-data"
        }
      }
    ).then(() => {
      console.log("file uploaded successfully")
    }).catch(err => {
      console.log(err);
    });

    // Do something with the files
  }, [])
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop })

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here</p> :
          <p>Drag 'n' drop profile image, or click to select profile image</p>
      }
    </div>
  )
}

function App() {
  return (
    <div className="App">
      <UserProfiles />
    </div>
  );
}



export default App;
