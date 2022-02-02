package com.sherdle.universal.model;

import java.util.ArrayList;
import java.util.List;

public class SupportInformationLab {
    private static SupportInformationLab supportLab;
   private List<SupportInformation>supportlist;

    private SupportInformationLab(){
        supportlist=new ArrayList<>();

    }

    public static synchronized SupportInformationLab getInstance(){
        if(supportLab==null){
          return supportLab=new SupportInformationLab();
        }
        return supportLab;
    }

    public List<SupportInformation> getSupportlist(){
        return supportlist;
    }

    public void addSupportList(List<SupportInformation> supportlist){
        this.supportlist=supportlist;
    }

    public void addSupport(SupportInformation supportInformation){
        supportlist.add(supportInformation);
    }

    public SupportInformation getSupport(String id){

        for(SupportInformation information:supportlist){
            if(information.getId().equals(id)){
                return information;
            }
        }
        return null;
    }

    public void deleteSupport(){
        supportlist.clear();
    }




}
