package com.example.abo8a.jobconnector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.tomer.fadingtextview.FadingTextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class SearchFragment extends Fragment {
    private LinearLayout search;
    private FusedLocationProviderClient mFusedLocationClient;
    static List<Address> addresses;
    EditText searchLocation;
    String textToShow[] = {"Search Jobs", "Search Companies", "Search Locations"};
    private TextSwitcher my_text_switcher;
    public static String mylocation="";
    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onStart() {
        super.onStart();

        init();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(){
        searchLocation=getActivity().findViewById(R.id.etSearchIn);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k  = new Intent(getActivity(), EmployeeSearchActivity.class);//ActivitySearch
                k.putExtra("location",mylocation);
                startActivity(k);
            }
        });
        search=getActivity().findViewById(R.id.GoToSearchLinear);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k  = new Intent(getActivity(), EmployeeSearchActivity.class);//ActivitySearch
                startActivity(k);
            }
        });
        // fillLocation();
        checkPermission();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initTextAnnimation();
        return inflater.inflate(R.layout.search_fragment, container, false);


    }
    private void initTextAnnimation(){
        //   String[] texts = {"text1","text2","text3"};
        FadingTextView FTV = (FadingTextView) getActivity().findViewById(R.id.fadingTextView);
        //  FTV.setTexts(texts);
        //FTV.setTimeout(10, SECONDS);
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fillLocation() {


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity(). getApplicationContext());

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.

                        if (location != null) {
                            // Logic to handle location object
                            Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());

                            try {
                                addresses = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                System.out.println("++++++ " + addresses.get(0).getCountryName() + "," + addresses.get(0).getSubAdminArea());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (addresses != null && addresses.size() >= 0) {
                                searchLocation.setText("Search jobs in "+addresses.get(0).getSubAdminArea());
                                mylocation=addresses.get(0).getSubAdminArea();
                            }
                        }
                    }
                });


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        System.out.println("++++++++++++++ in request ================");

        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    searchLocation.setVisibility(View.VISIBLE);

                    fillLocation();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    searchLocation.setVisibility(View.GONE);

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission() {

        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        System.out.println("++++++++++++++++++++ gps "+statusOfGPS);
        if(statusOfGPS) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                searchLocation.setVisibility(View.GONE);


                //       No explanation needed; request the android.Manifest.permission
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        0);



            } else {
                fillLocation();
//            // Permission has already been granted
            }
        }
        else
        {
            searchLocation.setVisibility(View.GONE);

        }
    }




}
