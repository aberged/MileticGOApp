package com.mileticgo.app;

import org.robovm.apple.uikit.UINavigationController;
import org.robovm.objc.annotation.CustomClass;

@CustomClass("NavigationViewController")
public class NavigationViewController extends UINavigationController {
    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
    }
}
