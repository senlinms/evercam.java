# evercam.java

A Java wrapper around Evercam API

##Baisc Usage
```java
import io.evercam.*;

//Get vendor object by name
Vendor axis = new Vendor("axis");
String defaultUsername = axis.getFirmware("*").getAuth("basic").getUsername();

//Get a list of all camera vendors
Vendor.getAll();

//Get vendor by mac address
Vendor.getByMac("00:00:00");
```
