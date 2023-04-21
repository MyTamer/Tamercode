package takatuka.vm.autoGenerated;

import java.io.*;
import takatuka.classreader.logic.file.*;
import takatuka.classreader.logic.util.*;

/**
 * 
 * Description:
 * <p>
 *
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class HeaderFileConstants {

    public static final String AUTO_GENERATED_MSG = "/* ***** This is an" + " autogenerated file. (time=" + System.currentTimeMillis() + ") **** */ \n\n";

    public static String headerStart(String headerFileName) {
        if (headerFileName == null) {
            Miscellaneous.printlnErr("Header file name is null. Check you configuration file ");
            Miscellaneous.exit();
        }
        if (!headerFileName.endsWith(".h")) {
            Miscellaneous.printlnErr("Header file name must end with .h i.e " + headerFileName);
            Miscellaneous.exit();
        }
        headerFileName = headerFileName.replace("\\", "/");
        if (headerFileName.indexOf("/") != -1) {
            headerFileName = headerFileName.substring(headerFileName.lastIndexOf("/") + 1);
        }
        headerFileName = (headerFileName.replace(".", "_")).toUpperCase();
        String upercaseHeaderName = "#ifndef" + " " + headerFileName;
        upercaseHeaderName += "\n#define" + " " + headerFileName;
        return AUTO_GENERATED_MSG + upercaseHeaderName + "\n\n";
    }

    public static String headerEnd() {
        return "\n#endif\n";
    }

    public static void writeHeaderFile(String headerFileData, String headerFileName) {
        try {
            String contents = headerStart(headerFileName) + "\n\n";
            contents += headerFileData + "\n" + HeaderFileConstants.headerEnd();
            File file = new File(headerFileName);
            ClassFileWriter.writeFile(new File(file.getAbsolutePath()), contents);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
    }

    private HeaderFileConstants() {
    }
}