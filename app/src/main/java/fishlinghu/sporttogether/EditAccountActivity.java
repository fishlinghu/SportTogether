package fishlinghu.sporttogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditAccountActivity extends AppCompatActivity {


    private DatabaseReference reference;
    private FirebaseUser GoogleUser;


    private static final String TAG = EditAccountActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);


        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        User user = new User( GoogleUser.getEmail()  ,"Booooooooob", 30318, "Volleyball", 10);
        reference.child("users").setValue(user);


        Query query = reference.child("users").orderByChild("level");
        // single read data from FireBase
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    User user = dataSnapshot.getValue(User.class);
                    Log.d("FireBaseTraining", "name = " + user.getName() + " , Age = " + user.getEmail());

                    for (DataSnapshot found : dataSnapshot.getChildren()) {
                        Log.d(TAG, found.toString() );

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });












    }
}
