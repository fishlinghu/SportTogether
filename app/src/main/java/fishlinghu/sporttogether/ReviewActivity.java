package fishlinghu.sporttogether;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Iterator;

/**
 * Created by fishlinghu on 4/10/17.
 */

public class ReviewActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private String AccountEmailKey;
    private User UserData;
    private Chatroom ChatroomData;

    private String roomKey = "";

    private ArrayList<String> userNameList = new ArrayList<String>();
    private ArrayList<String> userPhotoUrlList = new ArrayList<String>();
    private ArrayList<String> userEmailList = new ArrayList<String>();

    private Button mSubmitBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the room key
        Intent myIntent = getIntent();
        roomKey = myIntent.getStringExtra("roomKey");

        setContentView(R.layout.activity_review);

        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".",",");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.child("chatrooms").child(roomKey).child("users").getChildren()){
                    Log.d("reviewActivity", snapshot.getKey());
                    if(!AccountEmailKey.equals(snapshot.getKey())){
                        // add the name of other user to the list
                        UserData = snapshot.getValue(User.class);
                        userNameList.add( UserData.getName() );
                        userPhotoUrlList.add( UserData.getPhotoUrl() );
                        userEmailList.add( snapshot.getKey() );
                    }
                }
                LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutReview);
                int numUsers = userNameList.size();
                for(int i = 0; i < numUsers; i++) {
                    // show user name here
                    Log.d("reviewActivity", userNameList.get(i));

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
                    LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(70, 70);
                    tempImageButton.setLayoutParams(lp3);
                    Glide.with(ReviewActivity.this).load( userPhotoUrlList.get(i) ).into(tempImageButton);
                    ll.addView( tempImageButton );

                    final String tempEmailKey = userEmailList.get(i);

                    tempImageButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            // jump to the chatroom
                            Intent myIntent = new Intent(ReviewActivity.this, ProfileActivity.class);
                            myIntent.putExtra("userEmailKey", tempEmailKey);
                            startActivity(myIntent);
                        }
                    });

                    // add rating bar
                    RatingBar tempRatingBar = new RatingBar( getApplicationContext() );
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tempRatingBar.setId(10000+i);
                    tempRatingBar.setRating(5);
                    ll.addView(tempRatingBar, lp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSubmitBut = (Button) findViewById(R.id.button2);
        mSubmitBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // update the user rating
                double tempRating = -1;
                for(int i = 0; i < userNameList.size(); i++){
                    RatingBar tempRatingBar = (RatingBar) findViewById(10000+i);
                    tempRating = tempRatingBar.getRating();
                    reference.child("users").child( userNameList.get(i) ).child("ratings").child( AccountEmailKey ).setValue( tempRating );
                }
                // jump to main page
                Intent myIntent = new Intent(ReviewActivity.this, MainPageActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
