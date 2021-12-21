package com.mileticgo.app;

import org.robovm.apple.coregraphics.CGColor;
import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.uikit.UIActivityIndicatorView;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIBarMetrics;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UINavigationBar;
import org.robovm.apple.uikit.UIStoryboard;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.block.VoidBlock1;

@CustomClass("SettingsViewController")
public class SettingsViewController extends UIViewController {

    private UIButton logInOut;

    @IBOutlet
    public void setButtonPrijava(UIButton _ui) {
        this.logInOut = _ui;
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        if (Repository.get().getUser().isAnonymous()) {
            logInOut.setTitle("PRIJAVA", UIControlState.Normal);
        } else {
            logInOut.setTitle("ODJAVA", UIControlState.Normal);
        }
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Podešavanja");

        getNavigationController().getNavigationBar().setBackgroundImage(new UIImage(), UIBarMetrics.Default);
        getNavigationController().getNavigationBar().setShadowImage(new UIImage());
        getNavigationController().getNavigationBar().setTranslucent(true);
        getNavigationController().getNavigationBar().setBackgroundColor(new UIColor(0,0,0,0));
        getNavigationController().getNavigationBar().setBarTintColor(new UIColor(0,0,0,0));

        logInOut.addOnTouchUpInsideListener((control, event) -> {
            if (Repository.get().getUser().isAnonymous()) {
                UIStoryboard storyboard = new UIStoryboard("Storyboard", null);
                UIViewController secondVC = storyboard.instantiateViewController("SettingsLoginViewController");
                showViewController(secondVC, this);
            } else {
                System.out.println("loging out - " + Repository.get().getUser().toJson());
                Repository.get().logout((ready, updating, error, msg) -> {
                    DispatchQueue.getMainQueue().async(() -> {
                        if (error) {
                            showOKButtonPopup("Greška!", msg, "OK", null);
                        } else if (ready) {
                            showOKButtonPopup("Info", "Uspešno ste se odjavili.", "OK", null);
                            logInOut.setTitle("PRIJAVA", UIControlState.Normal);
                        }
                    });
                    System.out.println(msg);
                });
            }
        });
    }

    private void showOKButtonPopup(String title, String msg, String buttonLabel, VoidBlock1<UIAlertAction> btnHandler) {
        // create the alert
        UIAlertController alert = new UIAlertController(title, msg, UIAlertControllerStyle.Alert);
        // add an action (button)
        alert.addAction(new UIAlertAction(buttonLabel, UIAlertActionStyle.Default, btnHandler));
        // show the alert
        this.presentViewController(alert, true, null);
    }

}
