package User;

public class User {
    private int id;
    private String username;
    private String password;
    private ROLE role;

    public User() {
    }

    public User(int id, String username, String password, ROLE role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public enum ROLE {
        USER, ADMIN, UNKNOWN;
    }
}
