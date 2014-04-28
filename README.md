# evercam.java [![Build Status](https://travis-ci.org/evercam/evercam.java.png)](https://travis-ci.org/evercam/evercam.java)

A Java wrapper around Evercam API

## Basic Usage
```java
import io.evercam.*;

//Developer key and id required for basic API requests.
API.setDeveloperKeypair("developerApiKey","developerApiId")

//Request user's key and id from Evercam
ApiKeyPair userKeyPair = API.requestUserKeyPairFromEvercam(username, password);
String userApiKey = userKeyPair.getApiKey();
String userApiId = userKeyPair.getApiId();

//User key and id is required for authentication.
API.setUserKeypair(userApiKey, userApiId)
```
### Cameras
```java
//Create new camera
Camera camera = Camera.create(new CameraBuilder("cameraid", //unique id
                                               "cameraname", //name
                                               true) //is public or not
                              .setJpgUrl("/Streaming/channels/1/picture")
                              .setCameraUsername("username")
                              .setCameraPassword("password")
                              .setExternalHttpPort(80)
                              .build());

//Updates full or partial data for an existing camera
Camera.patch(new PatchCameraBuilder("cameraid")
                              .setName("cameraname")
                              .setJpgUrl("/Streaming/channels/1/picture")
                              .setCameraUsername("username")
                              .setCameraPassword("password")
                              .setExternalRtspPort(554)
                              .build());
                              
//Delete camera by Evercam ID
Camera.delete("cameraId");

//Get camera by Evercam ID
Camera camera = Camera.getById("cameraId");
```
### Snapshots
```java
//Store a camera live snapshot on Evercam server
Camera.archiveSnapshot("cameraId", "notes")

//Get a list of archived snapshot
ArrayList<Snapshot> snapshots = Camera.getArchivedSnapshot("cameraId");

//Fetch latest archived snapshot from Evercam with data
Snapshot latestSnapshot = Camera.getLatestArchivedSnapshot("cameraId",true).
byte[] snapshotImageData = latestSnapshot.getData();
```
### Users
```java
//Create a new Evercam user account
UserDetail detail = new UserDetail();
detail.setFirstname("Joe");
detail.setLastname("Bloggs");
detail.setCountrycode("us");
detail.setEmail("joe.bloggs@example.org");
detail.setUsername("joeyb");
detail.setPassword("password")
User user = User.create(detail);

//Returns the list of cameras owned by a particular user
ArrayList<Camera> cameras = User.getCameras("joeyb");

//Get Evercam user by username
User user = new User("username")
```
### Vendors && Models
```java
//Get camera vendor by id
Vendor.getById("hikvision");
//Get camera vendor by MAC address
Vendor vendor = Vendor.getByMac("54:E6:FC");

//Get vendor's model by name("*" means default model)
Model model = vendor.getModel("*");
//Get all default values from model
String defaultUsername = model.getDefaults().getAuth("basic").getUsername();
String defaultPassword = model.getDefaults().getAuth("basic").getPassword();
String defaultJpgUrl = model.getDefaults().getJpgURL();
```
### Public
[Endpoints for publicly discoverable camera](https://dashboard.evercam.io/dev#!/public) are not yet implemented in Java, It can be supported if is requested and will be updated as soon as possible.
### Shares
[Endpoints for camera shares](https://dashboard.evercam.io/dev#!/shares) are not yet implemented in Java. It can be supported if is requested and will be updated as soon as possible.
