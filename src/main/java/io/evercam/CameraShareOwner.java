package io.evercam;

import org.json.JSONObject;

public class CameraShareOwner extends EvercamObject implements CameraShareInterface {
    CameraShareOwner(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Unique user id of the owner
     */
    public String getUsername() {
        return jsonObject.getString("username");
    }

    /**
     * Email of the owner
     */
    public String getEmail() {
        return getStringNotNull("email");
    }

    /**
     * Full name of the owner
     */
    public String getFullName() {
        return getStringNotNull("fullname");
    }
}
