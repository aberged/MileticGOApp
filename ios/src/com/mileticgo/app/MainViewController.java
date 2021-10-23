package com.mileticgo.app;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIButtonType;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControl;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIEvent;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIModalPresentationStyle;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UITextBorderStyle;
import org.robovm.apple.uikit.UITextField;
import org.robovm.apple.uikit.UIViewController;

public class MainViewController extends UIViewController {

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        UILabel label = new UILabel();
        label.setFrame(new CGRect(10, 50, UIScreen.getMainScreen().getBounds().getWidth()*0.9, 40));
        label.setText(Repository.get().getUser().getName());

        // Setup button.
        UIButton button = new UIButton(UIButtonType.RoundedRect);
        button.setFrame(new CGRect(UIScreen.getMainScreen().getBounds().getWidth()/2 - 50, UIScreen.getMainScreen().getBounds().getHeight()/2-20, 100, 40));
        button.setTitle("Map", UIControlState.Normal);
        button.getTitleLabel().setFont(UIFont.getBoldSystemFont(22));
        button.addOnTouchUpInsideListener((control, event) -> {
            MapViewController map = new MapViewController();
            map.setModalPresentationStyle(UIModalPresentationStyle.FullScreen);
            getNavigationController().showViewController(map, getNavigationController());
        });
        // Setup textfiled
        UITextField username = new UITextField(
                new CGRect(
                        UIScreen.getMainScreen().getBounds().getWidth()/2 - UIScreen.getMainScreen().getBounds().getWidth()/3,
                        UIScreen.getMainScreen().getBounds().getHeight()/2+30,
                        2*UIScreen.getMainScreen().getBounds().getWidth()/3,
                        40));
        username.setBorderStyle(UITextBorderStyle.RoundedRect);
        username.setText(Repository.get().getUser().getName());

        UITextField password = new UITextField(
                new CGRect(
                        UIScreen.getMainScreen().getBounds().getWidth()/2 - UIScreen.getMainScreen().getBounds().getWidth()/3,
                        UIScreen.getMainScreen().getBounds().getHeight()/2+30+50,
                        2*UIScreen.getMainScreen().getBounds().getWidth()/3,
                        40));
        password.setBorderStyle(UITextBorderStyle.RoundedRect);

        UIButton login = new UIButton(UIButtonType.RoundedRect);
        login.setFrame(new CGRect(
                UIScreen.getMainScreen().getBounds().getWidth()/2 - 50,
                UIScreen.getMainScreen().getBounds().getHeight()/2+30+50+50,
                100,
                40));
        login.setTitle(Repository.get().getUser().isAnonymous()? "login" : "logout", UIControlState.Normal);
        login.getTitleLabel().setFont(UIFont.getBoldSystemFont(22));
        login.addOnTouchUpInsideListener((control, event) -> {
            if (Repository.get().getUser().isAnonymous()) {
                Repository.get().login(username.getText(), password.getText(), valid -> {
                    if (valid) {
                        DispatchQueue.getMainQueue().async(() -> {
                            label.setText(Repository.get().getUser().getName());
                            login.setTitle("logout", UIControlState.Normal);
                        });
                    }
                });
            } else {
                Repository.get().logout(valid -> {
                    if (valid) {
                        label.setText(Repository.get().getUser().getName());
                        login.setTitle("login", UIControlState.Normal);
                    }
                });
            }
        });

        getView().setBackgroundColor(UIColor.white());
        getView().addSubview(button);
        getView().addSubview(username);
        getView().addSubview(password);
        getView().addSubview(label);
        getView().addSubview(login);
        setTitle("Main");
    }
}
