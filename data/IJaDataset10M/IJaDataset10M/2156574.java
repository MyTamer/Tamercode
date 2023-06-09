package net.sf.laja.launch;

import java.util.Arrays;
import java.util.List;
import net.sf.laja.SyntaxTextPrinter;
import net.sf.laja.template.Template;

/**
 * This class is used to launch Laja.
 * 
 * @author Joakim Tengstrand
 */
public class LajaLauncher {

    private Settings settings = new Settings();

    public Settings getSettings() {
        return settings;
    }

    public static void main(String... args) {
        new LajaLauncher().execute(Arrays.asList(args));
    }

    /**
     * Parses the input parameters.
     * For every template, execute all commands on that template and generate.
	 * 
	 * @param args input parameters
	 */
    public void execute(List<String> args) {
        try {
            InargsParser inargsParser = new InargsParser(settings, args);
            if (inargsParser.getViewInfoCommand() != null) {
                inargsParser.getViewInfoCommand().execute();
                return;
            }
            importSystemTemplate(inargsParser);
            generateTemplate(inargsParser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void print(List<String> args) {
        try {
            InargsParser inargsParser = new InargsParser(settings, args);
            if (inargsParser.getViewInfoCommand() != null) {
                inargsParser.getViewInfoCommand().execute();
                return;
            }
            importSystemTemplate(inargsParser);
            printTemplate(inargsParser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void importSystemTemplate(InargsParser inargsParser) {
        if (inargsParser.getSystemImportsCommand() != null) {
            inargsParser.getSystemImportsCommand().execute(null);
        }
    }

    private void generateTemplate(InargsParser inargsParser) {
        for (GenerateCommand generateCommand : inargsParser.getGenerateCommands()) {
            Template template = generateCommand.parseTemplate(settings.getSystemImportsFilename());
            for (PrepareGenerateCommand prepareGenerateCommand : inargsParser.getPrepareGenerateCommands()) {
                prepareGenerateCommand.execute(template);
            }
            template.generate();
        }
    }

    private void printTemplate(InargsParser inargsParser) {
        for (GenerateCommand generateCommand : inargsParser.getGenerateCommands()) {
            Template template = generateCommand.parseTemplate(settings.getSystemImportsFilename());
            for (PrepareGenerateCommand prepareGenerateCommand : inargsParser.getPrepareGenerateCommands()) {
                prepareGenerateCommand.execute(template);
            }
            template.print(new SyntaxTextPrinter());
        }
    }
}
