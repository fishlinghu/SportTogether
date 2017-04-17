package fishlinghu.sporttogether;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.atomic.AtomicInteger;

public class ChatroomList extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap googleMap;
    private MapView mapView;
    private static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static Boolean PERMISSIONS_REQUEST_STATUS = false;

    private DatabaseReference reference;
    private FirebaseUser GoogleUser;
    private String AccountEmail;
    private String AccountEmailKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomlist);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapView = (MapView)findViewById(R.id.fragment_embedded_map_view_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        reference = FirebaseDatabase.getInstance().getReference();
        GoogleUser = FirebaseAuth.getInstance().getCurrentUser();
        AccountEmail = GoogleUser.getEmail();
        AccountEmailKey = AccountEmail.replace(".",",");

        Query query = reference.child("users").child( AccountEmailKey ).child("roomKey");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final LinearLayout ll = (LinearLayout) findViewById(R.id.ChatroomList);
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
                                    + "," + chatroomData.getLocation() );
                            ll.addView( tempButton );

                            final LatLng tempLatLng  =  new LatLng( chatroomData.getLatitude(), chatroomData.getLongitude());
                            final Marker tempMarker = googleMap.addMarker(new MarkerOptions()
                                    .position(tempLatLng)
                                    .title("Click Marker to join this activity!"));
                            tempMarker.setTag(tempRoomKey);

                            // set onClick
                            tempButton.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {

                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tempLatLng,15));
                                    // Zoom in, animating the camera.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                                    tempMarker.showInfoWindow();

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





    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        String roomkey = (String) marker.getTag();
        Intent myIntent = new Intent(ChatroomList.this, ChatActivity.class);
        myIntent.putExtra("roomKey", roomkey);
        startActivity(myIntent);
        finish();
        return false;

    }


    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        googleMap = map;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            PERMISSIONS_REQUEST_STATUS = true;

            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMarkerClickListener(this);


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


