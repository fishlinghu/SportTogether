package fishlinghu.sporttogether;

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
    private int zipcode;
    //private Map<String,Boolean> userMap = new ConcurrentHashMap<String,Boolean>();

    public Chatroom(){}

    public Chatroom(String sport, String time, int zipcode){
        this.sport = sport;
        this.intendedTime = time;
        this.zipcode = zipcode;
    }

    public String getSport() { return sport; }
    public String getIntendedTime() { return intendedTime; } //Why is this always return null!!!
    public int getZipcode() { return zipcode; }

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
