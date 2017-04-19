package fishlinghu.sporttogether;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private String AccountEmailKey;

    private ArrayList<String> userNameList = new ArrayList<String>();
    private ArrayList<String> userPhotoUrlList = new ArrayList<String>();
    private ArrayList<String> roomKeyList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".",",");

        // configure the button
        Query query = reference.child("users").child( AccountEmailKey ).child("request");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int counter = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ++counter;
                }
                // set the text of the button
                Button goRequest = (Button) findViewById(R.id.buttonFriendREQ);
                String buttonText = String.valueOf(counter) + " pending friend requests";
                goRequest.setText( buttonText );
                // set the event triggered by click
                goRequest.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // jump to the request page
                        Intent myIntent = new Intent(FriendActivity.this, FriendReqActivity.class);
                        startActivity(myIntent);
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // list all friends you got
        query = reference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child( AccountEmailKey ).child("friend").getChildren()){
                    final String friendEmailKey = snapshot.getKey();
                    User UserData = dataSnapshot.child( friendEmailKey ).getValue(User.class);
                    userNameList.add( UserData.getName() );
                    userPhotoUrlList.add( UserData.getPhotoUrl() );
                    roomKeyList.add( snapshot.getValue(String.class) );
                }
                LinearLayout ll = (LinearLayout) findViewById(R.id.llFriend);
                int numUsers = userNameList.size();
                for(int i = 0; i < numUsers; i++) {
                    // add textView (user name)
                    TextView tempTextView = new TextView( getApplicationContext() );
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tempTextView.setText( userNameList.get(i) );
                    tempTextView.setLayoutParams(lp2);
                    tempTextView.setTextColor(Color.BLACK);
                    tempTextView.setVisibility(View.VISIBLE);
                    //tempTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ll.addView( tempTextView );

                    // add user photo
                    ImageButton tempImageButton = new ImageButton( getApplicationContext() );
                    LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(120, 120);
                    tempImageButton.setLayoutParams(lp3);
                    Glide.with(FriendActivity.this).load( userPhotoUrlList.get(i) ).into(tempImageButton);
                    ll.addView( tempImageButton );

                    final String tempRoomKey = roomKeyList.get(i);

                    tempImageButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            // jump to the chatroom
                            Intent myIntent = new Intent(FriendActivity.this, FriendChatActivity.class);
                            myIntent.putExtra("roomKey", tempRoomKey);
                            startActivity(myIntent);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
