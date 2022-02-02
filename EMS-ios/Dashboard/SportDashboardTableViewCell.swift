//
//  SportDashboardTableViewCell.swift
//  SidebarMenu
//
//  Created by admin on 6/21/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class SportDashboardTableViewCell: UITableViewCell {

    @IBOutlet weak var sportLabel: UILabel!
    @IBOutlet weak var CheckedSport: UIButton!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
