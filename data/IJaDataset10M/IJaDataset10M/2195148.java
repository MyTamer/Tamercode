package net.sourceforge.pmd.renderers;

import java.util.Properties;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.Report.ProcessingError;
import net.sourceforge.pmd.renderers.CSVRenderer;
import net.sourceforge.pmd.renderers.Renderer;

public class CSVRendererTest extends AbstractRendererTst {

    public Renderer getRenderer() {
        return new CSVRenderer(new Properties());
    }

    public String getExpected() {
        return "\"Problem\",\"Package\",\"File\",\"Priority\",\"Line\",\"Description\",\"Rule set\",\"Rule\"" + PMD.EOL + "\"1\",\"\",\"n/a\",\"5\",\"1\",\"msg\",\"RuleSet\",\"Foo\"" + PMD.EOL;
    }

    public String getExpectedEmpty() {
        return "\"Problem\",\"Package\",\"File\",\"Priority\",\"Line\",\"Description\",\"Rule set\",\"Rule\"" + PMD.EOL;
    }

    public String getExpectedMultiple() {
        return "\"Problem\",\"Package\",\"File\",\"Priority\",\"Line\",\"Description\",\"Rule set\",\"Rule\"" + PMD.EOL + "\"1\",\"\",\"n/a\",\"5\",\"1\",\"msg\",\"RuleSet\",\"Foo\"" + PMD.EOL + "\"2\",\"\",\"n/a\",\"5\",\"1\",\"msg\",\"RuleSet\",\"Foo\"" + PMD.EOL;
    }

    public String getExpectedError(ProcessingError error) {
        return "\"Problem\",\"Package\",\"File\",\"Priority\",\"Line\",\"Description\",\"Rule set\",\"Rule\"" + PMD.EOL;
    }

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(CSVRendererTest.class);
    }
}
