package com.mileticgo.app;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.corelocation.CLLocationCoordinate2D;
import org.robovm.apple.mapkit.MKAnnotation;
import org.robovm.apple.mapkit.MKAnnotationView;
import org.robovm.apple.mapkit.MKMarkerAnnotationView;
import org.robovm.apple.mapkit.MKPinAnnotationView;
import org.robovm.apple.mapkit.MKPointAnnotation;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIView;

public class MapPin implements MKAnnotation {

    private CLLocationCoordinate2D coordinate2D;
    private String title;
    private String subTitle;
    private boolean unlocked;

    private MKMarkerAnnotationView view;

    public MapPin(CityPin pin) {
        coordinate2D = new CLLocationCoordinate2D(pin.getLat(), pin.getLng());
        title = pin.getTitle();
        subTitle = "";
        unlocked = pin.getUnlocked();
        view = new MKMarkerAnnotationView(this, pin.getId());
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
        return title;
    }

    @Override
    public String getSubtitle() {
        return subTitle;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public MKMarkerAnnotationView getView() {
        return view;
    }
}
