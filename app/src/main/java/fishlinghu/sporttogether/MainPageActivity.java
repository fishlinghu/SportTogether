package fishlinghu.sporttogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button EditButton = (Button) findViewById(R.id.Edit_Profile_button);
        Button MatchButton = (Button) findViewById(R.id.Matching_button);
        Button FriendButton = (Button) findViewById(R.id.Friends_button);

        EditButton.setOnClickListener(this);
        MatchButton.setOnClickListener(this);
        FriendButton.setOnClickListener(this);

        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();


    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Edit_Profile_button:
                Toast.makeText(v.getContext(), "edit", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainPageActivity.this, EditAccountActivity.class));
                break;
            case R.id.Matching_button:
                Toast.makeText(getApplicationContext(), "match", Toast.LENGTH_LONG).show();
                break;
            case R.id.Friends_button:
                Toast.makeText(getApplicationContext(), "Friend", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainPageActivity.this, MainActivity.class));
                break;

        }
    }






}
