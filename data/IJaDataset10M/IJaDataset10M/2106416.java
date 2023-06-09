package com.android.email.mail.internet;

import com.android.email.mail.MessagingException;
import com.android.email.mail.Multipart;
import com.android.email.mail.Part;
import com.android.email.mail.store.LocalStore.LocalAttachmentBodyPart;
import com.android.email.provider.AttachmentProvider;
import com.android.email.provider.EmailContent;
import android.content.ContentResolver;
import android.net.Uri;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailHtmlUtil {

    private static final Pattern PLAIN_TEXT_TO_ESCAPE = Pattern.compile("[<>&]| {2,}|\r?\n");

    /**
     * Resolve content-id reference in src attribute of img tag to AttachmentProvider's
     * content uri.  This method calls itself recursively at most the number of
     * LocalAttachmentPart that mime type is image and has content id.
     * The attribute src="cid:content_id" is resolved as src="content://...".
     * This method is package scope for testing purpose.
     *
     * @param text html email text
     * @param part mime part which may contain inline image
     * @return html text in which src attribute of img tag may be replaced with content uri
     */
    public static String resolveInlineImage(ContentResolver resolver, long accountId, String text, Part part, int depth) throws MessagingException {
        if (depth >= 10 || text == null) {
            return text;
        }
        String contentType = MimeUtility.unfoldAndDecode(part.getContentType());
        String contentId = part.getContentId();
        if (contentType.startsWith("image/") && contentId != null && part instanceof LocalAttachmentBodyPart) {
            LocalAttachmentBodyPart attachment = (LocalAttachmentBodyPart) part;
            Uri attachmentUri = AttachmentProvider.getAttachmentUri(accountId, attachment.getAttachmentId());
            Uri contentUri = AttachmentProvider.resolveAttachmentIdToContentUri(resolver, attachmentUri);
            String contentIdRe = "\\s+(?i)src=\"cid(?-i):\\Q" + contentId + "\\E\"";
            text = text.replaceAll(contentIdRe, " src=\"" + contentUri + "\"");
        }
        if (part.getBody() instanceof Multipart) {
            Multipart mp = (Multipart) part.getBody();
            for (int i = 0; i < mp.getCount(); i++) {
                text = resolveInlineImage(resolver, accountId, text, mp.getBodyPart(i), depth + 1);
            }
        }
        return text;
    }

    /**
     * Escape some special character as HTML escape sequence.
     * 
     * @param text Text to be displayed using WebView.
     * @return Text correctly escaped.
     */
    public static String escapeCharacterToDisplay(String text) {
        Pattern pattern = PLAIN_TEXT_TO_ESCAPE;
        Matcher match = pattern.matcher(text);
        if (match.find()) {
            StringBuilder out = new StringBuilder();
            int end = 0;
            do {
                int start = match.start();
                out.append(text.substring(end, start));
                end = match.end();
                int c = text.codePointAt(start);
                if (c == ' ') {
                    for (int i = 1, n = end - start; i < n; ++i) {
                        out.append("&nbsp;");
                    }
                    out.append(' ');
                } else if (c == '\r' || c == '\n') {
                    out.append("<br>");
                } else if (c == '<') {
                    out.append("&lt;");
                } else if (c == '>') {
                    out.append("&gt;");
                } else if (c == '&') {
                    out.append("&amp;");
                }
            } while (match.find());
            out.append(text.substring(end));
            text = out.toString();
        }
        return text;
    }
}
