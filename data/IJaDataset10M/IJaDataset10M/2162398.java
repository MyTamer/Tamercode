package sketch.experiment.sketchguidedrandom;

import sketch.ounit.fuzz.SketchGuidedRandomTestGenerator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SketchGuidedSAT4JTest extends TestCase {

    public static Test suite() {
        return new TestSuite(SketchGuidedSAT4JTest.class);
    }

    public void testGuidedSAT4J() throws Exception {
        String filePath = "./tmp/SAT4JConstrainTest.java";
        String junitclass = "SAT4JConstrainTest";
        int timelimit = 15;
        SketchGuidedRandomTestGenerator generator = new SketchGuidedRandomTestGenerator(filePath, junitclass, timelimit, timelimit);
        generator.setOutputPackageName("sketchguided.sat4j.autogenerated");
        generator.setMaxTestNum(500);
        generator.generate_tests();
    }
}
