package com.mileticgo.app;

import org.robovm.apple.uikit.UINavigationController;

public class NavigationViewController extends UINavigationController {
    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        this.showViewController(new MainViewController(), NavigationViewController.this);
    }
}
