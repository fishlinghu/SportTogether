package fishlinghu.sporttogether;

/**
 * Created by rainbowu on 3/22/17.
 */

public class User {

    private String name ;
    private int zipcode;
    private String sports ;
    private int level;
    private String roomKey;

    public User() {
    }

    public User( String name,  int zipcode, String sports, int level) {

        this.name = name;
        this.zipcode = zipcode;
        this.sports = sports;
        this.level = level;
        this.roomKey = "";
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
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

    public String getRoomKey() {return roomKey;}

    public void setRoomKey(String temp) {this.roomKey = temp;}


}
