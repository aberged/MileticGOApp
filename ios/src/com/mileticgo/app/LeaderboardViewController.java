package com.mileticgo.app;

import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellEditingStyle;
import org.robovm.apple.uikit.UITableViewDataSource;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.block.VoidBlock1;

import java.util.ArrayList;
import java.util.List;

@CustomClass("LeaderboardViewController")
public class LeaderboardViewController extends UIViewController implements UITableViewDataSource {

    private UITableView tableVC;
    @IBOutlet public void setTableVC(UITableView _ui) { this.tableVC = _ui; }

    private UILabel uiLabel;
    @IBOutlet
    public void setLabel(UILabel _ui) { this.uiLabel = _ui; }

    private UILabel uiScore;
    @IBOutlet
    public void setScore(UILabel _ui) { this.uiScore = _ui; }

    private final static List<TopScoreListItem> list = new ArrayList<>();

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Liderbord");
        getNavigationController().getNavigationBar().setTintColor(UIColor.white());

        uiLabel.setText(Repository.get().getUser().getName());
        uiScore.setText(String.valueOf(Repository.get().getUserInventoryCityPinsForActiveCityProfile().size()));

        initDataSource(tableVC);
    }

    private void initDataSource(final UITableView table) {
        Repository.get().getLeaderboard(new LeaderboardCallback() {
            @Override
            public void result(List<TopScoreListItem> _list) {
                DispatchQueue.getMainQueue().async(() -> {
                    LeaderboardViewController.list.clear();
                    LeaderboardViewController.list.addAll(_list);
                    table.reloadData();
                });
            }

            @Override
            public void error(String message) {
                DispatchQueue.getMainQueue().async(() ->
                        showTwoButtonPopup("Info", "Došlo je do greške. Proveri vezu s internetom i probaj ponovo?",
                        "OK", "Cancel",
                                uiAlertAction -> {
                                    // on ok retry
                                    initDataSource(table);
                                },
                                uiAlertAction -> {
                                    // on cancel just close
                                }));
            }
        });
    }

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
    }

    private void showTwoButtonPopup(String title, String msg, String button1Label, String button2Label,
                                    VoidBlock1<UIAlertAction> btnHandler1, VoidBlock1<UIAlertAction> btnHandler2) {
        // create the alert
        UIAlertController alert = new UIAlertController(title, msg, UIAlertControllerStyle.Alert);
        // add an action (button)
        alert.addAction(new UIAlertAction(button1Label, UIAlertActionStyle.Default, btnHandler1));
        alert.addAction(new UIAlertAction(button2Label, UIAlertActionStyle.Cancel, btnHandler2));
        // show the alert
        this.presentViewController(alert, true, null);
    }

    private void closeView() {
        getNavigationController().popViewController(true);
    }

/*
DATA SOURCE RELATED CODE
*/
    @Override
    public long getNumberOfRowsInSection(UITableView tableView, long section) {
        return list.size();
    }

    @Override
    public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
        System.out.println("getCellForRow(tw, " + indexPath.toString());
        System.out.println(list.get(indexPath.getItem()));

        LeaderBoardTableCell cell = (LeaderBoardTableCell) tableView.dequeueReusableCell("leaderBoardRestorationID", indexPath);
        list.get(indexPath.getItem()).setPosition(indexPath.getItem()+1);
        cell.setItem(list.get(indexPath.getItem()));

        return cell;
    }

    @Override
    public long getNumberOfSections(UITableView tableView) {
        return 1;
    }

    @Override
    public String getTitleForHeader(UITableView tableView, long section) {
        return null;
    }

    @Override
    public String getTitleForFooter(UITableView tableView, long section) {
        return null;
    }

    @Override
    public boolean canEditRow(UITableView tableView, NSIndexPath indexPath) {
        return false;
    }

    @Override
    public boolean canMoveRow(UITableView tableView, NSIndexPath indexPath) {
        return false;
    }

    @Override
    public List<String> getSectionIndexTitles(UITableView tableView) {
        return null;
    }

    @Override
    public long getSectionForSectionIndexTitle(UITableView tableView, String title, long index) {
        return 0;
    }

    @Override
    public void commitEditingStyleForRow(UITableView tableView, UITableViewCellEditingStyle editingStyle, NSIndexPath indexPath) {

    }

    @Override
    public void moveRow(UITableView tableView, NSIndexPath sourceIndexPath, NSIndexPath destinationIndexPath) {

    }

}
