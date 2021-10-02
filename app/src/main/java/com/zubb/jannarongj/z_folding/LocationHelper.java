package com.zubb.jannarongj.z_folding;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocationHelper {

    private String rs;
    private Context context;

    public static List<String> StorageList = new ArrayList<String>();

    public static List<String> section_d = new ArrayList<String>();
    public static List<String> sections = new ArrayList<String>();

    public static List<String> storages = new ArrayList<String>();



    public LocationHelper(Context context){
        this.context = context;
    }

    public void UpdateStorage(String txt){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.tv_rmd);
        txtView.setText(txt);
        getResult();

    }

    public void UpdateSection(String txt){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.tv_sec);
        txtView.setText(txt);
        getResult();


    }


    public void UpdateBin(String txt){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.tv_bin);
        txtView.setText(txt);
        getResult();

    }

    public void UpdateLoc(String txt){
        TextView txtView = (TextView) ((Activity)context).findViewById(R.id.tv_test);
        txtView.setText(txt);
    }





    private String textSec(String str){
        str = isNull(str);
        switch (str){
            case "F" : str = "หน้า";
                break;
            case "R" : str = "หลัง";
                break;
        }
        return str;
    }

    private String textSite(String str){
        str = isNull(str);
        switch (str){
            case "R" : str = "ขวา";
                break;
            case "L" : str = "ซ้าย";
                break;
        }
        return str;
    }


    public void onSelect(final int t){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        String head ="";
        String[] sec_set = {"1 ซ้าย","1 ขวา","3 ซ้าย","3 ขวา","4 ซ้าย","4 ขวา","5 ซ้าย","5 ขวา"};
        String[] rmd_set = {"MR8","MR7"};
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
                    switch (which){
                        case 0 : wsec = "1L";
                            break;
                        case 1 : wsec = "1R";
                            break;
                        case 2 : wsec = "3L";
                            break;
                        case 3 : wsec = "3R";
                            break;
                        case 4 : wsec = "4L";
                            break;
                        case 5 : wsec = "4R";
                            break;
                        case 6 : wsec = "5L";
                            break;
                        case 7 : wsec = "5R";
                            break;
                    }

                    stock.setSsection(finalSet[which]);
                    stock.setSt_sec(wsec);
                    UpdateSection(finalSet[which]);

                }else{

                    stock.setSstorage(finalSet[which]);
                    UpdateStorage(finalSet[which]);
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

    public String fPill(String p){
        String formatted = p ;
        if(p!=null && p.length()==1){
            formatted = "0"+p;
        }
        return  formatted ;
    }

    private void getResult(){

        MainActivity stock  = new MainActivity();

        rs = isNull(stock.getSstorage())+"-"+isNull(stock.getSt_sec())+"-"+isNull(stock.getSbin());
        stock.setRs(rs);
        UpdateLoc(rs);

    }


    public  void  onPilClick(){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pil_picker);
        dialog.setCancelable(true);

        final EditText edtPil = (EditText)dialog.findViewById(R.id.edtPil);
        Button btnSv = (Button)dialog.findViewById(R.id.btnSv);

        btnSv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pil = edtPil.getText().toString();

                MainActivity stock  = new MainActivity();
                stock.setSbin(fPill(pil));
                UpdateBin(pil);

                dialog.dismiss();
            }
        });

        dialog.show();

    }



}
