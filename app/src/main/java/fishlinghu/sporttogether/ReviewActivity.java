package fishlinghu.sporttogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);

        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".",",");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                roomKey = dataSnapshot.child("users").child(AccountEmailKey).getValue(User.class).getRoomKey();
                for(DataSnapshot snapshot : dataSnapshot.child("chatrooms").child(roomKey).child("users").getChildren()){
                    Log.d("reviewActivity", snapshot.getKey());
                    if(!AccountEmailKey.equals(snapshot.getKey())){
                        // add the name of other user to the list
                        userNameList.add( snapshot.getKey() );
                    }
                }
                LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutReview);
                int numUsers = userNameList.size();
                for(int i = 0; i < numUsers; i++) {
                    TextView tempTextView = new TextView( getApplicationContext() );
                    RatingBar tempRatingBar = new RatingBar( getApplicationContext() );

                    // show user name here
                    Log.d("reviewActivity", userNameList.get(i));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    tempTextView.setText( userNameList.get(i) );
                    //tempTextView.setText( "Fuck you" );
                    tempTextView.setVisibility(View.VISIBLE);
                    tempTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    ll.addView( tempTextView );
                    //ll.addView(tempTextView, lp2);
                    tempRatingBar.setRating(5);
                    //ll.addView(tempRatingBar, lp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
