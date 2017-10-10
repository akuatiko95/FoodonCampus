package com.example.foodoncampus;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * Created by GonzaloConcepciónMeg on 03/10/2017.
 */

public class FoodParser {

    private String jsonStr;

    //conexión con la API
    private String callAPI() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String foodJsonStr = null;

        try {
            URL url = new URL("http://services.web.ua.pt/sas/ementas?date=week&format=json");

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                foodJsonStr = null;
            }
            assert inputStream != null;
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                foodJsonStr = null;
            }
            foodJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            foodJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return foodJsonStr;
    }

    public String[][] getData()
            throws JSONException {

        //cache
        if (jsonStr==null)
            jsonStr = callAPI();



        JSONObject json = new JSONObject(jsonStr);

        JSONArray menu = json.getJSONObject("menus").getJSONArray("menu"); //menu completo

        //dos listas con el menú y el título sincronizados
        String[] content = new String[menu.length()];
        String[] titles = new String[menu.length()];

        for (int i=0;i<menu.length();i++) {
            JSONObject entry = menu.getJSONObject(i);

            //titles

            String canteen = entry.getJSONObject("@attributes").getString("canteen");
            String day = entry.getJSONObject("@attributes").getString("weekday");
            String meal = entry.getJSONObject("@attributes").getString("meal");

            titles[i] = day+" - "+canteen+" ("+meal+")";

            //content

            String disabled = entry.getJSONObject("@attributes").getString("disabled");

            if (!disabled.equals("0")) { //disable "= 0 --> no hay comida

                content[i] = disabled;

            } else { //cuando disable == 0 --> si hay comida

                content[i] = "";
                JSONArray items = entry.getJSONObject("items").getJSONArray("item");
                for (int x=0;x<items.length();x++) {
                    if (!items.getString(x).contains("{")) //doesn't exists
                        content[i] += items.getString(x)+"\n\n";
                }

            }

        }

        //tupla con todos los títulos y los contenidos
        String[][] resultStrs = new String[2][menu.length()];
        resultStrs[0] = titles;
        resultStrs[1] = content;


        return resultStrs;

    }

}
