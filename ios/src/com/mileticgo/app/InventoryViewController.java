package com.mileticgo.app;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UIPageControl;
import org.robovm.apple.uikit.UIScrollView;
import org.robovm.apple.uikit.UIScrollViewDelegate;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.block.VoidBlock1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CustomClass("InventoryViewController")
public class InventoryViewController extends UIViewController implements UIScrollViewDelegate {

    private UILabel uiCategory;
    @IBOutlet
    public void setCategory(UILabel _ui) { this.uiCategory = _ui; }

    private UIPageControl pager;
    @IBOutlet public void setPager(UIPageControl _ui) { this.pager = _ui; }

    private UIScrollView scrollView;
    @IBOutlet public void setScrollView(UIScrollView _ui) { this.scrollView = _ui; }

    private CategoryView pageOne;
    @IBOutlet public void setPageOne(CategoryView _ui) { this.pageOne = _ui; }

    private final ArrayList<CategoryView> pages = new ArrayList<>();

    private final List<CityPin> allPins = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();
    private final HashMap<String, List<CityPin>> dataSourceMap = new HashMap<>();

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Kolekcija");
        getNavigationController().getNavigationBar().setTintColor(UIColor.black());
        pager.addOnValueChangedListener(control -> {
            scrollView.setContentOffset(new CGPoint(pager.getCurrentPage()*scrollView.getFrame().getSize().getWidth(), 0), true);
            uiCategory.setText(categories.get((int)pager.getCurrentPage()));
        });
        pages.clear();
        this.allPins.clear();
        this.categories.clear();
        this.dataSourceMap.clear();
        this.allPins.addAll(Repository.get().getUserInventoryCityPinsForActiveCityProfile());
        for (CityPin pin: allPins
             ) {
            String cat = pin.getCategory();
            boolean alreadyThere = false;
            for (String s:categories
                 ) {
                if (s.equals(cat)) {
                    alreadyThere = true;
                    break;
                }
            }
            if (!alreadyThere) categories.add(cat);
        }
        for (String cat: categories
             ) {
            ArrayList<CityPin> list = new ArrayList<>();
            for (CityPin pin: allPins
                 ) {
                if (pin.getCategory().equals(cat)) {
                    list.add(pin);
                }
            }
            dataSourceMap.put(cat, list);
        }
        if (categories.size() > 0) {
            uiCategory.setText(categories.get(0));
        } else {
            uiCategory.setText("");
        }

        scrollView.setFrame(new CGRect(0,0, getView().getFrame().getSize().getWidth(), scrollView.getFrame().getSize().getHeight()));
        scrollView.setContentSize(new CGSize(scrollView.getFrame().getSize().getWidth()*categories.size(), scrollView.getFrame().getSize().getHeight()));
        scrollView.setScrollEnabled(true);
        pages.clear();
        pager.setNumberOfPages(categories.size());
        for (int i=0; i<categories.size();i++) {
            if (i==0) {
                pageOne.setFrame(new CGRect(0, 0, scrollView.getFrame().getSize().getWidth(),scrollView.getFrame().getSize().getHeight()));
                //dataSourceMap.get(categories.get(i)).addAll(dataSourceMap.get(categories.get(i))); // doubles data for test
                pageOne.setData(dataSourceMap.get(categories.get(i)), this);
            } else {
                CategoryView copy = pageOne.copy();
                pages.add(copy);
                copy.setFrame(new CGRect(i*scrollView.getFrame().getSize().getWidth(), 0, scrollView.getFrame().getSize().getWidth(), scrollView.getFrame().getSize().getHeight()));
                copy.setData(dataSourceMap.get(categories.get(i)), this);
                scrollView.addSubview(copy);
            }
        }

        if (categories.size() == 0) {
            showOKButtonPopup("Info", "Nema otključanih lokacija!", "OK", uiAlertAction -> closeView());
        }
    }

    @Override
    public void viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews();
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
SCROLLVIEW DELEGATE METHODS
*/
    @Override
    public void didScroll(UIScrollView scrollView) {
        long page = (long) Math.floor((scrollView.getContentOffset().getX()) / (scrollView.getFrame().getSize().getWidth()));
        pager.setCurrentPage(page);
        uiCategory.setText(page >= 0 && page < categories.size()? categories.get((int)page) : "");
    }

    @Override
    public void didZoom(UIScrollView scrollView) {

    }

    @Override
    public void willBeginDragging(UIScrollView scrollView) {

    }

    @Override
    public void willEndDragging(UIScrollView scrollView, CGPoint velocity, CGPoint targetContentOffset) {

    }

    @Override
    public void didEndDragging(UIScrollView scrollView, boolean decelerate) {

    }

    @Override
    public void willBeginDecelerating(UIScrollView scrollView) {

    }

    @Override
    public void didEndDecelerating(UIScrollView scrollView) {

    }

    @Override
    public void didEndScrollingAnimation(UIScrollView scrollView) {

    }

    @Override
    public UIView getViewForZooming(UIScrollView scrollView) {
        return null;
    }

    @Override
    public void willBeginZooming(UIScrollView scrollView, UIView view) {

    }

    @Override
    public void didEndZooming(UIScrollView scrollView, UIView view, double scale) {

    }

    @Override
    public boolean shouldScrollToTop(UIScrollView scrollView) {
        return false;
    }

    @Override
    public void didScrollToTop(UIScrollView scrollView) {

    }

    @Override
    public void scrollViewDidChangeAdjustedContentInset(UIScrollView scrollView) {
    }
}
