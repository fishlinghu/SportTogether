package fishlinghu.sporttogether;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import static fishlinghu.sporttogether.R.id.mapView;

public class EventMap extends Activity implements OnMapReadyCallback {


    private GoogleMap googleMap;
    private MapView mapView;
    private static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static Boolean PERMISSIONS_REQUEST_STATUS = false;
    private double eventLongititude;
    private double eventLatitude;
    private LatLng point;
    private Marker marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_map);

        //double[] location = getIntent().getDoubleArrayExtra("point");
        ArrayList<Double> location = (ArrayList<Double>) getIntent().getSerializableExtra("point");

        eventLatitude = location.get(0);
        eventLongititude = location.get(1);
        point  =  new LatLng( eventLatitude, eventLongititude);

        mapView = (MapView)findViewById(R.id.mapView_event);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


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
        marker = googleMap.addMarker(new MarkerOptions().position(point).title("Meeting point!"));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            PERMISSIONS_REQUEST_STATUS = true;

            return;
        }
        map.setMyLocationEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        marker.showInfoWindow();

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








}
