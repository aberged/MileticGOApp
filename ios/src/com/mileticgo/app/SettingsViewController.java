package com.mileticgo.app;

import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControlState;
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

    private UIButton bRegister;
    @IBOutlet
    public void setButtonRegister(UIButton _ui) {
        this.bRegister = _ui;
    }

    private UIButton bAbout;
    @IBOutlet
    public void setButtonAbout(UIButton _ui) {
        this.bAbout = _ui;
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        if (Repository.get().getUser().isAnonymous()) {
            logInOut.setTitle("PRIJAVA", UIControlState.Normal);
            bRegister.setHidden(false);
        } else {
            logInOut.setTitle("ODJAVA", UIControlState.Normal);
            bRegister.setHidden(true);
        }
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Podešavanja");
        getNavigationController().getNavigationBar().setTintColor(UIColor.white());

        logInOut.addOnTouchUpInsideListener((control, event) -> {
            if (Repository.get().getUser().isAnonymous()) {
                UIViewController secondVC = Main.storyboard().instantiateViewController("SettingsLoginViewController");
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
                            bRegister.setHidden(false);
                        }
                    });
                    System.out.println(msg);
                });
            }
        });
        bAbout.addOnTouchUpInsideListener((control, event) -> {
            UIViewController secondVC = Main.storyboard().instantiateViewController("LocationDetailsViewController");
            ((LocationDetailsViewController) secondVC).setPin(new CityPin("O aplikaciji", "detalji aplikacije"));
            //showViewController(secondVC, this);
            this.presentViewController(secondVC, true, null);
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
