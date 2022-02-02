//
//  ChattingInformation.swift
//  SidebarMenu
//
//  Created by admin on 6/10/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import Foundation

class ChattingInformation{
    var chat_id : String!
    var user_id : String!
    var cst_id : String!
    var message : String!
    var chat_time : String!
    var user_name : String!
    var user_profile_image :String!
    var attachment : String?
    var image : String?
    var removed : String?
    var edited_admin : String?
    var edited : String?
    
    init(chat_id : String , user_id : String , cst_id : String , message : String , chat_time : String , user_name : String , user_profile_image : String , attachement : String , image : String , removed : String , edited_admin : String , edited : String ){
        self.chat_id = chat_id
        self.user_id = user_id
        self.cst_id = cst_id
        self.message = message
        self.chat_time = chat_time
        self.user_name = user_name
        self.user_profile_image = user_profile_image
        self.attachment = attachement
        self.image = image
        self.removed = removed
        self.edited_admin = edited_admin
        self.edited = edited
        
    }
    
}
