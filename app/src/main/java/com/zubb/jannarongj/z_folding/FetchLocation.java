package com.zubb.jannarongj.z_folding;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchLocation {


    ConnectionClass connectionClass;
    String errorText ;


    public List getLocation(String plant){

        connectionClass = new ConnectionClass();
        List<Map<String, String>> locationlist  = new ArrayList<Map<String, String>>();

        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                errorText = "พบปัญหาการเชื่อมต่อ";
            } else {

                String g = "";
                String stmt = "select  * from tbl_location_n where Storage_Section = '"+plant+"' ";
                PreparedStatement getloc = con.prepareStatement(stmt);
                ResultSet rs = getloc.executeQuery();

                locationlist.clear();
                while (rs.next()) {
                    Map<String, String> datacar = new HashMap<String, String>();
                    datacar.put("loc", rs.getString("name"));
                    datacar.put("Storage_Bin", rs.getString("Storage_Bin"));

                    locationlist.add(datacar);

                }

            }

        } catch (Exception ex) {

            errorText = ex.getMessage();
        }

        return locationlist;
    }

}
