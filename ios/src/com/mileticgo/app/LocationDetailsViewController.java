package com.mileticgo.app;

import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UITextView;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;

@CustomClass("LocationDetailsViewController")
public class LocationDetailsViewController extends UIViewController {

    private CityPin selectedPin;

    private UILabel uiLabel;
    @IBOutlet public void setLabel(UILabel _ui) { this.uiLabel = _ui; }

    private UITextView uiText;
    @IBOutlet public void setText(UITextView _ui) { this.uiText = _ui; }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        if (selectedPin != null) {
            uiLabel.setText(selectedPin.getTitle());
            uiText.setText(selectedPin.getDescription());
        } else {
            uiLabel.setText("");
            uiText.setText("");
        }
        getNavigationItem().setBackButtonTitle("Detalji lokacije");
    }

    public void setPin(CityPin pin) {
        this.selectedPin = pin;
        if (selectedPin != null) {
            if (uiLabel != null) uiLabel.setText(selectedPin.getTitle());
            if (uiText != null)  uiText.setText(selectedPin.getDescription());
        } else {
            if (uiLabel != null) uiLabel.setText("");
            if (uiText != null)  uiText.setText("");
        }
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
    }
}
