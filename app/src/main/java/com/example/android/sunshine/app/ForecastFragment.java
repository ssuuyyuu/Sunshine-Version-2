package com.example.android.sunshine.app;


import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    ArrayAdapter<String> arrayAdapter;


    public ForecastFragment() {
        // Required empty public constructor
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();

        FetchWeatherTask task = new FetchWeatherTask();
        task.execute("75001");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.forecast_listView);

        String[] data = {};
        List<String> list = new ArrayList<String >(Arrays.asList(data));
        arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.forecast_item,
                R.id.forecast_listView_item,
                list);

        listView.setAdapter(arrayAdapter);
        return rootView;
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String[] doInBackground(String... params) {
            String[] result = null;
            String jsonStr = null;
            String baseURL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            int numDays = 7;

            Uri uri = Uri.parse(baseURL);
            String urlStr = uri.buildUpon()
                    .appendQueryParameter("q", params[0])
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", "metric")
                    .appendQueryParameter("cnt", Integer.toString(numDays)).build().toString();
            Log.d(LOG_TAG, "url: " + urlStr);

            URL url = null;
            try {
                url = new URL(urlStr);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                Log.d(LOG_TAG, "sb: " + sb.toString());


                if (sb.length() == 0) {
                    return null;
                }
                jsonStr = sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                return parseJson(jsonStr, numDays);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param strings The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            if (strings == null) {
                return;
            }
            arrayAdapter.clear();
            for (String string : strings) {
                arrayAdapter.add(string);
            }
        }

        private String[] parseJson(String jsonStr, int numDays) throws JSONException {
            String[] result = new String[numDays];

            JSONObject object = new JSONObject(jsonStr);
            JSONArray forecastArray = object.getJSONArray("list");

            for (int i = 0; i < forecastArray.length(); i++) {
                JSONObject forecast = (JSONObject)forecastArray.get(i);

                JSONArray weatherArray = (JSONArray)forecast.getJSONArray("weather");
                JSONObject weather = (JSONObject)weatherArray.get(0);
                String main = weather.getString("main");

                JSONObject temp = (JSONObject)forecast.getJSONObject("temp");
                String min = temp.getString("min");
                String max = temp.getString("max");
                result[i] = main + " " + min + " - " + max;
            }

            return result;
        }
    }

}
