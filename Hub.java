package com.zubb.jannarongj.z_folding;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Hub extends AppCompatActivity {

    TextView qad,fold;
    UserHelper usrHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);
        usrHelper = new UserHelper(this);



        qad = (TextView)findViewById(R.id.qad);
        fold = (TextView)findViewById(R.id.fold);

        if(Integer.parseInt(usrHelper.getLevel()) < 5){
            qad.setVisibility(View.GONE);
            Intent i = new Intent(Hub.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        fold.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent i = new Intent(Hub.this, MainActivity.class);
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
