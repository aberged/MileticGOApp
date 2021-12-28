package com.mileticgo.app;

import org.robovm.apple.arkit.ARAnchor;
import org.robovm.apple.arkit.ARCamera;
import org.robovm.apple.arkit.ARCollaborationData;
import org.robovm.apple.arkit.ARGeoTrackingStatus;
import org.robovm.apple.arkit.ARSKView;
import org.robovm.apple.arkit.ARSKViewDelegate;
import org.robovm.apple.arkit.ARSession;
import org.robovm.apple.arkit.ARWorldTrackingConfiguration;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.coremedia.CMSampleBuffer;
import org.robovm.apple.foundation.MatrixFloat4x4;
import org.robovm.apple.foundation.NSError;
import org.robovm.apple.spritekit.SKLabelNode;
import org.robovm.apple.spritekit.SKNode;
import org.robovm.apple.spritekit.SKScene;
import org.robovm.apple.spritekit.SKSceneScaleMode;
import org.robovm.apple.spritekit.SKView;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.block.VoidBlock1;

@CustomClass("GURUAR")
public class GURUAR extends UIViewController implements ARSKViewDelegate {

    private ARSKView ar;
    //@IBOutlet()
    //public void setAR(ARSKView _ui) { this.ar = _ui; }

    private CityPin cityPin;

    private ARSession session;

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        ar = (ARSKView) this.getView();
        // Set the view's delegate
        ar.setDelegate(this);
        // Show statistics such as fps and node count
        ar.setShowsFPS(true);
        ar.setShowsNodeCount(true);
        GURUScene scene = new GURUScene();
        scene.setScaleMode(SKSceneScaleMode.AspectFill);
        scene.setSize(new CGSize(this.getView().getBounds().getWidth(), this.getView().getBounds().getHeight()));
        ar.presentScene(scene);
    }

    public void setPin(CityPin pin) {
        this.cityPin = pin;
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        // Create a session configuration
        ARWorldTrackingConfiguration configuration = new ARWorldTrackingConfiguration();

        // Run the view's session
        ar.getSession().run(configuration);
    }

    @Override
    public void viewWillDisappear(boolean animated) {
        super.viewWillDisappear(animated);
        ar.getSession().pause();
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
        SKLabelNode label = new SKLabelNode("👾ARARAR👾");
        label.setFontColor(UIColor.white());
        label.setColor(UIColor.white());
        return label;
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
