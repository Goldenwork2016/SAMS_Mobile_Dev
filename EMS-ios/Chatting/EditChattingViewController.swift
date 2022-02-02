//
//  EditChattingViewController.swift
//  SidebarMenu
//
//  Created by admin on 6/11/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class EditChattingViewController: UIViewController {
    var isEdited = true ;
    
    var chatId : String?
    var message : String?
    var user_id : String?
    var image : String?
    var onSave:((_ isSaved :Bool)->())?
    
    @IBOutlet weak var EditButton: UIButton!
    @IBOutlet weak var ImageAttach: UIImageView!
    @IBOutlet weak var TextMessage: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
      TextMessage.text = message!
        getImageFromUrl(imageUrl: image!)
     
    }
    
    
    
   

    @IBAction func CancelButton(_ sender: UIButton) {
        self.dismiss(animated: true, completion: nil)
        onSave!(true)

    }
    
    @IBAction func DeleteButton(_ sender: UIButton) {
     
        
        deleteChatFromDatabase()
        onSave!(true)
        self.dismiss(animated: true, completion: nil)
      
        
        
    }
    
    @IBAction func EditButton(_ sender: UIButton) {
        if (isEdited == true){
            TextMessage.isEnabled = true ;
            EditButton.setTitle("Save", for: .normal)
            isEdited = false ;
            
        }else {
            // update message here . . . . ;
            TextMessage.isEnabled = false
            editChatFromDatabase()
            
            isEdited = true
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    // delete chat from database . . . . ;
    func deleteChatFromDatabase(){
        let URL_DELETE_CHAT = "https://admin.emsystemsolutions.com/api/remove_chat?chat_id=\(chatId!)"
        // parse chatting url to url datatype . . . . ;
        let url = URL(string: URL_DELETE_CHAT)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        
       
        
        // getting response for post method . . . . ;
        DispatchQueue.global().async {
            let task = URLSession.shared.dataTask(with: request){(data:Data?,response:URLResponse?,error:Error?) in
                if(error != nil){
                    print ("error\(error)")
                    
                    return
                }
                // getting values from json response . . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                
                 print (ResponseString)
                self.onSave!(true)
                
    }
            task.resume()
    
}
}
    
    // edit chat from database . . . .;
    func editChatFromDatabase(){
        let URL_EDIT_CHATTING = "https://admin.emsystemsolutions.com/api/edit_chat"
        // parse chatting url to url datatype . . . . ;
        let url = URL(string: URL_EDIT_CHATTING)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        let postString = "chat_id= \(chatId!)&message= \(TextMessage.text!)&user_id= \(user_id!)"
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
                 print (ResponseString)
                self.onSave!(true)
            }
            task.resume()
        }
    }
    
    func getImageFromUrl(imageUrl : String?){
        do{
            let url = URL(string: imageUrl!)
            let data = try Data(contentsOf: url!)
            
            self.ImageAttach.image = UIImage(data: data)
            
        }catch{
            print(error)
        }
    }
}
