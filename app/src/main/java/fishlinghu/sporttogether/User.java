package fishlinghu.sporttogether;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by rainbowu on 3/22/17.
 */

public class User {

    private String name ;
    private int zipcode;
    private String sports ;
    private int level;
    private String photoUrl;
    //private String roomKey;
    //private CopyOnWriteArrayList<Double> ratingList = new CopyOnWriteArrayList<>();

    public User() {
    }

    public User( String name,  int zipcode, String sports, int level, String photoUrl) {

        this.name = name;
        this.zipcode = zipcode;
        this.sports = sports;
        this.level = level;
        this.photoUrl = photoUrl;
        //this.roomKey = "";
        //this.ratingList.add(5.0);

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

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrll(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    //public String getRoomKey() {return roomKey;}
    //public void setRoomKey(String temp) {this.roomKey = temp;}
    /*
    public int getRatingCount() {return ratingList.size();}
    public double getRating() {
        double sum = 0;
        Iterator<Double> it = ratingList.iterator();
        while(it.hasNext()){
            sum += it.next();
        }
        return sum/ratingList.size();
    }
    public void addNewRating(double newRating){
        ratingList.add(newRating);
    }
    */
}
