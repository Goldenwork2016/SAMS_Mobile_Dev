//
//  AwardsInformation.swift
//  SidebarMenu
//
//  Created by admin on 5/30/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit
class AwardsInformation{
    var id : String?
    var StudentId : String?
    var Name : String?
    var LastName : String?
    var StatusId : String?
    var SportId : String?
    var Level : String?
    var SchoolId : String?
    var School : String?
    var Fall : String?
    var Winters : String?
    var Spring : String?
    var Summer : String?
    var Year : String?
    var Student_type :String?
    var AdmitType : String?
    var ClassLevel : String?
    var AwardActiveStatus : String?

    
    init ( id: String? , StudentId : String? , Name : String? , LastName : String? , StatusId :String? , SportId : String?,  Level : String? , SchoolId : String? , School : String? , Fall : String? , Winters : String? , Spring : String? , Summer : String? , Year : String? , Student_type : String , AdmitType : String , ClassLevel : String? , AwardActiveStatus : String?){
        self.id = id
        self.StudentId = StudentId
        self.Name = Name
        self.LastName = LastName
        self.StatusId = StatusId
        self.Level = Level
        self.SchoolId = SchoolId
        self.School = School
        self.Fall = Fall
        self.SportId=SportId
        self.Winters = Winters
        self.Spring = Spring
        self.Summer = Summer
        self.Year = Year
        self.Student_type = Student_type
        self.AdmitType = AdmitType
        self.ClassLevel = ClassLevel
        self.AwardActiveStatus = AwardActiveStatus
    }
    

 

    // create constructor to get ( studentId , studentName , statusId ) . . . . ;
init(StudentId:String,Name:String,StatusId:String){
    self.StudentId = StudentId
    self.Name = Name
    self.StatusId = StatusId
    
}
    
  
    
    
}
