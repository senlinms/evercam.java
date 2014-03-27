package io.evercam;

import java.util.UUID;

public class RandomUser
{
    public static final String FIRST_NAME = "JavaWrapper";
    public static final String LAST_NAME = "TestUser";
    public static final String COUNTRY_CODE = "us";

    private User user;
    private String username;
    private String password;
    private String email;

    public RandomUser() throws EvercamException
    {
        String randomUsername = randomUUID();
        String randomEmail = randomUUID();
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

    public static String randomUUID()
    {
        return String.valueOf(UUID.randomUUID()).replace("-","");
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
