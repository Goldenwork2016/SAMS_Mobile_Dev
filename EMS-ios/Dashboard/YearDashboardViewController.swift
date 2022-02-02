//
//  YearDashboardViewController.swift
//  SidebarMenu
//
//  Created by admin on 6/20/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class YearDashboardViewController: UIViewController , UITableViewDataSource ,UITableViewDelegate {
    var allyears = "";
    let URL_DASHBOARD_YEAR = "https://sams.emsystemsolutions.com/api/year_selection";
    // this array just for test table view . . . . ;
    var yearsArray = [String]() ;
    var selectedYear = "";
    var yearChoosen = Array<Int>();
    
    var onAdd:((_ isAdded :Bool)->())?

    @IBOutlet weak var tableViewYears: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        selectedYear = selectedYear.trimmingCharacters(in: .whitespaces)
        
        // split data inside year arrays . . . . ;
        // these variable are selected year . . . ;
        yearsArray = selectedYear.components(separatedBy: ",");
        
        
       

        
    }
    

    // when click save button . . . .;
    @IBAction func saveBtn(_ sender: UIButton) {
        var years = "";
        for n in 0...GlobalVariable.years_id.count-1 {
            let indexPath = IndexPath(row: n , section: 0)
            let cell = tableViewYears.cellForRow(at: indexPath ) as? YearDashboardTableViewCell
            if(cell?.CheckedBtn.isSelected == true){
                if(years == ""){
                  years += "\(GlobalVariable.years_id[n])"
                print (years) ;
                }else{
                    years += ",\(GlobalVariable.years_id[n])"
                    print (years) ;
                }
            }
        }
        print ("years is \(years)")
        putYearInsideDatabase(years: years)
        //reload previous screen before dismiss . . . . ;
        
        self.dismiss(animated: true , completion: nil)
        
    }
    
    
    // when click cancel button . . . . ;
    @IBAction func cancelBtn(_ sender: UIButton) {
        self.dismiss(animated: true
            , completion: nil)
    }
    
    
    // these method for table views . . . . ;
    // get number of rows in section . . . . ;
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return GlobalVariable.years_id.count
    }
    
    // put data inside every row in table view . . . . ;
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
    
     
        var cell = tableViewYears.dequeueReusableCell(withIdentifier: "year_cell", for: indexPath) as! YearDashboardTableViewCell
        
        cell.YearLabel.text = GlobalVariable.years_id[indexPath.row]
         cell.selectionStyle = .none
      
      
        // check if years is selected before . . . .;
        let indexPosition = indexPath.item ;
        for n in 0...yearsArray.count-1{
     
            if yearsArray[n] == GlobalVariable.years_id [indexPosition] {
          
                cell.CheckedBtn.isSelected = true ;
                yearsArray[n] = "no"
                print ("hello world")
            }// end if statement . . . ;
        }// end for loop . . . .;
        
   
       
        cell.CheckedBtn.addTarget(self, action: #selector(checkMarkButtonClicked(sender:)), for: .touchUpInside)
        return cell ; 
        
    }
    @objc func checkMarkButtonClicked(sender: UIButton){
        if(sender.isSelected){
            // if button is checked unchick . . . . ;
            sender.isSelected = false
            print("unchecked");
        }else{
            sender.isSelected = true ;

            print ("checked");
        }
        tableViewYears.reloadData()
        
    }
    
    
    
  
    
    
    
        
    
    
    
    func putYearInsideDatabase(years : String){
        // parse chatting url to url datatype . . . . ;
        let url = URL(string: URL_DASHBOARD_YEAR)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        let postString = "user_id= \(GlobalVariable.userId)&yearsFilterHome=\(years)"
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
