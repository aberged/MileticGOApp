package com.mileticgo.app;

import org.robovm.apple.uikit.UIBarMetrics;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;

@CustomClass("MainViewController")
public class MainViewController extends UIViewController {

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getNavigationItem().setBackButtonTitle("Nazad");
        getNavigationController().setNavigationBarHidden(true);
        getNavigationController().getNavigationBar().setTintColor(UIColor.white());

        getNavigationController().getNavigationBar().setBackgroundImage(new UIImage(), UIBarMetrics.Default);
        getNavigationController().getNavigationBar().setShadowImage(new UIImage());
        getNavigationController().getNavigationBar().setTranslucent(true);
    }
}
