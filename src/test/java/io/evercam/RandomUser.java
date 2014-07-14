package io.evercam;

import java.util.UUID;

public class RandomUser
{
    public static final String FIRST_NAME = "JavaWrapper";
    public static final String LAST_NAME = "TestUser";
    public static final String COUNTRY_CODE = "us";

    public static final String CAMERA_NAME = "Random Camera";
    public static final String CAMERA_INTERNAL_HOST = "192.168.1.2";
    public static final String CAMERA_EXTERNAL_HOST = "123.123.123.123";
    public static final int CAMERA_INTERNAL_HTTP = 80;
    public static final int CAMERA_INTERNAL_RTSP = 554;
    public static final int CAMERA_EXTERNAL_HTTP = 8080;
    public static final int CAMERA_EXTERNAL_RTSP = 554554;
    public static final String CAMERA_USERNAME = "username";
    public static final String CAMERA_PASSWORD = "password";
    public static final String CAMERA_JPG_URL = "/snapshot.jpg";
    public static final String CAMERA_RTSP_URL = "/rtsp";
    public static final String CAMERA_TIMEZONE = "Etc/UTC";
    public static final String CAMERA_VENDOR = "hikvision";
    public static final String CAMERA_VENDOR_NAME = "Hikvision Digital Technology";
    public static final String CAMERA_MODEL = Model.DEFAULT_MODEL;
    public static final String CAMERA_MAC = "11:11:11:11:11:11";

    public static final String CAMERA_INTERNAL_JPG_URL =  "http://192.168.1.2/snapshot.jpg";
    public static final String CAMERA_EXTERNAL_JPG_URL =  "http://123.123.123.123:8080/snapshot.jpg";
    public static final String CAMERA_INTERNAL_RTSP_URL =  "rtsp://192.168.1.2:554/h264/ch1/main/av_stream";
    public static final String CAMERA_EXTERNAL_RTSP_URL =  "rtsp://123.123.123.123:554554/h264/ch1/main/av_stream";
    public static final String CAMERA_INTERNAL_RTSP_URL_WITH_AUTH =  "rtsp://username:password@192.168.1.2:554/h264/ch1/main/av_stream";
    public static final String CAMERA_EXTERNAL_RTSP_URL_WITH_AUTH =  "rtsp://username:password@123.123.123.123:554554/h264/ch1/main/av_stream";

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
        CameraDetail detail = new CameraBuilder(randomUUID(), CAMERA_NAME, isPublic).setInternalHost(CAMERA_INTERNAL_HOST).setInternalHttpPort(CAMERA_INTERNAL_HTTP).setInternalRtspPort(CAMERA_INTERNAL_RTSP).setExternalHost(CAMERA_EXTERNAL_HOST).setExternalHttpPort(CAMERA_EXTERNAL_HTTP).setExternalRtspPort(CAMERA_EXTERNAL_RTSP).setCameraUsername(CAMERA_USERNAME).setCameraPassword(CAMERA_PASSWORD).setJpgUrl(CAMERA_JPG_URL).setRtspUrl(CAMERA_RTSP_URL).setTimeZone(CAMERA_TIMEZONE).setVendor(CAMERA_VENDOR).setModel(CAMERA_MODEL).setMacAddress(CAMERA_MAC).build();
        Camera camera = Camera.create(detail);
        API.setUserKeyPair(null, null);
        return camera;
    }

    public Camera addBasicCamera() throws EvercamException
    {
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(getUsername(), getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        CameraDetail detail = new CameraBuilder(randomUUID(), CAMERA_NAME, true).setInternalHost(CAMERA_INTERNAL_HOST).build();
        Camera camera = Camera.create(detail);
        API.setUserKeyPair(null, null);
        return camera;
    }

    public Camera addRealCamera() throws EvercamException
    {
        ApiKeyPair apiKeyPair = API.requestUserKeyPairFromEvercam(getUsername(), getPassword());
        API.setUserKeyPair(apiKeyPair.getApiKey(), apiKeyPair.getApiId());
        CameraDetail detail = new CameraBuilder(randomUUID(), CAMERA_NAME, true).setExternalHost(TestForReal.REAL_CAMERA_IP).setExternalHttpPort(8101)
                .setCameraUsername(TestForReal.REAL_CAMERA_USERNAME).setCameraPassword(TestForReal.REAL_CAMERA_PASSWORD).setJpgUrl("/Streaming/channels/1/picture").build();
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
