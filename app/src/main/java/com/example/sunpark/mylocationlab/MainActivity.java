package com.example.sunpark.mylocationlab;


import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

    public class MainActivity extends Activity {
        private GoogleMap map;
        static final LatLng SEOUL = new LatLng(37.56, 126.97);

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);

            map = mapFragment.getMap();
            //현재 위치로 가는 버튼 표시
            map.setMyLocationEnabled(true);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));//초기 위치...수정필요

            MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

                @Override
                public void gotLocation(Location location) {
                    String msg = "lon: " + location.getLongitude() + " -- lat: " + location.getLatitude();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    drawMarker(location);
                }
            };

            MyLocation myLocation = new MyLocation();
            myLocation.getLocation(getApplicationContext(), locationResult);
        }

            private void drawMarker(Location location) {
            map.clear();
            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

            //currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
            //마커 추가
            map.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .snippet("Lat:" + location.getLatitude() + "Lng:" + location.getLongitude())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("현재위치"));
        }


    }

