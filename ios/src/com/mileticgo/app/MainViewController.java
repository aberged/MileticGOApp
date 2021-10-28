package com.mileticgo.app;

import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIModalPresentationStyle;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;

@CustomClass("MainViewController")
public class MainViewController extends UIViewController {

    private UIButton mapButton;

    @IBOutlet
    public void setMapButtton(UIButton button) {
        this.mapButton = button;
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        getNavigationController().setNavigationBarHidden(true);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        mapButton.addOnTouchUpInsideListener((control, event) -> {
            MapViewController map = new MapViewController();
            map.setModalPresentationStyle(UIModalPresentationStyle.FullScreen);
            getNavigationController().showViewController(map, getNavigationController());
        });
    }
}
