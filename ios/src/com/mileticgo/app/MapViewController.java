package com.mileticgo.app;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.corelocation.CLLocation;
import org.robovm.apple.corelocation.CLLocationCoordinate2D;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.mapkit.MKAnnotation;
import org.robovm.apple.mapkit.MKAnnotationAdapter;
import org.robovm.apple.mapkit.MKAnnotationView;
import org.robovm.apple.mapkit.MKAnnotationViewDragState;
import org.robovm.apple.mapkit.MKClusterAnnotation;
import org.robovm.apple.mapkit.MKCoordinateRegion;
import org.robovm.apple.mapkit.MKCoordinateSpan;
import org.robovm.apple.mapkit.MKMapCamera;
import org.robovm.apple.mapkit.MKMapView;
import org.robovm.apple.mapkit.MKMapViewDelegate;
import org.robovm.apple.mapkit.MKMarkerAnnotationView;
import org.robovm.apple.mapkit.MKOverlay;
import org.robovm.apple.mapkit.MKOverlayRenderer;
import org.robovm.apple.mapkit.MKOverlayView;
import org.robovm.apple.mapkit.MKPinAnnotationView;
import org.robovm.apple.mapkit.MKUserLocation;
import org.robovm.apple.mapkit.MKUserTrackingMode;
import org.robovm.apple.uikit.NSTextAlignment;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIButtonType;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControl;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIFont;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIModalPresentationStyle;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;

import java.util.ArrayList;
import java.util.Map;

public class MapViewController extends UIViewController implements MKMapViewDelegate {

    private MKMapView map;

    public MapViewController() {
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("Mapa");
        map = new MKMapView(new CGRect(0, 0, UIScreen.getMainScreen().getBounds().getWidth(), UIScreen.getMainScreen().getBounds().getHeight()));
        map.setDelegate(this);
        map.setShowsUserLocation(true);
        map.setCamera(new MKMapCamera(new CLLocationCoordinate2D(
                Repository.get().getActiveCityProfile().getLat(),
                Repository.get().getActiveCityProfile().getLng()), 15000, 0, 0));

        ArrayList<CityPin> pins = (ArrayList<CityPin>) Repository.get().getActiveCityPins();
        for (CityPin pin: pins) {
            MapPin mapPin = new MapPin(new CLLocationCoordinate2D(
                            pin.getLat(), pin.getLng()), pin.getTitle(), "", pin.getUnlocked());
            map.addAnnotation(mapPin);
        }
        getView().addSubview(map);
    }

    @Override
    public void willChangeRegion(MKMapView mapView, boolean animated) {
        System.out.println("willChangeRegion - " + mapView);
    }

    @Override
    public void didChangeRegion(MKMapView mapView, boolean animated) {
        System.out.println("didChangeRegion - " + mapView);
    }

    @Override
    public void mapViewDidChangeVisibleRegion(MKMapView mapView) {
        System.out.println("mapViewDidChangeVisibleRegion - " + mapView);
    }

    @Override
    public void willStartLoadingMap(MKMapView mapView) {
        System.out.println("willStartLoadingMap - " + mapView);
    }

    @Override
    public void didFinishLoadingMap(MKMapView mapView) {
        System.out.println("didFinishLoadingMap - " + mapView);
    }

    @Override
    public void didFailLoadingMap(MKMapView mapView, NSError error) {
        System.out.println("didFailLoadingMap - " + mapView);
    }

    @Override
    public void willStartRenderingMap(MKMapView mapView) {
        System.out.println("willStartRenderingMap - " + mapView);
    }

    @Override
    public void didFinishRenderingMap(MKMapView mapView, boolean fullyRendered) {
        System.out.println("didFinishRenderingMap - " + mapView);
    }

    @Override
    public MKAnnotationView getAnnotationView(MKMapView mapView, MKAnnotation annotation) {
        System.out.println(annotation.getTitle() + "; unlocked - " + ((MapPin) annotation).isUnlocked());
        MKMarkerAnnotationView pin = ((MapPin) annotation).getView();
        if (((MapPin) annotation).isUnlocked()) {
            pin.setMarkerTintColor(UIColor.systemBlue());
        } else {
            pin.setMarkerTintColor(UIColor.gray());
        }
        return pin;
    }

    @Override
    public void didAddAnnotationViews(MKMapView mapView, NSArray<MKAnnotationView> views) {
        System.out.println("didAddAnnotationViews - " + mapView);
    }

    @Override
    public void calloutAccessoryControlTapped(MKMapView mapView, MKAnnotationView view, UIControl control) {
        System.out.println("calloutAccessoryControlTapped - " + mapView);
    }

    @Override
    public void didSelectAnnotationView(MKMapView mapView, MKAnnotationView view) {
        System.out.println("didSelectAnnotationView - " + mapView);
    }

    @Override
    public void didDeselectAnnotationView(MKMapView mapView, MKAnnotationView view) {
        System.out.println("didDeselectAnnotationView - " + mapView);
    }

    @Override
    public void willStartLocatingUser(MKMapView mapView) {
        System.out.println("willStartLocatingUser - " + mapView);
    }

    @Override
    public void didStopLocatingUser(MKMapView mapView) {
        System.out.println("didStopLocatingUser - " + mapView);
    }

    @Override
    public void didUpdateUserLocation(MKMapView mapView, MKUserLocation userLocation) {
        System.out.println("didUpdateUserLocation - " + mapView);
    }

    @Override
    public void didFailToLocateUser(MKMapView mapView, NSError error) {
        System.out.println("didFailToLocateUser - " + mapView);
    }

    @Override
    public void didChangeDragState(MKMapView mapView, MKAnnotationView view, MKAnnotationViewDragState newState, MKAnnotationViewDragState oldState) {
        System.out.println("didChangeDragState - " + mapView);
    }

    @Override
    public void didChangeUserTrackingMode(MKMapView mapView, MKUserTrackingMode mode, boolean animated) {
        System.out.println("didChangeUserTrackingMode - " + mapView);
    }

    @Override
    public MKOverlayRenderer getOverlayRenderer(MKMapView mapView, MKOverlay overlay) {
        return null;
    }

    @Override
    public void didAddOverlayRenderers(MKMapView mapView, NSArray<MKOverlayRenderer> renderers) {
        System.out.println("didAddOverlayRenderers - " + mapView);
    }

    /** @noinspection deprecation*/
    @Override
    public MKOverlayView getOverlayView(MKMapView mapView, MKOverlay overlay) {
        return null;
    }

    /** @noinspection deprecation*/
    @Override
    public void didAddOverlayViews(MKMapView mapView, NSArray<MKOverlayView> overlayViews) {
        System.out.println("didAddOverlayViews - " + mapView);
    }

    @Override
    public MKClusterAnnotation getClusterAnnotationForMemberAnnotations(MKMapView mapView, NSArray<?> memberAnnotations) {
        return null;
    }
}
