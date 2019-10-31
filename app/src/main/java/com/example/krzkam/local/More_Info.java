package com.example.krzkam.local;

import java.util.HashMap;
import org.json.JSONObject;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.krzkam.local.model.AddedPlace;
import io.realm.Realm;

public class More_Info extends Activity {

    private BaseApplication base = new BaseApplication();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__info);
        base.realm = Realm.getDefaultInstance();
        String reference = getIntent().getStringExtra("reference");
        if (reference.length() > 20){
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
            sb.append("reference=" + reference);
            sb.append("&sensor=true");
            sb.append("&key=AIzaSyAuQEQltaikY4W0MWQwjD2-yB0VPMh-Eds");
            PlacesTask placesTask = new PlacesTask();
            placesTask.execute(sb.toString());
        }
        else {
            localPlace(reference);
        }
    }

    private void localPlace(String reference) {
        AddedPlace localPlace = null;
        localPlace = base.parseDataFromBase(reference);
        String name = localPlace.getName();
        String vicinity = localPlace.getAddress();
        String website = localPlace.getWeb();
        String international_phone_number = localPlace.getPhone();
        displayDetails(name, vicinity, website, international_phone_number);
    }

    private class PlacesTask extends AsyncTask<String, Integer, String>{

        String data = null;
        @Override
        protected String doInBackground(String... url) {
            try{
                DownloadUrl rUrl = new DownloadUrl();
                data = rUrl.readUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, HashMap<String,String>>{

        JSONObject jObject;
        @Override
        protected HashMap<String,String> doInBackground(String... jsonData) {

            HashMap<String, String> hPlaceDetails = null;
            PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
            try{
                jObject = new JSONObject(jsonData[0]);
                hPlaceDetails = placeDetailsJsonParser.parse(jObject);
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return hPlaceDetails;
        }


        @Override
        protected void onPostExecute(HashMap<String,String> hPlaceDetails){

            String name = hPlaceDetails.get("name");
            String vicinity = hPlaceDetails.get("vicinity");
            String website = hPlaceDetails.get("website");
            String rating = hPlaceDetails.get("rating");
            String international_phone_number = hPlaceDetails.get("international_phone_number");
            String weekday_text = hPlaceDetails.get("weekday_text");
            displayDetails(name, vicinity, website, international_phone_number);
            TextView tv_rating = (TextView) findViewById(R.id.rating);
            if (rating==null){
                rating = "Rating unknown";
                tv_rating.setText(rating);
            }
            else tv_rating.setText("Rating: "+rating);

            TextView tv_open_hours = (TextView) findViewById(R.id.opening_hours);
            if (weekday_text==null) weekday_text = "Opening hours unknown";
            tv_open_hours.setText(weekday_text);
        }
    }

    private void displayDetails(String name, String vicinity, String website, String phone){
        TextView tv_place_name = (TextView) findViewById(R.id.place_name);
        if (name==null || name.length() == 0)
        {
            name = "Name unknown";
            tv_place_name.setText(name);
        }
        else tv_place_name.setText(name);

        TextView tv_address = (TextView) findViewById(R.id.address);
        if (vicinity == null || vicinity.length()==0)
        {
            vicinity = "Address unknown";
            tv_address.setText(vicinity);

        } else tv_address.setText("Address: "+vicinity);

        TextView tv_url = (TextView) findViewById(R.id.url);
        if (website==null || website.length() == 0) {
            website = "URL unknown";
            tv_url.setText(website);
        }
        else tv_url.setText("Website: "+website);

        TextView tv_phone = (TextView) findViewById(R.id.phone);
        if (phone==null || phone.length() == 0){
            phone = "Phone unknown";
            tv_phone.setText(phone);
        }
        else tv_phone.setText("Phone: "+phone);
    }
}