package vangthao.app.thoitiet.model;

public class UserLogin {
    private int id;
    private String userName;
    private String email;

    public UserLogin(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserLogin(int id, String userName, String email) {
        this.id = id;
        this.userName = userName;
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
