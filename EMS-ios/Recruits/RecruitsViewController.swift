
import UIKit

class RecruitsViewController: UIViewController , UITableViewDelegate , UITableViewDataSource  {
  
    @IBOutlet var addButton: UIBarButtonItem!
    @IBOutlet var menuButton: UIBarButtonItem!
    @IBOutlet var tableViewSupport: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
      
       
        
        self.view.addGestureRecognizer(self.revealViewController().panGestureRecognizer())
        // when click in menu button  . . . . ;
        menuButton.target = self.revealViewController()
        menuButton.action = Selector("revealToggle:")
      
      
        
     
    }
  
    override func viewWillAppear(_ animated: Bool) {
        SupportInformationLab.getInstance.clearSupportList()
       
        getDataFromDatabase()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return SupportInformationLab.getInstance.getSupport().count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var supportList = SupportInformationLab.getInstance.getSupport()
        let cell:TableViewCellSupport = tableViewSupport.dequeueReusableCell(withIdentifier: "supportcell") as! TableViewCellSupport
      
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
        
        cell.TicketLabel.text = supportList[indexPath.row].ticket!
        cell.SubjectLabel.text = supportList[indexPath.row].subject!
        cell.StatusImage.image = UIImage(named: getImageStatus(status: supportList[indexPath.row].status!))
        
        
        return cell
        
    }
    
    // to convert status to image . . . ;
    func getImageStatus(status : String)->String{
        if status == "3"{
            return "yellow"
        }else if status == "4"{
            return "blue"
        }
        return "green"
    }
    
    // number of section . . . ;
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    // get data from database . . . .; 
    func getDataFromDatabase (){
        let supportLab = SupportInformationLab.getInstance
        
        let RecruitsURL = URL(string: "https://sams.emsystemsolutions.com/Mobile/getRecruits?user_id=\(GlobalVariable.userId)&user_type=\(GlobalVariable.userType)")!
        
        // request for url . .;
        var request = URLRequest(url: RecruitsURL)
    
        
        // getting response for post method . . . . ;
        DispatchQueue.global().async {
            let task = URLSession.shared.dataTask(with: request){(data:Data?,response:URLResponse?,error:Error?) in
                if(error != nil){
                    print ("error\(error)")
                    
                    return
                }
                // getting values from json response . . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                print(ResponseString)
                
                // convert data to json response . . . ;
                do {
                    let json = try JSONSerialization.jsonObject(with: data!, options: []) as? [String:Any]
                    var user_id : String?
                    var cst_id : String?
                    var ticket : String?
                    var subject : String?
                    var status : String?
                    var name : String?
                    var type : String?
                    
                    
                    // to fetch support_ticket . .. . . ;
                    if  let support_ticket = json!["support_ticket"] as? [[String: Any]]{
                        for support in support_ticket {
                            // Mark : get all required data from this json response . . . ;
                             user_id = support["user_id"] as? String
                             cst_id = support["cst_id"] as? String
                            ticket = support["ticket_id"] as? String
                             subject = support["ticket_subject"] as? String
                             status = support["ticket_status"] as? String
                            GlobalVariable.cst_id = cst_id!
                      
                            // to get user name and permission . . . .;
                            if let user_account = json!["user_accounts_list"] as? [String: Any]{
                                
                                var userID = user_account[user_id!] as? AnyObject
                                name = userID!["name"] as? String
                                type = userID!["permission_type"] as? String
                                
                        
                            
                        }
                            // put data inside support lab  . . . ;
                            supportLab.addSupport(support: SupportInformation(id: user_id!, cst_id: cst_id!, user_type: type!, ticket: ticket!, subject: subject!, status: status!, username: name!))
                            
                            // print data to tested . . . .;
                            print ("id \(user_id!) cstid \(cst_id!) usertype \(type!) ticket \(ticket!) subject \(subject!) status \(status!) username \(name!)")
                        
                    }
                  
                 
                    
                 
                        
                        DispatchQueue.main.async {
                            self.tableViewSupport.reloadData()
                            
                        }
                    
                            
                        
                    
                    }// end if statment . . .
                    
                 
                    
                  
           
                    
                    
                }catch{
                    print ("convert or parsing to json is incorrect !")
                }
                
            }
            task.resume()
            
            
        }
        
        
        
    }
    
    
    // to pass data to another view controller . . . ;
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "support_segue"){
            
            // set destination view controller . . . ;
            let chattingController = segue.destination as! ChattingViewController
            
            // get selected row (position) .. . . . . ;
            let position = tableViewSupport.indexPathForSelectedRow
            
            // put data inside variable (from destination view controller . . . ; )
            chattingController.cst = SupportInformationLab.getInstance.getSupport()[position!.row].cst_id!
            chattingController.ticket_num = SupportInformationLab.getInstance.getSupport()[position!.row].ticket!
            
            let transition = CATransition()
            transition.duration = 0.5
            transition.type = CATransitionType.moveIn
            transition.subtype = CATransitionSubtype.fromLeft
            transition.timingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut)
            view.window!.layer.add(transition,forKey: kCATransition)
            present(chattingController,animated: true,completion: nil)
            
        } else if(segue.identifier == "add_support_segue"){
            let AddController = segue.destination as! AddSupportViewController
            AddController.onAdd = onAdd
            
           
        }
        
   }
    
    
    func onAdd (_ isAdded : Bool) -> () {
        SupportInformationLab.getInstance.clearSupportList()
        
        getDataFromDatabase()
    }
  
   

}
