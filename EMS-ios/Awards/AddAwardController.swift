//
//  AddAwardController.swift
//  SidebarMenu
//
//  Created by admin on 6/7/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class AddAwardController: UIViewController , UIPickerViewDelegate , UIPickerViewDataSource {
   
    @IBOutlet weak var summerLabelup: NSLayoutConstraint!
    
    @IBOutlet weak var springTextUp: UIView!
    @IBOutlet weak var springUp: NSLayoutConstraint!
    @IBOutlet weak var fallLabel: UILabel!
    
    @IBOutlet weak var fallLabelH: NSLayoutConstraint!
    
    @IBOutlet weak var fallTextH: NSLayoutConstraint!
    
    @IBOutlet weak var SummerTopEditCons: NSLayoutConstraint!
    @IBOutlet weak var summerUp: NSLayoutConstraint!
    @IBOutlet weak var springTextH: NSLayoutConstraint!
    @IBOutlet weak var summerTextH: NSLayoutConstraint!
    @IBOutlet weak var winterTextH: NSLayoutConstraint!
    @IBOutlet weak var springLabelH: NSLayoutConstraint!
    @IBOutlet weak var winterlabelH: NSLayoutConstraint!
    @IBOutlet weak var summerLabelH: NSLayoutConstraint!
    @IBOutlet weak var WinterText: UITextField!
    @IBOutlet weak var SummerText: UITextField!
    @IBOutlet weak var winterLabel: UILabel!
    @IBOutlet weak var summerLabel: UILabel!
    @IBOutlet weak var springLabel: UILabel!
    @IBOutlet weak var StudentIdText: UITextField!
    @IBOutlet weak var txtError: UILabel!
    @IBOutlet weak var SpringText: UITextField!
    @IBOutlet weak var FallText: UITextField!
    @IBOutlet weak var LevelPickerView: UIPickerView!
    @IBOutlet weak var SportPickerView: UIPickerView!
    @IBOutlet weak var LastNameText: UITextField!
    @IBOutlet weak var FirstNameText: UITextField!
    @IBOutlet weak var YearPickerView: UIPickerView!
    @IBOutlet weak var SchoolPickerView: UIPickerView!
     var onAdd:((_ isAdded :Bool)->())?

    // declare variable . . . ;
    var levels = ["JV" , "Varsity"]
    let ADD_URL = "https://sams.emsystemsolutions.com/api/add_award"
    
    override func viewDidLoad() {
        super.viewDidLoad()
       hideSeason(visible: true)
        showEdit(position: 0)
        // Do any additional setup after loading the view.
    }
    
    // put title inside picer view . . . ;
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == SchoolPickerView {
           return GlobalVariable.school_items[row].getName()
        }else if pickerView == YearPickerView {
            return GlobalVariable.years_id[row]
        } else if pickerView == SportPickerView {
            return GlobalVariable.sports_items[row].getName()
        }else {
            return levels[row]
        }
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // to set number of rows in picker view . . . . ;
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if(pickerView == SchoolPickerView){
            return GlobalVariable.school_id.count
        }else if (pickerView == YearPickerView){
            return GlobalVariable.years_id.count
        }else if (pickerView == SportPickerView){
            return GlobalVariable.sport_id.count
        }else{
            return levels.count
        }
    }
   
    @IBAction func SaveButton(_ sender: UIButton) {
        txtError.text = "..."
        putAwardInsideDatabase();
        
        
    }
    
    // validate if input is nil . . . ;
    func validateInputIsEmpty()->Bool{
   
        if(StudentIdText!.text == nil ||
            FirstNameText!.text == nil ||
            LastNameText!.text == nil ||
            StudentIdText!.text == "" ||
            FirstNameText!.text == "" ||
            LastNameText!.text == "" ||
            (!WinterText.isHidden && WinterText.text == nil) ||
             (!WinterText.isHidden && WinterText.text == "") ||
            (!SummerText.isHidden && SummerText.text == nil) ||
            (!SummerText.isHidden && SummerText.text == "") ||
            (!SpringText.isHidden && SpringText.text == nil) ||
            (!SpringText.isHidden && SpringText.text == "") ||
            (!FallText.isHidden && FallText.text == nil) ||
            (!FallText.isHidden && FallText.text == "")
            ){
            
        return false ;
        }
        return true ;
    }
    
    @IBAction func CancelButton(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
        onAdd!(false)
    }
    
   
    
    
    // put award inside database . . . ;
    func putAwardInsideDatabase(){
        
        if(!validateInputIsEmpty()){
            txtError.text = "Please fill the required info !"
            // put message label here  . . . . ;
            return
        }
        let user_id = GlobalVariable.userId
        
        var selectedSchool = SchoolPickerView.selectedRow(inComponent: 0)
        let school = GlobalVariable.school_items[selectedSchool].getId()!
        
        var selectedYear = YearPickerView.selectedRow(inComponent: 0)
        let year = GlobalVariable.years_items[selectedYear].getId()!
        
        let student_id = String(describing: StudentIdText.text!)
        
        var first_name = String(describing: FirstNameText.text!)
        
        var last_name = String(describing: LastNameText.text!)
        
        let selectedSport = SportPickerView.selectedRow(inComponent: 0)
        let sport = GlobalVariable.sports_items[selectedSport].getId()!
        
        let selectLevel = LevelPickerView.selectedRow(inComponent: 0)
        
        let level = levels[selectLevel]
        
        
        let fall = String(describing: FallText.text!)
        
        let spring = String (describing: SpringText.text!)
        
        let summer = String(describing: SummerText.text!)
        let winter = String(describing: WinterText.text!)
     
        let url = URL(string: ADD_URL)
        
        var request = URLRequest(url: url!)
        request.httpMethod = "POST"
        
        let postString = "user_id=\(user_id)&college_name=\(school)&award=\(year)&s_id=\(student_id)&fname=\(first_name)&lname=\(last_name)&sport=\(sport)&sport_level=\(level)&fall=\(fall)&spring=\(spring)&summer=\(summer)&winter=\(winter)"
        
        print (postString)
        
        request.httpBody = postString.data(using: String.Encoding.utf8)
        
        DispatchQueue.main.async {
          let task = URLSession.shared.dataTask(with: request)
            {(data:Data?,response:URLResponse?,error:Error?) in
                if(error != nil){
                    print ("error\(error)")
                    self.txtError.text = "Something was Error !"
                    return
                }
                // get response . . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                print ("response String is \(ResponseString)")
                
                // check response ..... ;
                if(ResponseString!.contains("add successfull")){
                    self.onAdd!(true)
                    self.txtError.text = "Add Successfull"
                    
                    self.txtError.textColor = UIColor.green
                    // put message to ensure update . . . ;
                    
                    // dismiss this page after add award . . . ;
                    self.dismiss(animated: true, completion: nil)
                }
                else
                {
                    
                        self.txtError.text = "Student not found";
                        
                }
                
            }
            task.resume()
         
        }
        
        
    }
    
    
    //create method for picker view ( to check selected row . . . ) ;
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == YearPickerView {
            
            showEdit(position: row)
            
        }// end checking picker view paranthes  . . . ;
    }
    
    
    // func for selected year . . . . ;
    func showEdit(position : Int){
        // hide views before show correct season ( Edit Text ) . . . . ;
        hideSeason(visible: false)
        // get year name from global array in GlobalVariable class . . . .;
        var yearName = GlobalVariable.years_id[position]
        print (" year name is \(yearName)")
        
        // get length from yearitem in global variable . . . ;
        var YearLength = GlobalVariable.years_items.count-1
        
        // to loop in year items . . . ;
        for i in 0...YearLength {
            
            if (yearName == GlobalVariable.years_items[i].getName()){
                var yearId = GlobalVariable.years_items[i].getId()
                
                // create loop for loop inside term items and get terms . . . .;
                for j in 0...GlobalVariable.yearFormatted.count-1{
                    
                    // check if user id == terms item then show specific season . . . ;
                    if (yearId == GlobalVariable.year_formatted_items[j].getId()){
                        var season = GlobalVariable.year_formatted_items[j].getName()
                        print("term is : \(season)")
                        
                        getSeason(term: season!)
                    }// end if statement that get season  . . . ;
                    
                }// end for loop for get terms from global Variable list items  . . .;
                
                
                
            }// end if statement for check year name . . . .;
            
            
            
        }// end bracket for looping in year items . . . ;
        
    }
    
    // visibility of  edit text ( fall , spring  , winter , summer ) . . . .;
    func hideSeason(visible : Bool){
    
            // for fall label and fall text . . . ;
            self.fallLabelH.constant = 0
            self.fallLabel.isHidden = true
            
            self.fallTextH.constant = 0
            self.FallText.isHidden = true
            
            fallLabel.setNeedsUpdateConstraints()
            FallText.setNeedsUpdateConstraints()
           
            
            
            // for winter label and winter text . .. ;
            self.winterLabel.isHidden = true
            self.WinterText.isHidden = true
            
            self.winterlabelH.constant = 0
            self.winterTextH.constant = 0
            
            winterLabel.setNeedsUpdateConstraints()
            WinterText.setNeedsUpdateConstraints()
            
            
            
            
            // for spring label and spring text . . . ;
            self.springLabelH.constant = 0
            self.springTextH.constant = 0
            
            
            springLabel.isHidden = true;
            SpringText.isHidden = true ;
            
            springLabel.setNeedsUpdateConstraints()
            SpringText.setNeedsUpdateConstraints()
            
            // for summer label and summer text . . . . ;
            self.summerLabelH.constant = 0
            
            self.summerTextH.constant = 0
            summerLabel.isHidden = true ;
            SummerText.isHidden  = true ;
        
            self.summerLabel.setNeedsUpdateConstraints()
            self.SummerText.setNeedsUpdateConstraints()
             self.view.layoutIfNeeded()
           /* fallLabel.isHidden = false;
            springLabel.isHidden = false;
            summerLabel.isHidden = false;
            winterLabel.isHidden = false;
            FallText.isHidden = false;
            SpringText.isHidden = false;
            SummerText.isHidden = false;
            WinterText.isHidden = false;
            */
            // set edit text empty . . . ;
            FallText.text = ""
            SpringText.text = ""
            WinterText.text = ""
            SummerText.text = ""
            
        
        
    }
    
    // method for get term depended of his term . . . ;
    func getSeason(term : String){
        switch term {
        case "1":
            
            self.fallLabelH.constant = 21
            self.fallLabel.isHidden = false
            
            self.fallTextH.constant = 30
            self.FallText.isHidden = false
            self.view.layoutIfNeeded()
            
           
          
        case "2":
              self.winterLabel.isHidden = false
              self.WinterText.isHidden = false
            
            self.winterlabelH.constant = 21
            self.winterTextH.constant = 30
            self.view.layoutIfNeeded()
            
            
         
        case "3":
            self.springLabelH.constant = 21
            
            
            self.springTextH.constant = 30
            
            self.springUp.constant = 16

            springLabel.isHidden = false;
            SpringText.isHidden = false ;
            self.view.layoutIfNeeded()
           
            
        default:
            self.summerLabelH.constant = 21
            self.springUp.constant = 0
            self.SummerTopEditCons.constant = 0 ;
            self.summerUp.constant = 0
            self.summerLabelup.constant = 0
            self.summerTextH.constant = 30
             summerLabel.isHidden = false ;
             SummerText.isHidden  = false ;
            self.view.layoutIfNeeded()

           
        }
    }
    
    
    
    
    
    
    
}
