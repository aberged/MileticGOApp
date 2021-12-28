package com.mileticgo.app;

import org.robovm.apple.arkit.ARSKView;
import org.robovm.apple.spritekit.SKScene;

public class GURUScene extends SKScene {

    private boolean isWorldSetup = false;

    @Override
    public void update(double currentTime) {
        super.update(currentTime);
        if (getView() instanceof ARSKView) {
            isWorldSetup = true;
        }
    }
}
