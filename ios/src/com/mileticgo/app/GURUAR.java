package com.mileticgo.app;

import org.robovm.apple.arkit.ARAnchor;
import org.robovm.apple.arkit.ARCamera;
import org.robovm.apple.arkit.ARCollaborationData;
import org.robovm.apple.arkit.ARGeoTrackingStatus;
import org.robovm.apple.arkit.ARSKView;
import org.robovm.apple.arkit.ARSKViewDelegate;
import org.robovm.apple.arkit.ARSession;
import org.robovm.apple.coremedia.CMSampleBuffer;
import org.robovm.apple.foundation.MatrixFloat4x4;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.spritekit.SKLabelNode;
import org.robovm.apple.spritekit.SKNode;
import org.robovm.apple.spritekit.SKView;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.block.VoidBlock1;

@CustomClass("GURUAR")
public class GURUAR extends UIViewController implements ARSKViewDelegate {

    private ARSKView ar;
    @IBOutlet()
    public void setAR(ARSKView _ui) { this.ar = _ui; }

    private CityPin cityPin;

    private ARSession session;

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        session = ar.getSession();
        try {
            if (session != null) {
                MatrixFloat4x4 translation = new MatrixFloat4x4();
                translation.getC3().setZ((float) -0.2);
                MatrixFloat4x4 arTransform = session.getCurrentFrame().getCamera().getTransform();
                arTransform.setC3(translation.getC3());

                ARAnchor anchor = new ARAnchor(arTransform);
                session.addAnchor(anchor);
            } else {
                if (cityPin != null) Repository.get().addPinToInventory(cityPin);
                showOKButtonPopup("Info", "AR ne radi. Lokacija je otkljucana.", "OK", uiAlertAction -> closeView());
            }
        }catch (Throwable err) {

        }
    }

    public void setPin(CityPin pin) {
        this.cityPin = pin;
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
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

/*
AR
*/
    @Override
    public SKNode getNodeForAnchor(ARSKView view, ARAnchor anchor) {
        return new SKLabelNode("👾ARARAR👾");
    }

    @Override
    public void didAddNode(ARSKView view, SKNode node, ARAnchor anchor) {

    }

    @Override
    public void willUpdateNode(ARSKView view, SKNode node, ARAnchor anchor) {

    }

    @Override
    public void didUpdateNode(ARSKView view, SKNode node, ARAnchor anchor) {

    }

    @Override
    public void didRemoveNode(ARSKView view, SKNode node, ARAnchor anchor) {

    }

    @Override
    public void didFailWithError(ARSession session, NSError error) {

    }

    @Override
    public void cameraDidChangeTrackingState(ARSession session, ARCamera camera) {

    }

    @Override
    public void sessionWasInterrupted(ARSession session) {

    }

    @Override
    public void sessionInterruptionEnded(ARSession session) {

    }

    @Override
    public boolean sessionShouldAttemptRelocalization(ARSession session) {
        return false;
    }

    @Override
    public void didOutputAudioSampleBuffer(ARSession session, CMSampleBuffer audioSampleBuffer) {

    }

    @Override
    public void didOutputCollaborationData(ARSession session, ARCollaborationData data) {

    }

    @Override
    public void didChangeGeoTrackingStatus(ARSession session, ARGeoTrackingStatus geoTrackingStatus) {

    }

    @Override
    public boolean shouldRenderViewAtTime(SKView view, double time) {
        return false;
    }
}
