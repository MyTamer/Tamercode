package cruise.umple.implementation.php;

import org.junit.*;
import cruise.umple.implementation.*;

public class PhpUnidirectionalOneTest extends UnidirectionalOneTest {

    @Before
    public void setUp() {
        super.setUp();
        language = "Php";
        languagePath = "php";
    }
}
