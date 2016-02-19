package io.evercam;

import org.json.JSONObject;

public class ProxyUrl extends EvercamObject {
    ProxyUrl(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getHls() {
        return jsonObject.getString("hls");
    }

    public String getRtmp() {
        return jsonObject.getString("rtmp");
    }
}