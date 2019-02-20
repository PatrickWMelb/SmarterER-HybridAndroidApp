package wang.patrick.smarter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WeatherAPI {


    private static final String weatherAPI_KEY = "&APPID=0063155aa9ad29ff1cb0c36a1341cdfd";
    private static final String googleGeoAPI_KEY = "AIzaSyB4apmNhkb0_pnD3i7VLGBB5eKGUeVsJ-c";


    public static String WeatherCall(String lat, String lng) {

        URL apiurl = null;

        HttpURLConnection connection = null;
        String textResult = "";
        try {
            apiurl = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + lat +"&lon="  +lng + weatherAPI_KEY);
            connection = (HttpURLConnection) apiurl.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
        }

    public static String getLocation(String location) {

        URL apiurl = null;

        HttpURLConnection connection = null;
        String textResult = "";
        try {
            apiurl = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + googleGeoAPI_KEY);
            connection = (HttpURLConnection) apiurl.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                textResult += scanner.nextLine();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }
        return textResult;
    }












}

