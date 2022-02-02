//
//  AwardControllerViewController.swift
//  SidebarMenu
//
//  Created by admin on 6/1/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class AwardControllerViewController: UIViewController , UITableViewDelegate , UITableViewDataSource , UITabBarDelegate , UISearchBarDelegate{
   
    @IBOutlet weak var addAwardBtn: UIBarButtonItem!
    @IBOutlet weak var searchbar: UISearchBar!
    
    let AWARD_URL = "https://sams.emsystemsolutions.com/api/awards";
   
    
    var AwardInfo = Array<AwardsInformation>()
    var filteredAwards = Array<AwardsInformation>()
    var isSearching = false ;
    
    @IBOutlet weak var nacigationbar: UINavigationItem!
    static var count:Int = 0
    var isLoaded = false
    @IBOutlet weak var tableViewAward: UITableView!
   
    @IBOutlet weak var menuAwardBtn: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
          self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
      
        
        
        
        searchbar.delegate = self
        searchbar.returnKeyType = UIReturnKeyType.done
        
        if(GlobalVariable.permission_add == "0"){
            self.nacigationbar.rightBarButtonItem?.isEnabled = false ;
            self.nacigationbar.rightBarButtonItem?.tintColor = UIColor.clear
        }
        


            
            // when click in menu button  . . . . ;
            menuAwardBtn.target = self.revealViewController()
            menuAwardBtn.action = Selector("revealToggle:")
        
        
    
       
    }
    // reload data when back . . . ; 
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
       
        AwardInformationLab.getInstance.clearAward();
        getDataFromDatabase()
        
    }
 
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if (isSearching){
            return filteredAwards.count
        }
        //return AwardInfo.count
        return AwardInformationLab.getInstance.getAwards().count
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "edit_segue"){
            let editController = segue.destination as! EditAwardController
          
             let ip = tableViewAward.indexPathForSelectedRow
            if isSearching {
                editController.indexItem = filteredAwards[ip!.row].id!
            }else{
            editController.indexItem = AwardInformationLab.getInstance.getAwards()[ip!.row].id!
            }
            let transition = CATransition()
            transition.duration = 0.5
            transition.type = CATransitionType.moveIn
            transition.subtype = CATransitionSubtype.fromLeft
            transition.timingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut)
            view.window!.layer.add(transition,forKey: kCATransition)
            present(editController,animated: true,completion: nil)
            
        }else {
            let addController = segue.destination as! AddAwardController
            
            addController.onAdd = onAdd
            
            let transition = CATransition()
                       transition.duration = 0.5
                       transition.type = CATransitionType.moveIn
                       transition.subtype = CATransitionSubtype.fromLeft
                       transition.timingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut)
                       view.window!.layer.add(transition,forKey: kCATransition)
                       present(addController,animated: true,completion: nil)
        }
        
    }
   
    func onAdd(_ isAdd : Bool) -> () {
        AwardInformationLab.getInstance.clearAward();
        getDataFromDatabase()
    }
    
    
    

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
           let cell:AwardTableViewCell = tableViewAward.dequeueReusableCell(withIdentifier: "cell", for: indexPath) as! AwardTableViewCell
        // to set gray or white color for cell . . . . ;
     if(indexPath.row % 2 == 0){
        if #available(iOS 11.0, *) {
            cell.contentView.backgroundColor = UIColor(named: "gray_cell")
        } else {
            // Fallback on earlier versions
        }
        }else {
            cell.contentView.backgroundColor = UIColor.white
        }
        
        // if array of search bar is filled . . .  ;
        if (isSearching){
         
            cell.student_id.text = filteredAwards[indexPath.row].StudentId
            cell.student_name.text = filteredAwards[indexPath.row].Name! + " " + filteredAwards[indexPath.row].LastName!
            cell.status_image.image = UIImage(named: getImageStatus(status: filteredAwards[indexPath.row].StatusId!))
        
        }else if (!isSearching){
            var informationList = AwardInformationLab.getInstance.getAwards()
        cell.student_id.text = informationList[indexPath.row].StudentId
            cell.student_name.text = informationList[indexPath.row].Name! + " " + informationList[indexPath.row].LastName!
        cell.status_image.image = UIImage(named: getImageStatus(status: informationList[indexPath.row].StatusId!))
        }
        return cell
    }
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String){
        if (searchBar.text == nil || searchBar.text! == ""){
            self.isSearching = false ;
            print ("search bar empty")
            self.tableViewAward.reloadData()
            
        }else{
            print ("search bar text is \(searchBar.text)")
            
            // if search bar not null or empty . . . ;
            let data = AwardInformationLab.getInstance.getAwards()
            filteredAwards.removeAll()
            
            
            self.isSearching = true ;
            filteredAwards =  data.filter({AwardsInformation -> Bool in
                return AwardsInformation.Name?.lowercased().contains(String(describing :searchBar.text!).lowercased()) ?? false || AwardsInformation.StudentId?.lowercased().contains(String(describing :searchBar.text!).lowercased()) ?? false
            })
            
            
            self.tableViewAward.reloadData()
        }
    }
    

    
    
    
    
    // to convert status to image . . . ;
    func getImageStatus(status : String)->String{
        if status == "1"{
            return "red"
        }else if status == "2"{
            return "yellow"
        }else if status == "4"{
            return "green"
        }
        return "black"
    }
    

    func getDataFromDatabase(){
        
        // parse award url to url datatype . . . . ;
        let url = URL(string: AWARD_URL)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        let postString = "user_id= \(GlobalVariable.userId)"
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
                
                
                // convert data to json response . . . ;
                do {
                    let json = try JSONSerialization.jsonObject(with: data!, options: []) as? [String:Any]
                    
                    // to fetch sport from json response . . . ;
                    if let fetch_sport = json!["sport_name_list"] as?[String:Any]{
                        for(key,value) in fetch_sport{
                            GlobalVariable.sport_id.append(key)
                            GlobalVariable.sports_items.append(ItemRecord(id: key,name: value as! String))
                            
                        }
                    }
                    
                    // to fetch collage from json response . . . ;
                    if let fetch_school = json!["college_name_list"] as?[String:Any]{
                    for(school_key,school_value) in fetch_school{
                    GlobalVariable.school_id.append(school_key)
                    GlobalVariable.school_items.append(ItemRecord(id: school_key, name: school_value as! String))
                    }
                }
                    
                    // to fetch year from json response . . . . ;
                    if let fetch_year = json!["sports_acadmic_year_listing"] as?[String:String]{
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
                        for (term_key,term_value) in fetch_year{
                            GlobalVariable.yearFormatted.append(term_key)
                            GlobalVariable.year_formatted_items.append(ItemRecord(id: term_key, name: term_value as! String))
                            print ("term_key is \(term_key) and term_value is \(term_value) ")
                        }
                    }
 
                    // to fetch user . .. . . ;
                    if  let fetch_user = json!["fetch_user"] as? [[String: Any]]{
                        for user in fetch_user {
                            // Mark : get all required data from this json response . . . ;
                            let id = user["id"] as? String
                            let std_id = user["student_id"] as? String
                            let fname = user["fname"] as? String
                            let status = user["award_active_status"] as? String
                            var student_type = user["student_type"] as? String
                            if(student_type == nil){
                                student_type = ""
                            }
                            var admit_type = user["admit_type"] as? String
                            if(admit_type == nil){
                                admit_type = ""
                            }
                            
                            let class_level = user["class_type"] as? String
                            
                            let lname = user["lname"] as? String
                            let sport_id = user["sport"] as? String
                            
                            let level = user["sport_level"] as? String
                            let school_id = user["college_name"] as? String
                            let school = "";
                            let fall = user["fall"] as? String
                            let winter = user["winter"] as? String
                            let spring = user["spring"] as? String
                            let summer = user["summer"] as? String
                            let year = user["award"] as? String
                            let award_active_status = user["award_active_status"] as? String
    
                            print ("award active status is : \(award_active_status!)")
                            
                            AwardInformationLab.getInstance.addAward(award: AwardsInformation(id: id, StudentId: std_id, Name: fname , LastName: lname, StatusId: status, SportId: sport_id, Level: level, SchoolId: school_id, School: school, Fall: fall, Winters: winter, Spring: spring, Summer: summer, Year: year, Student_type: student_type!, AdmitType: admit_type!, ClassLevel: class_level ,AwardActiveStatus: award_active_status!))
                     
                         
                        }
                     
                    }
                    DispatchQueue.main.sync {
                        self.tableViewAward.reloadData()
                    }
                    
                    
                }catch{
                    print ("convert or parsing to json is incorrect !")
                }
                
            }
            task.resume()
            
         
        }
        

        
        
    }
    // this method for check duplicate year in arrays . . . . ;
    func isYearDuplicated(name : String) -> Bool{
        if(GlobalVariable.years_items.count == 0 || GlobalVariable.years_items.count == 1){
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
