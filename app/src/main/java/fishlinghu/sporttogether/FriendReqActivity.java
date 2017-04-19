package fishlinghu.sporttogether;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FriendReqActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private String AccountEmailKey;
    private User UserData;

    private ArrayList<Integer> spinnerIDList = new ArrayList<Integer>();
    private ArrayList<String> userKeyList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_req);

        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".",",");

        Query query = reference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final LinearLayout ll = (LinearLayout) findViewById(R.id.llFriendReq);
                for(DataSnapshot snapshot : dataSnapshot.child( AccountEmailKey ).child("request").getChildren()){
                    userKeyList.add( snapshot.getKey() );
                    Log.d("friendReqActivity", snapshot.getKey());
                    UserData = dataSnapshot.child( snapshot.getKey() ).getValue(User.class);
                    String textViewStr = UserData.getName() + " wants to be your friend!";
                    // add textView (user name)
                    TextView tempTextView = new TextView( getApplicationContext() );
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tempTextView.setText( textViewStr);
                    tempTextView.setLayoutParams(lp2);
                    tempTextView.setTextColor(Color.BLACK);
                    tempTextView.setVisibility(View.VISIBLE);
                    ll.addView( tempTextView );
                    // create reply list
                    Spinner tempSpinner = new Spinner( getApplicationContext() );
                    int tempID = genID();
                    spinnerIDList.add(tempID);
                    tempSpinner.setId( tempID );
                    String[] replyList = new String[]{"Accept", "Ignore"};
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, replyList);
                    tempSpinner.setAdapter(adapter);
                    ll.addView( tempSpinner );
                }
                Button submitButton = new Button( getApplicationContext() );
                submitButton.setText("Reply request(s)");
                ll.addView( submitButton );
                submitButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // get reply
                        for(int i = 0; i < spinnerIDList.size(); ++i){
                            Spinner tempSpinner = (Spinner) findViewById( spinnerIDList.get(i) );
                            String reply = tempSpinner.getSelectedItem().toString();
                            // create chatrooms for both users if the friend request is accepted
                            if(reply.equals("Accept")){
                                // add chatroom for these two users
                                String theOtherUserKey = userKeyList.get(i);
                                Chatroom NewChatroom = new Chatroom(null, null , null, null, null, null); // should turn hour into integer!!
                                String roomKey = reference.child("chatrooms").push().getKey();
                                reference.child("chatrooms").child(roomKey).setValue(NewChatroom);
                                reference.child("chatrooms").child(roomKey).child("messages").push();
                                Toast.makeText(getApplicationContext(), roomKey, Toast.LENGTH_LONG).show();

                                // set the "roomKey" field in User and Room
                                reference.child("users").child(AccountEmailKey).child("friend").child(theOtherUserKey).setValue(roomKey);
                                reference.child("users").child(theOtherUserKey).child("friend").child(AccountEmailKey).setValue(roomKey);
                                reference.child("chatrooms").child(roomKey).child("users").child(AccountEmailKey).setValue("");
                                reference.child("chatrooms").child(roomKey).child("users").child(theOtherUserKey).setValue("");
                            }
                        }
                        // remove all the requests
                        reference.child("users").child(AccountEmailKey).child("request").removeValue();
                        // jump to the friend page
                        Intent myIntent = new Intent(FriendReqActivity.this, FriendActivity.class);
                        startActivity(myIntent);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private int genID(){
        AtomicInteger sNextGeneratedId = new AtomicInteger(1);
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF)
                newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}
