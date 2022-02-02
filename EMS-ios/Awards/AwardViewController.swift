//
//  AwardViewController.swift
//  SidebarMenu
//
//  Created by admin on 5/29/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class AwardViewController: UIViewController, UITableViewDelegate, UITableViewDataSource{
   
    let AWARD_URL = "https://admin.emsystemsolutions.com/api/awards";
    var AwardInfo=Array<AwardsInformation>()

    @IBOutlet var tableViewAward: UITableView!
    @IBOutlet var menuButton: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if self.revealViewController() != nil {
            // when click in menu button  . . . . ;
            menuButton.target = self.revealViewController()
            menuButton.action = Selector("revealToggle:")
            self.view.addGestureRecognizer((self.revealViewController()? . panGestureRecognizer())!)
        }
        
        // put fake data to award array . . . . ;
        AwardInfo.append(AwardsInformation(StudentId: "Tr444",Name: "meeete",StatusId: "red"))
        AwardInfo.append(AwardsInformation(StudentId: "T2225d",Name: "malek",StatusId: "green"))
        AwardInfo.append(AwardsInformation(StudentId: "Tr444",Name: "erg",StatusId: "red"))
        AwardInfo.append(AwardsInformation(StudentId: "T3435d",Name: "dfd",StatusId: "green"))
        AwardInfo.append(AwardsInformation(StudentId: "Tr444",Name: "erg",StatusId: "red"))
        AwardInfo.append(AwardsInformation(StudentId: "T3435d",Name: "dfd",StatusId: "green"))
        
        //   to relode data .. . . . ;
        self.tableViewAward.reloadData()
       
   
     
    }
    
    // we need one section only . .. . . . ;
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    // count of award info . . . . ;
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print (AwardInfo.count)
        return AwardInfo.count
    }
    // to put data inside cells . . . . ; 
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell:AwardTableViewCell = tableViewAward.dequeueReusableCell(withIdentifier: "awardcell", for: indexPath) as! AwardTableViewCell
    
      
        // MARK : put status to image status . . . . ;
        return cell
    }
    
  // to change background for cell before showin in screen . . . ;
  /*  func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if indexPath.row % 2 != 0 {
            cell.backgroundColor = UIColor.lightGray
        }else {
            cell.backgroundColor = UIColor.white
        }
     
    }*/

    // send user id  to awards api to get data . . . .;
    func getAwardsWebServes (awardUrl:String){
        // parse string url to URL  . . . . ;
    let url = URL(string: awardUrl)
        // request url . . .  ;
        var request = URLRequest(url: url!)
        
        // set value . . . . ;
       // request.setValue("application/x-www-form-urlencoded", forHTTPHeaderField: "Content-Type)
        // set request method . . . .;
         request.httpMethod = "POST"
        // set user id from global variable to post string . . . .;
        let postString = "user_id\(GlobalVariable.userId)"
        
        // put argument who new to pass from post array in http body . . . ;
        request.httpBody = postString.data(using: .utf8)
        
        // getting response for post method .( put here ) . ;
        
        
        
        
    }
    
    

}
