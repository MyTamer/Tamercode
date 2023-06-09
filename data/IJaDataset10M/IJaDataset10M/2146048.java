package com.ravi.notify.mail;

import java.io.IOException;
import java.io.InputStream;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMultipart;
import org.apache.camel.Converter;
import org.apache.camel.converter.IOConverter;

/**
 * JavaMail specific converters.
 *
 * @version 
 */
@Converter
public final class MailConverters {

    private MailConverters() {
    }

    /**
     * Converts the given JavaMail message to a String body.
     * Can return null.
     */
    @Converter
    public static String toString(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) content;
            if (multipart.getCount() > 0) {
                BodyPart part = multipart.getBodyPart(0);
                content = part.getContent();
            }
        }
        if (content != null) {
            return content.toString();
        }
        return null;
    }

    /**
     * Converts the given JavaMail multipart to a String body, where the contenttype of the multipart
     * must be text based (ie start with text). Can return null.
     */
    @Converter
    public static String toString(Multipart multipart) throws MessagingException, IOException {
        int size = multipart.getCount();
        for (int i = 0; i < size; i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.getContentType().startsWith("text")) {
                return part.getContent().toString();
            }
        }
        return null;
    }

    /**
     * Converts the given JavaMail message to an InputStream.
     */
    @Converter
    public static InputStream toInputStream(Message message) throws IOException, MessagingException {
        return message.getInputStream();
    }

    /**
     * Converts the given JavaMail multipart to a InputStream body, where the contenttype of the multipart
     * must be text based (ie start with text). Can return null.
     */
    @Converter
    public static InputStream toInputStream(Multipart multipart) throws IOException, MessagingException {
        String s = toString(multipart);
        if (s == null) {
            return null;
        }
        return IOConverter.toInputStream(s, null);
    }
}
