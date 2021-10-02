package com.zubb.jannarongj.z_folding;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends AppCompatActivity {

    ConnectionClass connectionClass;
    String username ,id,gPass,gver,plant,level;
    UserHelper usrHelper ;
    Button btnLogin ,b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,bd;
    ProgressBar pbbar;
    TextView izeText,ver ;
    Version vers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vers = new Version();
        usrHelper = new UserHelper(this);
        connectionClass = new ConnectionClass();
        izeText = (TextView) findViewById(R.id.izetext);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        ver = (TextView) findViewById(R.id.ver);
        pbbar = (ProgressBar)findViewById(R.id.pbbar);
        b1 = (Button) findViewById(R.id.b1); b6 = (Button) findViewById(R.id.b6);
        b2 = (Button) findViewById(R.id.b2); b7 = (Button) findViewById(R.id.b7);
        b3 = (Button) findViewById(R.id.b3); b8 = (Button) findViewById(R.id.b8);
        b4 = (Button) findViewById(R.id.b4); b9 = (Button) findViewById(R.id.b9);
        b5 = (Button) findViewById(R.id.b5); b0 = (Button) findViewById(R.id.b0);
        bd = (Button) findViewById(R.id.bd);
        pbbar.setVisibility(View.GONE);
        gver = vers.Version;
        ver.setText("ver : "+gver+" ");
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gPass = izeText.getText().toString();
                Base base = new Base();
                base.execute("");
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "1");
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "2");
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "3");
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "4");
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "5");
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "6");
            }
        });
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "7");
            }
        });
        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "8");
            }
        });
        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "9");
            }
        });
        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                izeText.setText(izeText.getText() + "0");
            }
        });

        bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str=izeText.getText().toString();
                if (str.length() >=1 ) {
                    str = str.substring(0, str.length() - 1);
                    izeText.setText(str);
                }

            }
        });


    }



    public class Base extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

            pbbar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String r) {

            pbbar.setVisibility(View.GONE);
            if (gPass.equals(id)){
                usrHelper.createSession(username,id,plant,level);
                Intent i = new Intent(Login.this, Hub.class);
                startActivity(i);
                finish();
            }else{
                if(isSuccess==true){
                    Toast.makeText(Login.this,"รหัสไม่ถูกต้อง",Toast.LENGTH_LONG).show();
                    izeText.setText("");
                }else{
                    Toast.makeText(Login.this,z,Toast.LENGTH_LONG).show();
                    // izeText.setText("");
                }

            }

        }
        @Override
        protected String doInBackground(String... params) {

            try {

                Connection con =  connectionClass.CONN();

                if (con == null) {
                    z = "Network มีปัญหา\nกรุณาตรวจสอบ WIFI";
                } else {

                    String query = "select *,isnull(lv,0) as lvl from tbl_id_user where Id ='"+gPass+"'  " ;
                    PreparedStatement ts = con.prepareStatement(query);
                    ResultSet bs = ts.executeQuery();
                    while (bs.next()) {

                        username = bs.getString("name");
                        id = bs.getString("Id");
                        plant = bs.getString("plant");
                        level = bs.getString("lvl");

                    }
                    isSuccess = true;
                    z = "Success";
                }
            } catch (Exception ex) {
                z = ex.getMessage();//"Error retrieving data from table";
                isSuccess = false;
            }
            return z;
        }
    }
    @Override
    public void onBackPressed()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("ออกจากโปรแกรม");
        builder.setMessage("ต้องการออกจากโปรแกรมหรือไม่");
        builder.setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                usrHelper.deleteSession();

                finish();
            }
        });
        builder.show();
    }


}
