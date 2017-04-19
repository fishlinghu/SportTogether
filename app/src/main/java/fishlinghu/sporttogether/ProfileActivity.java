package fishlinghu.sporttogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private String userEmailKey;
    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String currentUserEmail;
    private String currentUserEmailKey;
    private User UserData;
    private TextView tempTextView;
    private ImageView tempImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = GoogleUser.getEmail();
        currentUserEmailKey = currentUserEmail.replace(".", ",");

        // get the room key
        Intent myIntent = getIntent();
        userEmailKey = myIntent.getStringExtra("userEmailKey");

        reference.child("users").child( userEmailKey ).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData = dataSnapshot.getValue(User.class);

                tempTextView = (TextView) findViewById(R.id.textViewProfileName);
                tempTextView.setText( UserData.getName() );

                tempTextView = (TextView) findViewById(R.id.textViewProfileSport);
                tempTextView.setText( UserData.getSports() );

                tempImageView = (ImageView) findViewById(R.id.imageViewOtherProfile);
                Glide.with(ProfileActivity.this).load(UserData.getPhotoUrl()).into( tempImageView );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button sendRequest = (Button) findViewById(R.id.buttonSendFriendReq);
        // set the event triggered by click
        sendRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // send friend req to target user
                reference.child("users").child(userEmailKey).child("request").child(currentUserEmailKey).setValue("");
            }
        });
    }
}
