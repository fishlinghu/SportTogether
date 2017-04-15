package fishlinghu.sporttogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

import static android.view.View.generateViewId;

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
                final LinearLayout ll = (LinearLayout) findViewById(R.id.llSelectRoom);
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final String tempRoomKey = snapshot.getKey();

                    //Chatroom chatroomData;
                    reference.child("chatrooms").child(tempRoomKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Chatroom chatroomData = dataSnapshot.getValue(Chatroom.class);
                            if(chatroomData == null)
                                return;
                            Button tempButton = new Button(getApplicationContext());
                            int tempID = genID();
                            tempButton.setId( tempID );
                            tempButton.setText( chatroomData.getSport()
                                    + "," + chatroomData.getIntendedTime()
                                    + "," + chatroomData.getZipcode() );
                            ll.addView( tempButton );

                            // set onClick
                            tempButton.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    // jump to the chatroom
                                    Intent myIntent = new Intent(SelectRoomActivity.this, ChatActivity.class);
                                    myIntent.putExtra("roomKey", tempRoomKey);
                                    startActivity(myIntent);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
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
