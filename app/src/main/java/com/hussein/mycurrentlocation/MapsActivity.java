package com.hussein.mycurrentlocation;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        CheckUserPermsions();
        LoadStudent();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    void CheckUserPermsions(){
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }

        LocationListner();// init the contact list

    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationListner();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText( this,"your message" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    void  LocationListner(){
        LocationListener locationListener = new MyLocationListener(this);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 3, locationListener);


        myThread m_thread= new myThread();
        m_thread.start();
    }

    Location oldLocation;
    class myThread extends Thread{
        myThread(){
            oldLocation= new Location("Start");
            oldLocation.setLatitude(0);
            oldLocation.setLongitude(0);
        }
        public void run(){
            while (true) try {
                //Thread.sleep(1000);
                if (oldLocation.distanceTo(MyLocationListener.location) == 0) {
                    continue;
                }
                oldLocation = MyLocationListener.location;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMap.clear();
                        for (int i = 0; i < ListStudents.size(); i++) {
                            Student student = ListStudents.get(i);

                            if (student.isCatch == false) {
                                LatLng studentlocation =
                                        new LatLng(student.location.getLatitude(), student.location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(studentlocation)
                                        .title(student.name)
                                        .snippet(student.des + ",Durée:" + student.duree)
                                        .icon(BitmapDescriptorFactory.fromResource(student.Image)
                                        ));

                                // catch the student
                                if (MyLocationListener.location.distanceTo(student.location) < 2) {
                                    Duree = Duree + student.duree;
                                    Toast.makeText(MapsActivity.this, "je t'ei trouvée " + Duree,
                                            Toast.LENGTH_SHORT).show();
                                    student.isCatch = true;
                                    ListStudents.set(i, student);

                                }
                            }

                        }

                    }
                });



            } catch (Exception ex) {
            }
        }
    }

//

    void getLocation() {

        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }

        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    //

    double Duree =0;
    //Add list f students
    ArrayList<Student> ListStudents= new ArrayList<>();
    void LoadStudent(){
        ListStudents.add(new Student(  R.drawable.student, "soufiane",  "Study in Elche: Android", 3.5,  31.510283,  -9.774174  ));

    }
}
