package com.mileticgo.app;

import org.robovm.apple.corelocation.CLLocationAccuracy;
import org.robovm.apple.corelocation.CLLocationCoordinate2D;
import org.robovm.apple.corelocation.CLLocationManager;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.mapkit.MKAnnotation;
import org.robovm.apple.mapkit.MKAnnotationView;
import org.robovm.apple.mapkit.MKAnnotationViewDragState;
import org.robovm.apple.mapkit.MKClusterAnnotation;
import org.robovm.apple.mapkit.MKMapCamera;
import org.robovm.apple.mapkit.MKMapView;
import org.robovm.apple.mapkit.MKMapViewDelegate;
import org.robovm.apple.mapkit.MKOverlay;
import org.robovm.apple.mapkit.MKOverlayRenderer;
import org.robovm.apple.mapkit.MKOverlayView;
import org.robovm.apple.mapkit.MKUserLocation;
import org.robovm.apple.mapkit.MKUserTrackingMode;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UIBarButtonItemStyle;
import org.robovm.apple.uikit.UIControl;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;

import java.util.ArrayList;

@CustomClass("MapViewController")
public class MapViewController extends UIViewController implements MKMapViewDelegate {

    private MKMapView map;
    private final CLLocationManager locationManager = new CLLocationManager();

    @IBOutlet public void setMap(MKMapView _map) {
        this.map = _map;
    }

    public MapViewController() {
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        locationManager.requestWhenInUseAuthorization();
        locationManager.setDesiredAccuracy(CLLocationAccuracy.Best);
        locationManager.setDistanceFilter(CLLocationManager.getDistanceFilterNone());
        locationManager.startUpdatingLocation();

        map.setDelegate(this);
        map.setShowsCompass(true);
        map.setCamera(new MKMapCamera(new CLLocationCoordinate2D(
                Repository.get().getActiveCityProfile().getLat(),
                Repository.get().getActiveCityProfile().getLng()), 21000, 0, 0));
        ArrayList<CityPin> pins = (ArrayList<CityPin>) Repository.get().getActiveCityPins();
        for (CityPin pin: pins) {
            MapPin mapPin = new MapPin(pin);
            map.addAnnotation(mapPin);
        }
        map.setShowsUserLocation(true);
        map.setUserTrackingMode(MKUserTrackingMode.None);

        getNavigationItem().setRightBarButtonItem(new UIBarButtonItem("Hide me", UIBarButtonItemStyle.Plain, barButtonItem -> {
            map.setShowsUserLocation(!map.showsUserLocation());
            if (map.showsUserLocation()) {
                barButtonItem.setTitle("Hide me");
                barButtonItem.setStyle(UIBarButtonItemStyle.Plain);
            } else {
                barButtonItem.setTitle("Show me");
                barButtonItem.setStyle(UIBarButtonItemStyle.Done);
            }
        }));

        getNavigationController().setNavigationBarHidden(false);
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
        //System.out.println("mapViewDidChangeVisibleRegion - " + mapView);
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
        if (!(annotation instanceof MapPin)) return null;
        System.out.println(annotation.getTitle() + "; unlocked - " + ((MapPin) annotation).isUnlocked());
        return (MapPin) annotation;
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
        if (!(view instanceof MapPin)) return;
        if (((MapPin)view).isNear() && !((MapPin)view).isUnlocked()) {
            System.out.println("GOTO AR");
        } else if (((MapPin)view).isUnlocked()) {
            System.out.println("GOTO details");
        }
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
        for (MKAnnotation pin: map.getAnnotations()) {
            if (!(pin instanceof MapPin)) continue;
            System.out.println("loc: " + pin.getTitle());
            boolean near = ((MapPin)pin).setIsNear(distance(
                    pin.getCoordinate().getLatitude(),
                    pin.getCoordinate().getLongitude(),
                    userLocation.getCoordinate().getLatitude(),
                    userLocation.getCoordinate().getLongitude()
            ) < 10);
            System.out.println("isNear - " + ((MapPin) pin).isNear());
        }
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

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }

}
