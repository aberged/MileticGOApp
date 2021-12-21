package com.mileticgo.app;

import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;

@CustomClass("LeaderboardViewController")
public class LeaderboardViewController extends UIViewController {

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Liderbord");
        getNavigationController().getNavigationBar().setTintColor(UIColor.white());
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
    }
}
