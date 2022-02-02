//
//  SportDashboardViewController.swift
//  SidebarMenu
//
//  Created by admin on 6/21/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class SportDashboardViewController: UIViewController , UITableViewDataSource , UITableViewDelegate  {
   
    let URL_DASHBOARD_SPORT = "https://sams.emsystemsolutions.com/api/sport_selection" ;
    
    var sportId = ""
    var selectedSport = ""
     var sportArray = [String]()
    var selectedSportArray = [String]()
    
   var onAdd:((_ isAdded :Bool)->())?

    @IBOutlet weak var tableViewSport: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        // to trim spots id  and selected sports . . . . .
        sportId = sportId.trimmingCharacters(in: .whitespaces)
        selectedSport = selectedSport.trimmingCharacters(in: .whitespaces)
        print("sport id is =\(sportId)")
        print("selected sport  is =\(selectedSport)")
        // split sport id , selected sport inside s arrays . . . ;
        sportArray = sportId.components(separatedBy: ",")
        selectedSportArray = selectedSport.components(separatedBy: ",")
      
      
        // Do any additional setup after loading the view.
    }
    

    @IBAction func cancelBtn(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func saveBtn(_ sender: Any) {
        var sports = "";
        for n in 0...sportId.count-1 {
            let indexPath = IndexPath(row: n , section: 0)
            let cell = tableViewSport.cellForRow(at: indexPath ) as? SportDashboardTableViewCell
            if(cell?.CheckedSport.isSelected == true){
                if(sports == ""){
                    sports += "\(sportArray[n])"
                    print (sports) ;
                }else{
                    sports += ",\(sportArray[n])"
                    print (sports) ;
                }
            }
        }
        print ("sport is \(sports)")
        putSportInsideDatabase(sports: sports)
        //reload previous screen before dismiss . . . . ;
        
        self.dismiss(animated: true , completion: nil)
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
      //return testArray.count
        return sportArray.count
           }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableViewSport.dequeueReusableCell(withIdentifier: "sport_cell", for: indexPath) as! SportDashboardTableViewCell
        var sport = GlobalVariable.getSportName(sport: sportArray[indexPath.row])
        cell.sportLabel.text = sport
        cell.selectionStyle = .none
        
        
        for n in 0...selectedSportArray.count-1{
            if(sportArray[indexPath.row] == selectedSportArray[n]){
                print ("sport selected")
                
                cell.CheckedSport.isSelected = true
                selectedSportArray[n] = "no"
                
            }
        }
     cell.CheckedSport.addTarget(self, action: #selector(checkMarkButtonClicked(sender:)), for: .touchUpInside)
        return cell ;
                               }
    
    @objc func checkMarkButtonClicked(sender: UIButton){
        if(sender.isSelected){
            // if button is checked unchick . . . . ;
            sender.isSelected = false
           // sender.setImage(UIImage(named:"checked"), for: .normal)
            print("unselected");
            //
        }else{
            sender.isSelected = true ;
           // sender.setImage(UIImage(named:"unchecked"), for: .normal)
            print("selected");

            
        }
        tableViewSport.reloadData()
        
    }
    
    
    func putSportInsideDatabase(sports : String){
        // parse chatting url to url datatype . . . . ;
        let url = URL(string: URL_DASHBOARD_SPORT)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        let postString = "user_id= \(GlobalVariable.userId)&sportsFilterHome=\(sports)"
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
                self.onAdd!(true)
                
                
                
            }
            task.resume()
            
            
        }
        
    }
    
    
  
    

}
