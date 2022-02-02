//
//  YearDashboardTableViewCell.swift
//  SidebarMenu
//
//  Created by admin on 6/20/19.
//  Copyright © 2019 AppCoda. All rights reserved.
//

import UIKit

class YearDashboardTableViewCell: UITableViewCell {

    @IBOutlet weak var CheckedBtn: UIButton!
    @IBOutlet weak var YearLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

}
