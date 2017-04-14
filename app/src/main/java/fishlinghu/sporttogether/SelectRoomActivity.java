package fishlinghu.sporttogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SelectRoomActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private String AccountEmailKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_room);

        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".",",");

        Query query = reference.child("users").child( AccountEmailKey ).child("roomKey");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String tempRoomKey = snapshot.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
