# evercam.java [![Build Status](https://travis-ci.org/evercam/evercam.java.png)](https://travis-ci.org/evercam/evercam.java)

A Java wrapper around Evercam API

## Basic Usage
```java
import io.evercam.*;

//Developer key and id required for basic API requests.
API.setDeveloperKeypair("developerApiKey","developerApiId")

//Request user's key and id from Evercam
ApiKeyPair userKeyPair = API.requestUserKeyPairFromEvercam("username/Email", "password");
String userApiKey = userKeyPair.getApiKey();
String userApiId = userKeyPair.getApiId();

//User key and id is required for authentication.
API.setUserKeypair(userApiKey, userApiId)
```
### Cameras
```java
//Create new camera
Camera camera = Camera.create(new CameraBuilder("cameraid", //unique identifier
                                               "cameraname", //friendly name
                                               true) //is public or not
                              .setJpgUrl("/Streaming/channels/1/picture")
                              .setCameraUsername("username")
                              .setCameraPassword("password")
                              .setExternalHost("89.101.225.158")
                              .setExternalHttpPort(80)
                              .setLocation(latitude, longtitute)
                              .build());

//Updates full or partial data for an existing camera
Camera.patch(new PatchCameraBuilder("cameraid")
                              .setName("cameraname")
                              .setJpgUrl("/Streaming/channels/1/picture")
                              .setInternalHost("192.168.1.168")
                              .setInternalRtspPort(554)
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

//Returns the list of cameras owned by a particular user, including shared cameras.
ArrayList<Camera> cameras = User.getCameras("joeyb", true);

//Fetch Evercam user details by username or Email address.
User user = new User("username/Email")
```
### Vendors && Models
```java
//Get camera vendor by id
Vendor.getById("hikvision");
//Get camera vendor by MAC address
Vendor vendor = Vendor.getByMac("54:E6:FC");

//Get vendor's model by specifying model name
Model model = vendor.getModel(Model.DEFAULT_MODEL);
//Get all default values from model
String defaultUsername = model.getDefaults().getAuth(Auth.TYPE_BASIC).getUsername();
String defaultPassword = model.getDefaults().getAuth(Auth.TYPE_BASIC).getPassword();
String defaultJpgUrl = model.getDefaults().getJpgURL();
```
### Public
[Endpoints for publicly discoverable camera](https://dashboard.evercam.io/dev#!/public) are not yet implemented in Java, It can be supported if is requested and will be updated as soon as possible.
### Shares
[Endpoints for camera shares](https://dashboard.evercam.io/dev#!/shares) are not yet implemented in Java. It can be supported if is requested and will be updated as soon as possible.
