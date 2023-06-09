package gov.nasa.worldwind.util.gdal;

import java.io.File;

/**
 * @author Lado Garakanidze
 * @version $Id: GDALDataFinder.java 1 2011-07-16 23:22:47Z dcollins $
 */
class GDALDataFinder extends GDALAbstractFileFilter {

    public GDALDataFinder() {
        super("gdal_datum.csv");
    }

    public boolean accept(File pathname) {
        String filename;
        String dir;
        if (null != pathname && !isHidden(pathname.getAbsolutePath()) && null != (dir = pathname.getParent()) && !this.listFolders.contains(dir) && null != (filename = pathname.getName()) && searchPattern.equalsIgnoreCase(filename)) {
            this.listFolders.add(dir);
            return true;
        }
        Thread.yield();
        return false;
    }
}
