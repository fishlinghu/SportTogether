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
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by fishlinghu on 3/29/17.
 */

public class CreateChatroomActivity extends AppCompatActivity {

    private DatabaseReference reference;
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


        // set the item in the sport selection list
        final Spinner spinnerSport = (Spinner)findViewById(R.id.spinnerSport);
        String[] sportList = new String[]{"Volleyball", "Basketball", "Badminton"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sportList);
        spinnerSport.setAdapter(adapter);

        // set the item in the time selection list
        final Spinner spinnerTime = (Spinner)findViewById(R.id.spinnerTime);
        String[] timeList = new String[]{"3:00 pm", "4:00 pm", "5:00 pm"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        spinnerTime.setAdapter(adapter2);

        // find exitText for zipcode
        EditTextZipcode = (EditText) findViewById(R.id.editText3);

        ButtonDone = (Button) findViewById(R.id.buttonDone);
        ButtonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String sport = spinnerSport.getSelectedItem().toString();
                String time = spinnerTime.getSelectedItem().toString();
                int zipcode = Integer.parseInt(EditTextZipcode.getText().toString());

                // Toast.makeText(v.getContext(), "Account created", Toast.LENGTH_LONG).show();

                //startActivity(new Intent(CreateChatroomActivity.this, MainPageActivity.class));
                //finish();

                // look for existing chatroom
                foundMatch(sport, time, zipcode);
                if(foundRoom){
                    Toast.makeText(getApplicationContext(), roomKey, Toast.LENGTH_LONG).show();
                    // jump to that existing chatroom
                }
                else{
                    Chatroom NewChatroom = new Chatroom( sport, time, zipcode ); // should turn hour into integer!!
                    roomKey = reference.child("chatrooms").push().getKey();
                    reference.child("chatrooms").child(roomKey).setValue(NewChatroom);
                    Toast.makeText(getApplicationContext(), roomKey, Toast.LENGTH_LONG).show();
                    // jump to that new chatroom
                }
                Intent myIntent = new Intent(CreateChatroomActivity.this, ChatActivity.class);
                myIntent.putExtra("roomKey", roomKey);
                //startActivity(myIntent); still debugging that page
            }
        });
    }

    // check if there are matching room for the player
    private void foundMatch(final String sport, final String time, final int zipcode){
        reference.child("chatrooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatroom temp = snapshot.getValue(Chatroom.class);
                    Log.d("foundMatch", sport+", "+temp.getSport());
                    Log.d("foundMatch", time+", "+temp.getIntendedTime()); // why is getTime always return null?
                    Log.d("foundMatch", zipcode+", "+temp.getZipcode());
                    if(sport.equals(temp.getSport()) && time.equals(temp.getIntendedTime()) && zipcode==temp.getZipcode()){
                        // a matched room found
                        foundRoom = true;
                        roomKey = snapshot.getKey();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
