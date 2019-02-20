package wang.patrick.smarter;

/*

import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.database.SQLException;
import android.database.Cursor;




public class MainApiActivity extends AppCompatActivity {
    protected DBManager dbManager;
    protected TextView textView;

    Calendar rightNow = Calendar.getInstance();

    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

    protected String currentFridge;
    protected String currentWash;
    protected String currentAir;
    protected Boolean[] HourGened[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String resid = getIntent().getExtras().getString("LoginResid");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_main);
        final TextView tv = findViewById(R.id.tvResults);
        dbManager = new DBManager(this);
        textView = (TextView) this.findViewById(R.id.textView);

        final ElecUsageGen elecgen = new ElecUsageGen();
        elecgen.Gen24();

        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnForceCall = findViewById(R.id.btnForceCall);
        Button btnForceAdd = findViewById(R.id.btnForceAdd);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create an anonymous AsyncTask
/*
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        String result;
                        result = WeatherAPI.WeatherCall();
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String result) {

                        tv.setText(result);
                    }
                }.execute();
            }
        });


        btnForceCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentHour = rightNow.get(Calendar.HOUR_OF_DAY);



                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {

                        currentFridge = elecgen.getFridge(currentHour);
                        currentWash = elecgen.getWash(currentHour);
                        currentAir = elecgen.getAir(currentHour,21.0);

                        return "0.0";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        tv.setText(currentFridge + currentWash + currentAir);
                        insertData(currentFridge,currentWash,currentAir);

                    }
                }.execute();






            }
        });

        btnForceAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                }

        });



    }






    public void insertData(String fridge, String wash,String air){
        try {
            dbManager.open();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        dbManager.insertUser(String.valueOf(currentHour), fridge, wash,air);


        dbManager.close();

    }
    public String readData(){
        Cursor c = dbManager.getAllUsers();
        String s="";
        if (c.moveToFirst()) {
            do {
                s+="hour: " + c.getString(0) + "\t" + "Fridge: " + c.getString(1)
                        + "\t" + "Wash: " + c.getString(2)+ "\t" + "Air" + c.getString(3)+"\n";
            } while (c.moveToNext());
        }
        return s;
    }

    public void deleteData(String id){
        try {
            dbManager.open();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        dbManager.deleteUser(id);
        textView.setText(readData());
        dbManager.close();
    }

}


*/