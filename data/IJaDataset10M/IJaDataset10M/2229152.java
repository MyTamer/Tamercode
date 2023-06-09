package ontool.model.codegen;

import java.util.Properties;
import ontool.model.NetworkModel;

/**
 * Code generator interface.
 *
 * <p>The implementation is not required to perform any validation, thus
 * one can assume an error free model.
 *
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S.R. Gomes<a/>
 * @version $Id: CodeGenerator.java,v 1.1 2003/10/22 03:06:50 asrgomes Exp $
 */
public interface CodeGenerator {

    /**
     * Sets the properties.
     * @param props
     */
    void setProperties(Properties props);

    /**
     * Gets the properties.
     * @return properties
     */
    Properties getProperties();

    /**
     * Generate the source files.
     * @param net reference model
     */
    void generate(NetworkModel net) throws CodeGeneratorException;

    /**
     * Compiles the recently generated source files.
     */
    void compile() throws CodeGeneratorException;

    /**
     * Get the error messages, if any.
     */
    String getMessages();

    void setMessages(String m);

    /**
     * Removes any garbage generated by the code generator.
     */
    void clean();
}
