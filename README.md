# evercam.java [![Build Status](https://travis-ci.org/evercam/evercam.java.png)](https://travis-ci.org/evercam/evercam.java)

A Java wrapper around Evercam API

## Basic Usage
```java
//import Evercam library
import io.evercam.*;

//Evercam Authentication required to access private contents
API.setAuth("evercamUsername","evercamPassword");
```
### Cameras
```java
//Request camera by ID, auth required for private cameras.
Camera camera = Camera.getById("id");
//Get camera snapshot as stream
InputStream stream = camera.getSnapshotStream();
//Get camera basic auth
camera.getAuth(Auth.TYPE_BASIC);

//Create camera in Evercam account, auth required
Camera camera = Camera.create(new CameraBuilder("cameraid", //unique id, not null
                                               "cameraname", //name, not null
                                               true, //is public or not, not null
                                               new String[]{"http://192.168.1.1:80"}) //endpoints, not null
                              .setSnapshotJPG("/Streaming/channels/1/picture")
                              .setBasicAuth("admin", "12345")
                              .build());

//Updates full or partial data for an existing camera, auth required for private cameras.
Camera.patch(new PatchCameraBuilder("cameraid") //not null
                              .setName("cameraname")
                              .setSnapshotJPG("/Streaming/channels/1/picture")
                              .setBasicAuth("admin", "12345")
                              .build());

```
### Users
```java
//Create a new user with Evercam
UserDetail detail = new UserDetail();
detail.setFirstname("Joe");
detail.setLastname("Bloggs");
detail.setCountrycode("us");
detail.setEmail("joe.bloggs@example.org");
detail.setUsername("joeyb");
User user = User.create(detail);

//Returns the list of cameras owned by a particular user
ArrayList<Camera> cameras = User.getCameras("joeyb");
```
### Vendors && Models
```java
//Coming Soon
```
