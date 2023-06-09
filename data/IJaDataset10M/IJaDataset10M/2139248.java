package com.thaiopensource.relaxng.input;

import com.thaiopensource.relaxng.edit.SchemaCollection;
import com.thaiopensource.relaxng.translate.util.InvalidParamsException;
import com.thaiopensource.resolver.Resolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import java.io.IOException;

public abstract class AbstractMultiInputFormat implements MultiInputFormat {

    public SchemaCollection load(String uri, String[] params, String outputFormat, ErrorHandler eh, Resolver resolver) throws InputFailedException, InvalidParamsException, IOException, SAXException {
        return load(new String[] { uri }, params, outputFormat, eh, resolver);
    }
}
