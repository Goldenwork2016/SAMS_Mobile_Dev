//
//  ItemRecord.swift
//  SidebarMenu
//
//  Created by admin on 6/3/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import Foundation
class ItemRecord{
    private var id : String?
    private var name : String?
    
    init (id : String? , name : String?){
        self.id = id
        self.name = name
    }
    // set method for items record . . . . ;
    func setId(id : String?){
        self.id = id ;
    }
    
    func setName(name : String?){
        self.name = name ;
    }
    
    
    // getter method for items record . . . . ;
    func getId()->String?{
        return self.id ;
    }
    
    func getName()->String?{
        return self.name
    }
    
    
    
    
    
}
