package fishlinghu.sporttogether;

/**
 * Created by fishlinghu on 3/29/17.
 */

public class Chatroom {
    private String sport;
    private String intendedTime;
    private int zipcode;

    public Chatroom(){}

    public Chatroom(String sport, String time, int zipcode){
        this.sport = sport;
        this.intendedTime = time;
        this.zipcode = zipcode;
    }

    public String getSport() { return sport; }
    public String getIntendedTime() { return intendedTime; } //Why is this always return null!!!
    public int getZipcode() { return zipcode; }
}
