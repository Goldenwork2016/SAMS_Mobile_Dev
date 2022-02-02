//
//  AwardTableViewCell.swift
//  SidebarMenu
//
//  Created by admin on 5/30/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class AwardTableViewCell: UITableViewCell {

    @IBOutlet weak var status_image: UIImageView!
    
    @IBOutlet weak var student_id: UILabel!
    @IBOutlet weak var student_name: UILabel!
    
   
    
    @IBAction func restoreAward(_ sender: Any) {
    
        
    }
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
 
        // Configure the view for the selected state
        
    }
    

}
