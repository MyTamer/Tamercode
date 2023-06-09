package org.ektorp.impl;

import java.util.*;
import org.codehaus.jackson.*;
import org.codehaus.jackson.map.*;
import org.ektorp.http.*;

/**
 * 
 * @author henrik lundgren
 *
 */
public class DocIdResponseHandler extends StdResponseHandler<List<String>> {

    private final JsonFactory jsonFactory;

    public DocIdResponseHandler(ObjectMapper om) {
        jsonFactory = om.getJsonFactory();
    }

    @Override
    public List<String> success(HttpResponse hr) throws Exception {
        JsonParser jp = jsonFactory.createJsonParser(hr.getContent());
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new RuntimeException("Expected data to start with an Object");
        }
        boolean inRow = false;
        List<String> result = null;
        while (jp.nextToken() != null) {
            switch(jp.getCurrentToken()) {
                case START_ARRAY:
                    inRow = true;
                    break;
                case END_ARRAY:
                    inRow = false;
                    break;
                case FIELD_NAME:
                    String n = jp.getCurrentName();
                    if (inRow) {
                        if ("id".equals(n)) {
                            jp.nextToken();
                            result.add(jp.getText());
                        }
                    } else if ("total_rows".equals(n)) {
                        jp.nextToken();
                        result = new ArrayList<String>(jp.getIntValue());
                    }
                    break;
            }
        }
        return result;
    }
}
