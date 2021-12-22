package com.mileticgo.app;

import org.robovm.apple.corefoundation.CFBundle;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.corelocation.CLLocationCoordinate2D;
import org.robovm.apple.foundation.NSBundle;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.mapkit.MKAnnotation;
import org.robovm.apple.mapkit.MKAnnotationView;
import org.robovm.apple.mapkit.MKMarkerAnnotationView;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIView;

import java.io.File;

public class MapPin extends MKAnnotationView implements MKAnnotation {

    private CLLocationCoordinate2D coordinate2D;

    private final CityPin cityPin;

    public MapPin(CityPin pin) {
        super();
        this.cityPin = pin;
        this.setCoordinate(new CLLocationCoordinate2D(pin.getLat(), pin.getLng()));
        this.setAnnotation(this);
        this.setDisplayPriority(1000);
        this.setFrame(new CGRect(0,0,10,10));
        //this.setBackgroundColor(UIColor.yellow());
        this.setHidden(false);
        this.setDisplayPriority(10000);
        this.setEnabled(true);
        colorPin();
    }

    private void colorPin() {
        if (isUnlocked()) {
            //setMarkerTintColor(UIColor.systemBlue());
            setImage(UIImage.getImage("pin_checked.png"));
        } else {
            if (isNear()) {
                //setMarkerTintColor(UIColor.systemGreen());
                setImage(UIImage.getImage("pin_active.png"));
            }else{
                //setMarkerTintColor(UIColor.gray());
                setImage(UIImage.getImage("pin_locked.png"));
            }
        }
    }

    public boolean setIsNear(boolean near) {
        boolean changed = near != cityPin.isNear();
        this.cityPin.setNear(near);
        if (changed)
            colorPin();
        return changed;
    }

    public boolean isNear() {
        return cityPin.isNear();
    }

    @Override
    public CLLocationCoordinate2D getCoordinate() {
        return coordinate2D;
    }

    @Override
    public void setCoordinate(CLLocationCoordinate2D v) {
        coordinate2D = v;
    }

    @Override
    public String getTitle() {
        return cityPin.getTitle();
    }

    @Override
    public String getSubtitle() { return ""; }

    public boolean isUnlocked() { return cityPin.getUnlocked(); }

    public CityPin getPin() {
        return cityPin;
    }
}
