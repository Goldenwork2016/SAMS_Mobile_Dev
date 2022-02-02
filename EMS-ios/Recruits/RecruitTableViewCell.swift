//
//  AwardTableViewCell.swift
//  SidebarMenu
//
//  Created by admin on 5/30/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class RecruitTableViewCell: UITableViewCell {

    @IBOutlet weak var status_image: UIImageView!
    
    @IBOutlet weak var student_id: UILabel!
    @IBOutlet weak var student_name: UILabel!
   
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
 
        // Configure the view for the selected state
        
    }
    

}
