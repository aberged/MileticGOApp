package com.mileticgo.app;

import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIButton;
import org.robovm.apple.uikit.UIColor;
import org.robovm.apple.uikit.UIControlState;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBOutlet;
import org.robovm.objc.block.VoidBlock1;

@CustomClass("SettingsViewController")
public class SettingsViewController extends UIViewController {

    private UIButton logInOut;
    @IBOutlet
    public void setButtonPrijava(UIButton _ui) {
        this.logInOut = _ui;
    }

    private UIButton bRegister;
    @IBOutlet
    public void setButtonRegister(UIButton _ui) {
        this.bRegister = _ui;
    }

    private UIButton bAbout;
    @IBOutlet
    public void setButtonAbout(UIButton _ui) {
        this.bAbout = _ui;
    }

    private final static String aboutTxt = "Mobilna aplikacija Gradovi Unapređene Realnosti ili GURU nastala je sa ciljem digitalizacije kulture Republike Srbije. U slučaju GURU aplikacije, taj proces koristi tehnologije geolokacije i AR (Augmented Reality) da korisnicima približi značajne tačke za istoriju, kulturu, sport, umetnost, javni život i druge relevantne oblasti koje čine identitet svake države.\n" +
            "GURU aplikacija predstavlja kolekciju preko 100 lokacija u Novom Sadu, od kojih svaka nosi priču značajnu za ovaj grad, kao i ljude i događaje koji su ga stvorili. Koncept korišćenja uključuje odlazak na bilo koju od inicijalno dostupnih fizičkih lokacija (pinova) i “otključavanje” iste kroz korišćenje AR funkcije. Nakon otključavanja, korisnik može da dobije informacije o istoj lokaciji i onome što je čini značajnom. Kroz etapni proces otključavanja svih dostupnih pinova, korisnik dobija dodatne lokacije za istraživanje. Na taj način, korisnik polako može da upoznaje Novi Sad i saznaje njegove priče, a samim tim i njegovu prošlost, sadašnjost, ali i budućnost. Korisnik takođe može da vidi kako je rangiran u odnosu na druge korisnike putem Leaderbord opcije.\n" +
            "Mobilna aplikacija Gradovi Unapređene Realnosti nastala je uz podršku Ministarstva kulture i informisanja Republike Srbije i program projekata u kulturi \"SAVREMENO STVARALAŠTVO\", kao i podršku Gradske uprave za kulturu Novog Sada.\n" +
            "Sve komentare, primedbe i sugestije (uključujući i nove lokacije koje GURU treba da sadrži) šaljite na office@aimistudio.com";

    @Override
    public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        if (Repository.get().getUser().isAnonymous()) {
            logInOut.setTitle("PRIJAVA", UIControlState.Normal);
            bRegister.setHidden(false);
        } else {
            logInOut.setTitle("ODJAVA", UIControlState.Normal);
            bRegister.setHidden(true);
        }
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        getNavigationController().setNavigationBarHidden(false);
        getNavigationItem().setBackButtonTitle("Podešavanja");
        getNavigationController().getNavigationBar().setTintColor(UIColor.white());

        logInOut.addOnTouchUpInsideListener((control, event) -> {
            if (Repository.get().getUser().isAnonymous()) {
                UIViewController secondVC = Main.storyboard().instantiateViewController("SettingsLoginViewController");
                showViewController(secondVC, this);
            } else {
                System.out.println("loging out - " + Repository.get().getUser().toJson());
                showTwoButtonPopup("Odjava", "Nastavi sa odjavom?", "Odjava", "Cancel",
                        uiAlertAction -> Repository.get().logout((ready, updating, error, msg) -> {
                            DispatchQueue.getMainQueue().async(() -> {
                                if (error) {
                                    showOKButtonPopup("Greška!", msg, "OK", null);
                                } else if (ready) {
                                    showOKButtonPopup("Info", "Uspešno ste se odjavili.", "OK", null);
                                    logInOut.setTitle("PRIJAVA", UIControlState.Normal);
                                    bRegister.setHidden(false);
                                }
                            });
                            System.out.println(msg);
                        }),
                        uiAlertAction -> {});
            }
        });
        bAbout.addOnTouchUpInsideListener((control, event) -> {
            UIViewController secondVC = Main.storyboard().instantiateViewController("LocationDetailsViewController");
            ((LocationDetailsViewController) secondVC).setPin(new CityPin("O aplikaciji", aboutTxt));
            this.presentViewController(secondVC, true, null);
        });
    }

    private void showOKButtonPopup(String title, String msg, String buttonLabel, VoidBlock1<UIAlertAction> btnHandler) {
        // create the alert
        UIAlertController alert = new UIAlertController(title, msg, UIAlertControllerStyle.Alert);
        // add an action (button)
        alert.addAction(new UIAlertAction(buttonLabel, UIAlertActionStyle.Default, btnHandler));
        // show the alert
        this.presentViewController(alert, true, null);
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

}
