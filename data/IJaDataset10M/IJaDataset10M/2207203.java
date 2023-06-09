package net.sf.jabref.export;

import java.util.Set;
import net.sf.jabref.BibtexDatabase;
import net.sf.jabref.Globals;
import net.sf.jabref.MetaData;
import net.sf.jabref.sql.DBExporterAndImporterFactory;

/**
 * MySQLExport contributed by Lee Patton.
 * PostgreSQLExport contributed by Fred Stevens.
 */
public class PostgreSQLExport extends ExportFormat {

    public PostgreSQLExport() {
        super(Globals.lang("PostgreSQL database"), "postgresql", null, null, ".sql");
    }

    /**
     * First method called when user starts the export.
     * 
     * @param database
     *            The bibtex database from which to export.
     * @param file
     *            The filename to which the export should be writtten.
     * @param encoding
     *            The encoding to use.
     * @param keySet
     *            The set of IDs of the entries to export.
     * @throws java.lang.Exception
     *             If something goes wrong, feel free to throw an exception. The
     *             error message is shown to the user.
     */
    public void performExport(final BibtexDatabase database, final MetaData metaData, final String file, final String encoding, Set<String> keySet) throws Exception {
        new DBExporterAndImporterFactory().getExporter("POSTGRESQL").exportDatabaseAsFile(database, metaData, keySet, file);
    }
}
