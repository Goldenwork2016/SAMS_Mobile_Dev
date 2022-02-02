//
//  AddSupportViewController.swift
//  SidebarMenu
//
//  Created by admin on 6/8/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class AddSupportViewController: UIViewController {
     let ADD_SUPPORT_URL = "https://sams.emsystemsolutions.com/api/add_support"
    @IBOutlet weak var messageText: UITextView!
    @IBOutlet weak var subjectText: UITextField!
    var onAdd:((_ isAdded :Bool)->())?
    override func viewDidLoad() {
        super.viewDidLoad()

      
    }
    

    @IBAction func CancelBtn(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
        onAdd?(false)
    }
    
    @IBAction func AddSupportBtn(_ sender: UIButton) {
        if !validateViews(){
            // put toast message here .. . ;
            onAdd?(false)
            return
        }
        passDataToDatabase()
        
    }
    
    func passDataToDatabase(){
        
        let url = URL(string: ADD_SUPPORT_URL)
        
        var request = URLRequest(url: url!)
        request.httpMethod = "POST"
        
        let user_id = GlobalVariable.userId
        var ticket_subject : String!
        ticket_subject = String(describing: subjectText.text!)
        var ticket_message : String!
        ticket_message = String(describing: messageText.text!)
        
        
        let postString = "user_id=\(user_id)&ticket_subject=\(ticket_subject!)&ticket_message=\(ticket_message!)"
        
        print (postString)
        
        request.httpBody = postString.data(using: String.Encoding.utf8)
        
        DispatchQueue.main.async {
            let task = URLSession.shared.dataTask(with: request)
            {(data:Data?,response:URLResponse?,error:Error?) in
                if(error != nil){
                    print ("error\(error)")
                    return
                }
                // get response . . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                print ("response String is \(ResponseString)")
                
                // check response ..... ;
                if(ResponseString!.contains("add successfull")){
                    self.onAdd?(true)
                    // put message to ensure update . . . ;
                    
                    // dismiss this page after add award . . . ;
                    self.dismiss(animated: true, completion: nil)
                }
                
            }
            task.resume()
            
        }
        
    }// end passDatabase method . . ;
    
    func validateViews()->Bool{
        if (subjectText.text == ""||messageText.text == "" || subjectText.text == nil || messageText == nil){
            return false
        }
        return true
    }
}
