//
//  LoginControllerViewController.swift
//  SidebarMenu
//
//  Created by admin on 5/29/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class LoginControllerViewController: UIViewController {

    @IBOutlet var PasswordText: UITextField!
    @IBOutlet var EmailText: UITextField!
    @IBOutlet weak var label_text: UILabel!
    var isCorrect : Bool?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "login_segue"){
            let revealController = segue.destination as! SWRevealViewController
            
           
            let transition = CATransition()
            transition.duration = 0.5
            transition.type = CATransitionType.moveIn
            transition.subtype = CATransitionSubtype.fromLeft
            transition.timingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.easeInEaseOut)
            view.window!.layer.add(transition,forKey: kCATransition)
            present(revealController,animated: true,completion: nil)
    }
    }

    @IBAction func LoginBtn(_ sender: UIButton) {
        label_text.text = "" ;
        // set password text validate . . . . ;
        if(EmailText.text == "" || PasswordText.text == ""){
            label_text.text = "        Please Fill The Required Info !"
            return ;
        }else {
            let url = URL(string: "https://sams.emsystemsolutions.com/api/login/?email="+EmailText.text!  + "&password=" + PasswordText.text!)!
            
            //performSegue(withIdentifier: "login_segue", sender: nil)
            
            DispatchQueue.global().async {
            do {
                
                // get data from server . . . . ;
                let data = try Data(contentsOf: url)
                let Str_data = String(data: data , encoding: .utf8)
                print(Str_data)
                if Str_data?.contains("Email & Password is Incorrect!") == true{
                    
                   self.isCorrect = false
                    DispatchQueue.main.async {
                        self.label_text.text = "Your Email or Password is InCorrect !"

                    }
                    
                    return ;
                }else if (Str_data?.contains("not support mobile")) == true {
                    self.isCorrect = false ;
                    DispatchQueue.main.async {
                        self.label_text.text = "Your School doesn't Support Mobile App";
                    }
                    return ;
                }
                // convert data to json encode . . . . ;
                let json = try JSONSerialization.jsonObject(with: data) as! [String : Any]
                print(json);
                let userId = json["result"]
                let userType = json["user_type"]!
                let add = json["permission_add"]!
                let approve = json["permission_approve"]!
                let deny = json["permission_deny"]!
                let edit = json["permission_edit"]!
                
                GlobalVariable.userId = userId as! String
                GlobalVariable.userType = userType as! String
                GlobalVariable.permission_add = add as! String
                GlobalVariable.permission_approve = approve as! String
                GlobalVariable.permission_deny = deny as! String
                GlobalVariable.permission_edit = edit as! String
                print ("add \(add)  approve \(approve)  deny \(deny)  edit \(edit) ")
                
                   self.isCorrect = true
                
                    // Mark : this field to pass data to another controller . . . . ;
                   // self.label_text.text = "user id is \(GlobalVariable.userId), user type is \(GlobalVariable.userType)"
                    
                DispatchQueue.main.async {
                    self.performSegue(withIdentifier: "login_segue", sender: nil)
                }
                   // self.performSegue(withIdentifier: "login_segue", sender: nil)
                
                
            }
            catch {
                
                print ("Something Was Error !")
            }
                
            }
        
            
        /*    // for check if correct then login else toast message . . . . ;
            if isCorrect == true {
                self.label_text.text = "user id is \(GlobalVariable.userId), user type is \(GlobalVariable.userType)"
                self.performSegue(withIdentifier: "login_segue", sender: nil)
                
                return;
            }else if isCorrect == nil {
                print ("empty")
            }else{
            self.label_text.text = "Your Email or Password is InCorrect !"
            }
*/
        
        }
        
       
        
    }
    
}
