package models;

public class UserWithCreds {
    private String email;
    private String password;

    public UserWithCreds(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
