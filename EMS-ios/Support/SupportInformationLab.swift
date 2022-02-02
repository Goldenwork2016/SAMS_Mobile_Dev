//
//  SupportInformationLab.swift
//  SidebarMenu
//
//  Created by admin on 6/8/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import Foundation

class SupportInformationLab {
    
    private var supportList = Array<SupportInformation>()
    
    
    private init(){}
    
    private static var supportLab : SupportInformationLab!
    
    
    public static var getInstance : SupportInformationLab {
        get {
            if supportLab == nil {
                
                supportLab = SupportInformationLab()
                
            }
            return supportLab!
        }
    }
    
    // to put award from award information . . . . . ;
    func addSupport(support : SupportInformation){
        supportList.append(support)
    }
    
    // to get list  award from awads information
    func getSupport()->Array<SupportInformation>{
        return supportList
    }
    
    // to get support Information from supportList through id . . . ;
    func getSupportInfo(id:String)->SupportInformation?{
        
        for support in supportList{
            if support.id == id{
                return support
            }
        }
        return nil
    }
    
    
    func clearSupportList(){
        self.supportList.removeAll()
}

}
