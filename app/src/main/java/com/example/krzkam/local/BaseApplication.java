package com.example.krzkam.local;

import android.app.Application;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krzkam.local.model.AddedPlace;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by krzkam on 30.01.2018.
 */

public class BaseApplication extends Application {

    public Realm realm;
    private GoogleMap mMap;


    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(config);
    }

    public void save_into_database(final String pName, final String inLat, final String inLng,
                                    final String type, final String address, final String phone,
                                    final String web    ) {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                String id = "";
                id = randomString(20);
                AddedPlace addedPlace = bgRealm.createObject(AddedPlace.class);
                addedPlace.setName(pName);
                addedPlace.setLat(inLat);
                addedPlace.setLng(inLng);
                addedPlace.setType(type);
                addedPlace.setAddress(address);
                addedPlace.setPhone(phone);
                addedPlace.setWeb(web);
                addedPlace.setId(id);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //Toast.makeText(InsertActivity.this,"stored OK", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                //Toast.makeText(InsertActivity.this,"ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refresh_views(GoogleMap maps, String type, double currentLat, double currentLng, int PROXIMITY_RADIUS) {
        RealmResults<AddedPlace> placeRealmResults = realm.where(AddedPlace.class).equalTo("type",type).findAll();
        mMap = maps;
        for(AddedPlace place: placeRealmResults){
            MarkerOptions markerOptions = new MarkerOptions();
            double lat = Double.parseDouble(place.getLat());
            double lng = Double.parseDouble(place.getLng());
            float dist = calcDist(currentLat, currentLng, lat, lng);
            if (dist < PROXIMITY_RADIUS) {
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.snippet(place.getId());
                String vicinity = "unknown";
                if(place.getAddress().length()>0) vicinity = place.getAddress();
                markerOptions.title(place.getName()+"\nAddress: "+vicinity);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.pin2);
                markerOptions.icon(icon);
                mMap.addMarker(markerOptions);
            }
        }
    }

    private static final String DATA = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static Random RANDOM = new Random();
    private static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            sb.append(DATA.charAt(RANDOM.nextInt(DATA.length())));
        }
        return sb.toString();
    }

    private void delete_from_database(String name) {
        final RealmResults<AddedPlace> place = realm.where(AddedPlace.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                place.deleteAllFromRealm();
            }
        });
    }

    public void deletePlace(String reference) {
        final RealmResults<AddedPlace> placeRealmResults = realm.where(AddedPlace.class).equalTo("id",reference).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                placeRealmResults.deleteAllFromRealm();
            }
        });

    }

    private float calcDist(double lat1, double lon1, double lat2, double lon2){
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lon1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lon2);

        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;
    }

    public AddedPlace parseDataFromBase(String reference){
        RealmResults<AddedPlace> placeRealmResults = realm.where(AddedPlace.class).equalTo("id",reference).findAll();
        AddedPlace localPlace = placeRealmResults.first();
        return localPlace;
    }
}
