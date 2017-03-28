package fishlinghu.sporttogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class EditAccountActivity extends AppCompatActivity {


    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private User UserData;


    private EditText EditTextName;
    private EditText EditTextZipcode;
    private EditText EditTextSports;
    private EditText EditTextLevel;

    private Button ButtonUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        EditTextName = (EditText) findViewById(R.id.editText_Name);
        EditTextZipcode = (EditText) findViewById(R.id.editText_Zipcode);
        EditTextSports = (EditText) findViewById(R.id.editText_Sport);
        EditTextLevel = (EditText) findViewById(R.id.editText_Level);

        ButtonUpdate = (Button) findViewById(R.id.updateButton);

        //User user = new User( "Booooooooob", 30318, "Volleyball", 10 );

        // FireBase doesn't accept dots as part of a key of a node, turn dot into comma
        AccountEmail = GoogleUser.getEmail();


        // single read data from FireBase
        Query query = reference.child("users").child( AccountEmail.replace(".",",") );

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserData = dataSnapshot.getValue(User.class);

                    EditTextName.setText(UserData.getName(), TextView.BufferType.EDITABLE);
                    EditTextZipcode.setText( Integer.toString(UserData.getZipcode()) , TextView.BufferType.EDITABLE);
                    EditTextSports.setText(UserData.getSports(), TextView.BufferType.EDITABLE);
                    EditTextLevel.setText(Integer.toString(UserData.getLevel()), TextView.BufferType.EDITABLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ButtonUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                UserData.setName(EditTextName.getText().toString());
                UserData.setZipcode(Integer.parseInt( EditTextZipcode.getText().toString()));
                UserData.setSports(EditTextSports.getText().toString());
                UserData.setLevel(Integer.parseInt(EditTextLevel.getText().toString()));

                reference.child("users").child( AccountEmail.replace(".",",") ).setValue(UserData);

                Toast.makeText(v.getContext(), "Profile Updated", Toast.LENGTH_LONG).show();
            }
        });


    }
}
