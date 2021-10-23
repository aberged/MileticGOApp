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

    public MapPin(CLLocationCoordinate2D c, String t, String st, boolean ul) {
        coordinate2D = c;
        title = t;
        subTitle = st;
        unlocked = ul;
        view = new MKMarkerAnnotationView(this, t);
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
