package fishlinghu.sporttogether;

/**
 * Created by rainbowu on 3/22/17.
 */

public class User {
    private String email;
    private String name ;
    private int zipcode;
    private String sports ;
    private int level;

    public User() {
    }

    public User(String email,  String name,  int zipcode, String sports, int level) {
        this.email = email;
        this.name = name;
        this.zipcode = zipcode;
        this.sports = sports;
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public int getZipcode() {
        return zipcode;
    }

    public void setAge(int zipcode) {
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
