package com.zubb.jannarongj.z_folding;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdjLine extends Activity {

    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView mDateDisplay,qweight;
    private ProgressBar pbbar ;
    private ListView lvf ;

    private EditText hideEdt,debugEdt;
    String charge,spec,size,grade,bundle,qty,weight,scan_mat,remark,qa_grade,rmd_date,barcheck,length,plant,station;
    String tab_hn,tab_id,scanresult,itxt,item_id;
    String eXcharge,eXbundle,eXadd_stamp,eXuser,exSize,exBarcode;
    final Context context = this;

    //TextView txt_sumcount,txt_sumqty,txt_sumweight;
    int sumcount=0,sumqty=0,sumweight=0;



    public int getErNf() {
        return erNf;
    }

    public void setErNf(int erNf) {
        this.erNf = erNf;
    }

    int erNf;

    ConnectionClass connectionClass;
    UserHelper usrHelper;

    static final int DATE_DIALOG_ID = 0;
    String idate,paramdate;

    List<Map<String, String>> foldlist  = new ArrayList<Map<String, String>>();

    Locale THLocale = new Locale("en", "TH");
    NumberFormat nf = NumberFormat.getInstance(THLocale);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adj_line);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        connectionClass = new ConnectionClass();
        usrHelper = new UserHelper(this);
        hideEdt = (EditText) findViewById(R.id.hedt);



        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        mDateDisplay = (TextView) findViewById(R.id.mDateDisplay);
       /*txt_sumcount = (TextView) findViewById(R.id.sumcount);
        txt_sumqty = (TextView) findViewById(R.id.sumqty);
        txt_sumweight = (TextView) findViewById(R.id.sumweight);*/

        lvf = (ListView) findViewById(R.id.lvf);
        pbbar.setVisibility(View.GONE);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

        FillList fillList = new FillList();
        fillList.execute(paramdate);
        hideEdt.requestFocus();

        hideEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String demo = "";

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //  Toast.makeText(AddProducts.this, "D : " + hideEdt.getText().toString().trim(), Toast.LENGTH_SHORT).show();

                    if (hideEdt.getText().toString().trim().contains("DEMO")) {
                        demo = hideEdt.getText().toString().trim().replace("DEMO", "").replace("*", "").replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");

                    } else {
                        demo = hideEdt.getText().toString().trim().replace("*", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
                    }
                    insertSCAN(demo);


                }

                return false;

            }

        });

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();

            if(keycode == 120 || keycode == 520){
                hideEdt.requestFocus();
            }
        }
        return super.dispatchKeyEvent(KEvent);
    }

    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){
            this.scanresult = rsscan;

           //todo intent to adj screen

            Intent i = new Intent(AdjLine.this, AdjActivity.class);
            i.putExtra("bar_id", rsscan);

            startActivity(i);

            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");

    }


    private void updateDisplay() {
        String m;
        String d;
        this.mDateDisplay.setText(
                new StringBuilder()

                        .append(mDay).append("/")
                        .append(mMonth + 1).append("/")
                        .append(mYear + 543).append(" "));

        if((mMonth+1)< 10){
            m =  "0"+(mMonth+1);
        }else{
            m = String.valueOf((mMonth + 1));
        }
        if((mDay)<= 9){
            d =  "0"+(mDay);
        }else{
            d = String.valueOf((mDay));
        }

        this.idate = mYear+"-"+m+"-"+d;
        this.paramdate = mYear+""+m+""+d;

        // Toast.makeText(MainActivity.this, "id "+this.idate+"\n"+"pd "+this.paramdate, Toast.LENGTH_LONG).show();

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();

                    FillList fillList = new FillList();
                    fillList.execute(paramdate);
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);

        }
        return null;
    }




    public class FillList extends AsyncTask<String, String, String> {

        String z = "";


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {


            String[] from = {"id","charge","oqa","nqa","qty","matcode","remark"};
            int[] views = {R.id.id,R.id.charge, R.id.oqa, R.id.nqa, R.id.qty, R.id.size, R.id.remark};
            final SimpleAdapter ADA = new SimpleAdapter(AdjLine.this,
                    foldlist, R.layout.adp_pp_adj, from,
                    views) { };
            lvf.setAdapter(ADA);
            lvf.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);

                    String t_id = (String) obj.get("id");
                   // String t_charge = (String) obj.get("charge");

                   // tab_hn = t_charge ;
                    tab_id = t_id ;
                    arg1.setSelected(true);

                }
            });

            /*txt_sumcount.setText(""+nf.format(sumcount)+" มัด");
            txt_sumqty.setText(""+nf.format(sumqty)+" เส้น");
            txt_sumweight.setText(""+nf.format(sumweight)+" kg.");*/

            pbbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String where = "where convert(nvarchar(30),adj_date,112) = '"+params[0].trim()+"' ";

                    String sql = "Select *  from vw_ppadj " +where +" order by adj_date desc ";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    int ids = 0 ;

                    foldlist.clear();
                    while (rs.next()) {
                        ids ++;
                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("id",String.valueOf(ids));
                        //datanums.put("item_id", rs.getString("adj_no"));
                        datanums.put("charge", rs.getString("rmd_charge")+"-"+rs.getString("rmd_qty2"));
                        datanums.put("qty", rs.getString("rmd_qty3")+" เส้น");
                        datanums.put("oqa", rs.getString("oqa"));
                        datanums.put("nqa", rs.getString("nqa"));
                        datanums.put("matcode", rs.getString("rmd_matcode"));
                        datanums.put("remark", rs.getString("rmd_remark"));

                        foldlist.add(datanums);

                    }

                 /*   String sumq = "SELECT count(adj_no) AS sc ,sum(rmd_qty3) as sq ,sum(rmd_weight) AS sw  FROM vw_ppadj  "+where ;
                    PreparedStatement qps = con.prepareStatement(sumq);
                    ResultSet qrs = qps.executeQuery();

                    while (qrs.next()) {
                        sumcount = qrs.getInt("sc");
                        sumqty = qrs.getInt("sq");
                        sumweight = qrs.getInt("sw");
                    }*/

                    z = "Success";

                }

            } catch (Exception ex) {

                z = ex.getMessage().toString();

            }

            return z;

        }
    }


    public void onDateclick(View v) {
        showDialog(DATE_DIALOG_ID);
    }

}
