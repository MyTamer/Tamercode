package com.android.exchange.adapter;

import com.android.email.provider.EmailContent;
import com.android.email.provider.EmailProvider;
import com.android.email.provider.ProviderTestUtils;
import com.android.email.provider.EmailContent.Account;
import com.android.email.provider.EmailContent.Body;
import com.android.email.provider.EmailContent.Mailbox;
import com.android.email.provider.EmailContent.Message;
import com.android.exchange.EasSyncService;
import com.android.exchange.adapter.EmailSyncAdapter.EasEmailSyncParser;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.test.ProviderTestCase2;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EmailSyncAdapterTests extends ProviderTestCase2<EmailProvider> {

    EmailProvider mProvider;

    Context mMockContext;

    public EmailSyncAdapterTests() {
        super(EmailProvider.class, EmailProvider.EMAIL_AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMockContext = getMockContext();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Create and return a short, simple InputStream that has at least four bytes, which is all
     * that's required to initialize an EasParser (the parent class of EasEmailSyncParser)
     * @return the InputStream
     */
    public InputStream getTestInputStream() {
        return new ByteArrayInputStream(new byte[] { 0, 0, 0, 0, 0 });
    }

    EasSyncService getTestService() {
        Account account = new Account();
        account.mId = -1;
        Mailbox mailbox = new Mailbox();
        mailbox.mId = -1;
        EasSyncService service = new EasSyncService();
        service.mContext = getContext();
        service.mMailbox = mailbox;
        service.mAccount = account;
        return service;
    }

    EmailSyncAdapter getTestSyncAdapter() {
        EasSyncService service = getTestService();
        EmailSyncAdapter adapter = new EmailSyncAdapter(service.mMailbox, service);
        return adapter;
    }

    /**
     * Check functionality for getting mime type from a file name (using its extension)
     * The default for all unknown files is application/octet-stream
     */
    public void testGetMimeTypeFromFileName() throws IOException {
        EasSyncService service = getTestService();
        EmailSyncAdapter adapter = new EmailSyncAdapter(service.mMailbox, service);
        EasEmailSyncParser p = adapter.new EasEmailSyncParser(getTestInputStream(), adapter);
        String mimeType = p.getMimeTypeFromFileName("foo.jpg");
        assertEquals("image/jpeg", mimeType);
        mimeType = p.getMimeTypeFromFileName("foo.JPG");
        assertEquals("image/jpeg", mimeType);
        mimeType = p.getMimeTypeFromFileName("this_is_a_weird_filename.gif");
        assertEquals("image/gif", mimeType);
        mimeType = p.getMimeTypeFromFileName("foo.");
        assertEquals("application/octet-stream", mimeType);
        mimeType = p.getMimeTypeFromFileName(".....");
        assertEquals("application/octet-stream", mimeType);
        mimeType = p.getMimeTypeFromFileName("foo");
        assertEquals("application/octet-stream", mimeType);
        mimeType = p.getMimeTypeFromFileName("");
        assertEquals("application/octet-stream", mimeType);
    }

    public void testFormatDateTime() throws IOException {
        EmailSyncAdapter adapter = getTestSyncAdapter();
        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.set(2008, 11, 11, 18, 19, 20);
        String date = adapter.formatDateTime(calendar);
        assertEquals("2008-12-11T18:19:20.000Z", date);
        calendar.clear();
        calendar.set(2012, 0, 2, 23, 0, 1);
        date = adapter.formatDateTime(calendar);
        assertEquals("2012-01-02T23:00:01.000Z", date);
    }

    public void testSendDeletedItems() throws IOException {
        EasSyncService service = getTestService();
        EmailSyncAdapter adapter = new EmailSyncAdapter(service.mMailbox, service);
        Serializer s = new Serializer();
        ArrayList<Long> ids = new ArrayList<Long>();
        ArrayList<Long> deletedIds = new ArrayList<Long>();
        Context context = mMockContext;
        adapter.mContext = context;
        final ContentResolver resolver = context.getContentResolver();
        Account acct = ProviderTestUtils.setupAccount("account", true, context);
        adapter.mAccount = acct;
        Mailbox box1 = ProviderTestUtils.setupMailbox("box1", acct.mId, true, context);
        adapter.mMailbox = box1;
        Message msg1 = ProviderTestUtils.setupMessage("message1", acct.mId, box1.mId, true, true, context);
        ids.add(msg1.mId);
        Message msg2 = ProviderTestUtils.setupMessage("message2", acct.mId, box1.mId, true, true, context);
        ids.add(msg2.mId);
        Message msg3 = ProviderTestUtils.setupMessage("message3", acct.mId, box1.mId, true, true, context);
        ids.add(msg3.mId);
        assertEquals(3, EmailContent.count(context, Message.CONTENT_URI, null, null));
        for (long id : ids) {
            resolver.delete(ContentUris.withAppendedId(Message.SYNCED_CONTENT_URI, id), null, null);
        }
        assertEquals(0, EmailContent.count(context, Message.CONTENT_URI, null, null));
        assertEquals(3, EmailContent.count(context, Message.DELETED_CONTENT_URI, null, null));
        adapter.sendDeletedItems(s, deletedIds, true);
        assertEquals(3, deletedIds.size());
        deletedIds.clear();
        Message msg4 = ProviderTestUtils.setupMessage("message3", acct.mId, box1.mId, true, true, context);
        assertEquals(1, EmailContent.count(context, Message.CONTENT_URI, null, null));
        Body body = Body.restoreBodyWithMessageId(context, msg4.mId);
        ContentValues values = new ContentValues();
        values.put(Body.SOURCE_MESSAGE_KEY, msg2.mId);
        body.update(context, values);
        adapter.sendDeletedItems(s, deletedIds, true);
        assertEquals(2, deletedIds.size());
        assertFalse(deletedIds.contains(msg2.mId));
    }
}
