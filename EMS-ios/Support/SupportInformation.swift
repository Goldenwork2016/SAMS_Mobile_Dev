//
//  SupportInformation.swift
//  SidebarMenu
//
//  Created by admin on 6/7/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class SupportInformation{
    var id : String?
    var cst_id : String?
    var user_type : String?
    var ticket : String?
    var subject : String?
    var status : String?
    var username : String?
    
    
    init(id : String? , cst_id : String? , user_type : String? , ticket : String? , subject : String? , status : String? , username : String?) {
        self.id = id
        self.cst_id = cst_id
        self.user_type = user_type
        self.ticket = ticket
        self.subject = subject
        self.status = status
        self.username = username
    }

}
