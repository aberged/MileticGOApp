package com.mileticgo.app;

import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;

@CustomClass("InventoryViewController")
public class InventoryViewController extends UIViewController {

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Kolekcija");
        getNavigationController().getNavigationBar().setTintColor(UIColor.black());
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
    }
}
