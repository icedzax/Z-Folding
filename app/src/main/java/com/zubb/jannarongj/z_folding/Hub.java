package com.zubb.jannarongj.z_folding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Hub extends Activity {

    TextView qad,fold,saw;
    UserHelper usrHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        usrHelper = new UserHelper(this);



        qad = (TextView)findViewById(R.id.qad);
        fold = (TextView)findViewById(R.id.fold);
        saw = (TextView)findViewById(R.id.saw);
        saw.setVisibility(View.GONE);

       /* if(Integer.parseInt(usrHelper.getLevel()) < 5){
            qad.setVisibility(View.GONE);
            Intent i = new Intent(Hub.this, MainActivity.class);
            startActivity(i);
            finish();
        }*/

        if(usrHelper.getPlant().equals("MMT")){
            saw.setVisibility(View.VISIBLE);
        }

        if(Integer.parseInt(usrHelper.getLevel()) < 5){
            qad.setVisibility(View.GONE);
            if(!usrHelper.getPlant().equals("MMT")){
                Intent i = new Intent(Hub.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }else{
            qad.setVisibility(View.VISIBLE);
        }

        saw.setVisibility(View.VISIBLE);



        fold.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Hub.this, MainActivity.class);
                i.putExtra("type",0);
                startActivity(i);
            }
        });

        saw.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent i = new Intent(Hub.this, MainActivity.class);
                i.putExtra("type",1);
                startActivity(i);
            }
        });

        qad.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Hub.this, AdjLine.class);
                startActivity(i);
            }
        });
    }

}
