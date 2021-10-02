package com.zubb.jannarongj.z_folding;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class CallAPI {

    private static String LOG_TAG = "LINE";

    public void postBin( String body){

        String url ="https://hook.zubbsteel.com/line-ci/binapi/receive";
        url = url.replaceAll("\\s","");
        new MakeNetworkCall().execute(url, "Post",body);
    }

    public void postDelBin( String body){

        String url ="https://hook.zubbsteel.com/line-ci/binapi/receive";
        url = url.replaceAll("\\s","");
        new MakeNetworkCall().execute(url, "Post",body);
    }

    InputStream ByGetMethod(String ServerURL) {
        InputStream DataInputStream = null;
        try {

            URL url = new URL(ServerURL);
            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            cc.setReadTimeout(8000);
            cc.setConnectTimeout(8000);
            cc.setRequestMethod("GET");
            cc.setDoInput(true);
            cc.connect();


            int response = cc.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in GetData", e);
        }
        return DataInputStream;

    }

    InputStream ByPostMethod(String ServerURL, String Param_p) {

        InputStream DataInputStream = null;
        try {

            String PostParam = Param_p;
            //
            URL url = new URL(ServerURL);

            HttpURLConnection cc = (HttpURLConnection)
                    url.openConnection();
            cc.setReadTimeout(10000);
            cc.setConnectTimeout(10000);
            cc.setRequestMethod("POST");
            cc.setDoInput(true);
            cc.connect();

            DataOutputStream dos = new DataOutputStream(cc.getOutputStream());
            dos.writeBytes(PostParam);
            dos.flush();
            dos.close();

            int response = cc.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                DataInputStream = cc.getInputStream();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in PostData", e);
        }
        return DataInputStream;

    }

    String ConvertStreamToString(InputStream stream) {

        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
        } finally {

            try {
                stream.close();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error in ConvertStreamToString", e);
            }
        }
        return response.toString();


    }



    private class MakeNetworkCall extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... arg) {

            InputStream is = null;
            String URL = arg[0];
            String param = arg[2];
            //  Log.d(LOG_TAG, "URL: " + URL);
            String res = "";


            if (arg[1].equals("Post")) {

                is = ByPostMethod(URL,param);

            } else {

                is = ByGetMethod(URL);
            }
            if (is != null) {
                res = ConvertStreamToString(is);
            } else {
                res = "Something went wrong";
            }
            return res;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //  Log.d(LOG_TAG, "Result: " + result);
        }
    }

    public String setBody(HashMap<String, String> params){
        String sBody = "";
        sBody = "storage="+params.get("storage");
        sBody = sBody+"&section="+params.get("section");
        sBody = sBody+"&bin="+params.get("bin");
        sBody = sBody+"&item_barcode="+params.get("item_barcode");
        sBody = sBody+"&charge="+params.get("charge");
        sBody = sBody+"&bundle="+params.get("bundle");
        sBody = sBody+"&qty="+params.get("qty");
        sBody = sBody+"&weight="+params.get("weight");
        sBody = sBody+"&rmd_grade="+params.get("rmd_grade");
        sBody = sBody+"&qa_grade="+params.get("qa_grade");

        try {
            sBody = sBody+"&remark="+ URLEncoder.encode(params.get("remark"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        sBody = sBody+"&user_add="+params.get("user_add");
        sBody = sBody+"&rmd_date="+params.get("rmd_date");
        sBody = sBody+"&flag="+params.get("flag");
        sBody = sBody+"&batch="+params.get("batch");
        sBody = sBody+"&mat="+params.get("mat");
        sBody = sBody+"&location="+params.get("location");

        return sBody;
    }

    String getBatch(String charge, String bundle){
        String batch = "";
        int i= Integer.parseInt(bundle);
        if(charge.substring(0,1).equals("7") || charge.substring(0,1).equals("8")){

            batch = charge+""+ String.format("%02d",  i);
        }else{
            batch = charge+""+ String.format("%03d",  i);
        }
        return batch;
    }

}
