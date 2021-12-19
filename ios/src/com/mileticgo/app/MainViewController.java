package com.mileticgo.app;

import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;

@CustomClass("MainViewController")
public class MainViewController extends UIViewController {

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        getNavigationController().setNavigationBarHidden(true);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getNavigationItem().setBackButtonTitle("Nazad");
    }
}
