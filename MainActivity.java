package com.zubb.jannarongj.z_folding;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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


public class MainActivity extends AppCompatActivity {

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
    final Context context = this;

    TextView txt_sumcount,txt_sumqty,txt_sumweight;
    int sumcount=0,sumqty=0,sumweight=0;

    public int getErDup() {
        return erDup;
    }

    public void setErDup(int erDup) {
        this.erDup = erDup;
    }

    int erDup;

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
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        connectionClass = new ConnectionClass();
        usrHelper = new UserHelper(this);
        hideEdt = (EditText) findViewById(R.id.hedt);

        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        mDateDisplay = (TextView) findViewById(R.id.mDateDisplay);
        txt_sumcount =(TextView) findViewById(R.id.sumcount);
        txt_sumqty =(TextView) findViewById(R.id.sumqty);
        txt_sumweight =(TextView) findViewById(R.id.sumweight);
        btndelete = (Button) findViewById(R.id.btndelete);

        lvf = (ListView)findViewById(R.id.lvf);
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

        });

        btndelete.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {


                if (tab_id == null) {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("กรุณาเลือกรายการที่จะลบ");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    builder.show();

                } else {
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("ลบรายการ " + tab_hn + " หรือไม่ ?");
                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DeletePro deletePro = new DeletePro();
                            deletePro.execute(tab_id);

                        }
                    });
                    builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dialog.dismiss();
                        }
                    });
                    builder.show();

                }

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

            AddProScan addProScan = new AddProScan();
            addProScan.execute(rsscan);

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

                Toast.makeText(MainActivity.this, z, Toast.LENGTH_SHORT).show();
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
    }
    public void adjQty(String hn,int qty,Double weight){

        final Dialog adjdialog = new Dialog(MainActivity.this);
        adjdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        adjdialog.setContentView(R.layout.dialog_adjust);

        adjdialog.setCancelable(true);
        TextView thn,oqty;
        EditText eqty;
        Button svbtn,cnbtn;
        qweight = (TextView)adjdialog.findViewById(R.id.tv_weight);
        thn = (TextView)adjdialog.findViewById(R.id.thn);
        oqty= (TextView)adjdialog.findViewById(R.id.tv_oqty);
        eqty = (EditText) adjdialog.findViewById(R.id.eqty);
        svbtn =(Button)adjdialog.findViewById(R.id.btnQtysv) ;
        cnbtn =(Button)adjdialog.findViewById(R.id.btnCan) ;
        thn.setText(hn);
        eqty.setText("");

        oqty.setText(""+qty);
        qweight.setText(""+Math.round(weight));
        double mm = 0.0;

        mm = (weight/qty);

        final double finalMm = mm;
        eqty.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0 )
                    qweight.setText(""+(Math.round(Double.parseDouble(String.valueOf(s))* finalMm)));
            }
        });


        svbtn.setOnClickListener(new View.OnClickListener() {
            EditText eqty = (EditText) adjdialog.findViewById(R.id.eqty);

            public void onClick(View v) {
                AdjItem adji = new AdjItem();
                adji.execute(eqty.getText().toString().trim(),item_id,qweight.getText().toString());

                adjdialog.dismiss();
            }
        });
        cnbtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                adjdialog.dismiss();
            }

        });
        adjdialog.show();

    }

    public class AdjItem extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);

            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();

            FillList fillList = new FillList();
            fillList.execute(paramdate);

        }
        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "update tbl_folded set qty = '"+params[0]+"' ,  weight = '"+params[2]+"' ," +
                            "  WHERE item_id = "+params[1]+"  ";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "แก้ไขเรียบร้อยแล้ว";
                    isSuccess = true;

                    //Log.d("update",query);

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage().toString();
            }
            return z;
        }
    }


    public void onErrorDialog(int eDup ,int notFound) {
        String msg = "";
        if (scanresult == null || scanresult.equals("")) {
            scanresult = "PICK";
        }
        if (eDup == 0 && notFound == 0 ) {
            msg = itxt + "\n\n" + "Code " + scanresult;
        }

            if (notFound == 1) {
                msg = "ไม่พบข้อมูลที่สแกน !" + "\n\n" + "Code " + scanresult;
            } else {

                if (eDup == 1) {

                    msg = "ซ้ำ HN " + eXcharge + "-" + eXbundle + "\nสินค้า : " + exSize + "\nโดย " + eXuser.substring(0, eXuser.length() - 4) + "\nวันที่ " + eXadd_stamp.substring(0, 16) + "\n\n" + "Code " + scanresult;
                }  else{
                    msg = itxt + "\n\n" + "Code " + scanresult;
                }
            }

        new AlertDialog.Builder(context)

                .setTitle("ผิดพลาด")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
               /* .setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })  */

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";


        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {


            String[] from = {"id","charge","qa","qty","weight","matcode","remark"};
            int[] views = {R.id.id,R.id.charge, R.id.qa_grade, R.id.qty, R.id.weight, R.id.size, R.id.remark};
            final SimpleAdapter ADA = new SimpleAdapter(MainActivity.this,
                    foldlist, R.layout.adp_hn, from,
                    views) { };
            lvf.setAdapter(ADA);
            lvf.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);

                    String t_id = (String) obj.get("item_id");
                    String t_charge = (String) obj.get("charge");

                    tab_hn = t_charge;
                    tab_id = t_id;
                    arg1.setSelected(true);


                }
            });
            /*if (usrHelper.getLevel().equals("10")) {
                lvf.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                                   int arg2, long arg3) {

                            HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                    .getItem(arg2);
                            String qhn = (String) obj.get("charge");
                            int qqty = Integer.parseInt((String) obj.get("qty"));
                            double qweight = Integer.parseInt((String) obj.get("rmd_weight"));
                            tab_id = (String) obj.get("id");
                            arg1.setSelected(true);
                            adjQty(qhn,qqty,qweight);

                        return true;
                    }
                });


            }*/

            txt_sumcount.setText(""+nf.format(sumcount)+" มัด");
            txt_sumqty.setText(""+nf.format(sumqty)+" เส้น");
            txt_sumweight.setText(""+nf.format(sumweight)+" kg.");

            pbbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String where = "where convert(nvarchar(30),add_stamp,112) = '"+params[0].trim()+"' ";

                    String sql = "Select id,charge,bundle,qty,rmd_qa_grade,rmd_size,rmd_remark,rmd_weight,matcode  from tbl_folded " +where +" order by add_stamp desc ";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                    int ids = 0 ;

                    foldlist.clear();
                    while (rs.next()) {
                        ids ++;
                        Map<String, String> datanums = new HashMap<String, String>();
                        datanums.put("id",String.valueOf(ids));
                        datanums.put("item_id", rs.getString("id"));
                        datanums.put("charge", rs.getString("charge")+"-"+rs.getString("bundle"));
                        datanums.put("qty", rs.getString("qty"));
                        datanums.put("qa", rs.getString("rmd_qa_grade"));
                        datanums.put("matcode", rs.getString("matcode"));
                        datanums.put("remark", rs.getString("rmd_remark"));
                        datanums.put("weight", rs.getString("rmd_weight"));

                        foldlist.add(datanums);

                    }

                    String sumq = "SELECT count(item_barcode) AS sc ,sum(qty) as sq ,sum(rmd_weight) AS sw  FROM tbl_folded  "+where ;
                    PreparedStatement qps = con.prepareStatement(sumq);
                    ResultSet qrs = qps.executeQuery();

                    while (qrs.next()) {
                        sumcount = qrs.getInt("sc");
                        sumqty = qrs.getInt("sq");
                        sumweight = qrs.getInt("sw");
                    }

                    z = "Success";

                }

            } catch (Exception ex) {

                z = ex.getMessage().toString();

            }

            return z;

        }
    }

    public class DeletePro extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
                FillList fillList = new FillList();
                fillList.execute(paramdate);
            }

            tab_hn = null;
            tab_id = null;

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "delete tbl_folded WHERE id = "+params[0]+"  ";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();
                    z = "ลบสำเร็จ";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "ERROR DEL แจ้ง IT 1203";
            }

            return z;
        }
    }

    public void onDateclick(View v) {
        showDialog(DATE_DIALOG_ID);
    }

}
