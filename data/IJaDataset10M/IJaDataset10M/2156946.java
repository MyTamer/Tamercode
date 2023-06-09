package colibri.io.relation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import colibri.lib.Relation;

/**
 * Class for exporting a binary relation to a .con file.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class RelationWriterCON {

    /**
	 * writes the relation to a file in .con format
	 * @param relation the relation to be written
	 * @param file the file to which the relation shall be written. The file will be closed by this method.
	 * @throws IOException
	 */
    public void write(Relation relation, File file) throws IOException {
        final String beginDocument = "%\n%\n%\n\n\n";
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(beginDocument);
            Iterator<Comparable> objects = relation.getAllObjects().iterator();
            while (objects != null && objects.hasNext()) {
                Comparable object = objects.next();
                writer.write(object.toString() + ":\t\t");
                Iterator<Comparable> attributes = relation.getAttributes(object);
                while (attributes != null && attributes.hasNext()) {
                    Comparable attribute = attributes.next();
                    writer.write(attribute.toString() + " ");
                }
                writer.write(";\n");
            }
        } finally {
            writer.close();
        }
    }
}
