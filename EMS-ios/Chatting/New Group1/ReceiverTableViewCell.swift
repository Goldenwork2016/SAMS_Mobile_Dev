//
//  ReceiverTableViewCell.swift
//  SidebarMenu
//
//  Created by admin on 6/10/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class ReceiverTableViewCell: UITableViewCell {

    @IBOutlet weak var textMessageHC: NSLayoutConstraint!
    @IBOutlet weak var ContainerMessage: UIView!
    @IBOutlet weak var EditText: UILabel!
    @IBOutlet weak var DateText: UILabel!
    @IBOutlet weak var ImageAttach: UIImageView!
    @IBOutlet weak var MessageText: UILabel!
    @IBOutlet weak var PersonName: UILabel!
    @IBOutlet weak var PersonalImage: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    
    // to put data inside cell . . . . ; 
    func putDataInsideView(imagePerson : String , NamePerson : String , TextMessage : String , TextDate : String , imageAttach : String , attachement : String , removed : String , edited_admin : String , edited : String){
        
      
        // check if chat type is nothing then set empty text . . . with another required field . . . ;
        let chatType = getChatType(removed: removed, edit_admin: edited_admin, edited: edited)
        
        if(chatType == "nothing"){
            // set edit text empty . . . ;
            self.EditText.text = ""
        }else {
            // set edit text chat type . . . . ;
            self.EditText.text = chatType
        }
        
        // check if attachment is empty or not . . . ;
        if(attachement == "" || attachement == "null"){
            if (TextMessage == "user_upload_images"){
                MessageText.text = ""
                putImageInsideAttach(imageUrl: imageAttach)
            } else {
                // set ImageAttach hidden . . . . ;
                 ImageAttach.isHidden = true ;
            }
        } else {
            
            MessageText.text = TextMessage
  
            putImageInsideAttach(imageUrl: imageAttach)
        }
        
        // check if message contain user_upload_images then change text to empty . . . ;
        if (TextMessage == "user_upload_images"){
            self.MessageText.text = "";
        }else {
            self.MessageText.text = TextMessage;
        }
        
        // put image here .... (for person and attach ) ... ;
        self.PersonName.text = NamePerson;
        
        self.DateText.text = TextDate
        self.putImageInsideView(imageUrl: imagePerson)
        
    }
    
    func putImageInsideView( imageUrl : String){
     
        do{
            let url = URL(string: imageUrl)
            let data = try Data(contentsOf: url!)
            
            DispatchQueue.main.async {
                self.PersonalImage.image = UIImage(data: data)
            }
            
            
        }catch{
            print(error)
        }
    }
    
    
    func putImageInsideAttach( imageUrl : String){
        
          
        do{
     
            let url = URL(string: imageUrl)
            let data = try Data(contentsOf: url!)
            
            DispatchQueue.main.async {
                
            self.ImageAttach.image = UIImage(data: data)
            }
        }catch{
            print(error)
        }
    }
    }
    
    // get chat type ( if exist .. for admin only ) . . . ;
    func getChatType(removed : String? , edit_admin : String? , edited : String? )->String
    {
        if (removed != ""){
            return "(deleted)"
        }
        if (edit_admin != ""){
            return "(edited above)"
        }
        if (edited != ""){
            return "(edited)"
        }
        return "nothing"
    }
    
  
    
    



