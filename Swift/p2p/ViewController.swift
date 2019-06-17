//
//  ViewController.swift
//  p2p
//
//  Created by Zozo on 2019-06-16.
//  Copyright © 2019 Yazan Ghafir. All rights reserved.
//

import UIKit

class ViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {
    
    let list = ["Yazan", "Razan", "Meeraz", "Talya", "Zed"]
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return(list.count)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = UITableViewCell(style: UITableViewCell.CellStyle.default, reuseIdentifier: "noticeCell")
        cell.textLabel?.text = list[indexPath.row]
        return(cell)
    }
    

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }


}

