# evercam.java [![Build Status](https://travis-ci.org/evercam/evercam.java.png)](https://travis-ci.org/evercam/evercam.java)

A Java wrapper around Evercam API

## Basic Usage
```java
import io.evercam.*;

//Get camera by ID, auth is necessary for private cameras.
API.setAuth("evercamUsername","evercamPassword");
Camera camera = Camera.getById("id");
//Get camera snapshot as stream
InputStream stream = camera.getSnapshotStream();
//Get camera basic auth
camera.getAuth(Auth.TYPE_BASIC);
//Create camera in Evercam account, auth required
CameraDetail detail = new CameraDetail();
detail.setId("id");
detail.setName("name");
detail.setPublic(true);
detail.setSnapshotJPG("/Streaming/channels/1/picture");
detail.setBasicAuth("admin", "12345");
detail.setEndpoints(new String[]{"http://192.168.1.1:80"});
Camera.create(detail);

//Get vendor object by name
Vendor axis = new Vendor("axis");
String defaultUsername = axis.getFirmware("*").getAuth("basic").getUsername();

//Get a list of all camera vendors
Vendor.getAll();

//Get vendor by mac address
Vendor.getByMac("00:00:00");
```
