package org.xmlcml.cml.converters.cml;

import static org.xmlcml.euclid.EuclidConstants.S_PERIOD;
import static org.xmlcml.euclid.EuclidConstants.S_UNDER;
import java.io.File;
import java.io.FileOutputStream;
import nu.xom.Element;
import nu.xom.Node;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.cml.base.CMLUtil;
import org.xmlcml.cml.converters.AbstractMerger;
import org.xmlcml.cml.converters.ConverterCommand;
import org.xmlcml.cml.converters.Type;

/** 
 * this does not look finished yet
 * 
 * @author pm286
 *
 */
public class CMLMerger extends AbstractMerger {

    private static final Logger LOG = Logger.getLogger(CMLMerger.class);

    static {
        LOG.setLevel(Level.DEBUG);
    }

    public static final String[] typicalArgsForConverterCommand = { "-sd", "D:\\projects1\\thesis\\harter7\\data", "-is", "cdx.cml", "-os", "mol.cml", "-converter", "org.xmlcml.cml.converters.cml.CMLXPathSplitter", "-xpath", "//cml:molecule" };

    public CMLMerger() {
    }

    @Override
    protected void outputNode(Node node, int serial) {
        String fileExtension = ((ConverterCommand) this.getCommand()).getOutsuffix();
        if (fileExtension == null) {
            throw new RuntimeException("must set output suffix");
        }
        if (node == null) {
            throw new RuntimeException("Cannot output null node");
        }
        String filenameS = createFilename(outputDirectory, outputFile, serial);
        try {
            LOG.debug(" wrote FILE " + filenameS + " .. " + new File(filenameS).getAbsolutePath());
            CMLUtil.debug((Element) node, new FileOutputStream(filenameS), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createFilename(File outdir, File file, int serial) {
        String filename = outdir + File.separator + FilenameUtils.getName(file.toString()) + S_UNDER + serial;
        String outsuffix = ((ConverterCommand) this.getCommand()).getOutsuffix();
        if (outsuffix != null) {
            filename += S_PERIOD + outsuffix;
        }
        return filename;
    }

    public Type getInputType() {
        return Type.CML;
    }

    public Type getOutputType() {
        return Type.CML;
    }

    @Override
    public int getConverterVersion() {
        return 0;
    }
}
