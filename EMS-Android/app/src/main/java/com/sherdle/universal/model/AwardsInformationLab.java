package com.sherdle.universal.model;

import java.util.ArrayList;
import java.util.List;

public class AwardsInformationLab {
    private static AwardsInformationLab mInformationLab;
    List<AwardInformation>list;

    private AwardsInformationLab(){
        list=new ArrayList<>();
    }

    public static synchronized AwardsInformationLab getInstance(){
        if(mInformationLab==null){
            return mInformationLab=new AwardsInformationLab();
        }
        return mInformationLab;
    }

    public void putAward(AwardInformation awardInformation){
        list.add(awardInformation);
    }

    public void deleteAwards(){
        list.clear();
    }


    public List<AwardInformation> getAwardsList(){
        return list;
    }

    public AwardInformation getStudentInformation(String id) {
        for(AwardInformation information:list){
            if(information.getId().equals(id)){
                return information;
            }
        }
        return null;
    }




}
