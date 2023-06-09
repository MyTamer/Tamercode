package opennlp.ccg.realize.hypertagger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import opennlp.ccg.grammar.Grammar;
import opennlp.ccg.hylo.HyloHelper;
import opennlp.ccg.realize.Realizer;
import opennlp.ccg.synsem.LF;
import org.jdom.Document;
import org.jdom.Element;

/**
 * @author espinosa
 *	This class abstracts over a collection of LFs contained in a collection of files.
 */
public class LFLoader implements Iterator<LFInfo> {

    static class XmlFilenameFilter implements FileFilter {

        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".xml");
        }
    }

    Grammar grammar;

    ArrayList<File> lfFiles;

    int filePos = 0;

    LinkedList<LFInfo> lfs;

    int total = 0;

    int skipped = 0;

    /**
	 * Constructs a new LFLoader which will load LFs from a collection of files or directories under a base directory.
	 * @param grammarFile The grammar to use
	 * @param baseDir The base directory. Paths will be interpreted relative to this directory.
	 * @param paths The files to load the LFs from. Directories or files can be given. Directories are not searched recursively. Only files ending
	 *  in .xml will be loaded.
	 */
    public LFLoader(File grammarFile, File baseDir, List<String> paths) {
        lfs = new LinkedList<LFInfo>();
        URL grammarURL = null;
        try {
            grammarURL = grammarFile.toURI().toURL();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        try {
            grammar = new Grammar(grammarURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lfFiles = new ArrayList<File>();
        paths = normalize(paths);
        for (String lfFilename : paths) {
            File f = new File(baseDir, lfFilename);
            if (f.isDirectory()) {
                lfFiles.addAll(Arrays.asList(f.listFiles(new XmlFilenameFilter())));
            } else {
                lfFiles.add(f);
            }
        }
    }

    private List<String> normalize(List<String> paths) {
        ArrayList<String> ret = new ArrayList<String>();
        for (String s : paths) {
            if (s.indexOf(',') < 0) {
                ret.add(s.trim());
            } else {
                String[] fields = s.split(",");
                for (String f : fields) {
                    ret.add(f.trim());
                }
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private void loadFile(File lfFile) {
        Document doc = null;
        int n = 0;
        try {
            doc = grammar.loadFromXml(lfFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Couldn't open input file " + lfFile + ", skipping.\n");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Element root = doc.getRootElement();
        List<Element> testItems = root.getChildren();
        for (Element item : testItems) {
            String lfNum = "unk";
            lfNum = item.getAttributeValue("info");
            Element itemLFElt = item.getChild("lf");
            Element itemPredInfoElt = item.getChild("pred-info");
            String predInfo = itemPredInfoElt.getAttributeValue("data");
            if (predInfo == null || predInfo.equals("")) {
                System.err.println("No pred-info found for lf #" + n + " in file " + lfFile + ", skipping.");
                skipped++;
                continue;
            }
            LF lf = Realizer.getLfFromElt(itemLFElt);
            LF flatLF = HyloHelper.flattenLF(lf);
            lfs.offer(new LFInfo(flatLF, predInfo, lfNum));
            n++;
            total++;
        }
        System.err.println("LFL: loaded " + n + " LFs from " + lfFile);
    }

    public boolean hasNext() {
        if (!lfs.isEmpty()) {
            return true;
        }
        while (lfs.isEmpty()) {
            if (filePos == lfFiles.size()) {
                return false;
            }
            loadFile(lfFiles.get(filePos));
            filePos++;
        }
        return true;
    }

    public LFInfo next() {
        if (!lfs.isEmpty()) {
            return lfs.poll();
        }
        while (lfs.isEmpty()) {
            if (filePos == lfFiles.size()) {
                return null;
            }
            loadFile(lfFiles.get(filePos));
        }
        return lfs.poll();
    }

    public void remove() {
        throw new RuntimeException("Method not implemented");
    }

    public int getTotal() {
        return this.total;
    }

    public int getSkipped() {
        return this.skipped;
    }
}
