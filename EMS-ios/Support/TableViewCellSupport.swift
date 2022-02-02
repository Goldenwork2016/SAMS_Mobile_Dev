//
//  TableViewCellSupport.swift
//  SidebarMenu
//
//  Created by admin on 6/8/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class TableViewCellSupport: UITableViewCell {
    @IBOutlet weak var TicketLabel : UILabel!
    @IBOutlet weak var SubjectLabel : UILabel!
    
    @IBOutlet weak var StatusImage : UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
