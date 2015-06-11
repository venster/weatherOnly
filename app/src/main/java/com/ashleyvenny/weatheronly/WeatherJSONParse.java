package com.ashleyvenny.weatheronly;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ashleyvo on 6/8/15.
 */
public class WeatherJSONParse {

    private City city;  //holds name, lat, long
    private WeatherOfDay weather;
    private currentTemp temp;

    public WeatherJSONParse(){
        city=new City();
        weather = new WeatherOfDay();
        temp = new currentTemp();


    }


    public void parseInfo(String data) throws JSONException {
        JSONObject JObj = new JSONObject(data);

        JSONObject JcoordInfo = getObject("coord",JObj);

        //because weather is in an array for some reason, get the array of weather
        JSONArray weatherArr = JObj.getJSONArray("weather");

        //get weather info out from array
        JSONObject JSONWeather= weatherArr.getJSONObject(0);
        //get temperature
        JSONObject JSONTemp = getObject("main",JObj);

        city.setName(getString("name",JObj));
        city.setLat(getDouble("lat",JcoordInfo));
        city.setLon(getDouble("lon",JcoordInfo));
        city.setId(getInt("id",JObj));

        //weatherinfo
        weather.setDescrip(getString("description",JSONWeather));
        weather.setIcon(getString("icon",JSONWeather));
        weather.setIcon_url(weather.getIcon());
        weather.setId(getInt("id",JSONWeather));
        weather.setMainWeather(getString("main",JSONWeather));

        //tempinfo
        temp.setcTemp(getDouble("temp",JSONTemp));
        temp.setMaxTemp(getDouble("temp_max",JSONTemp));
        temp.setMinTemp(getDouble("temp_min",JSONTemp));
        //Log.d("Max Temp",Double.toString(temp.getMaxTemp()) );
        //Log.d("Min Temp",Double.toString(temp.getMinTemp()));





    }

    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static Double  getDouble(String tagName, JSONObject jObj) throws JSONException {
        return (Double) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    public City getCity() {
        return city;
    }

    public WeatherOfDay getWeather() {
        return weather;
    }

    public currentTemp getTemp() {
        return temp;
    }
}
