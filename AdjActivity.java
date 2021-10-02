package com.zubb.jannarongj.z_folding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjActivity extends AppCompatActivity {

    final Context context = this;
    String get_bar_id,user,ver;
    UserHelper usrHelper ;
    ConnectionClass connectionClass;
    EditText hideEdt,qtynum,wenum;
    ProgressBar pbbar ;
    Button qtySave,qtyCancel,btnSave;
    TextView txtVbeln,txtHn,txtQaGrade,txtRemark,txtWeight,txtLength,txtQty,txtType,txtDate,txtPeriod,txtSize,oldWe,oldqty;
    ListView lv_vbeln,lv_adj;
    int rmd_weight=0,rmd_qty=0,maxno;

    String g_barid,srmdmat,prmdmat,grademat,upmat,r_rmd_date,scanresult,rmd_qa_grade,rmd_size,rmd_date,rmd_hn,rmd_remark,rmd_period,rmd_length,rmd_grade,rmd_spec,matcode,rmd_id,rmd_no,rmd_charge,rmd_bundle,item_barcode;

    List<Map<String, String>> vbelnlist  = new ArrayList<Map<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adj);

        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        hideEdt = (EditText) findViewById(R.id.hedt);
        pbbar = (ProgressBar) findViewById(R.id.pbbar);

        user =  usrHelper.getUserName();
        ver = usrHelper.getVer();
        pbbar.setVisibility(View.GONE);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                g_barid = null;

            } else {
                g_barid = extras.getString("bar_id").trim();

            }
        } else {

            g_barid = (String) savedInstanceState.getSerializable("bar_id");
        }


        hideEdt.requestFocus();

        hideEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            String demo ="";
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    if(hideEdt.getText().toString().trim().contains("DEMO")){
                        demo = hideEdt.getText().toString().trim().replace("DEMO","").replace("*","").replaceAll("\r", "").replaceAll("\t", "");

                    }else{
                        demo = hideEdt.getText().toString().trim().replace("*","").replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
                    }
                    insertSCAN(demo);
                    //getLocation();

                }

                return false;

            }

        });

        btnSave =(Button) findViewById(R.id.btnSave);
        txtSize = (TextView) findViewById(R.id.txtSize);
        txtVbeln = (TextView) findViewById(R.id.txtVbeln);
        txtHn = (TextView) findViewById(R.id.txtHn);
        txtQaGrade = (TextView) findViewById(R.id.txtQaGrade);
        txtRemark = (TextView) findViewById(R.id.txtRemark);
        txtWeight = (TextView) findViewById(R.id.txtWeight);
        txtLength = (TextView) findViewById(R.id.txtLength);
        txtQty = (TextView) findViewById(R.id.txtQty);
        txtType = (TextView) findViewById(R.id.txtType);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtPeriod = (TextView) findViewById(R.id.txtPeriod);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                get_bar_id = null;

            } else {
                get_bar_id = extras.getString("bar_id");

            }

        } else {
            get_bar_id = (String) savedInstanceState.getSerializable("bar_id");

        }

        FillList fillList = new FillList();
        fillList.execute(get_bar_id);
        this.item_barcode = get_bar_id;


        btnSave.setEnabled(false);
        btnSave.setText("เลือกประเภทการปรับเกรด");

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdjActivity.this);
                builder.setTitle("ยืนยันการบันทึก");
                builder.setMessage("ต้องการยืนยันการปรับเกรดหรือไม่");
                builder.setIcon(R.drawable.ic_alert);
                builder.setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        SaveAdj savex = new SaveAdj();
                        savex.execute("");

                    }

                });
                builder.show();

            }
        });

    }

    public class FillList extends AsyncTask<String, String, String> {

        String z = "";
        String i = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            if(isSuccess==true){
                txtQaGrade.setText(rmd_qa_grade);
                txtSize.setText(rmd_size);
                txtWeight.setText(""+rmd_weight);
                txtDate.setText(rmd_date);
                txtHn.setText(rmd_hn);
                txtRemark.setText(rmd_remark);
                txtPeriod.setText(rmd_period);
                txtLength.setText(rmd_length);
                txtQty.setText(""+rmd_qty);
            }else{
                matMisMatch(i);
            }

            pbbar.setVisibility(View.GONE);

        }
        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con =  connectionClass.CONN();

                if (con == null) {
                    z = "Network มีปัญหา\nกรุณาตรวจสอบ WIFI หรือ แจ้ง IT 3201";
                } else {

                    String query = "select CONVERT(VARCHAR(30),rmd_date,103) as rmd_date2,rmd_charge+'-'+convert(nvarchar(10),r_bundle) as hn,* from vw_barcode_item  where bar_id = '"+params[0]+"' " ;

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        rmd_qty = rs.getInt("r_qty");
                        rmd_weight = rs.getInt("rmd_weight");
                        rmd_charge = rs.getString("rmd_charge");
                        rmd_bundle = rs.getString("r_bundle");
                        rmd_spec = rs.getString("rmd_spec");
                        rmd_grade = rs.getString("rmd_grade");
                        rmd_hn = rs.getString("hn");
                        rmd_date = rs.getString("rmd_date2");
                        r_rmd_date = rs.getString("rmd_date");
                        rmd_qa_grade = rs.getString("rmd_qa_grade");
                        rmd_remark = rs.getString("rmd_remark");
                        rmd_size = rs.getString("rmd_size");
                        rmd_period = rs.getString("rmd_period");
                        rmd_length = rs.getString("rmd_length");
                        rmd_id =  rs.getString("rmd_id");
                        rmd_no = rs.getString("rmd_no");
                        matcode = rs.getString("matcode");


                    }

                    String pquery = "select rmd_mat from tbl_production_scale  where bar_id = '"+params[0]+"' " ;

                    PreparedStatement pps = con.prepareStatement(pquery);
                    ResultSet prs = pps.executeQuery();

                    while (prs.next()) {
                        prmdmat = prs.getString("rmd_mat");
                    }

                    if(rmd_remark==null || rmd_remark.equals("")){
                        rmd_remark = "";
                    }


                    String vbelnqry = "  select distinct(vbeln),ar_name,WADAT_IST from tbl_shipmentplan where WADAT_IST > getdate() -1 " ;
                    PreparedStatement vps = con.prepareStatement(vbelnqry);
                    ResultSet vrs = vps.executeQuery();

                    vbelnlist.clear();
                    while (vrs.next()) {
                        Map<String, String> datavbeln = new HashMap<String, String>();
                        datavbeln.put("vbeln", vrs.getString("vbeln") );
                        datavbeln.put("arname", vrs.getString("ar_name") );
                        vbelnlist.add(datavbeln);
                        //Collections.copy(vbelnlist, templist);

                    }
                    isSuccess = true;
                    z = "Success";
                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";

                isSuccess = false;
                if(rmd_id == null) {
                    i = "N";
                }else {
                    i = ex.getMessage().toString();
                }

            }
            return z;
        }
    }

    public void onQtyClick(View v) {

        final Dialog dialog = new Dialog(AdjActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogqty);
        dialog.setCancelable(true);

        oldWe = (TextView) dialog.findViewById(R.id.oldWe);
        oldqty = (TextView) dialog.findViewById(R.id.Oldqty);

        qtynum = (EditText)dialog.findViewById(R.id.qtynum);
        wenum = (EditText)dialog.findViewById(R.id.wenum);
        qtySave  = (Button) dialog.findViewById(R.id.save);

        qtySave.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                String qtyvalue,wevalue;
                qtyvalue = qtynum.getText().toString();
                wevalue = wenum.getText().toString();

                update(qtyvalue,wevalue);
                dialog.dismiss();
            }
        });

        qtyCancel  = (Button) dialog.findViewById(R.id.cancel);
        qtyCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.cancel();
            }
        });

        oldWe.setText("" + rmd_weight);
        oldqty.setText(""+ rmd_qty);


        dialog.show();
    }

    public void update(String we , String qty){
        txtQty.setText(qty);
        txtWeight.setText(we);

    }

    public class SaveAdj extends AsyncTask<String, String, String> {
        String z = "";
        int iRMD = 99;

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(AdjActivity.this, r, Toast.LENGTH_SHORT).show();

            /*FillList fillList = new FillList();
            fillList.execute(item_barcode);*/
            Intent i = new Intent(AdjActivity.this, AdjLine.class);
            finish();
            startActivity(i);

            typeCheck("");


        }

        @Override
        protected String doInBackground(String... params) {


            try {
                Connection con = connectionClass.CONN();

                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String getmaxno = "select max(adj_no)+1 as maxno from  tbl_adj";
                    PreparedStatement gmn = con.prepareStatement(getmaxno);
                    ResultSet grs = gmn.executeQuery();
                    while (grs.next()) {
                        maxno = grs.getInt("maxno");
                    }


                    String Insert2 = saveSt(2);
                    PreparedStatement ipreparedStatement = con.prepareStatement(Insert2);
                    ipreparedStatement.executeUpdate();

                    String Insert1 = saveSt(1);
                    PreparedStatement ipreparedStatement2 = con.prepareStatement(Insert1);
                    ipreparedStatement2.executeUpdate();

                    String Update0 = saveSt(0);
                    PreparedStatement ipreparedStatement0 = con.prepareStatement(Update0);
                    ipreparedStatement0.executeUpdate();


                    z = "บันทึกเรียบร้อยแล้ว";
                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";

            }
            return z;
        }
    }

    public String saveSt(int i){
        String i_qa_grade,i_qty,i_weight,i_adj_type,i_do,SQL="" ;

        i_qa_grade = this.txtQaGrade.getText().toString();
        i_qty  = this.txtQty.getText().toString();
        i_weight = this.txtWeight.getText().toString();
        i_adj_type = this.txtType.getText().toString();
        i_do = this.txtVbeln.getText().toString();

        switch (i_qa_grade){
            case "A" :grademat = "1";
                break;
            case "A-" :grademat = "2";
                break;
            case "A2" :grademat = "3";
                break;
            case "QI" :grademat = "1";
                break;
            case "R" :grademat = "4";
                break;
        }

        if(matcode==null || matcode.equals("") || matcode.length()<4){
            upmat = "NULL";
        }else{

            upmat = "'"+matcode.substring(0,3)+""+grademat+""+matcode.substring(4,matcode.length())+"'";
        }

        if(prmdmat==null || prmdmat.equals("") || prmdmat.length()<4){
            srmdmat = " ";
        }else{
            srmdmat = " ,rmd_mat = "+upmat+"  ";
        }


        switch (i){
            case 1 : SQL = "insert into tbl_adj (adj_no,vbeln,rmd_adj_type,rmd_adj_time,rmd_adj_check,rmd_id,rmd_bar_id,rmd_date,rmd_no,rmd_charge,rmd_period,rmd_spec,rmd_size,rmd_grade,rmd_qty1,rmd_qty2,rmd_qty3,rmd_length,rmd_weight,rmd_qa_grade,rmd_remark,rmd_imgno,rmd_check,rmd_user,rmd_matcode)" +
                    " VALUES("+maxno+",'" + i_do + "','S',CURRENT_TIMESTAMP,'1','"+rmd_id+"','" + item_barcode + "','" + r_rmd_date + "','" + rmd_no + "','" + rmd_charge + "','" + rmd_period + "','" + rmd_spec + "','" + rmd_size + "','"+rmd_grade+"','" + rmd_qty + "','" + rmd_bundle + "','"+i_qty+"','" + rmd_length + "','" + i_weight + "','" + i_qa_grade + "','" + i_adj_type + "',NULL,NULL,'" + user + "'," + upmat + ")";
                break;
            case 2 : SQL = "insert into tbl_adj (adj_no,vbeln,rmd_adj_type,rmd_adj_time,rmd_adj_check,rmd_id,rmd_bar_id,rmd_date,rmd_no,rmd_charge,rmd_period,rmd_spec,rmd_size,rmd_grade,rmd_qty1,rmd_qty2,rmd_qty3,rmd_length,rmd_weight,rmd_qa_grade,rmd_remark,rmd_imgno,rmd_check,rmd_user,rmd_matcode)" +
                    " VALUES("+maxno+",'" + i_do + "','S',CURRENT_TIMESTAMP,'0','"+rmd_id+"','" + item_barcode + "','" + r_rmd_date + "','" + rmd_no + "','" + rmd_charge + "','" + rmd_period + "','" + rmd_spec + "','" + rmd_size + "','"+rmd_grade+"','" + rmd_qty + "','" + rmd_bundle + "','"+rmd_qty+"','" + rmd_length + "','" + rmd_weight + "','" + rmd_qa_grade + "','" + rmd_remark +"',NULL,NULL,'" + user + "','" + matcode + "')";
                break;
            case 0 : SQL = "update tbl_production_scale set rmd_qa_grade = '"+i_qa_grade+"' , rmd_qty3 = '"+i_qty+"' ,rmd_weight = '"+i_weight+"' , rmd_remark = '"+i_adj_type+"' "+srmdmat+" where bar_id = '"+item_barcode+"' and rmd_id = '"+rmd_id+"' ";
                break;
        }

        return SQL ;
    }

    public  void  onVBELNclick(View v){

        final Dialog dialog = new Dialog(AdjActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialogvbeln);

        dialog.setCancelable(true);
        Button pick = (Button)dialog.findViewById(R.id.btnPick);

        pick.setOnClickListener(new View.OnClickListener() {
            EditText doEdt = (EditText) dialog.findViewById(R.id.doEdt);
            public void onClick(View v) {
                txtVbeln.setText(doEdt.getText().toString().trim());
                dialog.dismiss();
            }
        });

        lv_vbeln = (ListView) dialog.findViewById(R.id.lvbeln);
        String[] from = {"vbeln", "arname"};
        int[] views = {R.id.r1, R.id.r2};
        final SimpleAdapter ADA = new SimpleAdapter(AdjActivity.this,
                vbelnlist, R.layout.adp_list_vbeln, from,
                views){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1) //TODO get id to value and assign color row
                {
                    view.setBackgroundColor(Color.parseColor("#D6EAF8"));
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#A9CCE3"));
                }
                return view;
            }
        };

        lv_vbeln.setAdapter(ADA);
        lv_vbeln.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                        .getItem(arg2);
                String svbeln = (String) obj.get("vbeln");

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2000);
                arg1.startAnimation(animation1);
                dialog.dismiss();
                txtVbeln.setText(svbeln);

            }

        });

        dialog.show();
    }

    public void onAdjTypeClick(View v){

        List<Map<String, String>> typelist  = new ArrayList<Map<String, String>>();
        String[] values  = new String[] {"ปรับเพื่อขาย","ปรับล่วงหน้า","ปรับเส้นขาด-เกิน","ปรับน้ำหนักขาด-เกิน","ปรับสินค้าขึ้นสลับ","ปรับเข้า QI รอตรวจ","ปรับออก QI ตรวจซ้ำแล้ว","ปรับเกรด","ปรับเหล็กพับ","ปรับรวมมัด"};
        for(int i=0;i<values.length;i++){
            Map<String, String> datatype = new HashMap<String, String>();
            datatype.put("t1",values[i]);
            typelist.add(datatype);
        }

        final Dialog dialog = new Dialog(AdjActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialogadjtype);
        dialog.setCancelable(true);


        lv_adj = (ListView) dialog.findViewById(R.id.lv_adj);
        String[] from = {"t1"};
        int[] views = {R.id.t1};
        final SimpleAdapter ADA = new SimpleAdapter(AdjActivity.this,
                typelist, R.layout.adp_list_adj, from,
                views){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1) //TODO get id to value and assign color row
                {
                    view.setBackgroundColor(Color.parseColor("#D6EAF8"));
                }
                else
                {
                    view.setBackgroundColor(Color.parseColor("#A9CCE3"));
                }
                return view;
            }
        };

        lv_adj.setAdapter(ADA);
        lv_adj.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                        .getItem(arg2);
                String adjtxt = (String) obj.get("t1");

                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2000);
                arg1.startAnimation(animation1);
                dialog.dismiss();
                typeCheck(adjtxt);
            }

        });
        dialog.show();
    }

    public void onQaClick(View v) {
        final CharSequence[] items = {"A", "A2", "A-",
                "QI", "R"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AdjActivity.this);
        builder.setTitle("แก้เกรด");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                txtQaGrade.setText(items[item]);
            }
        });
        builder.show();
    }


    public void typeCheck(String type){
        if(type.equals("เลือกประเภทการปรับเกรด") || type.equals("ประเภทการปรับเกรด")){
            type ="";
        }

        if(type.equals("") || type.equals("ประเภทการปรับเกรด") || type.equals("เลือกประเภทการปรับเกรด")){
            //แดง

            txtType.setBackgroundColor(Color.parseColor("#FF9393"));
            txtType.setTextColor(Color.parseColor("#E80000"));

            btnSave.setBackgroundColor(Color.parseColor("#FF9393"));
            btnSave.setTextColor(Color.parseColor("#E80000"));
            btnSave.setText("เลือกประเภทการปรับเกรด");
            btnSave.setEnabled(false);
            type ="เลือกประเภทการปรับเกรด";

        }else{
            //เชัียวจัด
            txtType.setBackgroundColor(Color.parseColor("#CDFFDC"));
            txtType.setTextColor(Color.parseColor("#00932C"));

            btnSave.setBackgroundColor(Color.parseColor("#00d34d"));
            btnSave.setTextColor(Color.parseColor("#000000"));
            btnSave.setText("SAVE");
            btnSave.setEnabled(true);

        }

        this.txtType.setText(type);

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent KEvent)
    {
        int keyaction = KEvent.getAction();

        if(keyaction == KeyEvent.ACTION_DOWN)
        {
            int keycode = KEvent.getKeyCode();

            //int keyunicode = KEvent.getUnicodeChar(KEvent.getMetaState());
            //char character = (char) keyunicode;
            //Toast.makeText(this,character+"-"+keycode,Toast.LENGTH_SHORT).show();

            if(keycode == 120 || keycode == 520){
                hideEdt.requestFocus();
            }
        }
        return super.dispatchKeyEvent(KEvent);
    }

    public void insertSCAN(String rsscan){

        if(rsscan.length()>0){
            this.scanresult = rsscan;

            FillList fillList = new FillList();
            fillList.execute(rsscan);
            this.item_barcode = rsscan;


            this.hideEdt.setText("");
        }else{
            this.hideEdt.setText("");
        }
        this.hideEdt.setText("");

    }


    public void matMisMatch(String in) {
        String msg = null;

        if(in.equals("N")){
            msg = "ไม่พบข้อมูล"+"\n "+scanresult+"";
        }else{
            msg = in+"\n "+scanresult+"";
        }

        new AlertDialog.Builder(context)

                .setTitle("ผิดพลาด")
                .setMessage(msg)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // continue with delete
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

}
