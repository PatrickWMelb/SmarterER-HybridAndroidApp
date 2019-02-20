package wang.patrick.smarter;

import android.app.Fragment;
import android.database.Cursor;
import android.database.SQLException;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    View vMain;
    protected TextView timeView;
    protected TextView weatherView;
    protected TextView nameView;
    protected TextView posView;
    Handler myHandler = new Handler();
    int currentHour;
    int currentMinute;
    int currentSecond;
    int currentDay;
    Integer resid;
    String fullName;
    String locAndPost;
    String lat;
    String lng;

    protected DBManager dbManager;

    protected boolean[] HourGened = new boolean[24];
    protected Integer SqlSentDate = -1;
    protected static ElecUsageGen elecgen = new ElecUsageGen();
    protected Integer LastGenDate;
    protected String currentFridge;
    protected String currentWash;
    protected String currentAir;
    protected String currentTemp;
    protected String latlong;

    ImageView image;
    String add;
    String dob;
    String email;
    String energy;
    String name;
    String mob;
    String numres;
    String post;
    String sur;


    protected boolean Positive;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        dbManager = new DBManager(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resid = (Integer.parseInt(bundle.getString("LoginResid")));
            fullName = bundle.getString("fullName");
            locAndPost = bundle.getString("locAndPost");
        }
        Calendar rightNow = Calendar.getInstance();
        LastGenDate = rightNow.get(Calendar.DAY_OF_WEEK);
        elecgen.Gennew24();


        Toast.makeText(getActivity(), resid.toString(), Toast.LENGTH_LONG).show();

        vMain = inflater.inflate(R.layout.fragment_homepage, container, false);

        updateTimeRun();
        updateWeatherRun();
        nameView = vMain.findViewById(R.id.homeName);
        image = vMain.findViewById(R.id.homePosimg);
        // weatherView = vMain.findViewById(R.id.homeWeather);

        nameView.setText("Welcome " + fullName);



        Button btnUpdate = vMain.findViewById(R.id.updateButton);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create an anonymous AsyncTask

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        try {
                            dbManager.open();
                        }catch(SQLException e) {
                            e.printStackTrace();
                        }
                       String result = readData();
                       return result;
                    }

                    @Override
                    protected void onPostExecute(String result) {

                        nameView.setText(result);
                    }
                }.execute();
            }
        });
        Button btnForcesql = vMain.findViewById(R.id.TestSql);
        btnForcesql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SqlEndofDay();



            }
        });



        return vMain;
        }



    public void updateTimeRun() {
        Runnable timerunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                // do your stuff - don't create a new runnable here!
                myHandler.postDelayed(this, 1000);
            }
        };

        myHandler.post(timerunnable);
    }


    public void updateWeatherRun() {
        Runnable weatherrunnable = new Runnable() {
            @Override
            public void run() {
                updateWeather();
                // do your stuff - don't create a new runnable here!
                myHandler.postDelayed(this, 1000*30);
            }
        };

        myHandler.post(weatherrunnable);


    }


    private void updateTime() {
        Calendar rightNow = Calendar.getInstance();
        currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        currentMinute = rightNow.get(Calendar.MINUTE);
        currentSecond = rightNow.get(Calendar.SECOND);
        timeView = vMain.findViewById(R.id.homeTime);
        timeView.setText(String.valueOf(currentHour) + ":" + String.valueOf(currentMinute) + ":" + String.valueOf(currentSecond));


    }

    private void updateWeather() {

        weatherView = vMain.findViewById(R.id.homeWeather);
        posView = vMain.findViewById(R.id.homePosmsg);

        new AsyncTask<String, Void, String>() {


            @Override
            protected String doInBackground(String... params) {
                String result;








                latlong = WeatherAPI.getLocation(locAndPost);
                try {
                    JSONObject jsonObject = new JSONObject(latlong);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    lat = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                    lng = jsonArray.getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                    result = WeatherAPI.WeatherCall(lat, lng);
                    jsonObject = new JSONObject(result);
                    result = jsonObject.getJSONObject("main").getString("temp");
                    Double temp = Double.parseDouble(result);
                    result = String.valueOf(temp - 273.15);
                    currentTemp =result;



                    /// logic for checking if thsi hour has been gen+ if hour gen = 23 and sqlsend ==fasle we send to sql,

                    Calendar rightNow = Calendar.getInstance();
                    currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                    currentDay = rightNow.get(Calendar.DAY_OF_WEEK);

                    //norecord gen today
                    if(LastGenDate != currentDay){
                        elecgen.Gennew24();
                        LastGenDate = currentDay;
                    }
                    if(SqlSentDate!=currentDay) {
                        //check if the current hour has been gen'd and inserted to Sql
                        if (!HourGened[currentHour]) {
                            currentFridge = elecgen.getFridge(currentHour);
                            currentWash = elecgen.getWash(currentHour);
                            currentAir = elecgen.getAir(currentHour, Double.parseDouble(result));

                                    //chekc if over threshold
                            if ((currentDay<=5) && (Double.parseDouble(currentFridge)+ Double.parseDouble(currentWash) + Double.parseDouble(currentAir)>=1.5)){
                                Positive =false;
                            }
                            else{
                                Positive = true;
                            }

                            Log.d("Fridge ======", currentFridge);
                            Log.d("Wash ======", currentWash);
                            Log.d("Fridge ======", currentAir);
                            insertData(currentFridge, currentWash, currentAir, currentTemp);
                            HourGened[currentHour] = true;

                        }

                        ///if sql has not been sent today and is end of day, send to sql
                        if (currentHour == 23) {
                            //send to from sql to Datab if last hour and not sent today
                            SqlEndofDay();
                            SqlSentDate = currentDay;
                            int i;
                            for (i = 0; i < 24; i++) {
                                HourGened[i] = false;
                            }
                        }

                    }




                } catch(Exception e){
                    e.printStackTrace();
                    result = "NO INFO FOUND";
                }




            return result;
            }

            @Override
            protected void onPostExecute(String result) {
                weatherView.setText("the current weather is : " + result);

                if (Positive){
                    posView.setText("GOOD JOB");
                    image.setImageResource(R.drawable.happy);

                }else{
                    posView.setText("BAD JOB");
                    image.setImageResource(R.drawable.sad);


                }

            }
        }.execute();


    }


    public void insertData(String fridge, String wash, String air, String temp){
        try {
            dbManager.open();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        dbManager.insertUser(String.valueOf(currentHour), fridge, wash,air, temp);

        dbManager.close();

    }


    public String readData(){

        try {
            dbManager.open();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        Cursor c = dbManager.getAllUsers();
        String s="";
        if (c.moveToFirst()) {
            do {
                s+="hour: " + c.getString(0) + "\t" + "Fridge: " + c.getString(1)
                        + "\t" + "Wash: " + c.getString(2)+ "\t" + "Air" + c.getString(3)+"\n" + "Temp" + c.getString(4)+"\n";
            } while (c.moveToNext());
        }
        dbManager.close();
        return s;
    }
//delete allinstead of 1 id at a time
    public void deleteData(){
        try {
            dbManager.open();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        dbManager.deleteAll();

        dbManager.close();
    }

    public void SqlEndofDay(){
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                //post sql to data base
                String UsageIDstring = String.valueOf(RestClient.findNumberofResident());
                int UsageId = Integer.parseInt(UsageIDstring) +1;
                String result = RestClient.findByResid(resid.toString());

                try{
                    JSONObject jsonObject = new JSONObject(result);
                    String add = jsonObject.getString("resaddress");
                    String dob = jsonObject.getString("resdob");
                    String email = jsonObject.getString("resemail");
                    String energy = jsonObject.getString("resenergy");
                    String name = jsonObject.getString("resfirstname");
                    String mob = jsonObject.getString("resmobile");
                    String numres = jsonObject.getString("resnumber");
                    String post = jsonObject.getString("respostcode");
                    String sur = jsonObject.getString("ressurname");


                    Log.d("add===", add);
                    Log.d("dob===", dob);
                    Log.d("email===", email);
                    Log.d("energy===", energy);
                    Log.d("name===", name);
                    Log.d("mob===", mob);
                    Log.d("numres===", numres);
                    Log.d("post===", post);
                    Log.d("sur===", sur);




                }catch (Exception e){
                    e.printStackTrace();

                }

                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                Log.d("date =====", date);

                Resident resident = new Resident(resid, name,sur,add,post,email,numres,energy,mob,dob);
                try {
                    dbManager.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Cursor c = dbManager.getAllUsers();
                String s = "";
                if (c.moveToFirst()) {
                    do {
                        ElecUsage elecUsage = new ElecUsage(UsageId,resident,Integer.parseInt(c.getString(0)), c.getString(1),c.getString(2),c.getString(3),c.getString(4),date);
                        RestClient.createElecUsage(elecUsage);
                        UsageId++;

                    } while (c.moveToNext());
                }

                dbManager.close();











                deleteData();








                return "lol";
            }

            @Override
            protected void onPostExecute(String result) {


            }
        }.execute();



    }



}


