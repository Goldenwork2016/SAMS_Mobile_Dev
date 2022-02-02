//
//  ChattingViewController.swift
//  SidebarMenu
//
//  Created by admin on 6/10/19.
//  Copyright © 2019 AppCoda. All rights reserved.
///Users/admin

import UIKit

class ChattingViewController: UIViewController , UITableViewDelegate , UITableViewDataSource , UIImagePickerControllerDelegate , UINavigationControllerDelegate{
    
   
    @IBOutlet weak var ImageViewGallery: UIImageView!
    @IBOutlet weak var ImageContainer: UIView!
    @IBOutlet weak var tableViewChatting: UITableView!
    @IBOutlet weak var textMessage: UITextField!
    let URL_ADD_CHAT = "https://sams.emsystemsolutions.com/api/add_data"
    // url for chat data . . . .
    let URL_CHATTING = "https://sams.emsystemsolutions.com/api/chat_data"
    var imageGalleryString = ""
    
    // variable where getting from previous viewController . . . .;
    var cst :String!
    var user_id = GlobalVariable.userId
    var ticket_num : String!
    
    // list for store chatting (with chattingInformation datatype . . .; )
    var chattingList = Array<ChattingInformation>()
    

    
    @IBOutlet weak var replyButton: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        // to show reverse . . . ;
        tableViewChatting.transform = CGAffineTransform(rotationAngle: -(CGFloat)(Double.pi))
        
        // to hide image container . . . ;
         ImageContainer.isHidden = true ;
  
        // to remove separator line from table view . . . ;
        self.tableViewChatting.separatorStyle = UITableViewCell.SeparatorStyle.none
        
        let nibNameReceiver = UINib(nibName: "ReceiverTableViewCell", bundle: nil)
        
        tableViewChatting.register(nibNameReceiver, forCellReuseIdentifier: "cell_receiver")
        
        
        let nibNameSender = UINib(nibName: "SenderTableViewCell", bundle: nil)
        
        tableViewChatting.register(nibNameSender, forCellReuseIdentifier: "sender_cell")
        

      
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        getChattingFromDatabase()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return chattingList.count
    }

    
    // put if statement for sender and receiver for items . . . ;
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let count = chattingList.count-1
        let information = chattingList[count - indexPath.item]
        if(information.user_id! == GlobalVariable.userId){
            
            let cell = tableViewChatting.dequeueReusableCell(withIdentifier: "cell_receiver",for:indexPath) as! ReceiverTableViewCell
            cell.selectionStyle = .none
            cell.putDataInsideView(imagePerson: information.user_profile_image!, NamePerson: information.user_name!, TextMessage: information.message!, TextDate: information.chat_time!, imageAttach: information.image! , attachement : information.attachment! , removed : information.removed! , edited_admin : information.edited_admin! , edited : information.edited!)
            // to show reverse . . . ;
            cell.transform = CGAffineTransform(rotationAngle: CGFloat(Double.pi))
            return cell ;
        }else {
            
            
            let cell = tableViewChatting.dequeueReusableCell(withIdentifier: "sender_cell",for:indexPath) as! SenderTableViewCell
            cell.selectionStyle = .none
            cell.putDataInsideView(imagePerson: information.user_profile_image!, NamePerson: information.user_name!, TextMessage: information.message!, TextDate: information.chat_time!, imageAttach: information.image! , attachement : information.attachment! , removed : information.removed! , edited_admin : information.edited_admin! , edited : information.edited!)
            // to show reverse . . . ;
            cell.transform = CGAffineTransform(rotationAngle: CGFloat(Double.pi))
            return cell ;
        }
      
        
        
        
    }
    
  
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        var count = chattingList.count-1
        if(chattingList[count-indexPath.item].user_id == GlobalVariable.userId && chattingList[count-indexPath.item].message != "user_upload_images"){
            performSegue(withIdentifier: "show_edit", sender: self)
        }
    }
 
    // to pass data to another view controller . . . ;
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if(segue.identifier == "show_edit"){
            var count = chattingList.count-1
            // set destination view controller . . . ;
            let editChatting = segue.destination as! EditChattingViewController
            
            // get selected row (position) .. . . . . ;
            let position = tableViewChatting.indexPathForSelectedRow
            
            // put data inside variable (from destination view controller . . . ; )
            editChatting.chatId = chattingList[count-position!.row].chat_id
            
           
            editChatting.message = chattingList[count-position!.row].message
            
            editChatting.user_id = chattingList[count-position!.row].user_id
            
            editChatting.image  = chattingList[count-position!.row].image
            
            
            // to get callback from edit dialog . . . . ;
            editChatting.onSave = onSave
            
           
            
            
        }
        
    }
    func onSave(_ isSaved: Bool) -> () {
        
        chattingList.removeAll()
        getChattingFromDatabase()
    }
    
    
    @IBAction func BackButton(_ sender: UIBarButtonItem) {
      dismissViewController()
    }
    
    // get data from database ..... ;
    func getChattingFromDatabase(){
        chattingList.removeAll()
        // parse chatting url to url datatype . . . . ;
        let url = URL(string: URL_CHATTING)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        let postString = "user_id= \(user_id)&cst_id= \(cst!)&ticket_number= \(ticket_num!)"
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
                
                // convert data to json response . . . ;
                do{
                    let json = try JSONSerialization.jsonObject(with: data!, options: []) as? [String:Any]
                    var chatId : String?
                     var uId : String?
                     var cst_id : String?
                     var msg : String?
                     var attachment : String?
                     var chat_time : String?
                     var image : String?
                     var username : String?  // get user name from another object in json response . . . .;
                     var img_profile : String? // get image profile from another object in json response . . . ;
                     var removed : String?
                     var edited : String?
                     var edit_admin : String?
                    
                    
                    // to fetch chat information . . . . ;
                    if  let chattingResult = json!["ticket_chatting_result"] as? [[String: Any]]{
                        self.chattingList.removeAll()
                        for chat in chattingResult {
                             chatId = chat["id"] as? String
                             uId = chat["user_id"] as? String
                             cst_id = chat["cst_id"] as? String
                             msg = chat["message"] as? String
                             attachment = chat["attachment"] as? String
                             chat_time = chat["chattime"] as? String
                             image = chat["image"] as? String
                             removed = chat["removed"] as? String
                             edited = chat["edited"] as? String
                             edit_admin = chat["edited_admin"] as? String
                            
                            // fetch userName from object . . . ;
                            if let fetchUserName = json!["user_name_list"] as?[String:Any]{
                                username = fetchUserName[uId!] as! String
                                
                            }
                            
                            // fetch image profile from object . . .;
                            if let fetchImageProfile = json!["profile_images_list"] as?[String:Any]{
                                let   isImgExist = fetchImageProfile[uId!] as? String
                                if (isImgExist == nil){
                                    img_profile = "https://admin.emsystemsolutions.com/assest/upload_profile/avatar1.png"
                                }else{
                                    img_profile = "https://admin.emsystemsolutions.com/assest/upload_profile/\(String(describing: isImgExist!))"
                                }
                                
                            }
                            
                            //check if msg = user_upload_image and image = no ..(continue) . . . . ;
                            if(msg == "user_upload_images" && image == "no"){
                                continue
                                
                                // check if removed or edit admin ! = null . . . . ;
                            } else if (removed != nil || edit_admin != nil ){
                                print ("removed is :\(removed) and edit admin is \(edit_admin)")
                                // check if user type is admin then store data if not (hide data ) )
                                if(GlobalVariable.userType != "admin" ){
                                     continue
                                } else {
                                    self.chattingList.append(ChattingInformation(chat_id: chatId!, user_id: uId!, cst_id: cst_id!, message: msg ?? "", chat_time: chat_time!, user_name: username!, user_profile_image: img_profile!, attachement: attachment ?? "", image: image!, removed: removed ?? "", edited_admin: edit_admin ?? "", edited: edited ?? ""))
                                }
                            } else {
                            print("loading data ")
                            self.chattingList.append(ChattingInformation(chat_id: chatId!, user_id: uId!, cst_id: cst_id!, message: msg ?? "", chat_time: chat_time!, user_name: username!, user_profile_image: img_profile!, attachement: attachment ?? "", image: image!, removed: removed ?? "", edited_admin: edit_admin ?? "", edited: edited ?? ""))
                            }
                           

                        }
                    }
                    
              
                    DispatchQueue.main.async {
                        self.tableViewChatting.reloadData()
                    }
                    
                }catch{
                    print ("error json parsing . . . ; ")
                }
                
           
                
            }
            task.resume()
            
            
        }
        
        
        
    }
    
    
    
    @IBAction func attachBtn(_ sender: UIButton) {
        let myPicketController = UIImagePickerController()
        myPicketController.delegate = self
        myPicketController.sourceType = UIImagePickerController.SourceType.photoLibrary
        self.present(myPicketController, animated: true, completion: nil)
        
    }
    
    // this function to recover the image selected by the user . . .  ;
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        let image_data = info[UIImagePickerController.InfoKey.originalImage] as? UIImage
        putImageFromGallery(Image_Data: image_data!)
        //let imageData:Data = (image_data?.pngData())!
        guard let imageData = image_data!.pngData() else {return}
        imageGalleryString = imageData.base64EncodedString(options: Data.Base64EncodingOptions.endLineWithLineFeed)
        
        self.dismiss(animated: true, completion: nil)
      
        
        
    }
    
    func putImageFromGallery(Image_Data : UIImage ){
      
            ImageContainer.isHidden = false ;
            ImageViewGallery.image = Image_Data
        
    }
    
    
    
    @IBAction func ReplayBtn(_ sender: UIButton) {
        var message = String(describing: textMessage.text!)
        if( message != "" || imageGalleryString != ""){
            passMessageToDatabase(CstData: cst, msg: message, userId: user_id, userType: GlobalVariable.userType, ticket_num: ticket_num)
        }
        textMessage.text = ""
      //  imageGalleryString = ""
        ImageContainer.isHidden = true
        
    }
    
    
    
    // this func to pass message to database . . . . ;
    func passMessageToDatabase(CstData : String , msg : String , userId : String , userType : String , ticket_num : String){
        
        // parse chatting url to url datatype . . . . ;
        let url = URL(string: URL_ADD_CHAT)!
        // request for url . . with sent post method . . . ;
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        let postString = "cst_id=\(CstData)&message=\(msg)&user_id=\(userId)&user_type=\(userType)&ticket_number=\(ticket_num)&file_content=\(imageGalleryString)&reply=reply"
        print ("from post method : \(imageGalleryString)")
        request.httpBody = postString.data(using: .utf8)
        
        // getting response for post method . . . . ;
        DispatchQueue.global().async {
            let task = URLSession.shared.dataTask(with: request){(data:Data?,response:URLResponse?,error:Error?) in
                if(error != nil){
                    print ("error= \(error)")
                    
                    return
                }
                // getting values from json response . . . . ;
                let ResponseString = String(data: data!, encoding: String.Encoding.utf8) as String!
                 print (ResponseString!)
                // reload screen . . . . ;
                self.chattingList.removeAll();
                self.getChattingFromDatabase()
                DispatchQueue.main.async {
                    self.tableViewChatting.reloadData()
                }
        
    }
            task.resume()
    
  }
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
}
