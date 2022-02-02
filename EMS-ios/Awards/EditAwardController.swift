//
//  EditAwardController.swift
//  SidebarMenu
//
//  Created by admin on 6/3/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class EditAwardController: UIViewController ,UIPickerViewDelegate, UIPickerViewDataSource {
    
    @IBOutlet weak var nav_item: UINavigationItem!
    // for season constraint . . . . ;
    @IBOutlet weak var summerTop: NSLayoutConstraint!
    @IBOutlet weak var navigationBar: UINavigationBar!
    
    @IBOutlet weak var fallTop: NSLayoutConstraint!
    @IBOutlet var summerTopCons: UIView!
    @IBOutlet weak var summerTopEditCons: NSLayoutConstraint!
    
    // for label season height constraints . . . ;
    @IBOutlet weak var springLabelH: NSLayoutConstraint!
    @IBOutlet weak var summerLabelH: NSLayoutConstraint!
    @IBOutlet weak var winterLabelH: NSLayoutConstraint!
    @IBOutlet weak var fallLabelH: NSLayoutConstraint!
    
    @IBOutlet weak var scrollViewEdit: UIScrollView!
    // for edittext season height constraint . . . ;
    @IBOutlet weak var saveBtn: UIButton!
    
    
    @IBOutlet weak var summerEditH: NSLayoutConstraint!
    @IBOutlet weak var springEditH: NSLayoutConstraint!
    @IBOutlet weak var fallEditH: NSLayoutConstraint!
    @IBOutlet weak var winterEditH: NSLayoutConstraint!
    
    @IBOutlet weak var summerEditTop: NSLayoutConstraint!

    @IBOutlet weak var denyBtn: UIButton!
    @IBOutlet weak var approveBtn: UIButton!
    @IBOutlet weak var fallEditTop: NSLayoutConstraint!
    @IBOutlet weak var EditSummer: UITextField!
    @IBOutlet weak var labelSummer: UILabel!
    @IBOutlet weak var labelWinter: UILabel!
    @IBOutlet weak var labelFall: UILabel!
    @IBOutlet weak var labelSpring: UILabel!
    @IBOutlet weak var EditWinter: UITextField!
    @IBOutlet weak var EditStudentId: UITextField!
    @IBOutlet weak var txtError: UILabel!
    @IBOutlet weak var Editbtn: UIBarButtonItem!
    @IBOutlet weak var EditSpring: UITextField!
    @IBOutlet weak var EditFall: UITextField!
    @IBOutlet weak var EditLastName: UITextField!
    @IBOutlet weak var EditFirstName: UITextField!
    @IBOutlet weak var SportPickerView: UIPickerView!
    @IBOutlet weak var YearPickerView: UIPickerView!
    @IBOutlet weak var SchoolPickerView: UIPickerView!
    @IBOutlet weak var LevelPickerView: UIPickerView!
    var indexItem:String = ""
    var school :String?
    var sport : String?
    var year : String?
    var showView = false
    let UPDATE_URL = "https://sams.emsystemsolutions.com/api/update"
    var information : AwardsInformation? = nil
    var levels = ["JV","Varsity"]
    
    // implement when start this view controller . . . ;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
       
        AwardControllerViewController.count = 0
        information=AwardInformationLab.getInstance.getStudentInfo(id: indexItem)!;
        EditFirstName.text = information?.Name
        EditLastName.text = information?.LastName
        EditStudentId.text = information!.StudentId
        EditFall.text = information!.Fall
        EditSpring.text = information!.Spring
        EditSummer.text = information!.Summer
        EditWinter.text = information!.Winters
        
        // to hide season . . . . ;
        hideSeason(visible: true)
        
        ApproveAndDenyVisibility(award_active_status: information!.AwardActiveStatus!)
        
       
     
        // for school . . . ;*/
        let schoolRow = GlobalVariable.school_id.firstIndex(of: String(describing: information!.SchoolId!))
    
        SchoolPickerView.selectRow(schoolRow!, inComponent: 0, animated: true)
        
        var sportRow = GlobalVariable.sport_id.firstIndex(of: String(describing: information!.SportId!))
        
        if(sportRow != nil){
       SportPickerView.selectRow(sportRow!, inComponent: 0, animated: true)
        }
        
        let levelRow = levels.firstIndex(of: String(describing: information!.Level!))
        LevelPickerView.selectRow(levelRow!, inComponent: 0, animated: true)
        
       // let yearid = GlobalVariable.getYearId(year: String(describing: information!.Year!));
        let yearRow = GlobalVariable.years_id.firstIndex(of: information!.Year!);
        showEdit(position: yearRow!)
        YearPickerView.selectRow(yearRow!, inComponent: 0, animated: true)
        
        
        
        if(GlobalVariable.permission_edit == "0"){
        self.Editbtn.isEnabled = false ;
        self.Editbtn.tintColor = UIColor.clear ;
        }
    }
    
   
    
   
    // this method for check visibilty for approve and deny depending of his permission . . .;
    func ApproveAndDenyVisibility(award_active_status : String) {
        
        // if deny == 0 .. then hide deny button . . . ;
        if(GlobalVariable.permission_deny == "0" || award_active_status != "1"){
            self.denyBtn.isHidden = true;
        }
        // if approve == 0 .. then hide approve button . . . ;
        if(GlobalVariable.permission_approve == "0" || award_active_status != "1"){
        self.approveBtn.isHidden = true ;
        }
        
        
    }
 
    
    // number of component . . . ;
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // number of row in component . . . ;
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        
        // for school picker view . . . . ;
        if pickerView == SchoolPickerView{
           return  GlobalVariable.school_id.count
            
            // for sport picker view . . . ;
        }else if pickerView == SportPickerView{
           return GlobalVariable.sport_id.count
        }else if pickerView == YearPickerView{
            // for year picker view . . . ;
           return  GlobalVariable.years_id.count
        }else{
            return levels.count
        }

        
    }
    
    
    // title for every component . . . ;
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == SchoolPickerView{
         return GlobalVariable.school_items[row].getName()
            
            // for sport picker view . . . ;
        }else if pickerView == SportPickerView{
            return GlobalVariable.sports_items[row].getName()
        }else if pickerView == YearPickerView{
            
            // for year picker view . . . ;
            return  GlobalVariable.years_id[row]
        }else{
            return levels[row]
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        if pickerView == SportPickerView{
            sport = GlobalVariable.sports_items[row].getId()
            
            
        }else if pickerView == YearPickerView{
             year = GlobalVariable.years_items[row].getId()
            showEdit(position: row)
            
            
            
            
        }else if pickerView == SchoolPickerView{
            school = GlobalVariable.school_items[row].getId()
        }else{
            print("ok")
        }
        
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
    
    
    // ( when click in back button ) . . ;
    @IBAction func BackBtn(_ sender: UIBarButtonItem) {
        // when pressed back button . . . ;
          self.dismissViewController()
    }
 
    @IBAction func SaveBtn(_ sender: UIButton) {
        txtError.text = "..."
        if(!validateView()){
            txtError.frame.origin = CGPoint(x: 250, y: 523);
            txtError.text = " ..             Please fill the required info !"
            return
        }
      UpdateAward()
        print("save button")
    }
    // admin@emsystemsolutions.com
    // admin123
    // for update award section . . . ;
    func UpdateAward(){
     SeasonAvailable()
        var awardInfo = AwardInformationLab.getInstance.getStudentInfo(id: indexItem)
        var award = AwardInformationLab.getInstance.getAwards();
        // put all information inside variable . . . ;
        let id = indexItem
        let s_id = EditStudentId.text!
        let fname = String(describing: EditFirstName.text!)
        let lname = String(describing: EditLastName.text!)
        let selectedSport = SportPickerView.selectedRow(inComponent: 0)
        let sportV = GlobalVariable.sports_items[selectedSport].getId()!
        
        let selectedSchool = SchoolPickerView.selectedRow(inComponent: 0)
        let schoolV=GlobalVariable.school_items[selectedSchool].getId()!
        
        let selectedYear = YearPickerView.selectedRow(inComponent: 0)
        let yearV = GlobalVariable.years_items[selectedYear].getId()!
        
        let fall = String(describing: EditFall.text!)
        let spring = String(describing: EditSpring.text!)
        let std_type = awardInfo!.Student_type!
        var admit_type = awardInfo!.AdmitType

        // check admit type . . . ;
        if (admit_type != nil){
            admit_type = awardInfo!.AdmitType!
        }else{
            admit_type = ""
        }
        let class_level = awardInfo!.ClassLevel
        let winter = String(describing: EditWinter.text!)
        let selectedLevel = LevelPickerView.selectedRow(inComponent: 0)
        let level = levels[selectedLevel]
        let summer = String(describing: EditSummer.text!)
       
        
        // parse award url to url datatype . . . . ;
        let url = URL(string: UPDATE_URL)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
       // replace level with spinner with to choices (JV , Varsity)
      
        let postString = "student_id=\(id)&college_name=\(schoolV)&award=\(yearV)&s_id=\(s_id)&fname=\(fname)&lname=\(lname)&student_type=\(std_type)&admit_type=\(String(describing: admit_type)) &class_level=\(class_level)&sport=\(sportV)&sport_level=\(level)&fall=\(fall)& winter=\(String(describing: winter))&spring=\(spring)&summer=\(String(describing: summer))"
        
        print(postString)
        
        request.httpBody = postString.data(using: .utf8)
        DispatchQueue.main.async {
            print ("thread start")
            
            let task = URLSession.shared.dataTask(with: request)
            {(data:Data?,response:URLResponse?,error:Error?) in
                if(error != nil){
                    print ("error")
                    print ("error\(error)")
                    self.txtError.text = "Something was error ! "
                    return
                }
                // get response  . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                print("Response String is \(ResponseString)")
                
                // check response and reload edit page . . . ;
                if(ResponseString!.contains("updated Successfully!")){
                    self.txtError.textColor=UIColor.green
                    self.txtError.text = "Updated Successfull"
                    AwardControllerViewController.count = 1
                     self.dismissViewController()
                }
                
            }
            task.resume()
        }
    }
    
  
    @IBAction func EditBtn(_ sender: UIBarButtonItem) {
     var items = self.navigationBar.items
        if showView{
            showView = false
            nav_item.rightBarButtonItem?.image = UIImage(named: "edit_update")
            showViews(shows: false)
            Editbtn.title = "Edit"
            putDataInsideViews()
             txtError.text = "..."
            saveBtn.isHidden = true ;
            
        
            // show deny and approve if edit is disable . . .;
            ApproveAndDenyVisibility(award_active_status: information!.AwardActiveStatus!)
            
            // to return selected row in picker view . . . ;
            // for school . . . ;*/
            let schoolRow = GlobalVariable.school_id.firstIndex(of: String(describing: information!.SchoolId!))
            
            SchoolPickerView.selectRow(schoolRow!, inComponent: 0, animated: true)
            
            var sportRow = GlobalVariable.sport_id.firstIndex(of: String(describing: information!.SportId!))
            
            if(sportRow != nil){
                SportPickerView.selectRow(sportRow!, inComponent: 0, animated: true)
            }
            
            let levelRow = levels.firstIndex(of: String(describing: information!.Level!))
            LevelPickerView.selectRow(levelRow!, inComponent: 0, animated: true)
            
            // let yearid = GlobalVariable.getYearId(year: String(describing: information!.Year!));
            let yearRow = GlobalVariable.years_id.firstIndex(of: information!.Year!);
            showEdit(position: yearRow!)
            YearPickerView.selectRow(yearRow!, inComponent: 0, animated: true)
            
            
            
           
        }else{
            showView = true
            Editbtn.title = "Cancel"
            nav_item.rightBarButtonItem?.image = UIImage(named: "cancel")
            showViews(shows: true)
            saveBtn.isHidden = false ;
            
            // hide  deny and approve if edit is enable. . .
            denyBtn.isHidden = true
            approveBtn.isHidden = true
           
        }
    
    }
    // for set enabled for views . . . . ;
    func showViews(shows:Bool){
        EditStudentId.isUserInteractionEnabled = shows
       EditSpring.isUserInteractionEnabled = shows
        EditSummer.isUserInteractionEnabled = shows
        EditWinter.isUserInteractionEnabled = shows
       EditFall.isUserInteractionEnabled = shows
      EditLastName.isUserInteractionEnabled = shows
        EditFirstName.isUserInteractionEnabled = shows
        SportPickerView.isUserInteractionEnabled = shows
        YearPickerView.isUserInteractionEnabled = shows
        SchoolPickerView.isUserInteractionEnabled = shows
        LevelPickerView.isUserInteractionEnabled = shows
        if(shows){
            // change image when click . . . . ;
         var image = UIImage(named: "cancel")
            image = image?.withRenderingMode(.alwaysOriginal)
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(image: image, style: .plain, target: nil, action: nil)
            
        }else {
            var image = UIImage(named: "edit")
            image = image?.withRenderingMode(.alwaysOriginal)
            self.navigationItem.rightBarButtonItem = UIBarButtonItem(image: image, style: .plain, target: nil, action: nil)
        }
    }
    
    // validate views . . . ;
    func validateView ()->Bool{
        if (EditStudentId.text != nil  && EditLastName.text != nil && EditFirstName.text != nil && EditFall.text != nil && EditSpring.text != nil){
            
            if (EditStudentId.text != "" && EditLastName.text != "" && EditFirstName.text != "" && EditFall.text != "" && EditSpring.text != ""){
                return true
            }
        }
        return false
    }
    
    func putDataInsideViews(){
        let information=AwardInformationLab.getInstance.getStudentInfo(id: indexItem);
        EditFirstName.text = information!.Name
        EditLastName.text = information!.LastName
        EditStudentId.text = information!.StudentId
        EditFall.text = information!.Fall
        EditSpring.text = information!.Spring
    }
    
    func dismissViewController (){
        let transition = CATransition()
        transition.duration = 0.5
        transition.type = CATransitionType.moveIn
        transition.subtype = CATransitionSubtype.fromRight
        transition.timingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut)
        view.window!.layer.add(transition,forKey: kCATransition)
        self.dismiss(animated: false, completion: nil)
    }
    
    
    
    // method for get term depended of his term . . . ;
    func getSeason(term : String){
        switch term {
        case "1":
            
            self.fallLabelH.constant = 26
            self.labelFall.isHidden = false
            self.fallTop.constant = 12
            self.summerTop.constant = 0
            self.summerEditTop.constant = 0
            self.fallEditTop.constant = 8
            self.fallEditH.constant = 30
            self.EditFall.isHidden = false
            self.view.layoutIfNeeded()
            
            
            
        case "2":
            self.labelWinter.isHidden = false
            self.EditWinter.isHidden = false
            
            self.winterLabelH.constant = 26
            self.winterEditH.constant = 30
            
            self.view.layoutIfNeeded()
            
            
            
        case "3":
            self.springLabelH.constant = 21
            
            
            self.springEditH.constant = 30
            
            
            labelSpring.isHidden = false;
            EditSpring.isHidden = false ;
            self.view.layoutIfNeeded()
            
            
        default:
            self.fallTop.constant = 0
            self.summerTop.constant = 0
            self.summerEditTop.constant = 0
            self.fallEditTop.constant = 0
            self.summerLabelH.constant = 21
            self.summerEditH.constant = 30
            labelSummer.isHidden = false ;
            EditSummer.isHidden  = false ;
            self.view.layoutIfNeeded()
            
            
        }
    }
    
    // visibility of  edit text ( fall , spring  , winter , summer ) . . . .;
    func hideSeason(visible : Bool){
        
        // for fall label and fall text . . . ;
        self.fallLabelH.constant = 0
        self.labelFall.isHidden = true
        
        self.fallEditH.constant = 0
        self.EditFall.isHidden = true
        
        labelFall.setNeedsUpdateConstraints()
        EditFall.setNeedsUpdateConstraints()
        
        
        
        // for winter label and winter text . .. ;
        self.labelWinter.isHidden = true
        self.EditWinter.isHidden = true
        
        self.winterLabelH.constant = 0
        self.winterEditH.constant = 0
        
        labelWinter.setNeedsUpdateConstraints()
        EditWinter.setNeedsUpdateConstraints()
        
        
        
        
        // for spring label and spring text . . . ;
        self.springLabelH.constant = 0
        self.springEditH.constant = 0
        
        
        labelSpring.isHidden = true;
        EditSpring.isHidden = true ;
        
        labelSpring.setNeedsUpdateConstraints()
        EditSpring.setNeedsUpdateConstraints()
        
        // for summer label and summer text . . . . ;
        self.summerLabelH.constant = 0
        
        self.summerEditH.constant = 0
        labelSummer.isHidden = true ;
        EditSummer.isHidden  = true ;
        
        self.labelSummer.setNeedsUpdateConstraints()
        self.EditSummer.setNeedsUpdateConstraints()
        self.view.layoutIfNeeded()
        
      
        
    }
    
    func SeasonAvailable(){
        // here we put logical if statements to clear edit season if is not visible on screen . . . ;
        
        if (EditFall.isHidden == true){
            EditFall.text = ""
        }
        if (EditWinter.isHidden == true){
            EditWinter.text = ""
        }
        if (EditSpring.isHidden == true){
            EditSpring.text = ""
        }
        if (EditSummer.isHidden == true){
            EditSummer.text = ""
        }
    }
    
    // when click on approve button . . .;
    @IBAction func ApproveBtn(_ sender: Any) {
       UpdateAwardStatusInDatabase(award_active_status: "approve")
        
    }
    
    // when click on deny button . . ;
    @IBAction func DenyBtn(_ sender: Any) {
        UpdateAwardStatusInDatabase(award_active_status: "deny")
    }
    
    // for update award_active_status . . .. . . .;
    func UpdateAwardStatusInDatabase(award_active_status : String){
        let award_status_url = "https://sams.emsystemsolutions.com/api/award_active_status";
        let url = URL(string: award_status_url)
        
        var request = URLRequest(url: url!)
        request.httpMethod = "POST"
        let postString = "id=\(information!.id!)&award_active_status=\(award_active_status)"
        print ("post string is \(postString)")
        request.httpBody = postString.data(using: String.Encoding.utf8)
        
        DispatchQueue.global().async {
            let task = URLSession.shared.dataTask(with: request)
            {(data:Data?,response:URLResponse?,error:Error?)
                in
                if(error != nil){
                    print ("error")
                    return
                }
                // get response . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                print ("response is \(ResponseString)")
                // check if response is "update successfull" ... then update true . . ;
                if((ResponseString!.contains("update successfull"))){
                   print("update successfull")
                    if(award_active_status == "approve"){
                        self.information!.AwardActiveStatus = "2"
                    }else{
                       self.information!.AwardActiveStatus = "5"
                    }
                    DispatchQueue.main.async {
                      
                    self.ApproveAndDenyVisibility(award_active_status: self.information!.AwardActiveStatus!)
                    print("award active status = \(self.information!.AwardActiveStatus!)")
                    }
                }
            }
            task.resume()
        }// close background thread . . . . ;
    }

}
