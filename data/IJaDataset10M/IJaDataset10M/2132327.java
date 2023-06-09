package org.dspace.app.mediafilter;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.dspace.content.Bitstream;
import org.dspace.content.BitstreamFormat;
import org.dspace.content.Bundle;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.content.ItemIterator;
import org.dspace.core.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.PluginManager;
import org.dspace.handle.HandleManager;
import org.dspace.search.DSIndexer;

/**
 * MediaFilterManager is the class that invokes the media filters over the
 * repository's content. a few command line flags affect the operation of the
 * MFM: -v verbose outputs all extracted text to STDOUT; -f force forces all
 * bitstreams to be processed, even if they have been before; -n noindex does not
 * recreate index after processing bitstreams; -i [identifier] limits processing 
 * scope to a community, collection or item; and -m [max] limits processing to a
 * maximum number of items.
 */
public class MediaFilterManager {

    public static boolean updateIndex = true;

    public static boolean isVerbose = false;

    public static boolean isForce = false;

    public static String identifier = null;

    public static int max2Process = Integer.MAX_VALUE;

    public static int processed = 0;

    private static MediaFilter[] filterClasses = null;

    private static Map filterFormats = new HashMap();

    public static void main(String[] argv) throws Exception {
        System.setProperty("java.awt.headless", "true");
        CommandLineParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("v", "verbose", false, "print all extracted text and other details to STDOUT");
        options.addOption("f", "force", false, "force all bitstreams to be processed");
        options.addOption("n", "noindex", false, "do NOT update the search index after filtering bitstreams");
        options.addOption("i", "identifier", true, "ONLY process bitstreams belonging to identifier");
        options.addOption("m", "maximum", true, "process no more than maximum items");
        options.addOption("h", "help", false, "help");
        CommandLine line = parser.parse(options, argv);
        if (line.hasOption('h')) {
            HelpFormatter myhelp = new HelpFormatter();
            myhelp.printHelp("MediaFilter\n", options);
            System.exit(0);
        }
        if (line.hasOption('v')) {
            isVerbose = true;
        }
        if (line.hasOption('n')) {
            updateIndex = false;
        }
        if (line.hasOption('f')) {
            isForce = true;
        }
        if (line.hasOption('i')) {
            identifier = line.getOptionValue('i');
        }
        if (line.hasOption('m')) {
            max2Process = Integer.parseInt(line.getOptionValue('m'));
            if (max2Process <= 1) {
                System.out.println("Invalid maximum value '" + line.getOptionValue('m') + "' - ignoring");
                max2Process = Integer.MAX_VALUE;
            }
        }
        filterClasses = (MediaFilter[]) PluginManager.getPluginSequence(MediaFilter.class);
        for (int i = 0; i < filterClasses.length; i++) {
            String filterName = filterClasses[i].getClass().getName();
            String formats = ConfigurationManager.getProperty("filter." + filterName + ".inputFormats");
            if (formats != null) {
                filterFormats.put(filterName, Arrays.asList(formats.split(",[\\s]*")));
            }
        }
        Context c = null;
        try {
            c = new Context();
            c.setIgnoreAuthorization(true);
            if (identifier == null) {
                applyFiltersAllItems(c);
            } else {
                DSpaceObject dso = HandleManager.resolveToObject(c, identifier);
                if (dso == null) {
                    throw new IllegalArgumentException("Cannot resolve " + identifier + " to a DSpace object");
                }
                switch(dso.getType()) {
                    case Constants.COMMUNITY:
                        applyFiltersCommunity(c, (Community) dso);
                        break;
                    case Constants.COLLECTION:
                        applyFiltersCollection(c, (Collection) dso);
                        break;
                    case Constants.ITEM:
                        applyFiltersItem(c, (Item) dso);
                        break;
                }
            }
            if (updateIndex) {
                System.out.println("Updating search index:");
                DSIndexer.updateIndex(c);
            }
            c.complete();
            c = null;
        } finally {
            if (c != null) {
                c.abort();
            }
        }
    }

    public static void applyFiltersAllItems(Context c) throws Exception {
        ItemIterator i = Item.findAll(c);
        while (i.hasNext() && processed < max2Process) {
            applyFiltersItem(c, i.next());
        }
    }

    public static void applyFiltersCommunity(Context c, Community community) throws Exception {
        Community[] subcommunities = community.getSubcommunities();
        for (int i = 0; i < subcommunities.length; i++) {
            applyFiltersCommunity(c, subcommunities[i]);
        }
        Collection[] collections = community.getCollections();
        for (int j = 0; j < collections.length; j++) {
            applyFiltersCollection(c, collections[j]);
        }
    }

    public static void applyFiltersCollection(Context c, Collection collection) throws Exception {
        ItemIterator i = collection.getItems();
        while (i.hasNext() && processed < max2Process) {
            applyFiltersItem(c, i.next());
        }
    }

    public static void applyFiltersItem(Context c, Item item) throws Exception {
        if (filterItem(c, item)) {
            c.commit();
            ++processed;
        }
        item.decache();
    }

    /**
     * iterate through the item's bitstreams in the ORIGINAL bundle, applying
     * filters if possible
     * 
     * @return true if any bitstreams processed, 
     *         false if none
     */
    public static boolean filterItem(Context c, Item myItem) throws Exception {
        Bundle[] myBundles = myItem.getBundles("ORIGINAL");
        boolean done = false;
        for (int i = 0; i < myBundles.length; i++) {
            Bitstream[] myBitstreams = myBundles[i].getBitstreams();
            for (int k = 0; k < myBitstreams.length; k++) {
                done |= filterBitstream(c, myItem, myBitstreams[k]);
            }
        }
        return done;
    }

    /**
     * Attempt to filter a bitstream
     * 
     * An exception will be thrown if the media filter class cannot be
     * instantiated, exceptions from filtering will be logged to STDOUT and
     * swallowed.
     * 
     * @return true if bitstream processed, 
     *         false if no applicable filter or already processed
     */
    public static boolean filterBitstream(Context c, Item myItem, Bitstream myBitstream) throws Exception {
        boolean filtered = false;
        for (int i = 0; i < filterClasses.length; i++) {
            List fmts = (List) filterFormats.get(filterClasses[i].getClass().getName());
            if (fmts.contains(myBitstream.getFormat().getShortDescription())) {
                try {
                    if (filterClasses[i].processBitstream(c, myItem, myBitstream)) {
                        myItem.update();
                        filtered = true;
                    }
                } catch (Exception e) {
                    System.out.println("ERROR filtering, skipping bitstream #" + myBitstream.getID() + " " + e);
                    e.printStackTrace();
                }
            }
        }
        return filtered;
    }
}
