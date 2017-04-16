package fishlinghu.sporttogether;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fishlinghu on 3/29/17.
 */

public class Chatroom {
    private String sport;
    private String intendedTime;
    private String location;
    private Double latitude;
    private Double longitude;


    //private Map<String,Boolean> userMap = new ConcurrentHashMap<String,Boolean>();

    public Chatroom(){}

    public Chatroom(String sport, String time, String location, Double latitude, Double longitude){
        this.sport = sport;
        this.intendedTime = time;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public String getSport() { return sport; }
    public String getIntendedTime() { return intendedTime; } //Why is this always return null!!!
    public String getLocation() { return location; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }



    /*
    public void addUser(String userEmail){
        userMap.put(userEmail, true);
    }
    public void removeUser(String userEmail){
        userMap.remove(userEmail);
    }

    public ArrayList<String> getOtherUserList(String userEmail){
        String tempStr;
        ArrayList<String> ret = new ArrayList<String>();

        // put other user's email in the list
        Iterator<String> it = userMap.keySet().iterator();
        while(it.hasNext()){
            tempStr = it.next();
            if(!tempStr.equals(userEmail))
                ret.add( tempStr );
        }

        return ret;
    }
    */

}
