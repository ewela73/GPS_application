package com.example.domi.gps_application;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements LocationListener {

    public ToggleButton tgButtOnfoot;
    public ToggleButton tgButtCar;
    public Button button_start, button_stop, button_showDatabase, button_route;
    public TextView tv_result;


    Timer timer = new Timer();

    DBHelper myDb;
    boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReference();
        clickOnFoot(getWindow().getDecorView());
        clickCar(getWindow().getDecorView());
        clickShowDB();
        setEnabledComponentFalse();
        myDb = new DBHelper(this);
        //myDb.insertData(1,1,1,1,1);
        //myDb.deleteAll();
        Cursor c = myDb.select("select * from " + DBHelper.TABLE_NAME);
        if (c.getCount() == 0) {
            actualy_idRoute = 0;
            lastIdPoint = 0;
            isFirst = true;
        } else isFirst = false;
    }

    /**
     * setRefernce
     * ustawnienie referncji dla komponentow
     */
    public void setReference() {
        tgButtOnfoot = (ToggleButton) findViewById(R.id.tgButtonOnFoot);
        tgButtCar = (ToggleButton) findViewById(R.id.tgButtonCar);
        button_start = (Button) findViewById(R.id.button_start);
        button_stop = (Button) findViewById(R.id.button_stop);
        button_showDatabase = (Button) findViewById(R.id.button_showDB);
        tv_result = (TextView) findViewById(R.id.textView_result);
        tv_result.setMovementMethod(new ScrollingMovementMethod());
        button_route = (Button) findViewById(R.id.button_route);
    }//koniec setReference


    public void clickShowDB() {

        button_showDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor curosr = myDb.getAllData();
                StringBuffer buffer = new StringBuffer();
                if (curosr.getCount() == 0) {
                    showMessage("Database error.", "Nothing found.");
                }

                while (curosr.moveToNext()) {
                    buffer.append("ID_POINT: " + curosr.getString(0) + "\n");
                    buffer.append("LATITUDE : " + curosr.getString(1) + "\n");
                    buffer.append("LONGITUDE : " + curosr.getString(2) + "\n");
                    buffer.append("IS_SYNCH: " + curosr.getString(3) + "\n");
                    buffer.append("ID_ROUTE: " + curosr.getString(4) + "\n\n");
                }
                showMessage("Data", buffer.toString());
            }

        });


    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    long actualy_idRoute = 0, lastIdPoint = 0;

    /**
     * setEnabledComponentFalse
     * ustawienie wiidocznosci komponentow na false
     * czyli wylaczone
     */
    public void setEnabledComponentFalse() {
        button_start.setEnabled(false);
        button_stop.setEnabled(false);
        button_route.setEnabled(false);
    }//koniec setEnabledComponentFalse

    int checkChooseOnFoot = 0;
    int checkChooseCar = 0;
    long number_meters = 0, number_minutes = 0;

    /**
     * clickOnFoot
     * obsluga tgButtonOnFoot
     * @param view
     */
    public void clickOnFoot(View view) {
        tgButtOnfoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (tgButtOnfoot.isChecked()) {
                    if (check_GPS_is_ON() == true) {
                        tgButtCar.setEnabled(false);
                        button_start.setEnabled(true);
                        number_meters = 0;
                        number_minutes = 1;

                        checkChooseOnFoot = 1;
                    }


                } else {
                    tgButtCar.setEnabled(true);
                    setEnabledComponentFalse();

                    checkChooseOnFoot = 0;
                }
            }
        });
    }//koniec clickOnFoot

    /**
     * clickCar
     * obsluga tgButtonCar
     * @param view
     */
    public void clickCar(View view) {
        tgButtCar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (tgButtCar.isChecked()) {
                    if (check_GPS_is_ON() == true) {
                        tgButtOnfoot.setEnabled(false);
                        button_start.setEnabled(true);

                        number_minutes = 0;
                        number_meters = 10;

                        checkChooseCar = 1;
                    }
                } else {
                    tgButtOnfoot.setEnabled(true);
                    setEnabledComponentFalse();

                    checkChooseCar = 0;
                }
            }
        });
    }//koniec clickCar

    public void checkWhichIsSelected(int car, int onFoot) {

        switch (car) {
            case 1: {
                tgButtCar.setEnabled(true);
            }
            break;
            case 0: {
                tgButtCar.setEnabled(false);
            }
            break;
        }

        switch (onFoot) {
            case 1: {
                tgButtOnfoot.setEnabled(true);
            }
            break;
            case 0: {
                tgButtOnfoot.setEnabled(false);
            }
            break;
        }
    }

    /**
     * clickExit
     * obluga button EXIT
     * @param view
     */
    public void clickExit(View view) {
        System.exit(0);
    }//koniec clickExit

    /**
     * clickStart
     * obluga Button start
     * @param view
     */
    public void clickStart(View view) {
        button_stop.setEnabled(true);
        button_start.setEnabled(false);

        tgButtOnfoot.setEnabled(false);
        tgButtCar.setEnabled(false);
        GPS_Point.array_GPS_Point.removeAll(GPS_Point.array_GPS_Point);
        getGPSdata(number_meters, number_minutes);

        if (isFirst == false) {
            String select_lastIdPoint = ("select " + DBHelper.COL_ID_POINT + " from " + DBHelper.TABLE_NAME + " order by " + DBHelper.COL_ID_POINT + " desc");
            String select_lastIdRoute = ("select " + DBHelper.COL_ID_ROUTE + " from " + DBHelper.TABLE_NAME + " order by " + DBHelper.COL_ID_ROUTE + " desc");
            Cursor kursor1 = myDb.select(select_lastIdPoint);
            Cursor kursor2 = myDb.select(select_lastIdRoute);
            kursor1.moveToNext();
            kursor2.moveToNext();
            lastIdPoint = Long.valueOf(kursor1.getString(0));
            actualy_idRoute = Long.valueOf(kursor2.getString(0));
            actualy_idRoute++;
        } else {
            actualy_idRoute++;
        }
        button_route.setEnabled(false);

    }//koniec clickStart

    /**
     * clickStop
     * obsluga Button stop
     * @param view
     */
    public void clickStop(View view) {

        button_start.setEnabled(true);
        button_stop.setEnabled(false);

        tv_result.setText("");

        checkWhichIsSelected(checkChooseCar, checkChooseOnFoot);

        GPS_Point new_point = new GPS_Point();
        new_point.printAllData();

        GPS_Point new_point1 = new GPS_Point(lastIdPoint++, latitude, longitude, 0, actualy_idRoute);
        GPS_Point.array_GPS_Point.add(new_point1);
        for (GPS_Point g : GPS_Point.array_GPS_Point) {
            myDb.insertData(g.getId_point(), g.getLatitude(), g.getLongitude(), g.isSynch(), g.getId_route());
        }

        stopUsingGPS();
        button_route.setEnabled(true);
//        if(GPS_Point.array_GPS_Point.size() > 1)
//        {
//            button_route.setEnabled(true);
//        }


    }//koniec clickStop

    ////////////// GPS ///////////////////////


    /**
     * check_GPS_is_ON
     * sprawdzenie czy GPS jest włączony
     */

    public boolean check_GPS_is_ON() {
        boolean isOn = false;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS is enabled in your device.", Toast.LENGTH_SHORT).show();
            isOn = true;
        } else {
            // Toast.makeText(this, "GPS isn't enabled in your device." , Toast.LENGTH_SHORT).show();
            alertDialogGPSisnotOn("GPS error.", "GPS is disabled in your device. Would you like to enable it?");

        }
        return isOn;
    }//koniec check_GPS_is_ON

    /**
     * alertDialogGPSisnotOn
     * metoda odpowiedzielna za wyswiretlanie bledu kiedy GPS jest wylaczony
     */
    public void alertDialogGPSisnotOn(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Go to Setting GPS.",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent GPSsetting = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(GPSsetting);
                    }
                });
        alert.setNegativeButton("Cancle",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();

    }//koniec alertDialog

    /**
     * alertDialogOK
     * wyswietlanie bledow , jeden klawisz OK
     * @param title
     * @param message
     */
    public void alertDialogOK(String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }//koniec alertDialogOK

    LocationManager locationM;
    Criteria kr;
    Location loc;
    String najlepszydostawca;
    String jaki_dostawca;
    Location lastLocation;
    double latitude;
    double longitude;
    Calendar calendar;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public void stopUsingGPS()
    {
        if (locationM != null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                }
            }

            locationM.removeUpdates(this);
        }
    }
    public void getGPSdata(long change_distance_meters, long change_time_minutes)//metry , minuty
    {

        final long MIN_DISTANCE_CHANGE_FOR_UPDATES = change_distance_meters;//podana wartosc w metrach
        final long MIN_TIME_UPDATES = 1000 * 60 * change_time_minutes;//podana wartosc w minutach

        calendar = Calendar.getInstance();
        locationM = (LocationManager) getSystemService(LOCATION_SERVICE);


        boolean isGPSEnabled = false;
        boolean isNetworkEnabled = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]
                        {
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10);
            }
        }
        else
        {
            isGPSEnabled = locationM.isProviderEnabled(LocationManager.GPS_PROVIDER);//sprawdzanie czy dostawca GPS jest osiagalny
            isNetworkEnabled = locationM.isProviderEnabled(LocationManager.NETWORK_PROVIDER);// sprawdzanie czy dostawca Network jest osiągalny
            if(!isNetworkEnabled && !isGPSEnabled)
            {
                alertDialogOK("Error GPS" , "Unavaliable GPS signal.");
            }
            if(isNetworkEnabled)
            {   jaki_dostawca="LocationManager.NETWORK_PROVIDER";
                locationM.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if(locationM != null)
                {
                    lastLocation = locationM.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if(lastLocation != null)
                    {
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                    }
                }
            }
            if(isGPSEnabled)
            {
                jaki_dostawca="LocationManager.GPS_PROVIDER";
                locationM.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if(locationM != null)
                {
                    lastLocation = locationM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(lastLocation != null)
                    {
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                    }
                }
            }
            if(isNetworkEnabled && isGPSEnabled)
            {
                kr = new Criteria();
                najlepszydostawca = locationM.getBestProvider(kr, true);
                jaki_dostawca=najlepszydostawca;
                locationM.requestLocationUpdates(najlepszydostawca, MIN_TIME_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if(locationM != null)
                {
                    lastLocation = locationM.getLastKnownLocation(najlepszydostawca);
                    if(lastLocation != null)
                    {
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                    }
                }
            }

        }


    }//koniec getGPSTime


    @Override
    public void onLocationChanged(Location location)
    {

        if(location != null)
        {
            latitude=location.getLatitude();
            longitude=location.getLongitude();

            calendar = Calendar.getInstance();
            tv_result.append("Latitude: " + latitude + "   " +
                    "Longitude: " + longitude + "   Time: " + dateFormat.format(calendar.getTime()) + "\n");
            GPS_Point new_point = new GPS_Point(lastIdPoint++,latitude,longitude,0,actualy_idRoute);
            GPS_Point.array_GPS_Point.add(new_point);

//            if(GPS_Point.array_GPS_Point.size()==20)
//            {
//                for (GPS_Point g : GPS_Point.array_GPS_Point)
//                {
//                    myDb.insertData(g.getId_point(), g.getLatitude(), g.getLongitude(), g.isSynch(), g.getId_route());
//                }
//                GPS_Point.array_GPS_Point.removeAll(GPS_Point.array_GPS_Point);
//            }

//            MapsActivity.point = new LatLng(latitude, longitude);
//            MapsActivity.option.add(MapsActivity.point);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {


    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void showRoute(View view)
    {
        startActivity(new Intent(MainActivity.this , MapsActivity.class));
    }
}
