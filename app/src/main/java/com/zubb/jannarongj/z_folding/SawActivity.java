package com.zubb.jannarongj.z_folding;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class SawActivity extends AppCompatActivity {

    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView mDateDisplay,qweight;
    private ProgressBar pbbar ;
    private ListView lvf ;
    private Button btndelete;
    private EditText hideEdt;

    String charge,spec,size,grade,bundle,qty,weight,scan_mat,remark,qa_grade,rmd_date,barcheck,length,plant,station;
    String tab_hn,tab_id,scanresult,itxt,item_id;
    String eXcharge,eXbundle,eXadd_stamp,eXuser,exSize,exBarcode;


    ConnectionClass connectionClass;
    UserHelper usrHelper;

    static final int DATE_DIALOG_ID = 0;
    String idate,paramdate;
    int type;

    Locale THLocale = new Locale("en", "TH");
    NumberFormat nf = NumberFormat.getInstance(THLocale);

    List<Map<String, String>> sawlist  = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saw);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                type = 0;
            } else {
                type = extras.getInt("type");
            }
        } else {
            type = (Integer) savedInstanceState.getSerializable("type");
        }



        connectionClass = new ConnectionClass();
        usrHelper = new UserHelper(this);
        hideEdt = (EditText) findViewById(R.id.hedt);

        pbbar = (ProgressBar) findViewById(R.id.pbbar);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
//        updateDisplay();

//        hideEdt.requestFocus();

       /* hideEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String demo ="";
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    if(hideEdt.getText().toString().trim().contains("DEMO")){
                        demo = hideEdt.getText().toString().trim().replace("DEMO","").replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");

                    }else{
                        demo = hideEdt.getText().toString().trim().replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
                    }
                    insertSCAN(demo);
                }

                return false;
            }

        });*/

    }

  /*  private void updateDisplay() {
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

    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){
            this.scanresult = rsscan;

            AddProScan addProScan = new AddProScan();
            addProScan.execute(rsscan);

            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");

    }

    public class AddProScan extends AsyncTask<String, String, String> {

        String i = "";
        String z = "";

        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {

            // Toast.makeText(MainActivity.this, z, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {

                FillList fillList = new FillList();
                fillList.execute(paramdate);

                Toast.makeText(SawActivity.this, z, Toast.LENGTH_SHORT).show();
                itxt = null;


            }else {
                onErrorDialog(getErDup(),getErNf());

                itxt = null;
                eXcharge = null;
                eXbundle = null;
                exSize   = null;
                exBarcode = null;
                eXuser = null;
                eXadd_stamp = null;
            }
            pbbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "พบปัญหาการเชื่อมต่อ";
                } else {
                    String cDup ="";

                    String checkDup = "select * from tbl_folded where item_barcode = '" + params[0].trim() + "' ";
                    PreparedStatement cd = con.prepareStatement(checkDup);
                    ResultSet cdps = cd.executeQuery();


                    while (cdps.next()) {
                        exBarcode = cdps.getString("item_barcode");
                        eXcharge = cdps.getString("charge");
                        eXbundle = cdps.getString("bundle");
                        eXuser = cdps.getString("user_add");
                        eXadd_stamp = cdps.getString("add_stamp");
                        exSize = cdps.getString("matcode");
                        cDup = cdps.getString("item_barcode");

                    }

                    if (cDup.equals("")) {
                        setErDup(0);
                    } else {
                        setErDup(1);
                    }


                    String getData = "select rmd_charge,rmd_spec,case when rmd_spec = 'Coil' then rmd_grade+' '+rmd_size else rmd_size end as rmd_size,rmd_grade,r_bundle,r_qty,rmd_weight,rmd_qa_grade,bar_id,matcode,rmd_remark,rmd_date,rmd_length,rmd_plant,rmd_station from vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
                    PreparedStatement getDataSt = con.prepareStatement(getData);
                    ResultSet rs = getDataSt.executeQuery();
                    while (rs.next()) {
                        rmd_date = rs.getString("rmd_date");
                        charge = rs.getString("rmd_charge");
                        spec = rs.getString("rmd_spec");
                        size = rs.getString("rmd_size");
                        grade = rs.getString("rmd_grade");
                        bundle = rs.getString("r_bundle");
                        qty = rs.getString("r_qty");
                        weight = rs.getString("rmd_weight");
                        scan_mat = rs.getString("matcode");
                        qa_grade = rs.getString("rmd_qa_grade");
                        remark = rs.getString("rmd_remark");
                        barcheck = rs.getString("bar_id");
                        length = rs.getString("rmd_length");
                        plant = rs.getString("rmd_plant");
                        station = rs.getString("rmd_station");

                    }
                    if(barcheck ==null || barcheck.equals("")){
                        setErNf(1);
                    }else{
                        setErNf(0);
                    }

                    if (remark==null){
                        remark ="";
                    }
                    if(getErDup() == 0 &&  getErNf() == 0){
                        String insrt = "insert into tbl_folded (item_barcode,rmd_date,charge,bundle,qty,matcode,location,rmd_spec,rmd_size" +
                                ",rmd_grade,rmd_length,rmd_weight,rmd_qa_grade,rmd_remark,rmd_plant,rmd_station,user_add,add_stamp) " +
                                " values ('"+params[0].trim()+"','"+rmd_date+"','"+charge+"','"+bundle+"','"+qty+"','"+scan_mat+"','"+usrHelper.getPlant()+"','"+spec+"','"+size+"','"+grade+"','"+length+"','"+weight+"','"+qa_grade+"','"+remark+"','"+plant+"','"+station.trim()+"','"+usrHelper.getUserName()+"_"+usrHelper.getVer()+"',current_timestamp)";
                        PreparedStatement preparedStatement = con.prepareStatement(insrt);
                        preparedStatement.executeUpdate();

                        String umat = "";

                        if(spec==null || spec.equals("")){
                            umat = "";
                        }
                        if(spec.length()>2){
                            umat  = ",rmd_spec = left(rmd_spec,2)+'F' ";
                        }

                        String upd = "update tbl_production_scale " +
                                "set rmd_mat = left(rmd_mat,2)+'F'+substring(rmd_mat,4,20) "+umat+" " +
                                "where bar_id = '"+params[0]+"' and rmd_spec = '"+spec+"' ";
                        PreparedStatement preparedF = con.prepareStatement(upd);
                        preparedF.executeUpdate();

                        //Log.d("ddxx",upd);

                        //TODO update code F on production scale




                        isSuccess=true ;

                    }else{
                        isSuccess = false;

                    }
                    // Log.d("qr",insrt);


                }

                z = "บันทึกเรียบร้อยแล้ว";
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage().toString();
                if(exBarcode == null) {

                }else {

                    itxt = ex.getMessage().toString();
                }

            }
            return z ;
        }
    }*/

}
