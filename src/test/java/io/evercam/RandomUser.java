package io.evercam;

import java.util.UUID;

public class RandomUser
{
    public static final String FIRST_NAME = "JavaWrapper";
    public static final String LAST_NAME = "TestUser";
    public static final String COUNTRY_CODE = "us";
    public static final Float LOCATION_LAT = 37.377166f;
    public static final Float LOCATION_LNG = -122.086966f;
    public static final String LOCATION_LAT_STRING = "37.377166f";
    public static final String LOCATION_LNG_STRING = "-122.086966f";
    public static final String CAMERA_NAME = "Random Camera";
    public static final String CAMERA_INTERNAL_HOST = "192.168.1.2";
    public static final String CAMERA_EXTERNAL_HOST = "123.123.123.123";
    public static final int CAMERA_INTERNAL_HTTP = 81;
    public static final int CAMERA_INTERNAL_RTSP = 554;
    public static final int CAMERA_EXTERNAL_HTTP = 8080;
    public static final int CAMERA_EXTERNAL_RTSP = 554554;
    public static final String CAMERA_USERNAME = "username";
    public static final String CAMERA_PASSWORD = "password";
    public static final String CAMERA_JPG_URL = "/snapshot.jpg";
    public static final String CAMERA_H264_URL = "/h264";
    public static final String CAMERA_MJPG_URL = "/mjpg";
    public static final String CAMERA_MPEG_URL = "/mpeg";
    public static final String CAMERA_AUDIO_URL = "/audio";
    public static final String CAMERA_TIMEZONE = "Etc/UTC";
    public static final String CAMERA_VENDOR = "hikvision";
    public static final String CAMERA_VENDOR_NAME = "Hikvision Digital Technology";
    public static final String CAMERA_MODEL_ID = "hikvision" + Model.DEFAULT_MODEL_SUFFIX;
    public static final String CAMERA_MODEL_NAME = Model.DEFAULT_MODEL_NAME;
    public static final String CAMERA_MAC = "11:11:11:11:11:11";

    public static final String CAMERA_INTERNAL_URL = "http://192.168.1.2:81";
    public static final String CAMERA_EXTERNAL_URL = "http://123.123.123.123:8080";
    public static final String CAMERA_INTERNAL_JPG_URL = "http://192.168.1.2:81/snapshot.jpg";
    public static final String CAMERA_EXTERNAL_JPG_URL = "http://123.123.123.123:8080/snapshot.jpg";
    public static final String CAMERA_INTERNAL_MJPG_URL = "http://192.168.1.2:81/mjpg";
    public static final String CAMERA_EXTERNAL_MJPG_URL = "http://123.123.123.123:8080/mjpg";
    public static final String CAMERA_INTERNAL_MPEG_URL = "rtsp://192.168.1.2:554/mpeg";
    public static final String CAMERA_EXTERNAL_MPEG_URL = "rtsp://123.123.123.123:554554/mpeg";
    public static final String CAMERA_INTERNAL_AUDIO_URL = "rtsp://192.168.1.2:554/audio";
    public static final String CAMERA_EXTERNAL_AUDIO_URL = "rtsp://123.123.123.123:554554/audio";
    public static final String CAMERA_INTERNAL_RTSP_URL = "rtsp://192.168.1.2:554/h264";
    public static final String CAMERA_EXTERNAL_RTSP_URL = "rtsp://123.123.123.123:554554/h264";
    public static final String CAMERA_INTERNAL_RTSP_URL_WITH_AUTH = "rtsp://username:password@192.168.1.2:554/h264";
    public static final String CAMERA_EXTERNAL_RTSP_URL_WITH_AUTH = "rtsp://username:password@123.123.123.123:554554/h264";

    private User user;
    private String username;
    private String password;
    private String email;

    public RandomUser() throws EvercamException
    {
        String randomUsername = randomUUID();
        String randomEmail = randomEmail();
        String randomPassword = randomUUID();
        this.username = randomUsername;
        this.email = randomEmail;
        this.password = randomPassword;

        UserDetail detail = new UserDetail();
        detail.setFirstname(FIRST_NAME);
        detail.setLastname(LAST_NAME);
        detail.setCountrycode(COUNTRY_CODE);
        detail.setEmail(randomEmail);
        detail.setUsername(randomUsername);
        detail.setPassword(randomPassword);

        user = User.create(detail);
    }

    public Camera addRandomCamera(boolean isPublic) throws EvercamException
    {
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(getUsername(), getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        CameraDetail detail = new CameraBuilder(CAMERA_NAME, isPublic).setInternalHost(CAMERA_INTERNAL_HOST).setInternalHttpPort(CAMERA_INTERNAL_HTTP).setInternalRtspPort(CAMERA_INTERNAL_RTSP).setExternalHost(CAMERA_EXTERNAL_HOST).setExternalHttpPort(CAMERA_EXTERNAL_HTTP).setExternalRtspPort(CAMERA_EXTERNAL_RTSP).setCameraUsername(CAMERA_USERNAME).setCameraPassword(CAMERA_PASSWORD).setJpgUrl(CAMERA_JPG_URL).setH264Url(CAMERA_H264_URL).setTimeZone(CAMERA_TIMEZONE).setVendor(CAMERA_VENDOR).setModel(CAMERA_MODEL_ID).setMacAddress(CAMERA_MAC).build();
        Camera camera = Camera.create(detail);
        API.setUserKeyPair(null, null);
        return camera;
    }

    public Camera addBasicCamera() throws EvercamException
    {
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(getUsername(), getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        CameraDetail detail = new CameraBuilder(CAMERA_NAME, true).setInternalHost(CAMERA_INTERNAL_HOST).setId
                (randomUUID()).build();
        Camera camera = Camera.create(detail);
        API.setUserKeyPair(null, null);
        return camera;
    }

    public Camera addFullCamera() throws EvercamException
    {
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(getUsername(), getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        CameraDetail detail = basicCamera().setLocation(LOCATION_LAT, LOCATION_LNG).setOnline(true).setInternalHost(RandomUser.CAMERA_INTERNAL_HOST).setInternalHttpPort(RandomUser.
                CAMERA_INTERNAL_HTTP).setInternalRtspPort(RandomUser.CAMERA_INTERNAL_RTSP).setExternalHost(RandomUser.CAMERA_EXTERNAL_HOST).setExternalHttpPort(RandomUser.CAMERA_EXTERNAL_HTTP).setExternalRtspPort(RandomUser.CAMERA_EXTERNAL_RTSP).setCameraUsername(RandomUser.CAMERA_USERNAME).setCameraPassword(RandomUser.CAMERA_PASSWORD).setJpgUrl(RandomUser.CAMERA_JPG_URL).setH264Url(RandomUser.CAMERA_H264_URL).setMjpgUrl(RandomUser.CAMERA_MJPG_URL).setMpegUrl(RandomUser.CAMERA_MPEG_URL).setAudioUrl(RandomUser.CAMERA_AUDIO_URL).setTimeZone(RandomUser.CAMERA_TIMEZONE).setVendor(RandomUser.CAMERA_VENDOR).setModel(RandomUser.CAMERA_MODEL_ID).setMacAddress(RandomUser.CAMERA_MAC).build();
        Camera camera = Camera.create(detail);
        API.setUserKeyPair(null, null);
        return camera;
    }

    public CameraBuilder basicCamera() throws EvercamException
    {
        return new CameraBuilder(CAMERA_NAME, true).setInternalHost(CAMERA_INTERNAL_HOST).setId(randomUUID());
    }

    public Camera addRealCamera() throws EvercamException
    {
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(getUsername(), getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        CameraDetail detail = new CameraBuilder(CAMERA_NAME, true).setExternalHost(LocalConstants.IP)
                .setExternalHttpPort(LocalConstants.HTTP_PORT).setCameraUsername(LocalConstants.USERNAME)
                .setCameraPassword(LocalConstants.PASSWORD).setJpgUrl("/Streaming/channels/1/picture").setId(randomUUID())
                .build();
        Camera camera = Camera.create(detail);
        API.setUserKeyPair(null, null);
        return camera;
    }

    public static String randomUUID()
    {
        return String.valueOf(UUID.randomUUID()).replace("-", "");
    }

    public static String randomEmail()
    {
        String randomUuid = randomUUID();
        char[] emailChars = randomUuid.toCharArray();
        emailChars[8] = '@';
        emailChars[15] = '.';
        return String.valueOf(emailChars);
    }

    public User getUser()
    {
        return user;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getEmail()
    {
        return email;
    }
}
