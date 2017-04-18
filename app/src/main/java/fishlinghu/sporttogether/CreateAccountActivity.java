package fishlinghu.sporttogether;

import android.content.Intent;
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

public class CreateAccountActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;

    private EditText EditTextName;
    private EditText EditTextZipcode;
    private EditText EditTextSports;
    private EditText EditTextLevel;

    private Button ButtonDone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        EditTextName = (EditText) findViewById(R.id.editText_Name);
        EditTextZipcode = (EditText) findViewById(R.id.editText_Zipcode);
        EditTextSports = (EditText) findViewById(R.id.editText_Sport);
        EditTextLevel = (EditText) findViewById(R.id.editText_Level);

        ButtonDone = (Button) findViewById(R.id.button_done);

        AccountEmail = GoogleUser.getEmail();

        ButtonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                User NewUserData = new User("nobody", '0', "", '0', "");

                NewUserData.setName(EditTextName.getText().toString());
                NewUserData.setZipcode(Integer.parseInt( EditTextZipcode.getText().toString()));
                NewUserData.setSports(EditTextSports.getText().toString());
                NewUserData.setLevel(Integer.parseInt(EditTextLevel.getText().toString()));
                NewUserData.setPhotoUrll( GoogleUser.getPhotoUrl().toString() );

                reference.child("users").child( AccountEmail.replace(".",",") ).setValue(NewUserData);

                Toast.makeText(v.getContext(), "Account created", Toast.LENGTH_LONG).show();

                startActivity(new Intent(CreateAccountActivity.this, MainPageActivity.class));
                finish();

            }
        });

    }

}
