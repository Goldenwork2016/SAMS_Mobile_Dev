//
//  DashboardViewController.swift
//  SidebarMenu
//
//  Created by admin on 5/29/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class DashboardViewController: UIViewController {
    var yearSelected = "";
    var Sport_id = "" ;
    var Selected_Sport = "" ;
    // url for dashboard . . . . ;
    let URL_DASHBOARD = "https://sams.emsystemsolutions.com/api/dashboard_info";
    // initialize for uiView component  . .. .;
    @IBOutlet weak var currrentLabel: UILabel!
    @IBOutlet weak var ViewGroupDashboard: UIView!
    @IBOutlet weak var limitLabel: UILabel!
    @IBOutlet weak var awardedLabel: UILabel!
    @IBOutlet weak var budgetLabel: UILabel!
    @IBOutlet weak var pendingLabel: UILabel!
    @IBOutlet weak var varsityLabel: UILabel!
    @IBOutlet weak var jvLabel: UILabel!
    @IBOutlet weak var rosterCurrentLabel: UILabel!
    @IBOutlet weak var goalLabel: UILabel!
    @IBOutlet var menuButton: UIBarButtonItem!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // if user type is atheltic director then hide dashboard view . . . ;
        if(GlobalVariable.userType == "admin"){
           ViewGroupDashboard.isHidden = true ;
       }
       
            // when click in menu button  . . . . ; 
            menuButton.target = self.revealViewController()
            menuButton.action = Selector("revealToggle:")
            self.view.addGestureRecognizer((self.revealViewController()? . panGestureRecognizer())!)

        getDashboardInfoFromDatabase();
        
    }
    
    // get dashboard data from database . . . . . ;
    func getDashboardInfoFromDatabase(){
        // parse chatting url to url datatype . . . . ;
        let url = URL(string: URL_DASHBOARD)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        
        let postString = "user_id= \(GlobalVariable.userId)"
        print ("postString is \(postString)")
        request.httpBody = postString.data(using: .utf8)
        
        // getting response for post method . . . . ;
        DispatchQueue.global().async {
            let task = URLSession.shared.dataTask(with: request){(data:Data?,response:URLResponse?,error:Error?) in
                if(error != nil){
                    print ("error\(error)")
                    
                    return
                }
                // getting values from json response . . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                // print (ResponseString)
                
                // convert data to json response . . . ;
                do{
                    let json = try JSONSerialization.jsonObject(with: data!, options: []) as? [String:Any]
                    var goal : String?
                    var roster_current : String?
                    var pending : String?
                    var budget : String?
                    var awarded : String?
                    var limit : String?
                    var school_current : String?
                    var jv : String?
                    var varsity  : String?
                    
                    
                    
                    // to fetch dashboard information . . . . ;
                    if  let dashboard = json!["dashboard"] as? [String: Any]{
                    goal = dashboard["goal"] as! String
                   roster_current = dashboard["roster_current"] as? String
                        pending = dashboard["pending"] as? String
                        budget = dashboard["budget"] as? String
                        awarded = dashboard["awarded"] as? String
                        limit = dashboard["limit"] as? String
                        school_current = dashboard["schoolarship_current"] as? String
                        jv = dashboard["jv"] as? String
                        varsity = dashboard["varsity"] as? String
                        
                        print ("goal is \(goal) , roster is \(roster_current) , pending \(pending) , budget \(budget) , awarded \(awarded) , limit is \(limit) , school_current \(school_current) , jv \(jv) , varsity  \(varsity)")
                        
                       
                    }
                 //   if(GlobalVariable.userType == "coach"){
                        self.Selected_Sport = "";
                    // to fetch sport id's information . . . . ;
                    if var sportId = json!["sport"] as? [String:Any]{
                        self.Sport_id = (sportId["sport_id"] as? String)!
                        
                        print("sport id is \(sportId)")
                   // }
                    
                    // to fetch selected sport information . . . . ;
                    self.Selected_Sport = (json!["selected_sport"] as? String)! ;
                    print ("selected sport is : \(self.Selected_Sport)")
                    }
                    
                    // for  year . . . . ;
                    if let years = json!["selected_year"] as? [String]{
                        self.yearSelected = ""
                        for selectedYear in years {
                            print("selected year is \(selectedYear)");
                            print ("selected after implode , \(self.yearSelected)")
                            if(selectedYear == ""){
                                
                                continue
                            }
                            if(self.yearSelected != ""){
                            self.yearSelected += ",\(selectedYear)";
                            }else{
                                 self.yearSelected += "\(selectedYear)";
                            }
                        }
                        print ("year selected \(self.yearSelected)");
                    }
                    
                    
                    
                    // this information for all application . . . . ;
                    
                    
                    // to fetch sport from json response . . . ;
                    if let fetch_sport = json!["sport_name_list"] as?[String:Any]{
                        GlobalVariable.sport_id.removeAll();
                        GlobalVariable.sports_items.removeAll();
                        for(key,value) in fetch_sport{
                        
                            GlobalVariable.sport_id.append(key)
                            GlobalVariable.sports_items.append(ItemRecord(id: key,name: value as! String))
                            
                        }
                    }
                    // to fetch collage from json response . . . ;
                    if let fetch_school = json!["college_name_list"] as?[String:Any]{
                        GlobalVariable.school_id.removeAll();
                        GlobalVariable.school_items.removeAll();
                        for(school_key,school_value) in fetch_school{
                            GlobalVariable.school_id.append(school_key)
                            GlobalVariable.school_items.append(ItemRecord(id: school_key, name: school_value as! String))
                        }
                    }
                    
                    // to fetch year from json response . . . . ;
                    if let fetch_year = json!["sports_acadmic_year_listing"] as?[String:String]{
                        GlobalVariable.years_id.removeAll();
                        GlobalVariable.years_items.removeAll();
                        for (year_key,year_value) in fetch_year{
                            
                            if self.isYearDuplicated(name: year_value){
                                print ("duplicate")
                            }else{
                                GlobalVariable.years_id.append(year_value)
                            }
                            GlobalVariable.years_items.append(ItemRecord(id: year_key, name: year_value as! String))
                            
                        }
                        print("global year is \(GlobalVariable.years_id.count)")
                        
                    }
                
                    // to fetch year term from json response . . . ;
                    if let fetch_year = json!["year_term"] as?[String:Any]{
                        GlobalVariable.yearFormatted.removeAll();
                        GlobalVariable.year_formatted_items.removeAll();
                        for (term_key,term_value) in fetch_year{
                            
                            GlobalVariable.yearFormatted.append(term_key)
                            GlobalVariable.year_formatted_items.append(ItemRecord(id: term_key, name: term_value as! String))
                            print ("term_key is \(term_key) and term_value is \(term_value) ")
                        }
                    }
                    
                    
                    
                    
                    
                    DispatchQueue.main.async {
                        // change text label for screen . . . . ;
                        self.putDataInsideViews(goal: goal!, roster_current: roster_current!, pending: pending!, budget: budget!, awarded: awarded!
                            , limit: limit!, school_current: school_current!, jv: jv! ,varsity: varsity!)
                    }
                    
                }catch{
                    print ("error json parsing . . . ; ")
                }
                
                
                
            }
            task.resume()
            
            
        }
        
    }
    
    func putDataInsideViews(goal : String , roster_current : String , pending : String , budget : String , awarded : String , limit : String , school_current : String , jv : String , varsity : String){
        goalLabel.text = goal
        rosterCurrentLabel.text = roster_current
        pendingLabel.text = pending
        budgetLabel.text = budget
        awardedLabel.text = awarded
        limitLabel.text = limit
        varsityLabel.text = varsity ;
        jvLabel.text = jv ;
        currrentLabel.text = school_current
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "year_segue"){
            let yearDialog = segue.destination as! YearDashboardViewController
            
            yearDialog.selectedYear = self.yearSelected
             yearDialog.onAdd = onAdd
            
            
        }else if(segue.identifier == "sport_segue"){
            let sportDialog = segue.destination as! SportDashboardViewController
            sportDialog.selectedSport = self.Selected_Sport ;
            sportDialog.sportId = self.Sport_id ;
            
            sportDialog.onAdd = onAdd
        }
     
    }
    func onAdd(_ isAdd : Bool) -> () {
       self.getDashboardInfoFromDatabase()
    }
    
    // for year button . .  . ;
    @IBAction func YearBtn(_ sender: Any) {
        print ("year Button ");
    }
    
    // for sport button . . . ;
    @IBAction func sportBtn(_ sender: Any) {
        print ("sport Button");
    }
    
    func isYearDuplicated(name : String) -> Bool{
        if(GlobalVariable.years_items.count == 0 ){
            return false
        }
        for i in 1...GlobalVariable.years_items.count {
            if(GlobalVariable.years_items[i-1].getName() == name){
                return true
            }
        }
        return false
    }
}
