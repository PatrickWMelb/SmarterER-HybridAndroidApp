package wang.patrick.smarter;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReportFragment extends Fragment {
    View vReport;
    EditText datePickerView;
    PieChart pieChart;
    Calendar myCalendar = Calendar.getInstance();
    private float[] yValue = {0.0f,0.0f,0.0f};
    private String[] xValue = {"Fridge","Air Con","Washing Mach"};

    String pickedDate;

    Integer resid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        vReport = inflater.inflate(R.layout.fragment_report, container, false);
        datePickerView = vReport.findViewById(R.id.pieDatepicker);

        pieChart = vReport.findViewById(R.id.pieChart);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            resid = (Integer.parseInt(bundle.getString("LoginResid")));

        }




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

        datePickerView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                
                
                
                
            }
        });










        return vReport;
    }




   

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        pickedDate = sdf.format(myCalendar.getTime());
        datePickerView.setText(pickedDate);



        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {

                try {

                    String restresult = RestClient.findDailykWhFromIdDate(resid.toString());
                    Log.d("resresult =", restresult);
                    JSONObject jsonObject = new JSONObject(restresult);

                    yValue[0] = Float.parseFloat(jsonObject.getString("fridge"));
                    yValue[1] = Float.parseFloat(jsonObject.getString("AC"));
                    yValue[2] = Float.parseFloat(jsonObject.getString("wash"));


                } catch (Exception e) {
                    e.printStackTrace();

                }

                return "nothing";
            }

            @Override
            protected void onPostExecute(String result) {
                addDataset(pieChart);
            }
        }.execute();









        
        


    }

    private void addDataset(PieChart pieChart) {
        ArrayList<PieEntry> yEntry= new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();
        int i;
        for(i = 0; i< yValue.length;i++){
            yEntry.add(new PieEntry(yValue[i],xValue[i]));

        }

        //merge to dataset
        PieDataSet pieDataSet = new PieDataSet(yEntry,"Appliance");
        pieDataSet.setValueTextSize(30);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.MAGENTA);
        pieDataSet.setColors(colors);

        Legend legend =pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(20);
        legend.setEnabled(true);
        legend.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        legend.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.invalidate();



    }

}
