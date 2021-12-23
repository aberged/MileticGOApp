package com.mileticgo.app;

import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.UIEdgeInsets;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIImageView;
import org.robovm.apple.uikit.UILabel;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellEditingStyle;
import org.robovm.apple.uikit.UITableViewDataSource;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;

import java.util.ArrayList;
import java.util.List;

@CustomClass("LeaderBoardTableCell")
public class LeaderBoardTableCell extends UITableViewCell {

    private UILabel uiLabel;
    @IBOutlet
    public void setLabel(UILabel _ui) { this.uiLabel = _ui; }

    private UILabel uiPosition;
    @IBOutlet
    public void setPosition(UILabel _ui) { this.uiPosition = _ui; }

    private UILabel uiScore;
    @IBOutlet
    public void setScore(UILabel _ui) { this.uiScore = _ui; }

    private UIImageView avatar;
    @IBOutlet void setAvatar(UIImageView _ui) { this.avatar = _ui; }

    private UIImageView uiImage;
    @IBOutlet
    public void setBg(UIImageView _ui) { this.uiImage = _ui; }

    public void setItem(TopScoreListItem ts) {
        uiLabel.setText(ts.getUserName());
        uiPosition.setText(String.valueOf(ts.getPosition()));
        uiScore.setText(String.valueOf(ts.getUserPoints()));
        uiImage.setAlpha(0.5);
        avatar.setAlpha(Repository.get().getUser().getEmail().equals(ts.getEmail())
                && !Repository.get().getUser().isAnonymous()? 1 : 0);
    }

    @Override
    public void layoutSubviews() {
        super.layoutSubviews();
        getContentView().setFrame(getContentView().getFrame().inset(new UIEdgeInsets(0,0,10,0)));
    }
}
