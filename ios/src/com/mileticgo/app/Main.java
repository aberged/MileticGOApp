package com.mileticgo.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSPreferences;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSMutableDictionary;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSUserDefaults;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationDelegateAdapter;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIWindow;

import java.io.File;
import java.util.List;

public class Main extends UIApplicationDelegateAdapter {
    private UIWindow window;
    private UIViewController rootViewController;

    @Override
    public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
        // Set up repository
        Repository.init(getPreferences("data"), valid -> {
            List<CityPin> pins = Repository.get().getActiveCityPins();
            System.out.println(pins);
        });
        // Set up the view controller.
        rootViewController = new NavigationViewController();
        // Create a new window at screen size.
        window = new UIWindow(UIScreen.getMainScreen().getBounds());
        // Set the view controller as the root controller for the window.
        window.setRootViewController(rootViewController);
        // Make the window visible.
        window.makeKeyAndVisible();
        return true;
    }

    public Preferences getPreferences (String name) {
        File libraryPath = new File(System.getenv("HOME"), "Library");
        File finalPath = new File(libraryPath, name + ".plist");

        @SuppressWarnings("unchecked")
        NSMutableDictionary<NSString, NSObject> nsDictionary = (NSMutableDictionary<NSString, NSObject>)NSMutableDictionary
                .read(finalPath);

        // if it fails to get an existing dictionary, create a new one.
        if (nsDictionary == null) {
            nsDictionary = new NSMutableDictionary<>();
            nsDictionary.write(finalPath, false);
        }
        return new IOSPreferences(nsDictionary, finalPath.getAbsolutePath());
    }

    public static void main(String[] args) {
        try (NSAutoreleasePool pool = new NSAutoreleasePool()) {
            UIApplication.main(args, null, Main.class);
        }
    }
}
