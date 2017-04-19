package fishlinghu.sporttogether;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.builders.Actions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by fishlinghu on 3/29/17.
 */

public class CreateChatroomActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback{

    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private String AccountEmailKey;
    private User UserData;
    private static Boolean PERMISSIONS_REQUEST_STATUS = false;
    private static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    private DataSnapshot tempSnapshot;
    private Button ButtonDone;
    private EditText EditTextLocation;

    private String roomKey = "";


    private GoogleMap googleMap;
    private View view;
    private MapView mapView;
    private LatLng MeetingPoint = new LatLng(0,0);
    private int mYear, mMonth, mDay;

    private Button dateButton;
    private TextView dateText;
    private Button timeButton;
    private TextView timeText;

    private String eventTime = "00:00";
    private String eventDate = "04-12-2017";
    private String eventSport = "Volleyball";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_chatroom);


        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".", ",");
        Button MapButton = (Button) findViewById(R.id.buttonMap);
        MapButton.setOnClickListener(this);


        // Map initialize
        mapView = (MapView) findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        dateText = (TextView)findViewById(R.id.dateText);
        dateButton = (Button)findViewById(R.id.dateButton);
        timeText = (TextView)findViewById(R.id.timeText);
        timeButton = (Button)findViewById(R.id.timebutton);

        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(CreateChatroomActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String format = setDateFormat(year,month,day);
                        eventDate = format;
                        dateText.setText(format);
                    }

                }, mYear,mMonth, mDay).show();
            }

        });

        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                // Create a new instance of TimePickerDialog and return it
                new TimePickerDialog(CreateChatroomActivity.this, new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeText.setText(hourOfDay + ":" + minute);
                        eventTime = hourOfDay + ":" + minute;
                    }
                }, hour, minute, false).show();
            }

        });

        // set the item in the sport selection list
        final Spinner spinnerSport = (Spinner) findViewById(R.id.spinnerSport);
        String[] sportList = new String[]{"Volleyball", "Basketball", "Badminton", "Tennis", "Running", "Swimming"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sportList);
        spinnerSport.setAdapter(adapter);


        // find exitText for zipcode
        EditTextLocation = (EditText) findViewById(R.id.editText_address);

        ButtonDone = (Button) findViewById(R.id.buttonDone);
        ButtonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                eventSport = spinnerSport.getSelectedItem().toString();

                final String sport = eventSport;
                final String time = eventTime;
                final String Date = eventDate;
                final String location = EditTextLocation.getText().toString();

                Calendar calendar = Calendar.getInstance();



                String tempStr = EditTextLocation.getText().toString();
                if (tempStr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter meeting point", Toast.LENGTH_LONG).show();
                    return;
                }

                if (MeetingPoint.latitude == 0 && MeetingPoint.longitude == 0){
                    Toast.makeText(getApplicationContext(), "Please Select a meeting point on the Map", Toast.LENGTH_LONG).show();
                    return;

                }



                // look for existing chatroom
                reference.child("chatrooms").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean flag = false;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Chatroom temp = snapshot.getValue(Chatroom.class);
                            Log.d("foundMatch", sport + ", " + temp.getSport());
                            Log.d("foundMatch", time + ", " + temp.getIntendedTime()); // why is getTime always return null?
                            Log.d("foundMatch", location + ", " + temp.getLocation());

                            if (sport.equals(temp.getSport()) && Date.equals(temp.getIntendedDate()) && time.equals(temp.getIntendedTime()) && location.equals(temp.getLocation())) {
                                // a matched room found
                                Log.d("foundMatch", "Match found!!!!!");
                                Toast.makeText(getApplicationContext(), "Room already exist, Try to join other's room", Toast.LENGTH_LONG).show();
                                flag = true;
                                break;

                            }
                        }
                        if (flag == false) {
                            // no matched room found, create new room
                            Chatroom NewChatroom = new Chatroom(sport, Date ,time, location, MeetingPoint.latitude, MeetingPoint.longitude); // should turn hour into integer!!
                            roomKey = reference.child("chatrooms").push().getKey();
                            reference.child("chatrooms").child(roomKey).setValue(NewChatroom);
                            reference.child("chatrooms").child(roomKey).child("messages").push();
                            Toast.makeText(getApplicationContext(), roomKey, Toast.LENGTH_LONG).show();

                            // set the "roomKey" field in User
                            //reference.child("users").child( AccountEmailKey ).child( "roomKey" ).setValue(roomKey);
                            reference.child("users").child(AccountEmailKey).child("roomKey").child(roomKey).setValue("");
                            reference.child("chatrooms").child(roomKey).child("users").child(AccountEmailKey).setValue("");
                            // bring the user to the room
                            Intent myIntent = new Intent(CreateChatroomActivity.this, ChatActivity.class);
                            myIntent.putExtra("roomKey", roomKey);
                            startActivity(myIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });





    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return  String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth) + "-"
                + String.valueOf(year) ;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMap:
                ArrayList<String> Eventdata = new ArrayList<String>();
                Eventdata.add(eventDate);
                Eventdata.add(eventSport);
                Intent myIntent = new Intent(CreateChatroomActivity.this, ChatroomList.class);
                myIntent.putExtra("Eventdata", Eventdata);
                startActivity(myIntent);

                break;

        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            PERMISSIONS_REQUEST_STATUS = true;

            return;
        }
        map.setMyLocationEnabled(true);


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                MeetingPoint = point;
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(point));
            }
        });


    }

    @Override
    public final void onDestroy()
    {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public final void onLowMemory()
    {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public final void onPause()
    {
        mapView.onPause();
        super.onPause();
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        return Actions.newView("CreateChatroom", "http://[ENTER-YOUR-URL-HERE]");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().start(getIndexApiAction());
    }

    @Override
    public void onStop() {

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        FirebaseUserActions.getInstance().end(getIndexApiAction());
        super.onStop();
    }
}
