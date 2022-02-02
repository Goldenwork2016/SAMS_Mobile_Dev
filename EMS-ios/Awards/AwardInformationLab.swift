//
//  AwardInformationLab.swift
//  SidebarMenu
//
//  Created by admin on 6/3/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import Foundation
class AwardInformationLab {
    
    private var AwardsList = Array<AwardsInformation>()
    
    
    private init(){}
    
    private static var awardLab : AwardInformationLab!
    
    
    public static var getInstance : AwardInformationLab {
        get {
            if awardLab == nil {
                
                awardLab = AwardInformationLab()
                
            }
            return awardLab!
        }
    }
    
    // to put award from award information . . . . . ;
    func addAward(award : AwardsInformation){
        AwardsList.append(award)
    }
    
    // to get list  award from awads information
    func getAwards()->Array<AwardsInformation>{
        return AwardsList
    }
    
    // to get student Information from award through id . . . ;
    func getStudentInfo(id:String)->AwardsInformation?{
        
        for award in AwardsList{
            if award.id == id{
                return award
            }
        }
        return nil
    }
    
    
    func clearAward(){
        self.AwardsList.removeAll()
    }
    // update AwardActiveStatus  . . . ; 
    func updateAwardActiveStatus(id : String , award_active_status :String){
        for award in AwardsList{
            if award.id == id {
                award.AwardActiveStatus = award_active_status ;
            }
        }
    }
    
    
    
}
