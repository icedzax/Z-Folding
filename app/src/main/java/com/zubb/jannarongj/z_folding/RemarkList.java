package com.zubb.jannarongj.z_folding;


import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class RemarkList {

    ConnectionClass  connectionClass;

    public List getLocation(String plant){

        connectionClass = new ConnectionClass();
        List<Map<String, String>> locationlist  = new ArrayList<Map<String, String>>();

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
               // errorText = "พบปัญหาการเชื่อมต่อ";
            } else {
                String where = "";
                if(plant.equals("OCP")){
                    where = " where plant = 'OCP' ";
                }else{
                    where = " where plant <> 'OCP' ";
                }
                String stmt = "select  * from tbl_remark  "+where ;
                Log.d("remark",stmt);
                PreparedStatement getloc = con.prepareStatement(stmt);
                ResultSet rs = getloc.executeQuery();

                locationlist.clear();
                while (rs.next()) {
                    Map<String, String> datacar = new HashMap<String, String>();
                    datacar.put("t1", rs.getString("remark"));
                    locationlist.add(datacar);

                }

            }

        } catch (Exception ex) {

           // errorText = ex.getMessage();
        }

        return locationlist;
    }

}
