package com.example.domi.gps_application;

import java.util.ArrayList;

/**
 * Created by Domiś on 2016-03-18.
 */
public class GPS_Point
{
    public long id_point;
    public double latitude;
    public double longitude;
    public int isSynch;// 1(yes) albo 0(no)
    public long id_route;


    static ArrayList<GPS_Point> array_GPS_Point = new ArrayList<>();

    public GPS_Point()
    {

    }
    public GPS_Point(long id_p,double lat, double lon, int isSy, long id_r)
    {
        this.id_point=id_p;
        this.latitude=lat;
        this.longitude=lon;
        this.isSynch = isSy;
        this.id_route=id_r;
    }


    public long getId_point() {
        return id_point;
    }

    public void setId_point(long id_point) {
        this.id_point = id_point;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int isSynch() {
        return isSynch;
    }

    public void setIsSynch(int isSynch) {
        this.isSynch = isSynch;
    }

    public long getId_route() {
        return id_route;
    }

    public void setId_route(long id_route) {
        this.id_route = id_route;
    }

    public static ArrayList<GPS_Point> getArray_GPS_Point() {
        return array_GPS_Point;
    }

    public static void setArray_GPS_Point(ArrayList<GPS_Point> array_GPS_Point) {
        GPS_Point.array_GPS_Point = array_GPS_Point;
    }

    public void printAllData()
    {
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        for(GPS_Point g : array_GPS_Point)
        {
            System.out.print(g.getId_point()+" "+g.getLatitude()+" "+g.getLongitude()+" "+g.isSynch()+" "+g.getId_route()+"\n");
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }
}
