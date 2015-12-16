package com.example.sunpark.mylocationlab;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SQLiteDatabase db;
    String dbName = "MemoList.db"; // name of Database;
    String tableName = "MemoTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    Button mBtInsert;
    Button mBtList;
    ArrayList<String> SpinnerList;
    ArrayList<String> TypeList;
    ArrayList<String> BodyList;
    ArrayList<String> latList;
    ArrayList<String> lngList;
    ArrayAdapter<String> baseAdapter;
    TextView text_latitude;
    TextView text_longtitude;
    EditText editText;
    Intent Int_list;

    HashMap<Integer, String> DataHashMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        db = openOrCreateDatabase(dbName, dbMode, null);
        createTable();

        SpinnerList = new ArrayList<String>();
        SpinnerList.add("학업");
        SpinnerList.add("취미");
        SpinnerList.add("식사");
        SpinnerList.add("여행");
        SpinnerList.add("모임");
        SpinnerList.add("휴식");
        SpinnerList.add("사고");
        SpinnerList.add("경조사");

        ArrayAdapter<String> spinner = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, SpinnerList);
        final Spinner sp = (Spinner) this.findViewById(R.id.spinner);
        sp.setAdapter(spinner);
        sp.setOnItemSelectedListener(this);

        text_latitude = (TextView) findViewById(R.id.textlat);
        text_longtitude = (TextView) findViewById(R.id.textlng);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());

                text_latitude.setText("Latitute : " + lat);
                text_longtitude.setText("Longtitute : " + lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        mBtInsert = (Button) findViewById(R.id.bt_insert);

        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = sp.getSelectedItem().toString();
                String body = editText.getText().toString();
                double lat = Double.parseDouble(text_latitude.getText().toString());
                double lng = Double.parseDouble(text_longtitude.getText().toString());

                insertData(type, body, lat, lng);
            }
        });

        BodyList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, BodyList);

        mBtList = (Button) findViewById(R.id.bt_List);
        Int_list = new Intent(this, Edit_ListActivity.class);

        mBtList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAll();
                startActivity(Int_list);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //테이블 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, " +
                    "type text not null, " + "body text not null, " + "lat double default 0, " + "lng double default 0);";
            db.execSQL(sql);
        } catch (SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }

    //테이블 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    //데이터 저장
    public void insertData(String type, String body, double latitude, double longtitude) {
        String sql = "insert into " + tableName + " values(NULL, '" + type + "', '" + body + "', '" + latitude + "', '" + longtitude + "');";
        db.execSQL(sql);
    }

    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String type = results.getString(1);
            String body = results.getString(2);
            String lat = String.valueOf(results.getDouble(3));
            String lng = String.valueOf(results.getDouble(4));
            Toast.makeText(this, "index= " + id + " type=" + type, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "index= " + id + " type=" + type);

            for (int i = 1; i < results.getColumnCount(); i++) {
                if (i == 1 && i == 2) {
                    DataHashMap.put(id, results.getString(i));
                } else {
                    DataHashMap.put(id, String.valueOf(results.getDouble(i)));
                }
            }


            TypeList.add(type);
            BodyList.add(body);
            latList.add(lat);
            lngList.add(lng);
            results.moveToNext();
        }
        results.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Edit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.sunpark.mylocationlab/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Edit Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.sunpark.mylocationlab/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

