package com.zubb.jannarongj.z_folding;

import java.util.Arrays;
import java.util.List;


/**
 * Created by jannarong.j on 10/22/2018.
 */

public class MLocation extends CurLocation{


    private  String pill ="";
    private  String lr = "";
    private  String fr = "";
    private  String ch = "";
    private  String cloc = "";
    private Boolean curLoc = false;

    public  String mplant = "";

    MLocation(String P){
        //  this.mplant  = P;
        setLocPlant(P);
    }

    CurLocation cl ;


    public String getCloc() {
        return cloc;
    }

    public void setSpnLoc(){
        this.curLoc = true;
    }

    public void setSPNCloc(String cloc) {
        cl = new CurLocation();
        this.cloc = cloc;
        cl.setCurlocation(cloc);

    }

    public void setCloc(String cloc) {
        cl = new CurLocation();

        if(getCh().equals("10")){
            this.cloc = "A"+cloc.substring(2,cloc.length());
            cl.setCurlocation("A"+cloc.substring(2,cloc.length()));
        }else{
            this.cloc = cloc;
            cl.setCurlocation(cloc);
        }

    }



    private String pilC[] ;
    private String arLocation[];


    List<String> locList ;
    List<String> numPilList;

    public void setLocPlant(String p){




        if(p.equals("SPN")){
            this.pilC = new String[]{"1", "2"};
            this.arLocation = new String[] {
                    "1L-01", "1L-02", "1L-03", "1L-04","1L-05", "1L-06","1L-07", "1L-08", "1L-09", "1L-10","1L-11", "1L-12", "1L-13", "1L-14","1L-15",
                    "1R-01", "1R-02", "1R-03", "1R-04","1R-05", "1R-06","1R-07", "1R-08", "1R-09", "1R-10","1R-11", "1R-12", "1R-13", "1R-14","1R-15",

                    "2L-01", "2L-02", "2L-03", "2L-04","2L-05", "2L-06","2L-07", "2L-08", "2L-09", "2L-10","2L-11", "2L-12", "2L-13", "2L-14","2L-15",
                    "2R-01", "2R-02", "2R-03", "2R-04","2R-05", "2R-06","2R-07", "2R-08", "2R-09", "2R-10","2R-11", "2R-12", "2R-13", "2R-14","2R-15",

                    "2L-20", "2L-21", "2L-22", "2L-23","2L-24", "2L-25","2L-26","2L-27","2L-28", "2L-29","2L-30", "2L-31","2L-32", "2L-33","2L-34","2L-35",
                    "2R-20", "2R-21", "2R-22", "2R-23","2R-24", "2R-25","2R-26","2R-27","2R-28", "2R-29","2R-30", "2R-31","2R-32", "2R-33","2R-34","2R-35",

                    "1L-20", "1L-21", "1L-22", "1L-23","1L-24", "1L-25","1L-26","1L-27","1L-28", "1L-29","1L-30", "1L-31","1L-32", "1L-33","1L-34","1L-35",
                    "1R-20", "1R-21", "1R-22", "1R-23","1R-24", "1R-25","1R-26","1R-27","1R-28", "1R-29","1R-30", "1R-31","1R-32", "1R-33","1R-34","1R-35",

            };
        }else{
            pilC = new String[]{"1","2","3","4","5","6","7","8","9"};
            arLocation = new String[]
                    {       "1L-20", "1L-21", "1L-22", "1L-23","1L-24", "1L-25",
                            "1R-20", "1R-21", "1R-22", "1R-23", "1R-24", "1R-25",

                            "2L-20", "2L-21","2L-22", "2L-23", "2L-24", "2L-25",
                            "2R-20", "2R-21","2R-22", "2R-23", "2R-24", "2R-25",

                            "3R-20", "3R-21", "3R-22","3R-23", "3R-24", "3R-25", "3R-26", "3R-27", "3R-28", "3R-29",
                            "3L-20", "3L-21", "3L-22","3L-23", "3L-24", "3L-25", "3L-26", "3L-27", "3L-28", "3L-29",

                            "4L-20", "4L-21", "4L-22", "4L-23", "4L-24", "4L-25","4L-26", "4L-27", "4L-28", "4L-29", "4L-30",
                            "4R-20", "4R-21", "4R-22", "4R-23", "4R-24", "4R-25", "4R-26", "4R-27", "4R-28", "4R-29", "4R-30",

                            "5L-07", "5L-08", "5L-09", "5L-10", "5L-11", "5L-12", "5L-13", "5L-14",
                            "5R-05", "5R-06", "5R-07", "5R-08", "5R-09", "5R-10", "5R-11", "5R-12", "5R-13", "5R-14",
                            "5L-20", "5L-21", "5L-22", "5L-23", "5L-24", "5L-25", "5L-26", "5L-27", "5L-28", "5L-29", "5L-30",
                            "5R-20", "5R-21", "5R-22", "5R-23", "5R-24","5R-25", "5R-26", "5R-27", "5R-28", "5R-29", "5R-30",

                            "6R-04", "6R-05", "6R-06", "6R-07", "6R-08", "6R-09", "6R-10", "6R-11", "6R-12", "6R-13", "6R-14",
                            "6L-04", "6L-05", "6L-06", "6L-07", "6L-08", "6L-09", "6L-10", "6L-11", "6L-12", "6L-13", "6L-14",

                            "6L-20", "6L-21", "6L-22", "6L-23", "6L-24", "6L-25", "6L-26", "6L-27", "6L-28", "6L-29", "6L-30", "6L-31",
                            "6R-20", "6R-21", "6R-22", "6R-23", "6R-24", "6R-25", "6R-26", "6R-27", "6R-28", "6R-29", "6R-30", "6R-31",

                            "7R-02", "7R-03", "7R-04", "7R-05", "7R-06", "7R-07", "7R-08", "7R-09", "7R-10", "7R-11", "7R-12", "7R-13", "7R-14",
                            "7L-02", "7L-03", "7L-04", "7L-05", "7L-06", "7L-07", "7L-08", "7L-09", "7L-10", "7L-11", "7L-12", "7L-13", "7L-14",
                            "7L-20", "7L-21", "7L-22", "7L-23", "7L-24", "7L-25", "7L-26", "7L-27", "7L-28", "7L-29", "7L-30", "7L-31",
                            "7R-20", "7R-21", "7R-22", "7R-23", "7R-24", "7R-25", "7R-26", "7R-27", "7R-28", "7R-29", "7R-30", "7R-31",

                            "7R-02", "7R-03", "7R-04", "7R-05", "7R-06", "7R-07", "7R-08", "7R-09", "7R-10", "7R-11", "7R-12", "7R-13", "7R-14",
                            "7L-02", "7L-03", "7L-04", "7L-05", "7L-06", "7L-07", "7L-08", "7L-09", "7L-10", "7L-11", "7L-12", "7L-13", "7L-14",

                            "8R-01", "8R-02", "8R-03", "8R-04", "8R-05", "8R-06", "8R-07", "8R-08", "8R-12", "8R-13", "8R-14",
                            "8L-01", "8L-02", "8L-03", "8L-04", "8L-05", "8L-06", "8L-07", "8L-08", "8L-09", "8L-10", "8L-11", "8L-12", "8L-13", "8L-14",

                            "8L-20", "8L-21", "8L-22", "8L-23", "8L-24", "8L-25", "8L-26", "8L-27", "8L-28", "8L-29", "8L-30", "8L-31", "8L-32",
                            "8R-20", "8R-21", "8R-22", "8R-23", "8R-24", "8R-25", "8R-26", "8R-27", "8R-28", "8R-29", "8R-30", "8R-31", "8R-32",

                            "9L-21", "9L-22", "9L-23", "9L-24", "9L-25", "9L-26", "9L-27", "9L-28", "9L-29", "9L-30", "9L-31", "9L-32",
                            "9R-21", "9R-22", "9R-23", "9R-24", "9R-25", "9R-26", "9R-27", "9R-28", "9R-29", "9R-30", "9R-31", "9R-32",

                            "AL-21", "AL-23", "AL-24", "AL-25", "AL-26", "AL-27", "AL-28", "AL-29", "AL-30", "AL-31", "AL-32",
                            "AR-21", "AR-23", "AR-24", "AR-25", "AR-26", "AR-27", "AR-28", "AR-29", "AR-30", "AR-31", "AR-32",
                            "5S-01"
                    };
        }
        locList = Arrays.asList(arLocation);
        numPilList = Arrays.asList(pilC);
    }

    public Boolean getCurLoc() {
        return curLoc;
    }

    public void setCurLoc(Boolean curLoc) {
        this.curLoc = curLoc;
    }

    public Boolean checkLocation(String loc){

        if(loc.equals("1W5S"))   {
            setCurLoc(true);
            return true;
        }else{
            if(loc.length()<4){
                return false;
            }else{
                if(locList.contains(loc)){
                    setCurLoc(true);
                    return true;
                }else{
                    setCurLoc(false);
                    return false;
                }
            }
        }


    }

    public String getPill() {

        return pill;
    }

    public void setPill(String pill) {
        List<String> np;
        cl = new CurLocation();
        String pilx[] = new String[]{"1","2","3","4","5","6","7","8","9"};
        np = Arrays.asList(pilx);

        if(np.contains(pill)){
            this.pill = "0"+pill;
            cl.setPill("0"+pill);
        }else{

            this.pill = pill;
            cl.setPill(pill);


        }
        //Log.d("pilll : ",pill);

    }

    public String getLr() {
        return lr;
    }

    public void setLr(String lr) {
        cl = new CurLocation();
        this.lr = lr;
        cl.setLr(lr);
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        cl = new CurLocation();
        this.fr = "";
        cl.setFr("");
    }

    public String getCh() {
        return ch;
    }

    public void setCh(String ch) {
        cl = new CurLocation();
        this.ch = ch;
        cl.setCh(ch);
    }


    public String storagePlant(String plant) {
        String storage ="";
        if(plant.equals("ZUBB") || plant.equals("RS")){
            storage = "WHQ";
        }
        else if(plant.equals("WPN")){
            storage = "WPN";
        }
        return storage;
    }
}
