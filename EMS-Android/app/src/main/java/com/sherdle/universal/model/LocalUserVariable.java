package com.sherdle.universal.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocalUserVariable {

    public static String userid;
    public static String usertype;
    public static String cst_id;
    public static String permission_add ;
    public static String permission_approve ;
    public static String permission_deny ;
    public static String permission_edit ;







     public static List<String>school=new ArrayList<>();
    public static List<String>year=new ArrayList<>();
    public static List<String>sport=new ArrayList<>();
    public static List<String>terms=new ArrayList<>();

    public static List<ItemsRecord>termsItems = new ArrayList<>();
    public static List<ItemsRecord>schoolsItems=new ArrayList<>();
    public static List<ItemsRecord>yearsItems=new ArrayList<>();
    public static List<ItemsRecord>sportsItems=new ArrayList<>();

    public static List<ItemsRecord> YearFormattedItems = new ArrayList<>();
    public static List<String> yearFormatted = new ArrayList<>();












    public static void deleteLocaleVariables(){
        school.clear();
        year.clear();
        sport.clear();
        schoolsItems.clear();
        yearsItems.clear();
        sportsItems.clear();
    }




}
