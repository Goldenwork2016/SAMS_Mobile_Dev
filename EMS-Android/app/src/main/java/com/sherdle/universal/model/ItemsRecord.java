package com.sherdle.universal.model;

import java.util.ArrayList;
import java.util.List;

public class ItemsRecord {
    String id;
    String name;


    public ItemsRecord(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    // search for id . . . . ;
    public static String searchForId(String text, List<ItemsRecord> list){
        for(int i=0;i<list.size();i++){
            if(text.equals(list.get(i).getName())){
                return list.get(i).getId();
            }
        }
        return null;
    }

    // search from text . . . .;
    public static String searchForName(String ids , List<ItemsRecord>list){
        for (int i= 0 ; i<list.size() ; i++){
            if(ids.equals(list.get(i).getId())){
                return list.get(i).getName();
            }
        }
        return null ;
    }

}
