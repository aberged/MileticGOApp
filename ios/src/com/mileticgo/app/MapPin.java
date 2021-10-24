package com.mileticgo.app;

import org.robovm.apple.corelocation.CLLocationCoordinate2D;
import org.robovm.apple.mapkit.MKAnnotation;
import org.robovm.apple.mapkit.MKMarkerAnnotationView;
import org.robovm.apple.uikit.UIColor;

public class MapPin extends MKMarkerAnnotationView implements MKAnnotation {

    private CLLocationCoordinate2D coordinate2D;

    private final CityPin cityPin;

    public MapPin(CityPin pin) {
        super();
        this.cityPin = pin;
        this.setCoordinate(new CLLocationCoordinate2D(pin.getLat(), pin.getLng()));
        this.setAnnotation(this);
        this.setDisplayPriority(1000);
        colorPin();
    }

    private void colorPin() {
        if (isUnlocked()) {
            setMarkerTintColor(UIColor.systemBlue());
        } else {
            if (isNear())
                setMarkerTintColor(UIColor.systemGreen());
            else
                setMarkerTintColor(UIColor.gray());
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

}
