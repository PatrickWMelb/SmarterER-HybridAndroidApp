package wang.patrick.smarter;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {
    protected EditText dobView;
    protected EditText nameView;
    protected EditText surView;
    protected EditText addView;
    protected EditText postView;
    protected EditText mobView;
    protected EditText emailView;
    protected EditText energyView;
    protected Spinner numresSpinner;
    protected EditText userView;
    protected EditText passView;
    protected Integer resid;
    boolean success;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dobView = findViewById(R.id.dob);
        nameView = findViewById(R.id.firstname);
        surView = findViewById(R.id.surname);
        addView = findViewById(R.id.address);
        postView = findViewById(R.id.postcode);
        mobView = findViewById(R.id.mobile);
        emailView = findViewById(R.id.email);
        energyView = findViewById(R.id.energyprov);
        userView = findViewById(R.id.username);
        passView = findViewById(R.id.password);
        numresSpinner = findViewById(R.id.numres);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        dobView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        Button regBtn = (Button) findViewById(R.id.reg_button);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        String name = nameView.getText().toString();
                        String dob = dobView.getText().toString();
                        String sur = surView.getText().toString();
                        String add = addView.getText().toString();
                        String post = postView.getText().toString();
                        String mob = mobView.getText().toString();
                        String email = emailView.getText().toString();
                        String energy = energyView.getText().toString();
                        String user = userView.getText().toString();
                        String pass = passView.getText().toString();
                        String numres = numresSpinner.getSelectedItem().toString();



                        String residstring = String.valueOf(RestClient.findNumberofResident());
                        resid = Integer.parseInt(residstring) +1;


                        if(!isValidUser(user)) {
                            return "User Name Taken or Empty";
                        }
                        else if (!isValidEmail(email)){
                            return "Email NOT Valid";
                        }
                        else if( !isValidMobile(mob)){

                            return "mobile not valid, 10 digits";

                        }else if (TextUtils.isEmpty(name)||TextUtils.isEmpty(dob)||TextUtils.isEmpty(sur)||TextUtils.isEmpty(add)||TextUtils.isEmpty(post)||TextUtils.isEmpty(energy)||TextUtils.isEmpty(pass)) {

                                return "Missing Value";

                        }else if (numres.length()>1){
                            return "Please select Number of residents";
                        }else if (post.length() != 4){
                            return "Postcode incorrect";
                        }else{


                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            Resident resident = new Resident(resid, name,sur,add,post,email,numres,energy,mob,dob);
                            ResCred rescred = new ResCred(user,resident,String.valueOf(pass.hashCode()),date);

                            RestClient.createResident(resident);
                            RestClient.createResCred(rescred);
                            success =true;
                            return  "all good ";
                        }

                    }

                    @Override
                    protected void onPostExecute(String result) {

                        Toast.makeText(RegisterActivity.this, result,
                                Toast.LENGTH_LONG).show();
                        if (success){
                            Intent intent=new Intent(RegisterActivity.this,LoginActivity.class); // redirecting to Mainapi.
                            startActivity(intent);

                        }


                    }
                }.execute();






            }


        });
    }






    private boolean isValidEmail(String email){
        //make sure email is not already registered
        if (RestClient.findByResemail(email).length()>2) {
            emailView.setText("Email already registered");

            return false;
        }
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String number) {
        return  ( !TextUtils.isEmpty(number) && number.matches("[0-9]+") && number.length()==10);
    }

    private boolean isValidUser(String user){
        //make sure email is not already registered
        if (RestClient.findByCredusername(user).length()>2) {
            //"[]" since the return is string


            return false;
        }
        return !TextUtils.isEmpty(user);
    }











    private void updateLabel(){
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobView.setText(sdf.format(myCalendar.getTime()));
    }
}
