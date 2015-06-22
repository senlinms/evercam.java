# evercam.java [![Build Status](https://travis-ci.org/evercam/evercam.java.png)](https://travis-ci.org/evercam/evercam.java)

The Java library around Evercam API

## Help make it better

The entire Evercam codebase is open source, see details: http://www.evercam.io/open-source and we'd love to see your pull requests!

For any bugs and discussions, please use [Github Issues](https://github.com/evercam/evercam.java/issues).

Any questions or suggestions around Evercam, drop us a line: http://www.evercam.io/contact


## Basic Usage
```java
import io.evercam.*;

//Request user's key and id from Evercam
ApiKeyPair userKeyPair = API.requestUserKeyPairFromEvercam("username/Email", "password");
String userApiKey = userKeyPair.getApiKey();
String userApiId = userKeyPair.getApiId();

//User key and id is required for authentication for all endpoints except /public, /vendors and /models.
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
Camera camera = Camera.getById("cameraId"
                                true);//Return camera with thumnail data or not
                                
//Returns the list of cameras owned by a particular user, including shared cameras and thumnail data
ArrayList<Camera> cameras = Camera.getAll("joeyb", true, true);
```
### Snapshots
```java
//Store a camera live snapshot on Evercam server
Snapshot.store("cameraId", "notes")

//Get a list of archived snapshot
ArrayList<Snapshot> snapshots = Snapshot.getArchivedSnapshots("cameraId");

//Fetch latest archived snapshot from Evercam with data
Snapshot latestSnapshot = Snapshot.getLatestArchivedSnapshot("cameraId",true).
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

//Fetch Evercam user details by username or Email address.
User user = new User("username/Email")
```
### Vendors && Models
```java
//Get camera vendor by id
Vendor vendor = Vendor.getById("hikvision");
//Search for camera vendors by MAC address
ArrayList<Vendor> vendorsByMac = Vendor.getByMac("54:E6:FC");
//Get a list of all supported vendors
ArrayList<Vendor> allVendors = Vendor.getAll();
//Search for vendors by vendor name
ArrayList<Vendor> vendorsByName = Vendor.getByName("vendorName");

//Get camera model by model id
Model model = Model.getById("ds-2cd2032-i");
//Get camera models that match the given name
ArrayList<Model> modelList = Model.getAllByName("DS-2CD2032-I");
//Get a list of camera model that associated with specified vendor id
ArrayList<Model> = Model.getAllByVendorId("hikvision");
//Get default model for a given vendor
Model defaultModel = Model.getDefaultModelByVendorId("hikvision");

//Retrieve default values from model
String defaultUsername = model.getDefaults().getAuth(Auth.TYPE_BASIC).getUsername();
String defaultPassword = model.getDefaults().getAuth(Auth.TYPE_BASIC).getPassword();
String defaultJpgUrl = model.getDefaults().getJpgURL();
```
### Shares
```java
//Create a new camera share
CameraShare.create("cameraId","username/Email","Snapshot,View,Edit,List");

//Get details for a share for a specific camera and user
CameraShare cameraShare = CameraShare.get("cameraId","username");
//Get the list of shares for a specified camera
ArrayList<CameraShare> shareList = CameraShare.getByCamera("cameraId");

//Delete an existing camera share by specifying user and camera
CameraShare.delete("cameraId","username/Email");
```
[Endpoints for PATCH camera shares and share requests](https://dashboard.evercam.io/dev#!/shares) are not yet implemented in Java, It can be supported if is requested and will be updated as soon as possible.
### Public
```java
//Fetch nearest publicly discoverable camera from within the Evercam system.
//If location isn't provided requester's IP address is used.
Camera camera = PublicCamera.getNearest("address/latLng/null");
//Returns jpg from nearest publicly discoverable camera from within the Evercam system.
InputStream jogStream = PublicCamera.getNearestJpg("address/latLng/null");
```
