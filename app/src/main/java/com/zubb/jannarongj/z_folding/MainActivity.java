package com.zubb.jannarongj.z_folding;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends Activity {

    private int mYear;
    private int mMonth;
    private int mDay;
    private TextView mDateDisplay,qweight;
    private ProgressBar pbbar ;
    private ListView lvf ;
    private Button btndelete;
    private EditText hideEdt;
    MLocation loc;
    CallAPI api;
    LocationHelper lch;
    String charge,spec,size,grade,bundle,qty,weight,scan_mat,remark,qa_grade,rmd_date,barcheck,length,plant,station;
    String period,tab_hn,tab_id,scanresult,itxt,item_id;
    String eXcharge,eXbundle,eXadd_stamp,eXuser,exSize,exBarcode;
    final Context context = this;
    TextView tv_loc,tv_storage,tv_section,tv_bin;
    int erMl;

    TextView txt_sumcount,txt_sumqty,txt_sumweight;
    int sumcount=0,sumqty=0,sumweight=0;

    public int getErDup() {
        return erDup;
    }

    public void setErDup(int erDup) {
        this.erDup = erDup;
    }

    int erDup;

    public int getErCut() {
        return erCut;
    }

    public void setErCut(int erCut) {
        this.erCut = erCut;
    }

    int erCut;

    public int getErloc() {
        return erloc;
    }

    public void setErloc(int erloc) {
        this.erloc = erloc;
    }

    int erloc;

    static String sstorage;
    static String ssection;
    static String sbin ;
    static String st_sec ;

    public int getErNf() {
        return erNf;
    }

    public void setErNf(int erNf) {
        this.erNf = erNf;
    }

    public static String getSt_sec() {
        return st_sec;
    }

    public static void setSt_sec(String st_sec) {
        MainActivity.st_sec = st_sec;
    }

    public static String getSstorage() {
        return sstorage;
    }

    public static void setSstorage(String sstorage) {
        MainActivity.sstorage = sstorage;

    }

    public int getErMl() {
        return erMl;
    }


    public static String getSsection() {
        return ssection;
    }


    public static void setSsection(String ssection) {
        MainActivity.ssection = ssection;
    }
    public static String getSbin() {
        return sbin;
    }

    public static void setSbin(String sbin) {
        MainActivity.sbin = sbin;
    }


    public static String getRs() {
        return rs;
    }

    public static void setRs(String rs) {
        MainActivity.rs = rs;
    }

    static String rs = "" ;


    int erNf;

    int type;

    String typex = "";
    String typer = "";
    String typew = "";
    String r_remark  = "";

    ConnectionClass connectionClass;
    UserHelper usrHelper;
    LinearLayout top;
    static final int DATE_DIALOG_ID = 0;
    String idate,paramdate;

    List<Map<String, String>> foldlist  = new ArrayList<Map<String, String>>();

    Locale THLocale = new Locale("en", "TH");
    NumberFormat nf = NumberFormat.getInstance(THLocale);
    TextView tvx ;
    static String table = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        connectionClass = new ConnectionClass();

        usrHelper = new UserHelper(this);
        lch = new LocationHelper(MainActivity.this);
        loc = new MLocation(usrHelper.getPlant());
        api = new CallAPI();
        //sLine = new Line();

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


        if(type ==1 ){
            typex = ",type";
            typer = ",'C' ";
            typew = " and type = 'C' ";
            r_remark = "C";

        }else{
            typex = "";
            typer = "";
            typew = " and type IS NULL  ";
            r_remark = "F";

        }

        top = (LinearLayout)findViewById(R.id.top);
        tv_storage = (TextView)findViewById(R.id.tv_rmd);
        tv_section = (TextView)findViewById(R.id.tv_sec);
        tv_bin = (TextView)findViewById(R.id.tv_bin);
        tv_loc = (TextView)findViewById(R.id.tv_test);

        hideEdt = (EditText) findViewById(R.id.hedt);

        pbbar = (ProgressBar) findViewById(R.id.pbbar);
        tvx = (TextView) findViewById(R.id.textViewXr);
        mDateDisplay = (TextView) findViewById(R.id.mDateDisplayr);
        txt_sumcount =(TextView) findViewById(R.id.sumcount);
        txt_sumqty =(TextView) findViewById(R.id.sumqty);
        txt_sumweight =(TextView) findViewById(R.id.sumweight);
        btndelete = (Button) findViewById(R.id.btndelete);

        lvf = (ListView)findViewById(R.id.lvf);
        pbbar.setVisibility(View.GONE);

        if(usrHelper.getPlant().equals("SPS")){
            top.setVisibility(View.GONE);
        }

        if(type == 0 ){
            table = "tbl_fg_transform";
        }else{
            table = "tbl_fg_transform";
            tvx.setText("เหล็กตัด");
//            table = "tbl_fg_cut";
        }
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


        tv_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(0);


            }
        });

        tv_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect(1);

            }
        });

        tv_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPilClick();

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
        String fmat = "";

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
                onErrorDialog(getErDup(),getErNf(),getErCut(),getErloc(),getErMl(),rs);

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

                    String checkDup = "select * from "+table+" where item_barcode = '" + params[0].trim() + "' ";
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


                    String getData = "select rmd_period,rmd_charge,rmd_spec,case when rmd_spec = 'Coil' then rmd_grade+' '+rmd_size else rmd_size end as rmd_size,rmd_grade,r_bundle,r_qty,rmd_weight,rmd_qa_grade,bar_id,matcode,rmd_remark,rmd_date,rmd_length,rmd_plant,rmd_station from vw_barcode_item where bar_id = '" + params[0].trim() + "' ";
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
                        period = rs.getString("rmd_period");

                    }

                    String a_f[] = {"DBM","RBM"} ;
                    String a_c[] = {"AGM","CHN","BMH"} ;
                    String cutMat = scan_mat.substring(0,3);
                    if(type==1 && !Arrays.asList(a_c).contains(cutMat)){
                        isSuccess = false;
                        setErCut(1);
                    }else if(type==0 && !Arrays.asList(a_f).contains(cutMat)) {

                            isSuccess = false;
                            setErCut(2);

                    }else {


                        if (barcheck == null || barcheck.equals("")) {
                            setErNf(1);
                        } else {
                            setErNf(0);
                        }

                        if (remark == null) {
                            remark = "";
                        }
                        if(usrHelper.getPlant().equals("SPS")){
                            setErloc(0);
                        }else{
                            setErloc(1);
                            if (getRs().length() >= 9) {
                                setErloc(0);
                            }
                        }



                        if (getErDup() == 0 && getErNf() == 0 && getErloc() == 0) {
                            String irs = getRs();
                            if(usrHelper.getPlant().equals("SPS")){
                                irs = "1406";
                            }

                            String insrt = "insert into " + table + " (item_barcode,rmd_date,charge,bundle,qty,matcode,location,rmd_spec,rmd_size" +
                                    ",rmd_grade,rmd_length,rmd_weight,rmd_qa_grade,rmd_remark,rmd_plant,rmd_station,user_add,add_stamp " + typex + ",f_loc) " +
                                    " values ('" + params[0].trim() + "','" + rmd_date + "','" + charge + "','" + bundle + "','" + qty + "','" + scan_mat + "','" + usrHelper.getPlant() + "','" + spec + "','" + size + "','" + grade + "','" + length + "','" + weight + "','" + qa_grade + "','" + remark + "','" + plant + "','" + station.trim() + "','" + usrHelper.getUserName() + "_" + usrHelper.getVer() + "',current_timestamp" + typer + ",'" + irs + "')";
                            PreparedStatement preparedStatement = con.prepareStatement(insrt);

                            preparedStatement.executeUpdate();


                            String umat = "";
                            if (type == 0) {
                                if (spec == null || spec.equals("")) {
                                    umat = "";
                                }
                                if (spec.length() > 2) {
                                    if (spec.substring(0, 2).equals("RB") || spec.substring(0, 2).equals("DB")) {
                                        umat = ",rmd_spec = left(rmd_spec,2)+'F' ";
                                    }

                                }

                                 fmat = scan_mat.replaceFirst("M", "F");

                                String upd = "SET NOCOUNT ON  update tbl_production_scale " +
                                        "set rmd_mat = '" + fmat + "' "
                                        + "where bar_id = '" + params[0] + "' and rmd_spec = '" + spec + "' ";
                                PreparedStatement preparedF = con.prepareStatement(upd);
                                if (usrHelper.getPlant().equals("MMT")) {
                                    preparedF.executeUpdate();
                                } else {
                                    preparedF.executeQuery();
                                }


                            } else {

                                String g_mat = "dbo.getmatcode('" + spec + "','" + grade + "','" + size + "','" + length + "','" + qa_grade + "','" + period + "','" + plant + "')";
                                String upd = "update tbl_production_scale " +
                                        "set rmd_mat = " + g_mat + " " +
                                        "where bar_id = '" + params[0] + "' and rmd_spec = '" + spec + "' ";
                                PreparedStatement preparedF = con.prepareStatement(upd);
                                if (usrHelper.getPlant().equals("MMT")) {
                                    preparedF.executeUpdate();
                                } else {
                                    preparedF.executeQuery();
                                }

                            }
                            isSuccess = true;

                            HashMap<String,String> paramBin = new HashMap<String,String>();

                            if(usrHelper.getPlant().equals("SPS")){
                                paramBin.put("location", "06");
                                paramBin.put("storage", "SPS");
                                paramBin.put("section", "14");
                                paramBin.put("bin", "06");
                            }else{
                                paramBin.put("location", getRs().substring(4));
                                paramBin.put("storage", getRs().substring(0,3));
                                paramBin.put("section", getRs().substring(4,6));
                                paramBin.put("bin", getRs().substring(7,9));
                            }




                            paramBin.put("item_barcode", params[0].trim());
                            paramBin.put("charge", charge);
                            paramBin.put("bundle", bundle);
                            paramBin.put("qty", qty);
                            paramBin.put("weight", weight);
                            paramBin.put("rmd_grade", grade);
                            paramBin.put("qa_grade", qa_grade);
                            paramBin.put("remark", r_remark);
                            paramBin.put("user_add", usrHelper.getUserName());
                            paramBin.put("rmd_date", rmd_date);
                            paramBin.put("flag", "NULL");
                            paramBin.put("batch", api.getBatch(charge,bundle));
                            paramBin.put("mat", fmat);



                            api.postBin(api.setBody(paramBin));


                        } else {
                            isSuccess = false;

                        }

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

                    String query = "update "+table+" set qty = '"+params[0]+"' ,  weight = '"+params[2]+"' ," +
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


    public void onErrorDialog(int eDup ,int notFound,int cut, int eLoc, final int eMl,String rsx) {
        String msg = "";

        if (eMl == 1) {
            msg = "ไม่มี " + rsx + " ในระบบ \nกรุณาเลือกใหม่";

        }else{



        if (scanresult == null || scanresult.equals("")) {
            scanresult = "PICK";
        }
        if (eDup == 0 && notFound == 0 && cut ==0) {
            msg = itxt + "\n\n" + "Code " + scanresult;
        }

            if (notFound == 1) {
                msg = "ไม่พบข้อมูลที่สแกน !" + "\n\n" + "Code " + scanresult;
            } else {

                if (eDup == 1) {

                    msg = "ซ้ำ HN " + eXcharge + "-" + eXbundle + "\nสินค้า : " + exSize + "\nโดย " + eXuser.substring(0, eXuser.length() - 4) + "\nวันที่ " + eXadd_stamp.substring(0, 16) + "\n\n" + "Code " + scanresult;
                }
                else if(cut ==1){
                    msg = "ไม่สามรถตัดสินค้า " +scan_mat.substring(0,3)+ "\n\n" + "Code " + scanresult;
                }
                else if(cut ==2){
                    msg = "ไม่สามารถพับสินค้า " +scan_mat.substring(0,3)+  "\n\n" + "Code " + scanresult;
                }
                else if (eLoc == 1) {
                    msg = "กรุณาเลือก ช่อง-เสาก่อนยิง";
                }
                else{
                    msg = itxt + "\n\n" + "Code " + scanresult;
                }
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

                    String where = "where convert(nvarchar(30),add_stamp,112) = '"+params[0].trim()+"' and location = '"+usrHelper.getPlant()+"' "+typew+" ";

                    String sql = "Select id,charge,bundle,qty,rmd_qa_grade,rmd_size,rmd_remark,rmd_weight,matcode  from "+table+" " +where +" order by add_stamp desc ";
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

                    String sumq = "SELECT count(item_barcode) AS sc ,sum(qty) as sq ,sum(rmd_weight) AS sw  FROM "+table+"  "+where ;
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

                    String query = "delete "+table+" WHERE id = "+params[0]+"  ";
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
        Toast.makeText(MainActivity.this, "id "+this.idate+"\n"+"pd "+this.paramdate, Toast.LENGTH_LONG).show();
    }

    public void UpdateSection(String txt){

        tv_section.setText(txt);
        getResult();

    }

    public void UpdateLoc(String txt){


        tv_loc.setText(txt);

    }

    public void UpdateStorage(String txt){

        tv_storage.setText(txt);
        getResult();


    }



    public void UpdateBin(String txt){
        tv_bin.setText(txt);
        getResult();


    }

    public void setErMl(int erMl) {
        this.erMl = erMl;
    }


    private void getResult(){

        //rs = isNull(getSstorage())+"-"+isNull(getSt_sec())+"-"+isNull(getSbin());


            UpdateLoc(isNull(getSstorage())+"-"+isNull(getSt_sec())+"-"+isNull(getSbin()));


        setRs(isNull(getSstorage())+"-"+isNull(getSt_sec())+"-"+isNull(getSbin()));


        Boolean isfound = lch.StorageList.contains(getRs());
        if(isfound == true){
            tv_loc.setBackgroundColor(Color.parseColor("#21DF86"));
            setErMl(0);
        }else{
            tv_loc.setBackgroundColor(Color.parseColor("#ffff4444"));
            setErMl(1);
            if(rs.length()>=9){
                onErrorDialog(getErDup(),getErNf(),getErCut(),getErloc(),getErMl(),rs);
            }

        }


    }


    public  void  onPilClick(){

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pil_picker);
        dialog.setCancelable(true);

        final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
        Button btnSv = (Button)dialog.findViewById(R.id.btnSv);

        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pil = edtPil.getText().toString();

                setSbin(lch.fPill(pil));
                UpdateBin(pil);

               /* fillList = new FillList();
                fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),isNull(getSbin()));*/
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void onSelect(final int t){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.AlertDialogCustom));
        String head ="";

        String[] sec_set = lch.section_d.toArray(new String[lch.section_d.size()]);
        String[] rmd_set = lch.storages.toArray(new String[lch.storages.size()]);
        final String[] rmd_sections = lch.sections.toArray(new String[lch.sections.size()]);
        String[] set = {""};

        switch (t){
            case 0 : head ="ตำแหน่งเก็บ"; set = rmd_set;
                break;
            case 1 : head ="ช่อง"; set = sec_set;
                break;
        }

        builder.setTitle(head);


        final String[] finalSet = set;
        builder.setItems(set, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity stock = new MainActivity();
                String wsec = "";
                if(t ==1){

                    wsec = rmd_sections[which];

                    setSsection(finalSet[which]);
                    setSt_sec(wsec);
                    tv_section.setText((finalSet[which]));
                    UpdateSection(finalSet[which]);


                /*        fillList = new FillList();
                        fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),"");*/




                }else{

                    setSstorage(finalSet[which]);
                    tv_storage.setText(finalSet[which]);
                    UpdateStorage((finalSet[which]));

/*
                        fillList = new FillList();
                        fillList.execute(isNull(getSstorage()),isNull(getSt_sec()),"");*/

                }

                //getResult();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String isNull(String str){
        if(str==null){
            return "";
        }else{
            return str ;
        }

    }

}
