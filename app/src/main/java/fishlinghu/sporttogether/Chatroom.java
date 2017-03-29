package fishlinghu.sporttogether;

/**
 * Created by fishlinghu on 3/29/17.
 */

public class Chatroom {
    private String sport;
    private int intendedTime;
    private int zipcode;

    public Chatroom(String sport, int hour, int zipcode){
        this.sport = sport;
        this.intendedTime = hour;
        this.zipcode = zipcode;
    }

    public String getSport() { return sport; }
    public int getTime() { return intendedTime; }
    public int getZipcode() { return zipcode; }
}
