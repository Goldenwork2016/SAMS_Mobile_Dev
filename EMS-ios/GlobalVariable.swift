//
//  GlobalVariable.swift
//  SidebarMenu
//
//  Created by admin on 5/29/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

struct GlobalVariable {
    static var userId = "";
    static var userType = "";
    static var cst_id = "";
    static var permission_add = "";
    static var permission_approve = "";
    static var permission_deny = "";
    static var permission_edit = "" ;
    
    public static var school_id = Array<String>() ;
    public static var years_id = Array<String>() ;
    public static var sport_id = Array<String>() ;
    public static var yearFormatted = Array<String>() ;
    
    public static var school_items = Array<ItemRecord>() ;
    public static var years_items = Array<ItemRecord>() ;
    public static var sports_items = Array<ItemRecord>() ;
    public static var year_formatted_items = Array<ItemRecord>() ;
    
    
    static func getYearId(year : String) -> String {
        for a in years_items {
            if(a.getName() == year){
                return a.getId()!
            }
        }
        return ""
    }
    
    static func getSportName(sport : String) -> String {
        for a in sports_items {
            if (a.getId() == sport){
                return a.getName()!
            }
        }
        return ""
    }
    

    
    

}
