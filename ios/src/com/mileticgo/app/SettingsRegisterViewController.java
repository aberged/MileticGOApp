package com.mileticgo.app;

import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.uikit.UIActivityIndicatorView;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UITextField;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.block.VoidBlock1;

@CustomClass("SettingsRegisterViewController")
public class SettingsRegisterViewController extends UIViewController {

    private UIButton bPrijava;
    private UITextField tEmail;
    private UITextField tPassword;
    private UITextField tPassword1;
    private UITextField tName;

    private UIActivityIndicatorView loadingUI;

    @IBOutlet
    public void setButtonPrijava(UIButton _ui) {
        this.bPrijava = _ui;
    }

    @IBOutlet
    public void setTFEmail(UITextField _ui) {
        this.tEmail = _ui;
    }

    @IBOutlet
    public void setTFPassword(UITextField _ui) {
        this.tPassword = _ui;
    }

    @IBOutlet
    public void setTFPassword1(UITextField _ui) {
        this.tPassword1 = _ui;
    }

    @IBOutlet
    public void setTFName(UITextField _ui) {
        this.tName = _ui;
    }

    @IBOutlet
    public void setUIActivityIndicator(UIActivityIndicatorView _ui) { this.loadingUI = _ui; }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Registracija");
        getNavigationController().getNavigationBar().setTintColor(UIColor.white());

        loadingUI.setHidden(true);
        bPrijava.addOnTouchUpInsideListener((control, event) -> {
            loadingUI.setHidden(false);
            loadingUI.startAnimating();
            this.getView().endEditing(true);
            System.out.println(tEmail.getText());
            System.out.println(tPassword.getText());
            Repository.get().register(tName.getText(), tEmail.getText(), tPassword.getText(), (ready, updating, error, msg) -> {
                DispatchQueue.getMainQueue().async(() -> {
                    loadingUI.stopAnimating();
                    loadingUI.setHidden(true);
                    if (error) {
                        showOKButtonPopup("Info", "Greška! Proverite ime, email, šifru i vezu sa internetom.", "OK", null);
                    } else if (ready) {
                        showOKButtonPopup("Info", "Uspešno ste se registrovali.", "OK", uiAlertAction -> closeView());
                    }
                });
                System.out.println(msg);
            });
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

    private void closeView() {
        getNavigationController().popViewController(true);
    }

}
