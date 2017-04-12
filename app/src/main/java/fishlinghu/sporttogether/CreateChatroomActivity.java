package fishlinghu.sporttogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by fishlinghu on 3/29/17.
 */

public class CreateChatroomActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private String AccountEmailKey;
    private User UserData;

    private DataSnapshot tempSnapshot;
    private Button ButtonDone;
    private EditText EditTextZipcode;
    private boolean foundRoom = false;
    private String roomKey = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_chatroom);



        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".",",");
        Button MapButton = (Button) findViewById(R.id.buttonMap);
        MapButton.setOnClickListener(this);

        // single read data from FireBase

        Query query = reference.child("users").child( AccountEmailKey );

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserData = dataSnapshot.getValue(User.class);
                    String userRoomKey = UserData.getRoomKey();
                    //UserData.addNewRating(8.0);
                    if(!userRoomKey.equals("")){
                        // user is already in a room, bring him there
                        Intent myIntent = new Intent(CreateChatroomActivity.this, ChatActivity.class);
                        myIntent.putExtra("roomKey", userRoomKey);
                        startActivity(myIntent);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // set the item in the sport selection list
        final Spinner spinnerSport = (Spinner)findViewById(R.id.spinnerSport);
        String[] sportList = new String[]{"Volleyball", "Basketball", "Badminton"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sportList);
        spinnerSport.setAdapter(adapter);

        // set the item in the time selection list
        final Spinner spinnerTime = (Spinner)findViewById(R.id.spinnerTime);
        String[] timeList = new String[]{"3:00 pm", "4:00 pm", "5:00 pm"}; // need to add more time here
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        spinnerTime.setAdapter(adapter2);

        // find exitText for zipcode
        EditTextZipcode = (EditText) findViewById(R.id.editText3);

        ButtonDone = (Button) findViewById(R.id.buttonDone);
        ButtonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String sport = spinnerSport.getSelectedItem().toString();
                final String time = spinnerTime.getSelectedItem().toString();
                final int zipcode = Integer.parseInt(EditTextZipcode.getText().toString());

                // Toast.makeText(v.getContext(), "Account created", Toast.LENGTH_LONG).show();

                //startActivity(new Intent(CreateChatroomActivity.this, MainPageActivity.class));
                //finish();

                // look for existing chatroom
                reference.child("chatrooms").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean flag = false;
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Chatroom temp = snapshot.getValue(Chatroom.class);
                            Log.d("foundMatch", sport+", "+temp.getSport());
                            Log.d("foundMatch", time+", "+temp.getIntendedTime()); // why is getTime always return null?
                            Log.d("foundMatch", zipcode+", "+temp.getZipcode());
                            if(sport.equals(temp.getSport()) && time.equals(temp.getIntendedTime()) && zipcode==temp.getZipcode()){
                                // a matched room found
                                Log.d("foundMatch", "Match found!!!!!");
                                foundRoom = true;
                                roomKey = snapshot.getKey();
                                reference.child("users").child( AccountEmailKey ).child( "roomKey" ).setValue(roomKey);
                                reference.child("chatrooms").child(roomKey).child("users").child( AccountEmailKey ).setValue("");
                                // bring the user to the room
                                Toast.makeText(getApplicationContext(), roomKey, Toast.LENGTH_LONG).show();
                                Intent myIntent = new Intent(CreateChatroomActivity.this, ChatActivity.class);
                                myIntent.putExtra("roomKey", roomKey);
                                startActivity(myIntent);
                                flag = true;
                                break;
                                //return;
                            }
                        }
                        if(flag == false){
                            // no matched room found, create new room
                            Chatroom NewChatroom = new Chatroom( sport, time, zipcode ); // should turn hour into integer!!
                            roomKey = reference.child("chatrooms").push().getKey();
                            reference.child("chatrooms").child(roomKey).setValue(NewChatroom);
                            reference.child("chatrooms").child(roomKey).child("messages").push();
                            Toast.makeText(getApplicationContext(), roomKey, Toast.LENGTH_LONG).show();

                            // set the "roomKey" field in User
                            reference.child("users").child( AccountEmailKey ).child( "roomKey" ).setValue(roomKey);
                            reference.child("chatrooms").child(roomKey).child("users").child( AccountEmailKey ).setValue("");
                            // bring the user to the room
                            Intent myIntent = new Intent(CreateChatroomActivity.this, ChatActivity.class);
                            myIntent.putExtra("roomKey", roomKey);
                            startActivity(myIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMap:
                //Toast.makeText(v.getContext(), "edit", Toast.LENGTH_LONG).show();
                startActivity(new Intent(CreateChatroomActivity.this, MapsActivity.class));
                break;

        }
    }




}
