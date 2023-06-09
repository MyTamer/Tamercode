package net.seagis.coverage.metadata;

import net.seagis.coverage.catalog.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.seagis.catalog.Database;
import net.seagis.catalog.BoundedSingletonTable;
import net.seagis.catalog.CatalogException;

/**
 * Connection to a table of {@linkplain SeriesMetadata layer-level metadata}.
 * 
 * Metadata specific to the {@linkplain Layer layer}.
 *
 * @author Sam Hiatt
 * @version $Id: SeriesMetadataTable.java 615 2008-05-25 16:03:45Z desruisseaux $
 */
public class SeriesMetadataTable extends BoundedSingletonTable<SeriesMetadata> {

    /**
     * Creates a layer metadata table.
     *
     * @param database Connection to the database.
     */
    public SeriesMetadataTable(final Database database) {
        this(new SeriesMetadataQuery(database));
    }

    /**
     * Constructs a new {@code LayerTable} from the specified query.
     */
    private SeriesMetadataTable(final SeriesMetadataQuery query) {
        super(query);
        setIdentifierParameters(query.byName, null);
    }

    /**
     * Creates a layer metadata table using the same initial configuration than the specified table.
     */
    public SeriesMetadataTable(final SeriesMetadataTable table) {
        super(table);
    }

    /**
     * Creates a layer metadata from the current row in the specified result set.
     *
     * @param  results The result set to read.
     * @return The entry for current row in the specified result set.
     * @throws CatalogException if an inconsistent record is found in the database.
     * @throws SQLException if an error occured while reading the database.
     */
    protected SeriesMetadata createEntry(final ResultSet results) throws CatalogException, SQLException {
        final SeriesMetadataQuery query = (SeriesMetadataQuery) super.query;
        final String id = results.getString(indexOf(query.id));
        final String seriesName = results.getString(indexOf(query.seriesName));
        final String legendURI = results.getString(indexOf(query.legendURI));
        final String pubDate = results.getString(indexOf(query.pubDate));
        final String pocId = results.getString(indexOf(query.pocId));
        final String version = results.getString(indexOf(query.version));
        final String forecast = results.getString(indexOf(query.forecast));
        final String themekey1 = results.getString(indexOf(query.themekey1));
        final String themekey2 = results.getString(indexOf(query.themekey2));
        final String themekey3 = results.getString(indexOf(query.themekey3));
        final String themekey4 = results.getString(indexOf(query.themekey4));
        final String themekey5 = results.getString(indexOf(query.themekey5));
        final String themekey6 = results.getString(indexOf(query.themekey6));
        final String themekey7 = results.getString(indexOf(query.themekey7));
        final String themekey8 = results.getString(indexOf(query.themekey8));
        final SeriesMetadataEntry entry = new SeriesMetadataEntry(id, seriesName, legendURI, pubDate, pocId, version, forecast, themekey1, themekey2, themekey3, themekey4, themekey5, themekey6, themekey7, themekey8);
        return entry;
    }

    /**
     * Clears this table cache.
     */
    @Override
    public synchronized void flush() {
        super.flush();
    }
}
