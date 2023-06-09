package risk.game.utility;

import risk.game.entity.map.Continent;
import risk.game.entity.map.Land;
import risk.game.entity.map.World;
import risk.game.entity.player.Player;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.swt.widgets.Composite;

public class MapUtility {

    public static Map<String, Continent> CreateContinents(World world, Composite composite) {
        Map<String, Continent> continentsMap;
        Continent Asia, Europe, Australia, North_America, South_America, Africa;
        ArrayList<Land> AsiaLands = new ArrayList<Land>();
        Asia = new Continent("Asia", null, 7, AsiaLands);
        Asia.setWorld(world);
        Land afghanistan = new Land("Afghanistan", null, null, Asia, composite);
        Land china = new Land("China", null, null, Asia, composite);
        Land india = new Land("India", null, null, Asia, composite);
        Land irkutsk = new Land("Irkutsk", null, null, Asia, composite);
        Land japan = new Land("Japan", null, null, Asia, composite);
        Land kamchatka = new Land("Kamchatka", null, null, Asia, composite);
        Land middle_East = new Land("Middle_East", null, null, Asia, composite);
        Land mongolia = new Land("Mongolia", null, null, Asia, composite);
        Land siam = new Land("Siam", null, null, Asia, composite);
        Land siberia = new Land("Siberia", null, null, Asia, composite);
        Land ural = new Land("Ural", null, null, Asia, composite);
        Land yakutsk = new Land("Yakutsk", null, null, Asia, composite);
        AsiaLands.add(afghanistan);
        AsiaLands.add(china);
        AsiaLands.add(india);
        AsiaLands.add(irkutsk);
        AsiaLands.add(japan);
        AsiaLands.add(kamchatka);
        AsiaLands.add(middle_East);
        AsiaLands.add(mongolia);
        AsiaLands.add(siam);
        AsiaLands.add(siberia);
        AsiaLands.add(ural);
        AsiaLands.add(yakutsk);
        ArrayList<Land> EuropeLands = new ArrayList<Land>();
        Europe = new Continent("Europe", null, 5, EuropeLands);
        Europe.setWorld(world);
        Land great_Britain = new Land("Great_Britain", null, null, Europe, composite);
        Land iceland = new Land("Iceland", null, null, Europe, composite);
        Land northern_Europe = new Land("Northern_Europe", null, null, Europe, composite);
        Land scandinavia = new Land("Scandinavia", null, null, Europe, composite);
        Land southern_Europe = new Land("Southern_Europe", null, null, Europe, composite);
        Land ukraine_Europe = new Land("Ukraine_Europe", null, null, Europe, composite);
        Land western_Europe = new Land("Western_Europe", null, null, Europe, composite);
        EuropeLands.add(great_Britain);
        EuropeLands.add(iceland);
        EuropeLands.add(northern_Europe);
        EuropeLands.add(scandinavia);
        EuropeLands.add(southern_Europe);
        EuropeLands.add(ukraine_Europe);
        EuropeLands.add(western_Europe);
        ArrayList<Land> AustraliaLands = new ArrayList<Land>();
        Australia = new Continent("Australia", null, 2, AustraliaLands);
        Australia.setWorld(world);
        Land eastern_Australia = new Land("Eastern_Australia", null, null, Australia, composite);
        Land indonesia = new Land("Indonesia", null, null, Australia, composite);
        Land new_Guinea = new Land("New_Guinea", null, null, Australia, composite);
        Land western_Australia = new Land("Western_Australia", null, null, Australia, composite);
        AustraliaLands.add(eastern_Australia);
        AustraliaLands.add(indonesia);
        AustraliaLands.add(new_Guinea);
        AustraliaLands.add(western_Australia);
        ArrayList<Land> North_AmericaLands = new ArrayList<Land>();
        North_America = new Continent("North_America", null, 5, North_AmericaLands);
        North_America.setWorld(world);
        Land alaska = new Land("Alaska", null, null, North_America, composite);
        Land alberta = new Land("Alberta", null, null, North_America, composite);
        Land central_America = new Land("Central_America", null, null, North_America, composite);
        Land eastern_United_States = new Land("Eastern_United_States", null, null, North_America, composite);
        Land greenland = new Land("Greenland", null, null, North_America, composite);
        Land northwest_Territory = new Land("Northwest_Territory", null, null, North_America, composite);
        Land ontario = new Land("Ontario", null, null, North_America, composite);
        Land quebec = new Land("Quebec", null, null, North_America, composite);
        Land western_United_States = new Land("Western_United_States", null, null, North_America, composite);
        North_AmericaLands.add(alaska);
        North_AmericaLands.add(alberta);
        North_AmericaLands.add(central_America);
        North_AmericaLands.add(eastern_United_States);
        North_AmericaLands.add(greenland);
        North_AmericaLands.add(northwest_Territory);
        North_AmericaLands.add(ontario);
        North_AmericaLands.add(quebec);
        North_AmericaLands.add(western_United_States);
        ArrayList<Land> South_AmericaLands = new ArrayList<Land>();
        South_America = new Continent("South_America", null, 2, South_AmericaLands);
        South_America.setWorld(world);
        Land argentina = new Land("Argentina", null, null, South_America, composite);
        Land brazil = new Land("Brazil", null, null, South_America, composite);
        Land peru = new Land("Peru", null, null, South_America, composite);
        Land venezuela = new Land("Venezuela", null, null, South_America, composite);
        South_AmericaLands.add(argentina);
        South_AmericaLands.add(brazil);
        South_AmericaLands.add(peru);
        South_AmericaLands.add(venezuela);
        ArrayList<Land> AfricaLands = new ArrayList<Land>();
        Africa = new Continent("Africa", null, 3, AfricaLands);
        Africa.setWorld(world);
        Land central_Africa = new Land("Central_Africa", null, null, Africa, composite);
        Land east_Africa = new Land("East_Africa", null, null, Africa, composite);
        Land egypt = new Land("Egypt", null, null, Africa, composite);
        Land madagascar = new Land("Madagascar", null, null, Africa, composite);
        Land north_Africa = new Land("North_Africa", null, null, Africa, composite);
        Land south_Africa = new Land("South_Africa", null, null, Africa, composite);
        AfricaLands.add(central_Africa);
        AfricaLands.add(east_Africa);
        AfricaLands.add(egypt);
        AfricaLands.add(madagascar);
        AfricaLands.add(north_Africa);
        AfricaLands.add(south_Africa);
        List<Land> landBorderList;
        landBorderList = new ArrayList<Land>();
        landBorderList.add(ural);
        landBorderList.add(china);
        landBorderList.add(india);
        landBorderList.add(middle_East);
        landBorderList.add(ukraine_Europe);
        afghanistan.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(siam);
        landBorderList.add(india);
        landBorderList.add(afghanistan);
        landBorderList.add(ural);
        landBorderList.add(siberia);
        landBorderList.add(mongolia);
        china.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(middle_East);
        landBorderList.add(afghanistan);
        landBorderList.add(china);
        landBorderList.add(siam);
        india.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(mongolia);
        landBorderList.add(siberia);
        landBorderList.add(yakutsk);
        landBorderList.add(kamchatka);
        irkutsk.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(mongolia);
        landBorderList.add(kamchatka);
        japan.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(japan);
        landBorderList.add(mongolia);
        landBorderList.add(irkutsk);
        landBorderList.add(yakutsk);
        landBorderList.add(alaska);
        kamchatka.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(india);
        landBorderList.add(afghanistan);
        landBorderList.add(ukraine_Europe);
        landBorderList.add(southern_Europe);
        landBorderList.add(egypt);
        landBorderList.add(east_Africa);
        middle_East.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(china);
        landBorderList.add(siberia);
        landBorderList.add(irkutsk);
        landBorderList.add(kamchatka);
        landBorderList.add(japan);
        mongolia.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(china);
        landBorderList.add(india);
        landBorderList.add(indonesia);
        siam.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(yakutsk);
        landBorderList.add(irkutsk);
        landBorderList.add(mongolia);
        landBorderList.add(china);
        landBorderList.add(ural);
        siberia.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(siberia);
        landBorderList.add(china);
        landBorderList.add(afghanistan);
        landBorderList.add(ukraine_Europe);
        ural.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(kamchatka);
        landBorderList.add(irkutsk);
        landBorderList.add(siberia);
        yakutsk.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(western_Europe);
        landBorderList.add(northern_Europe);
        landBorderList.add(scandinavia);
        landBorderList.add(iceland);
        great_Britain.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(greenland);
        landBorderList.add(scandinavia);
        landBorderList.add(great_Britain);
        iceland.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(ukraine_Europe);
        landBorderList.add(southern_Europe);
        landBorderList.add(western_Europe);
        landBorderList.add(scandinavia);
        landBorderList.add(great_Britain);
        northern_Europe.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(ukraine_Europe);
        landBorderList.add(northern_Europe);
        landBorderList.add(great_Britain);
        landBorderList.add(iceland);
        scandinavia.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(ukraine_Europe);
        landBorderList.add(northern_Europe);
        landBorderList.add(western_Europe);
        landBorderList.add(middle_East);
        landBorderList.add(egypt);
        landBorderList.add(north_Africa);
        southern_Europe.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(northern_Europe);
        landBorderList.add(southern_Europe);
        landBorderList.add(middle_East);
        landBorderList.add(scandinavia);
        landBorderList.add(afghanistan);
        landBorderList.add(ural);
        ukraine_Europe.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(great_Britain);
        landBorderList.add(northern_Europe);
        landBorderList.add(southern_Europe);
        landBorderList.add(north_Africa);
        western_Europe.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(western_Australia);
        landBorderList.add(new_Guinea);
        eastern_Australia.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(western_Australia);
        landBorderList.add(new_Guinea);
        landBorderList.add(siam);
        indonesia.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(eastern_Australia);
        landBorderList.add(western_Australia);
        landBorderList.add(indonesia);
        new_Guinea.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(eastern_Australia);
        landBorderList.add(new_Guinea);
        landBorderList.add(indonesia);
        western_Australia.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(kamchatka);
        landBorderList.add(northwest_Territory);
        landBorderList.add(alberta);
        alaska.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(alaska);
        landBorderList.add(northwest_Territory);
        landBorderList.add(ontario);
        landBorderList.add(western_United_States);
        alberta.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(western_United_States);
        landBorderList.add(eastern_United_States);
        landBorderList.add(venezuela);
        central_America.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(central_America);
        landBorderList.add(western_United_States);
        landBorderList.add(ontario);
        landBorderList.add(quebec);
        eastern_United_States.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(ontario);
        landBorderList.add(quebec);
        landBorderList.add(northwest_Territory);
        landBorderList.add(iceland);
        greenland.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(alaska);
        landBorderList.add(alberta);
        landBorderList.add(ontario);
        landBorderList.add(greenland);
        northwest_Territory.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(quebec);
        landBorderList.add(eastern_United_States);
        landBorderList.add(western_United_States);
        landBorderList.add(alberta);
        landBorderList.add(northwest_Territory);
        landBorderList.add(greenland);
        ontario.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(eastern_United_States);
        landBorderList.add(ontario);
        landBorderList.add(greenland);
        quebec.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(central_America);
        landBorderList.add(eastern_United_States);
        landBorderList.add(ontario);
        landBorderList.add(alberta);
        western_United_States.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(peru);
        landBorderList.add(brazil);
        argentina.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(argentina);
        landBorderList.add(peru);
        landBorderList.add(venezuela);
        landBorderList.add(north_Africa);
        brazil.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(venezuela);
        landBorderList.add(brazil);
        landBorderList.add(argentina);
        peru.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(brazil);
        landBorderList.add(peru);
        landBorderList.add(central_America);
        venezuela.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(north_Africa);
        landBorderList.add(east_Africa);
        landBorderList.add(south_Africa);
        central_Africa.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(egypt);
        landBorderList.add(north_Africa);
        landBorderList.add(central_Africa);
        landBorderList.add(south_Africa);
        landBorderList.add(madagascar);
        landBorderList.add(middle_East);
        east_Africa.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(north_Africa);
        landBorderList.add(east_Africa);
        landBorderList.add(southern_Europe);
        landBorderList.add(middle_East);
        egypt.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(south_Africa);
        landBorderList.add(east_Africa);
        madagascar.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(egypt);
        landBorderList.add(east_Africa);
        landBorderList.add(central_Africa);
        landBorderList.add(southern_Europe);
        landBorderList.add(western_Europe);
        landBorderList.add(brazil);
        north_Africa.setBorderLandList(landBorderList);
        landBorderList = new ArrayList<Land>();
        landBorderList.add(central_Africa);
        landBorderList.add(east_Africa);
        landBorderList.add(madagascar);
        south_Africa.setBorderLandList(landBorderList);
        continentsMap = new HashMap<String, Continent>();
        continentsMap.put("Asia", Asia);
        continentsMap.put("Europe", Europe);
        continentsMap.put("Australia", Australia);
        continentsMap.put("North_America", North_America);
        continentsMap.put("South_America", South_America);
        continentsMap.put("Africa", Africa);
        return continentsMap;
    }

    public static void changeOwner(Land land, Player newOwner) {
        Player curPlayer = land.getOwner();
        if (curPlayer != null) {
            curPlayer.removeOwnedLand(land);
        }
        newOwner.addOwnedLand(land);
        land.setOwner(newOwner);
    }

    public static void changeOwner(Continent continent, Player newOwner) {
        Player curPlayer = continent.getOwner();
        if (curPlayer != null) {
            curPlayer.removeOwnedContinent(continent);
        }
        newOwner.addOwnedContinent(continent);
        continent.setOwner(newOwner);
    }

    public static String ContinentsToString(Map<String, Continent> continentsMap) {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = continentsMap.keySet();
        sb.append("The Continents are: ");
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            sb.append(((Continent) continentsMap.get(it.next())).toString());
        }
        return sb.toString();
    }
}
