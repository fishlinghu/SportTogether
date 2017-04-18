package fishlinghu.sporttogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private String userEmailKey;
    private DatabaseReference reference;
    private User UserData;
    private TextView tempTextView;
    private ImageView tempImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        reference = FirebaseDatabase.getInstance().getReference();

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
    }
}
