package com.android.im.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ContactList extends ImEntity {

    protected Address mAddress;

    protected String mName;

    protected boolean mDefault;

    protected ContactListManager mManager;

    private HashMap<String, Contact> mContactsCache;

    public ContactList(Address address, String name, boolean isDefault, Collection<Contact> contacts, ContactListManager manager) {
        mAddress = address;
        mDefault = isDefault;
        mName = name;
        mManager = manager;
        mContactsCache = new HashMap<String, Contact>();
        if (contacts != null) {
            for (Contact c : contacts) {
                mContactsCache.put(manager.normalizeAddress(c.getAddress().getFullName()), c);
            }
        }
    }

    @Override
    public Address getAddress() {
        return mAddress;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        if (null == name) {
            throw new NullPointerException();
        }
        mManager.setListNameAsync(name, this);
    }

    public void setDefault(boolean isDefault) {
        this.mDefault = isDefault;
    }

    public boolean isDefault() {
        return mDefault;
    }

    /**
     * Add a contact to the list. The contact is specified by its address
     * string.
     *
     * @param address
     *            the address string specifies the contact.
     * @throws IllegalArgumentException
     *             if the address is invalid.
     * @throws NullPointerException
     *             if the address string is null
     * @throws ImException
     *             if the contact is not allowed to be added
     */
    public void addContact(String address) throws ImException {
        address = mManager.normalizeAddress(address);
        if (null == address) {
            throw new NullPointerException();
        }
        if (mManager.isBlocked(address)) {
            throw new ImException(ImErrorInfo.CANT_ADD_BLOCKED_CONTACT, "Contact has been blocked");
        }
        if (containsContact(address)) {
            throw new ImException(ImErrorInfo.CONTACT_EXISTS_IN_LIST, "Contact already exists in the list");
        }
        mManager.addContactToListAsync(address, this);
    }

    /**
     * Remove a contact from the list. If the contact is not in the list,
     * nothing will happen. Otherwise, the contact will be removed from
     * the list on the server asynchronously.
     *
     * @param address
     *            the address of the contact to be removed from the list
     * @throws NullPointerException
     *             If the address is null
     */
    public void removeContact(Address address) throws ImException {
        if (address == null) {
            throw new NullPointerException();
        }
        Contact c = getContact(address);
        if (c != null) {
            removeContact(c);
        }
    }

    /**
     * Remove a contact from the list. If the contact is not in the list,
     * nothing will happen. Otherwise, the contact will be removed from
     * the list on the server asynchronously.
     *
     * @param contact
     *            the contact to be removed from the list
     * @throws NullPointerException
     *             If the contact is null
     */
    public void removeContact(Contact contact) throws ImException {
        if (contact == null) {
            throw new NullPointerException();
        }
        if (containsContact(contact)) {
            mManager.removeContactFromListAsync(contact, this);
        }
    }

    public synchronized Contact getContact(Address address) {
        return mContactsCache.get(mManager.normalizeAddress(address.getFullName()));
    }

    public synchronized Contact getContact(String address) {
        return mContactsCache.get(mManager.normalizeAddress(address));
    }

    public synchronized int getContactsCount() {
        return mContactsCache.size();
    }

    public synchronized Collection<Contact> getContacts() {
        return new ArrayList<Contact>(mContactsCache.values());
    }

    public synchronized boolean containsContact(String address) {
        return mContactsCache.containsKey(mManager.normalizeAddress(address));
    }

    public synchronized boolean containsContact(Address address) {
        return address == null ? false : mContactsCache.containsKey(mManager.normalizeAddress(address.getFullName()));
    }

    public synchronized boolean containsContact(Contact c) {
        return c == null ? false : mContactsCache.containsKey(mManager.normalizeAddress(c.getAddress().getFullName()));
    }

    protected void insertToCache(Contact contact) {
        mContactsCache.put(mManager.normalizeAddress(contact.getAddress().getFullName()), contact);
    }

    protected void removeFromCache(Contact contact) {
        mContactsCache.remove(mManager.normalizeAddress(contact.getAddress().getFullName()));
    }
}
