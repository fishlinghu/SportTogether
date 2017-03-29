package fishlinghu.sporttogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by fishlinghu on 3/29/17.
 */

public class CreateChatroomActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private Button ButtonDone;
    private EditText EditTextZipcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);

        reference = FirebaseDatabase.getInstance().getReference();


        // set the item in the sport selection list
        final Spinner spinnerSport = (Spinner)findViewById(R.id.spinnerSport);
        String[] sportList = new String[]{"Volleyball", "Basketball", "Badminton"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sportList);
        spinnerSport.setAdapter(adapter);

        // set the item in the time selection list
        final Spinner spinnerTime = (Spinner)findViewById(R.id.spinnerTime);
        String[] timeList = new String[]{"3:00 pm", "4:00 pm", "5:00 pm"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, timeList);
        spinnerTime.setAdapter(adapter2);

        // find exitText for zipcode
        EditTextZipcode = (EditText) findViewById(R.id.editText3);

        ButtonDone = (Button) findViewById(R.id.buttonDone);
        ButtonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String sport = spinnerSport.getSelectedItem().toString();
                String hour = spinnerTime.getSelectedItem().toString();
                int zipcode = Integer.parseInt(EditTextZipcode.getText().toString());

                Chatroom NewChatroom = new Chatroom( sport, 3, zipcode ); // should turn hour into integer!!

                reference.child("chatrooms").push().setValue(NewChatroom);

                // Toast.makeText(v.getContext(), "Account created", Toast.LENGTH_LONG).show();

                startActivity(new Intent(CreateChatroomActivity.this, MainPageActivity.class));
                finish();
            }
        });
    }
}
