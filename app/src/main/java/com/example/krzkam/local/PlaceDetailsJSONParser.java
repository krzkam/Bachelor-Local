package com.example.krzkam.local;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by krzkam on 11.01.2018.
 */

public class PlaceDetailsJSONParser {

    public HashMap<String,String> parse(JSONObject jObject){

        JSONObject jPlaceDetails = null;
        try {
            jPlaceDetails = jObject.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaceDetails(jPlaceDetails);
    }

    private HashMap<String, String> getPlaceDetails(JSONObject jPlaceDetails){

        HashMap<String, String> hPlaceDetails = new HashMap<String, String>();

        String name = "-NA-";
        String icon = "-NA-";
        String vicinity="-NA-";
        String latitude="";
        String longitude="";
        String formatted_address="-NA-";
        String formatted_phone="-NA-";
        String website="-NA-";
        String rating="-NA-";
        String international_phone_number="-NA-";
        String url="-NA-";
        JSONArray weekday_textA;
        StringBuilder weekday_text = null;

        try {
            // Extracting Place name, if available
            if(!jPlaceDetails.isNull("name")){
                name = jPlaceDetails.getString("name");
            }

            // Extracting Icon, if available
            if(!jPlaceDetails.isNull("icon")){
                icon = jPlaceDetails.getString("icon");
            }

            // Extracting Place Vicinity, if available
            if(!jPlaceDetails.isNull("vicinity")){
                vicinity = jPlaceDetails.getString("vicinity");
            }

            // Extracting Place formatted_address, if available
            if(!jPlaceDetails.isNull("formatted_address")){
                formatted_address = jPlaceDetails.getString("formatted_address");
            }

            // Extracting Place formatted_phone, if available
            if(!jPlaceDetails.isNull("formatted_phone_number")){
                formatted_phone = jPlaceDetails.getString("formatted_phone_number");
            }

            // Extracting website, if available
            if(!jPlaceDetails.isNull("website")){
                website = jPlaceDetails.getString("website");
            }

            // Extracting rating, if available
            if(!jPlaceDetails.isNull("rating")){
                rating = jPlaceDetails.getString("rating");
            }

            // Extracting rating, if available
            if(!jPlaceDetails.isNull("international_phone_number")){
                international_phone_number = jPlaceDetails.getString("international_phone_number");
            }

            // Extracting rating, if available
            if(!jPlaceDetails.isNull("international_phone_number")){
                weekday_textA = jPlaceDetails.getJSONObject("opening_hours").getJSONArray("weekday_text");
                weekday_text = new StringBuilder("Opening hours:");
                for (int i=0; i<7; i++) weekday_text.append("\n"+weekday_textA.get(i).toString());
            }

            // Extracting url, if available
            if(!jPlaceDetails.isNull("url")){
                url = jPlaceDetails.getString("url");
            }

            latitude = jPlaceDetails.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = jPlaceDetails.getJSONObject("geometry").getJSONObject("location").getString("lng");

            hPlaceDetails.put("name", name);
            hPlaceDetails.put("icon", icon);
            hPlaceDetails.put("vicinity", vicinity);
            hPlaceDetails.put("lat", latitude);
            hPlaceDetails.put("lng", longitude);
            hPlaceDetails.put("formatted_address", formatted_address);
            hPlaceDetails.put("formatted_phone", formatted_phone);
            hPlaceDetails.put("website", website);
            hPlaceDetails.put("rating", rating);
            hPlaceDetails.put("international_phone_number", international_phone_number);
            hPlaceDetails.put("url", url);
            hPlaceDetails.put("weekday_text", weekday_text.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hPlaceDetails;
    }
}
