package com.ashleyvenny.weatheronly;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    HttpURLConnection connection = null;
    private ProgressBar progressLoading;
    WeatherJSONParse weather= new WeatherJSONParse(); //all of the weather data with

    private TextView city;
    private FrameLayout WIcon;
    private TextView Temp;
    private TextView condition;
    private TextView conDescrip;
    private TextView deg,deg1,deg2,maxLab,minLab,minTemp,maxTemp;

    private ImageButton OWButton;
    URL picURL;
    Drawable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.location);
        WIcon = (FrameLayout) findViewById(R.id.wIcon);
        Temp = (TextView) findViewById(R.id.currentTemp);
        condition = (TextView) findViewById(R.id.mainWeather);
        conDescrip = (TextView) findViewById(R.id.weatherDes);
        progressLoading = (ProgressBar) findViewById(R.id.progressBar);
        deg=(TextView) findViewById(R.id.degree);
        deg1=(TextView) findViewById(R.id.degree2);
        deg2=(TextView) findViewById(R.id.degree3);
        maxLab=(TextView) findViewById(R.id.maxLab);
        minLab=(TextView) findViewById(R.id.minLab);
        OWButton=(ImageButton) findViewById(R.id.weatherFrom);
        maxTemp=(TextView) findViewById(R.id.maxTemp);
        minTemp=(TextView) findViewById(R.id.minTemp);

        //hide all the text
        city.setVisibility(View.GONE);
        WIcon.setVisibility(View.GONE);
        Temp.setVisibility(View.GONE);
        condition.setVisibility(View.GONE);
        conDescrip.setVisibility(View.GONE);
        minTemp.setVisibility(View.GONE);
        maxTemp.setVisibility(View.GONE);
        deg.setVisibility(View.GONE);
        deg1.setVisibility(View.GONE);
        deg2.setVisibility(View.GONE);
        maxLab.setVisibility(View.GONE);
        minLab.setVisibility(View.GONE);
        OWButton.setVisibility(View.GONE);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000, 10,locationListener);

        OWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.openweathermap.org/city/"+weather.getCity().getId();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

    }
    LocationListener locationListener = new LocationListener() {
        @Override
        //Get GPS Information here
        public void onLocationChanged(Location location) {
            weather.getCity().setLat(location.getLatitude());
            weather.getCity().setLon(location.getLongitude());
            //weathertext.setText(Double.toString(coord.getLat()));
            //temp.setText(Double.toString(location.getLatitude()));
            startLoadTask(MainActivity.this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };
    public void startLoadTask(Context c){
        if (isOnline()) {
            LoadData task = new LoadData();
            task.execute();
        } else {
            Toast.makeText(c, "Not online", Toast.LENGTH_LONG).show();
        }
    }
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    private class LoadData extends AsyncTask<String, Long, Long> {
        HttpURLConnection connection = null;


        @Override
        protected void onPreExecute() {
            progressLoading.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
        }

        protected Long doInBackground(String... strings) {
            String BASE_URL= "http://api.openweathermap.org/data/2.5/weather?lat=";
            String BASE_URL2="&lon=";
            String BASE_URL3="mode=json&units=imperial";

            Log.d("Attempting","STARTING TO CONNECT");
//

            try {
                URL dataUrl = new URL(BASE_URL+weather.getCity().getLat()+BASE_URL2+weather.getCity().getLon()+BASE_URL3);
                Log.d("URL", BASE_URL + weather.getCity().getLat() + BASE_URL2 + weather.getCity().getLon() + BASE_URL3);
                connection = (HttpURLConnection) dataUrl.openConnection();
                connection.connect();
                int status = connection.getResponseCode();
                //Log.d("TAG", "status " + status);
                //if it is successful
                if (status == 200) {
                    InputStream is = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String responseString;
                    StringBuilder sb = new StringBuilder();

                    while ((responseString = reader.readLine()) != null) {
                        sb = sb.append(responseString);
                    }
                    String photoData = sb.toString();

                    // Log.d("TAG", photoData);
                    weather.parseInfo(photoData);

                    //Log.d("AFTER PARSE", weather.getCity().getName());

                    //get icon
                    picURL= new URL(weather.getWeather().getIcon_url());
                    InputStream content = (InputStream)picURL.getContent();
                     d = Drawable.createFromStream(content , "src");


                    return 0l;
                } else {
                    return 1l;
                }
            } catch (MalformedURLException e) {

                e.printStackTrace();
                return 1l;
            } catch (IOException e) {
                e.printStackTrace();
                return 1l;
            } catch (JSONException e) {
                Log.d("TAG", "failparse");
                e.printStackTrace();
                return 1l;
            } finally {
                if (connection != null)
                    connection.disconnect();
            }

        }
        @Override
        protected void onPostExecute(Long result) {
            if (result != 1l) {


                city.setText(weather.getCity().getName());
                Temp.setText(Double.toString(weather.getTemp().getcTemp()));
                condition.setText(weather.getWeather().getMainWeather());
                conDescrip.setText(weather.getWeather().getDescrip());
                WIcon.setBackground(d);

                minTemp.setText(Double.toString(weather.getTemp().getMinTemp()));
                maxTemp.setText(Double.toString(weather.getTemp().getMaxTemp()));

                city.setVisibility(View.VISIBLE);
                WIcon.setVisibility(View.VISIBLE);
                Temp.setVisibility(View.VISIBLE);
                condition.setVisibility(View.VISIBLE);
                conDescrip.setVisibility(View.VISIBLE);
                minTemp.setVisibility(View.VISIBLE);
                maxTemp.setVisibility(View.VISIBLE);
                deg.setVisibility(View.VISIBLE);
                deg1.setVisibility(View.VISIBLE);
                deg2.setVisibility(View.VISIBLE);
                maxLab.setVisibility(View.VISIBLE);
                minLab.setVisibility(View.VISIBLE);
                OWButton.setVisibility(View.VISIBLE);




            } else {
                Toast.makeText(MainActivity.this, "AsyncTask didn't complete", Toast.LENGTH_LONG).show();
            }
            progressLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
