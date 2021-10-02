package com.zubb.jannarongj.z_folding;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jannarong.j on 5/1/2560.
 */
public class ConnectionClass {

    public static String getUip() {
        return uip;
    }

    public static void setUip(String uip) {
        ConnectionClass.uip = uip;
    }

    public static String getUpass() {
        return upass;
    }

    public static void setUpass(String upass) {
        ConnectionClass.upass = upass;
    }

    public static String uip ;

    public static String getUdbn() {
        return udbn;
    }

    public static void setUdbn(String udbn) {
        ConnectionClass.udbn = udbn;
    }

    public static String udbn ;
    public static String upass ;


    String ip = "192.168.100.222";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "PP";
    String un = "sa";
    String password = "";



    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String ConnURL = null;

        try {

            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + getUip() + ";"
                    + "databaseName=" + getUdbn() + ";user=" + un + ";password="
                    + getUpass() + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERROR", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }



}