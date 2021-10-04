package com.mileticgo.app.Entities;

import java.util.ArrayList;
import java.util.List;

public class CityProfile {
    double lat = 45.2550458;
    double lng = 19.8447484;

    private List<CityPin> cityPins;

    private String katolicko_groblje = "Tzv. Kompleks katoličkog groblja obuhvata prostor koji je lociran preko puta Jodne banje, " +
            "odnosno zauzima prostor duž većeg dela današnje Futoške i ulice Vojvode Knićanina. Površina groblja je nepravilnog " +
            "pravougaonog, odnosno trapezastog oblika i u potpunosti je okružena zidom i ogradom. Sa dve strane je od okolnih objekata " +
            "izdvojeno ulicama, dok je sa preostale dve strane groblje praktično stešnjeno uz okolne zgrade i kuće od kojih je izdvojeno samo zidom." +
            "ako je najčešće poznato kao ,,Katoličko groblje”, celokupni kompleks zapravo čini pet relativno izdvojenih prostornih grobnih celina: " +
            "Rimokatoličko, Evangelističko (Luteransko) i Reformatsko (Kalvinističko), zatim tzv. Nemačko groblje, kao i Vojno-Avijatičarsko groblje. " +
            "Dominantnu površinu celog kompleksa zauzima Rimokatoličko groblje, dok nešto manji prostor površine od oko dva hektara zauzima prostorna " +
            "celina na kojoj se nalaze izmešani grobovi reformatkse (kalvinističke), kao i grobovi evangelističke (luteranske) konfesionalne pripadnosti. " +
            "Rimokatoičko groblje je od luteranskog i reformatskog simbolično prostorno razdvojeno monumentalnom kapijom u obliku slavoluka ispod " +
            "koje prolazi centralna popločana staza, odnosno glavna putna komunikacija koja po dužini prolazi kroz čitav kompleks groblja." ;

    private String futoski_park = "Futoški park je nastao u prvoj dekadi 20.veka, neposredno po izgradnji Jodne banje, kao park specijalne namene podignut oko banje. " +
            "Prvobitno idejno rešewe ovog parka je ponudio Armin Pec Mlađi, čuveni mađarski pejzažni arhitekta. Ovaj projekat nije realizovan, ali su u izvedenom " +
            "rešenju prepoznatljivi elementi ovog projekta.Delimična rekonstrukcija parka je izvršena 1964.godine, prema projektu obnove Futoškog parka (1962.godine) " +
            "autora Ratibora Đorđevića. Pažljivo osmišljene staze, aleje, odmorišta, bogati cvetni parteri i dendroflora ukazuju na visok nivo prvobitnog projektatskog rešenja. " +
            "Park karakteriše veoma bogat fond biljnog materijala, sa preko 100 vrsta, varijeteta i formi dekorativne dendroflore, sa mnogobrojnim egzotičnim i autohtonim sortama." +
            "U jugozapadnom delu parka nalazi se plitko jezero nepravilnog oblika, koje se napaja atmosferskom i podzemnim vodama. U parku se nalazi više geotermalnih izvora. " +
            "Na teritoriji parka ukupno je do sada bušeno sedam bunara. Na području Futoškog parka ustanovljen je režim zaštite drugog stepena, a staranje je povereno „Gradskom zelenilu";

    private String stadium_karadjordje = "Na Vidovdan 28. juna 1924. godine otvoren je stadion „Karađorđe na kome su igrala dva fudbalska kluba, Vojvodina i Jude Makabi." +
            " Izgrađen je na mestu starog biciklističkog veledroma iz 1888. godine, koji je okupljao srpsku i jevrejsku zajednicu. Svečano je otvoren na 120-godišnjicu od" +
            " Prvog srpskog ustanka. Pomoć izgradnji stadiona dali su navijači i simpatizeri oba kluba, a značajna sredstva prikupljena su od gostovanja američkih operskih " +
            "grupa „Luzijana i „Plav ptica. Projektovao ga inženjer David Popović, tadašnji predsednik Vojvodine. Stadion je od svog otvaranja do Drugog svetskog rata nosio " +
            "naziv „Karađorđe“. Posle je bio poznat samo kao Gradski stadion, dok je prvobitno ime vraćeno 2007. godine. Ime je dobio po Karađorđu, vođi Prvog srpskog ustanka" +
            "Tokom Drugog svetskog rata podignuta je kućica na južnoj strani stadiona, 1991. je dovršena severna tribina. Atletska staza postavljena je 2004. a od 2008. do 2009." +
            " izvršena je manja rekonstrukcija, kada je izgrađena nova jugoistočna tribina, na koju je postavljen savremeni „FILIPS-ov semafor.";

    private String kancelarija = "mozes i da radis";

    private String biotest = "biotest laboratorija";

    public CityProfile(){
        CityPin p1 = new CityPin(45.24797381951175,19.828481122745778,"Staro katolicko groblje", katolicko_groblje, true, false);
        CityPin p2 = new CityPin(45.24991796512109,19.828086628223925,"Futoski park", futoski_park,false, false);
        CityPin p3 = new CityPin(45.2474397019441,19.84313811108686,"Stadio Karadjordje", stadium_karadjordje,false, false);
        CityPin p4 = new CityPin(45.259301731154984, 19.843383285506537,"Kancelarija", kancelarija,true, false);
        CityPin p5 = new CityPin(45.24361254451388, 19.80744289461784,"biotest", biotest,true, false);
        cityPins = new ArrayList<>();
        cityPins.add(p1);
        cityPins.add(p2);
        cityPins.add(p3);
        cityPins.add(p4);
        cityPins.add(p5);
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public List<CityPin> getCityPins() {
        return cityPins;
    }
}
