package com.vishnu.madirectory;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlDatabase extends SQLiteOpenHelper {
    public static final String dbname="macebook";
    public static final String table="details";
    public static final String idi="id";
    public static final String name="name";
    public static final String ph1="ph1";
    public static final String ph2="ph2";
    public static final String mail="mail";
    public static final String dep="depname";
    public static final String des="desname";
    public static final String status="status";
    public static final String filename="filename";
    public SqlDatabase( Context context) {

        super(context,dbname, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    public void dropTab(){
        SQLiteDatabase db1=getReadableDatabase();
        db1.execSQL("drop table if exists "+table);
    }
    public String[] dsiplayNam(){
        SQLiteDatabase db1=getReadableDatabase();
        Cursor cs=db1.rawQuery("select name from details",null);
        String nam[]=new String[cs.getCount()];
        int i=-1;
        while(cs.moveToNext()) {
            nam[++i]=cs.getString(0);
        }
        return nam;
        }
    public String[] dsiplayDepNam(){
         SQLiteDatabase db1=getReadableDatabase();
        Cursor cs=db1.rawQuery("select distinct depname from details",null);
        String nam[]=new String[cs.getCount()+1];
        int i=0;
        nam[0]="Select Department";
        while(cs.moveToNext()) {
            nam[++i]=cs.getString(0);
        }
        return nam;
    }
    public String[] dsiplayNamDep(String depi){
        SQLiteDatabase db1=getReadableDatabase();
        Cursor cs=db1.rawQuery("select name from details where depname =\""+depi+"\"",null);
        if(cs.getCount()==0){
            return null;
        }
        String nam[]=new String[cs.getCount()];
        int i=-1;
        while(cs.moveToNext()) {
            nam[++i]=cs.getString(0);
        }
        return nam;
    }
    public void createTab()
    {
        SQLiteDatabase db3=getWritableDatabase();
        db3.execSQL("create table "+table+" (id integer primary key ,name varchar(25),ph1 varchar(15),ph2 varchar(15),mail varchar(30),depname varchar(30),desname varchar(25),status varchar(20),filename varchar(30))");

    }
    public void insertData(int id1,String name1,String ph11,String ph21,String mail1,String dep1,String des1,String status1,String filename1){
        SQLiteDatabase db2=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(idi,id1);
        cv.put(name,name1);
        cv.put(ph1,ph11);
        cv.put(ph2,ph21);
        cv.put(mail,mail1);
        cv.put(dep,dep1);
        cv.put(des,des1);
        cv.put(status,status1);
        cv.put(filename,filename1);
        db2.insert(table,null,cv);
    }
    public String[] selectAll(int id){
        SQLiteDatabase db1=getReadableDatabase();
        Cursor cs=db1.rawQuery("select * from details where id="+id+"",null);
        String full[]=new String[9];
        cs.moveToNext();
        full[0]=cs.getString(1);
        full[1]=cs.getString(2);
        full[2]=cs.getString(3);
        full[3]=cs.getString(4);
        full[4]=cs.getString(5);
        full[5]=cs.getString(6);
        full[6]=cs.getString(7);
        full[7]=cs.getString(8);
        return  full;
    }
    public int idFind(String s){
        SQLiteDatabase db1=getReadableDatabase();
        String d[]=new String[1];
        d[0]=s;
        Cursor cs=db1.rawQuery("select id from details where name like ?",d);
        if(cs.getCount()==1) {
            cs.moveToNext();
            return (cs.getInt(0));
        }
        else
            return -1;
    }
    }

