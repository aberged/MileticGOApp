package com.mileticgo.app;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSKeyedArchiver;
import org.robovm.apple.foundation.NSKeyedUnarchiver;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIScrollView;
import org.robovm.apple.uikit.UITapGestureRecognizer;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;

import java.util.ArrayList;
import java.util.List;

@CustomClass("CategoryView")
public class CategoryView extends UIView {

    private final List<CityPin> dataSource = new ArrayList<>();

    private UIViewController vc;

    public CategoryView copy() {
        NSData data = NSKeyedArchiver.archivedDataWithRootObject(this, false, null);
        NSObject cell = NSKeyedUnarchiver.unarchive(data);
        if (cell != null){
            return (CategoryView) cell;
        }
        return null;
    }

    public void setData(List<CityPin> pins, UIViewController vc){
        this.dataSource.clear();
        this.dataSource.addAll(pins);

        this.vc = vc;

        int index = 0;
        NSArray<UIView> views = this.getSubviews();
        for (UIView v: views
             ) {
            if (v instanceof UILabel || v instanceof UIScrollView) v.removeFromSuperview();
        }

        UIScrollView scroll = new UIScrollView();
        scroll.setFrame(new CGRect(0,20,this.getFrame().getSize().getWidth(), 2*this.getFrame().getSize().getHeight()));
        scroll.setContentSize(new CGSize(this.getFrame().getSize().getWidth(), dataSource.size()*55+120));
        this.addSubview(scroll);
        for (final CityPin pin: dataSource
             ) {
            UILabel lab = new UILabel();
            lab.setFrame(new CGRect(20, index*55, this.getFrame().getSize().getWidth()-40, 40));
            lab.setBackgroundColor(UIColor.white());
            lab.setTintColor(UIColor.black());
            lab.setTextColor(UIColor.black());
            lab.setText("    " + pin.getTitle());
            UITapGestureRecognizer tap = new UITapGestureRecognizer();
            tap.addListener(gestureRecognizer -> {
                System.out.println("GOTO details");
                UIViewController secondVC = Main.storyboard().instantiateViewController("LocationDetailsViewController");
                ((LocationDetailsViewController) secondVC).setPin(pin);
                vc.presentViewController(secondVC, true, null);
            });
            lab.addGestureRecognizer(tap);
            lab.setUserInteractionEnabled(true);
            index++;
            scroll.addSubview(lab);
        }
    }
}
