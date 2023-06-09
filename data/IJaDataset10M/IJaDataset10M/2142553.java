package com.limegroup.gnutella.settings;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import com.limegroup.gnutella.ErrorService;
import com.limegroup.gnutella.MessageService;
import com.limegroup.gnutella.util.FileUtils;

/**
 * Class for handling all LimeWire settings that are stored to disk.  To
 * add a new setting, simply add a new public static member to the list
 * of settings.  Each setting constructor takes the name of the key and 
 * the default value, and all settings are typed.  Choose the correct 
 * <tt>Setting</tt> subclass for your setting type.  It is also important
 * to choose a unique string key for your setting name -- otherwise there
 * will be conflicts.
 */
public final class SettingsFactory implements Iterable<Setting> {

    /**
     * Time interval, after which the accumulated information expires
     */
    private static final long EXPIRY_INTERVAL = 14 * 24 * 60 * 60 * 1000;

    /**
     * An internal Setting to store the last expire time
     */
    private LongSetting LAST_EXPIRE_TIME = null;

    /** 
     * <tt>File</tt> object from which settings are loaded and saved 
     */
    private File SETTINGS_FILE;

    private final String HEADING;

    /**
     * <tt>Properties</tt> instance for the defualt values.
     */
    protected final Properties DEFAULT_PROPS = new Properties();

    /**
     * The <tt>Properties</tt> instance containing all settings.
     */
    protected final Properties PROPS = new Properties(DEFAULT_PROPS);

    private ArrayList<Setting> settings = new ArrayList<Setting>(10);

    /**
     * A mapping of simppKeys to Settings. Only Simpp Enabled settings will be
     * added to this list. As setting are created, they are added to this map so
     * that when simpp settings are loaded, it's easy to find the targeted
     * settings.
     */
    private Map<String, Setting> simppKeyToSetting = new HashMap<String, Setting>();

    private boolean expired = false;

    /**
     * Creates a new <tt>SettingsFactory</tt> instance with the specified file
     * to read from and write to.
     *
     * @param settingsFile the file to read from and to write to
     */
    public SettingsFactory(File settingsFile) {
        this(settingsFile, "");
    }

    /**
     * Creates a new <tt>SettingsFactory</tt> instance with the specified file
     * to read from and write to.
     *
     * @param settingsFile the file to read from and to write to
     * @param heading heading to use when writing property file
     */
    public SettingsFactory(File settingsFile, String heading) {
        SETTINGS_FILE = settingsFile;
        if (SETTINGS_FILE.isDirectory()) SETTINGS_FILE.delete();
        HEADING = heading;
        reload();
    }

    /**
     * Returns the iterator over the settings stored in this factory.
     *
     * LOCKING: The caller must ensure that this factory's monitor
     *   is held while iterating over the iterator.
     */
    public synchronized Iterator<Setting> iterator() {
        return settings.iterator();
    }

    /**
     * Reloads the settings with the predefined settings file from
     * disk.
     */
    public synchronized void reload() {
        if (!SETTINGS_FILE.isFile()) {
            setExpireValue();
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(SETTINGS_FILE);
            try {
                PROPS.load(fis);
            } catch (IllegalArgumentException ignored) {
            } catch (StringIndexOutOfBoundsException sioobe) {
            } catch (IOException iox) {
                String msg = iox.getMessage();
                if (msg != null) {
                    msg = msg.toLowerCase();
                    if (msg.indexOf("corrupted") == -1) throw iox;
                }
                SETTINGS_FILE.delete();
                MessageService.showError("ERROR_PROPS_CORRUPTED");
            }
        } catch (IOException e) {
            ErrorService.error(e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        for (Setting set : settings) set.reload();
        setExpireValue();
    }

    /**
     * Sets the last expire time if not already set.
     */
    private synchronized void setExpireValue() {
        if (LAST_EXPIRE_TIME == null) {
            LAST_EXPIRE_TIME = createLongSetting("LAST_EXPIRE_TIME", 0);
            expired = (LAST_EXPIRE_TIME.getValue() + EXPIRY_INTERVAL < System.currentTimeMillis());
            if (expired) LAST_EXPIRE_TIME.setValue(System.currentTimeMillis());
        }
    }

    /**
     * Changes the backing file to use for this factory.
     */
    public synchronized void changeFile(File toUse) {
        SETTINGS_FILE = toUse;
        if (SETTINGS_FILE.isDirectory()) SETTINGS_FILE.delete();
        revertToDefault();
        reload();
    }

    /**
     * Reverts all settings to their factory defaults.
     */
    public synchronized void revertToDefault() {
        for (Setting set : settings) set.revertToDefault();
    }

    /**
     * Save setting information to property file
     * We want to NOT save any properties which are the default value,
     * as well as any older properties that are no longer in use.
     * To avoid having to manually encode the file, we clone
     * the existing properties and manually remove the ones
     * which are default and aren't required to be saved.
     * It is important to do it this way (as opposed to creating a new
     * properties object and adding only those that should be saved
     * or aren't default) because 'adding' properties may fail if
     * certain settings classes haven't been statically loaded yet.
     * (Note that we cannot use 'store' since it's only available in 1.2)
     */
    public synchronized void save() {
        Properties toSave = (Properties) PROPS.clone();
        for (Setting set : settings) {
            if (!set.shouldAlwaysSave() && set.isDefault()) toSave.remove(set.getKey());
        }
        OutputStream out = null;
        try {
            if (SETTINGS_FILE.isDirectory()) SETTINGS_FILE.delete();
            File parent = SETTINGS_FILE.getParentFile();
            if (parent != null) {
                parent.mkdirs();
                FileUtils.setWriteable(parent);
            }
            FileUtils.setWriteable(SETTINGS_FILE);
            try {
                out = new BufferedOutputStream(new FileOutputStream(SETTINGS_FILE));
            } catch (IOException ioe) {
                SETTINGS_FILE.delete();
                out = new BufferedOutputStream(new FileOutputStream(SETTINGS_FILE));
            }
            toSave.store(out, HEADING);
        } catch (IOException e) {
            ErrorService.error(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * Return settings properties
     */
    Properties getProperties() {
        return PROPS;
    }

    /**
     * Creates a new <tt>StringSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized StringSetting createStringSetting(String key, String defaultValue) {
        StringSetting result = new StringSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    /**
     * @param useSimpp if true, makes the setting SimppEnabled
     */
    public synchronized StringSetting createSettableStringSetting(String key, String defaultValue, String simppKey) {
        StringSetting result = new StringSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>BooleanSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized BooleanSetting createBooleanSetting(String key, boolean defaultValue) {
        BooleanSetting result = new BooleanSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    /**
     * if max != min, the setting becomes unsettable
     */
    public synchronized BooleanSetting createSettableBooleanSetting(String key, boolean defaultValue, String simppKey) {
        BooleanSetting result = new BooleanSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>IntSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized IntSetting createIntSetting(String key, int defaultValue) {
        IntSetting result = new IntSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized IntSetting createSettableIntSetting(String key, int defaultValue, String simppKey, int min, int max) {
        IntSetting result = new IntSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey, min, max);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>ByteSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized ByteSetting createByteSetting(String key, byte defaultValue) {
        ByteSetting result = new ByteSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized ByteSetting createSettableByteSetting(String key, byte defaultValue, String simppKey, byte min, byte max) {
        ByteSetting result = new ByteSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey, min, max);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>LongSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized LongSetting createLongSetting(String key, long defaultValue) {
        LongSetting result = new LongSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized LongSetting createSettableLongSetting(String key, long defaultValue, String simppKey, long min, long max) {
        LongSetting result = new LongSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey, min, max);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>PowerOfTwoSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting, which must be a
     *            power of two.
     */
    public synchronized PowerOfTwoSetting createPowerOfTwoSetting(String key, long defaultValue) {
        PowerOfTwoSetting result = new PowerOfTwoSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized PowerOfTwoSetting createSettablePowerOfTwoSetting(String key, long defaultValue, String simppKey, long min, long max) {
        PowerOfTwoSetting result = new PowerOfTwoSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey, min, max);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>FileSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized FileSetting createFileSetting(String key, File defaultValue) {
        String parentString = defaultValue.getParent();
        if (parentString != null) {
            File parent = new File(parentString);
            if (!parent.isDirectory()) parent.mkdirs();
        }
        FileSetting result = new FileSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized FileSetting createSettableFileSetting(String key, File defaultValue, String simppKey) {
        String parentString = defaultValue.getParent();
        if (parentString != null) {
            File parent = new File(parentString);
            if (!parent.isDirectory()) parent.mkdirs();
        }
        FileSetting result = new FileSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    public synchronized ProxyFileSetting createProxyFileSetting(String key, FileSetting defaultSetting) {
        ProxyFileSetting result = new ProxyFileSetting(DEFAULT_PROPS, PROPS, key, defaultSetting);
        handleSettingInternal(result, null);
        return result;
    }

    /**
     * Creates a new <tt>ColorSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized ColorSetting createColorSetting(String key, Color defaultValue) {
        ColorSetting result = ColorSetting.createColorSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized ColorSetting createSettableColorSetting(String key, Color defaultValue, String simppKey) {
        ColorSetting result = ColorSetting.createColorSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>CharArraySetting</tt> instance for a character array 
     * setting with the specified key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized CharArraySetting createCharArraySetting(String key, char[] defaultValue) {
        CharArraySetting result = CharArraySetting.createCharArraySetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized CharArraySetting createSettableCharArraySetting(String key, char[] defaultValue, String simppKey) {
        CharArraySetting result = new CharArraySetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>FloatSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized FloatSetting createFloatSetting(String key, float defaultValue) {
        FloatSetting result = new FloatSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized FloatSetting createSettableFloatSetting(String key, float defaultValue, String simppKey, float min, float max) {
        FloatSetting result = new FloatSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey, min, max);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>StringArraySetting</tt> instance for a String array 
     * setting with the specified key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized StringArraySetting createStringArraySetting(String key, String[] defaultValue) {
        StringArraySetting result = new StringArraySetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized StringArraySetting createSettableStringArraySetting(String key, String[] defaultValue, String simppKey) {
        StringArraySetting result = new StringArraySetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    public synchronized StringSetSetting createStringSetSetting(String key, String defaultValue) {
        StringSetSetting result = new StringSetSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    /**
     * Creates a new <tt>FileArraySetting</tt> instance for a File array 
     * setting with the specified key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized FileArraySetting createFileArraySetting(String key, File[] defaultValue) {
        FileArraySetting result = new FileArraySetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized FileArraySetting createSettableFileArraySetting(String key, File[] defaultValue, String simppKey) {
        FileArraySetting result = new FileArraySetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>FileSetSetting</tt> instance for a File array 
     * setting with the specified key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized FileSetSetting createFileSetSetting(String key, File[] defaultValue) {
        FileSetSetting result = new FileSetSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized FileSetSetting createSettableFileSetSetting(String key, File[] defaultValue, String simppKey) {
        FileSetSetting result = new FileSetSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new expiring <tt>BooleanSetting</tt> instance with the
     * specified key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting 
     */
    public synchronized BooleanSetting createExpirableBooleanSetting(String key, boolean defaultValue) {
        BooleanSetting result = createBooleanSetting(key, defaultValue);
        if (expired) result.revertToDefault();
        return result;
    }

    /**
     * Creates a new expiring <tt>IntSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized IntSetting createExpirableIntSetting(String key, int defaultValue) {
        IntSetting result = createIntSetting(key, defaultValue);
        if (expired) result.revertToDefault();
        return result;
    }

    /**
     * Creates a new expiring <tt>LongSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized LongSetting createExpirableLongSetting(String key, long defaultValue) {
        LongSetting result = createLongSetting(key, defaultValue);
        if (expired) result.revertToDefault();
        return result;
    }

    /**
     * Creates a new <tt>FontNameSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized FontNameSetting createFontNameSetting(String key, String defaultValue) {
        FontNameSetting result = new FontNameSetting(DEFAULT_PROPS, PROPS, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    public synchronized FontNameSetting createSettableFontNameSetting(String key, String defaultValue, String simppKey) {
        FontNameSetting result = new FontNameSetting(DEFAULT_PROPS, PROPS, key, defaultValue, simppKey);
        handleSettingInternal(result, simppKey);
        return result;
    }

    /**
     * Creates a new <tt>PasswordSetting</tt> instance with the specified
     * key and default value.
     *
     * @param key the key for the setting
     * @param defaultValue the default value for the setting
     */
    public synchronized PasswordSetting createPasswordSettingMD5(String key, String defaultValue) {
        PasswordSetting result = new PasswordSetting(DEFAULT_PROPS, PROPS, PasswordSetting.MD5, key, defaultValue);
        handleSettingInternal(result, null);
        return result;
    }

    private synchronized void handleSettingInternal(Setting setting, String simppKey) {
        settings.add(setting);
        setting.reload();
        if (simppKey != null) {
            SimppSettingsManager simppSetMan = SimppSettingsManager.instance();
            String simppValue = simppSetMan.getRemanentSimppValue(simppKey);
            if (simppValue != null) {
                simppSetMan.cacheUserPref(setting, setting.getValueAsString());
                setting.setValue(simppValue);
            }
            simppKeyToSetting.put(simppKey, setting);
        }
    }

    /**
     * Package access for getting a loaded setting corresponding to a simppKey
     */
    synchronized Setting getSettingForSimppKey(String simppKey) {
        return simppKeyToSetting.get(simppKey);
    }
}
