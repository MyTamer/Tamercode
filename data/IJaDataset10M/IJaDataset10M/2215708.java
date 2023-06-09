package com.android.sdklib.xml;

import com.android.sdklib.SdkConstants;
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.resources.Keyboard;
import com.android.sdklib.resources.Navigation;
import com.android.sdklib.resources.TouchScreen;
import com.android.sdklib.xml.ManifestData.Activity;
import com.android.sdklib.xml.ManifestData.Instrumentation;
import com.android.sdklib.xml.ManifestData.SupportsScreens;
import com.android.sdklib.xml.ManifestData.UsesConfiguration;
import com.android.sdklib.xml.ManifestData.UsesFeature;
import com.android.sdklib.xml.ManifestData.UsesLibrary;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class AndroidManifestParser {

    private static final int LEVEL_TOP = 0;

    private static final int LEVEL_INSIDE_MANIFEST = 1;

    private static final int LEVEL_INSIDE_APPLICATION = 2;

    private static final int LEVEL_INSIDE_APP_COMPONENT = 3;

    private static final int LEVEL_INSIDE_INTENT_FILTER = 4;

    private static final String ACTION_MAIN = "android.intent.action.MAIN";

    private static final String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER";

    public interface ManifestErrorHandler extends ErrorHandler {

        /**
         * Handles a parsing error and an optional line number.
         * @param exception
         * @param lineNumber
         */
        void handleError(Exception exception, int lineNumber);

        /**
         * Checks that a class is valid and can be used in the Android Manifest.
         * <p/>
         * Errors are put as {@link IMarker} on the manifest file.
         * @param locator
         * @param className the fully qualified name of the class to test.
         * @param superClassName the fully qualified name of the class it is supposed to extend.
         * @param testVisibility if <code>true</code>, the method will check the visibility of
         * the class or of its constructors.
         */
        void checkClass(Locator locator, String className, String superClassName, boolean testVisibility);
    }

    /**
     * XML error & data handler used when parsing the AndroidManifest.xml file.
     * <p/>
     * During parsing this will fill up the {@link ManifestData} object given to the constructor
     * and call out errors to the given {@link ManifestErrorHandler}.
     */
    private static class ManifestHandler extends DefaultHandler {

        private final ManifestData mManifestData;

        private final ManifestErrorHandler mErrorHandler;

        private int mCurrentLevel = 0;

        private int mValidLevel = 0;

        private Activity mCurrentActivity = null;

        private Locator mLocator;

        /**
         * Creates a new {@link ManifestHandler}.
         *
         * @param manifestFile The manifest file being parsed. Can be null.
         * @param errorListener An optional error listener.
         * @param gatherData True if data should be gathered.
         * @param javaProject The java project holding the manifest file. Can be null.
         * @param markErrors True if errors should be marked as Eclipse Markers on the resource.
         */
        ManifestHandler(IAbstractFile manifestFile, ManifestData manifestData, ManifestErrorHandler errorHandler) {
            super();
            mManifestData = manifestData;
            mErrorHandler = errorHandler;
        }

        @Override
        public void setDocumentLocator(Locator locator) {
            mLocator = locator;
            super.setDocumentLocator(locator);
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            try {
                if (mManifestData == null) {
                    return;
                }
                if (mValidLevel == mCurrentLevel) {
                    String value;
                    switch(mValidLevel) {
                        case LEVEL_TOP:
                            if (AndroidManifest.NODE_MANIFEST.equals(localName)) {
                                mManifestData.mPackage = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_PACKAGE, false);
                                String tmp = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_VERSIONCODE, true);
                                if (tmp != null) {
                                    try {
                                        mManifestData.mVersionCode = Integer.valueOf(tmp);
                                    } catch (NumberFormatException e) {
                                    }
                                }
                                mValidLevel++;
                            }
                            break;
                        case LEVEL_INSIDE_MANIFEST:
                            if (AndroidManifest.NODE_APPLICATION.equals(localName)) {
                                value = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_PROCESS, true);
                                if (value != null) {
                                    mManifestData.addProcessName(value);
                                }
                                value = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_DEBUGGABLE, true);
                                if (value != null) {
                                    mManifestData.mDebuggable = Boolean.parseBoolean(value);
                                }
                                mValidLevel++;
                            } else if (AndroidManifest.NODE_USES_SDK.equals(localName)) {
                                mManifestData.setMinSdkVersionString(getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION, true));
                                mManifestData.setTargetSdkVersionString(getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_TARGET_SDK_VERSION, true));
                            } else if (AndroidManifest.NODE_INSTRUMENTATION.equals(localName)) {
                                processInstrumentationNode(attributes);
                            } else if (AndroidManifest.NODE_SUPPORTS_SCREENS.equals(localName)) {
                                processSupportsScreensNode(attributes);
                            } else if (AndroidManifest.NODE_USES_CONFIGURATION.equals(localName)) {
                                processUsesConfiguration(attributes);
                            } else if (AndroidManifest.NODE_USES_FEATURE.equals(localName)) {
                                UsesFeature feature = new UsesFeature();
                                value = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME, true);
                                if (value != null) {
                                    feature.mName = value;
                                }
                                value = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_REQUIRED, true);
                                if (value != null) {
                                    Boolean b = Boolean.valueOf(value);
                                    if (b != null) {
                                        feature.mRequired = b;
                                    }
                                }
                                value = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_GLESVERSION, true);
                                if (value != null) {
                                    try {
                                        int version = Integer.decode(value);
                                        feature.mGlEsVersion = version;
                                    } catch (NumberFormatException e) {
                                    }
                                }
                                mManifestData.mFeatures.add(feature);
                            }
                            break;
                        case LEVEL_INSIDE_APPLICATION:
                            if (AndroidManifest.NODE_ACTIVITY.equals(localName)) {
                                processActivityNode(attributes);
                                mValidLevel++;
                            } else if (AndroidManifest.NODE_SERVICE.equals(localName)) {
                                processNode(attributes, SdkConstants.CLASS_SERVICE);
                                mValidLevel++;
                            } else if (AndroidManifest.NODE_RECEIVER.equals(localName)) {
                                processNode(attributes, SdkConstants.CLASS_BROADCASTRECEIVER);
                                mValidLevel++;
                            } else if (AndroidManifest.NODE_PROVIDER.equals(localName)) {
                                processNode(attributes, SdkConstants.CLASS_CONTENTPROVIDER);
                                mValidLevel++;
                            } else if (AndroidManifest.NODE_USES_LIBRARY.equals(localName)) {
                                value = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME, true);
                                if (value != null) {
                                    UsesLibrary library = new UsesLibrary();
                                    library.mName = value;
                                    value = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_REQUIRED, true);
                                    if (value != null) {
                                        Boolean b = Boolean.valueOf(value);
                                        if (b != null) {
                                            library.mRequired = b;
                                        }
                                    }
                                    mManifestData.mLibraries.add(library);
                                }
                            }
                            break;
                        case LEVEL_INSIDE_APP_COMPONENT:
                            if (mCurrentActivity != null && AndroidManifest.NODE_INTENT.equals(localName)) {
                                mCurrentActivity.resetIntentFilter();
                                mValidLevel++;
                            }
                            break;
                        case LEVEL_INSIDE_INTENT_FILTER:
                            if (mCurrentActivity != null) {
                                if (AndroidManifest.NODE_ACTION.equals(localName)) {
                                    String action = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME, true);
                                    if (action != null) {
                                        mCurrentActivity.setHasAction(true);
                                        mCurrentActivity.setHasMainAction(ACTION_MAIN.equals(action));
                                    }
                                } else if (AndroidManifest.NODE_CATEGORY.equals(localName)) {
                                    String category = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME, true);
                                    if (CATEGORY_LAUNCHER.equals(category)) {
                                        mCurrentActivity.setHasLauncherCategory(true);
                                    }
                                }
                            }
                            break;
                    }
                }
                mCurrentLevel++;
            } finally {
                super.startElement(uri, localName, name, attributes);
            }
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            try {
                if (mManifestData == null) {
                    return;
                }
                if (mValidLevel == mCurrentLevel) {
                    mValidLevel--;
                }
                mCurrentLevel--;
                if (mValidLevel == mCurrentLevel) {
                    switch(mValidLevel) {
                        case LEVEL_INSIDE_APPLICATION:
                            mCurrentActivity = null;
                            break;
                        case LEVEL_INSIDE_APP_COMPONENT:
                            if (mManifestData.mLauncherActivity == null && mCurrentActivity != null && mCurrentActivity.isHomeActivity() && mCurrentActivity.isExported()) {
                                mManifestData.mLauncherActivity = mCurrentActivity;
                            }
                            break;
                        default:
                            break;
                    }
                }
            } finally {
                super.endElement(uri, localName, name);
            }
        }

        @Override
        public void error(SAXParseException e) {
            if (mErrorHandler != null) {
                mErrorHandler.handleError(e, e.getLineNumber());
            }
        }

        @Override
        public void fatalError(SAXParseException e) {
            if (mErrorHandler != null) {
                mErrorHandler.handleError(e, e.getLineNumber());
            }
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
            if (mErrorHandler != null) {
                mErrorHandler.warning(e);
            }
        }

        /**
         * Processes the activity node.
         * @param attributes the attributes for the activity node.
         */
        private void processActivityNode(Attributes attributes) {
            String activityName = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME, true);
            if (activityName != null) {
                activityName = AndroidManifest.combinePackageAndClassName(mManifestData.mPackage, activityName);
                String exportedStr = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_EXPORTED, true);
                boolean exported = exportedStr == null || exportedStr.toLowerCase().equals("true");
                mCurrentActivity = new Activity(activityName, exported);
                mManifestData.mActivities.add(mCurrentActivity);
                if (mErrorHandler != null) {
                    mErrorHandler.checkClass(mLocator, activityName, SdkConstants.CLASS_ACTIVITY, true);
                }
            } else {
                mCurrentActivity = null;
            }
            String processName = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_PROCESS, true);
            if (processName != null) {
                mManifestData.addProcessName(processName);
            }
        }

        /**
         * Processes the service/receiver/provider nodes.
         * @param attributes the attributes for the activity node.
         * @param superClassName the fully qualified name of the super class that this
         * node is representing
         */
        private void processNode(Attributes attributes, String superClassName) {
            String serviceName = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME, true);
            if (serviceName != null) {
                serviceName = AndroidManifest.combinePackageAndClassName(mManifestData.mPackage, serviceName);
                if (mErrorHandler != null) {
                    mErrorHandler.checkClass(mLocator, serviceName, superClassName, false);
                }
            }
            String processName = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_PROCESS, true);
            if (processName != null) {
                mManifestData.addProcessName(processName);
            }
        }

        /**
         * Processes the instrumentation node.
         * @param attributes the attributes for the instrumentation node.
         */
        private void processInstrumentationNode(Attributes attributes) {
            String instrumentationName = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_NAME, true);
            if (instrumentationName != null) {
                String instrClassName = AndroidManifest.combinePackageAndClassName(mManifestData.mPackage, instrumentationName);
                String targetPackage = getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_TARGET_PACKAGE, true);
                mManifestData.mInstrumentations.add(new Instrumentation(instrClassName, targetPackage));
                if (mErrorHandler != null) {
                    mErrorHandler.checkClass(mLocator, instrClassName, SdkConstants.CLASS_INSTRUMENTATION, true);
                }
            }
        }

        /**
         * Processes the supports-screens node.
         * @param attributes the attributes for the supports-screens node.
         */
        private void processSupportsScreensNode(Attributes attributes) {
            mManifestData.mSupportsScreensFromManifest = new SupportsScreens();
            mManifestData.mSupportsScreensFromManifest.setResizeable(getAttributeBooleanValue(attributes, AndroidManifest.ATTRIBUTE_RESIZEABLE, true));
            mManifestData.mSupportsScreensFromManifest.setAnyDensity(getAttributeBooleanValue(attributes, AndroidManifest.ATTRIBUTE_ANYDENSITY, true));
            mManifestData.mSupportsScreensFromManifest.setSmallScreens(getAttributeBooleanValue(attributes, AndroidManifest.ATTRIBUTE_SMALLSCREENS, true));
            mManifestData.mSupportsScreensFromManifest.setNormalScreens(getAttributeBooleanValue(attributes, AndroidManifest.ATTRIBUTE_NORMALSCREENS, true));
            mManifestData.mSupportsScreensFromManifest.setLargeScreens(getAttributeBooleanValue(attributes, AndroidManifest.ATTRIBUTE_LARGESCREENS, true));
        }

        /**
         * Processes the supports-screens node.
         * @param attributes the attributes for the supports-screens node.
         */
        private void processUsesConfiguration(Attributes attributes) {
            mManifestData.mUsesConfiguration = new UsesConfiguration();
            mManifestData.mUsesConfiguration.mReqFiveWayNav = getAttributeBooleanValue(attributes, AndroidManifest.ATTRIBUTE_REQ_5WAYNAV, true);
            mManifestData.mUsesConfiguration.mReqNavigation = Navigation.getEnum(getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_REQ_NAVIGATION, true));
            mManifestData.mUsesConfiguration.mReqHardKeyboard = getAttributeBooleanValue(attributes, AndroidManifest.ATTRIBUTE_REQ_HARDKEYBOARD, true);
            mManifestData.mUsesConfiguration.mReqKeyboardType = Keyboard.getEnum(getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_REQ_KEYBOARDTYPE, true));
            mManifestData.mUsesConfiguration.mReqTouchScreen = TouchScreen.getEnum(getAttributeValue(attributes, AndroidManifest.ATTRIBUTE_REQ_TOUCHSCREEN, true));
        }

        /**
         * Searches through the attributes list for a particular one and returns its value.
         * @param attributes the attribute list to search through
         * @param attributeName the name of the attribute to look for.
         * @param hasNamespace Indicates whether the attribute has an android namespace.
         * @return a String with the value or null if the attribute was not found.
         * @see SdkConstants#NS_RESOURCES
         */
        private String getAttributeValue(Attributes attributes, String attributeName, boolean hasNamespace) {
            int count = attributes.getLength();
            for (int i = 0; i < count; i++) {
                if (attributeName.equals(attributes.getLocalName(i)) && ((hasNamespace && SdkConstants.NS_RESOURCES.equals(attributes.getURI(i))) || (hasNamespace == false && attributes.getURI(i).length() == 0))) {
                    return attributes.getValue(i);
                }
            }
            return null;
        }

        /**
         * Searches through the attributes list for a particular one and returns its value as a
         * Boolean. If the attribute is not present, this will return null.
         * @param attributes the attribute list to search through
         * @param attributeName the name of the attribute to look for.
         * @param hasNamespace Indicates whether the attribute has an android namespace.
         * @return a String with the value or null if the attribute was not found.
         * @see SdkConstants#NS_RESOURCES
         */
        private Boolean getAttributeBooleanValue(Attributes attributes, String attributeName, boolean hasNamespace) {
            int count = attributes.getLength();
            for (int i = 0; i < count; i++) {
                if (attributeName.equals(attributes.getLocalName(i)) && ((hasNamespace && SdkConstants.NS_RESOURCES.equals(attributes.getURI(i))) || (hasNamespace == false && attributes.getURI(i).length() == 0))) {
                    String attr = attributes.getValue(i);
                    if (attr != null) {
                        return Boolean.valueOf(attr);
                    } else {
                        return null;
                    }
                }
            }
            return null;
        }
    }

    private static final SAXParserFactory sParserFactory;

    static {
        sParserFactory = SAXParserFactory.newInstance();
        sParserFactory.setNamespaceAware(true);
    }

    /**
     * Parses the Android Manifest, and returns a {@link ManifestData} object containing the
     * result of the parsing.
     *
     * @param manifestFile the {@link IAbstractFile} representing the manifest file.
     * @param gatherData indicates whether the parsing will extract data from the manifest. If false
     * the method will always return null.
     * @param errorHandler an optional errorHandler.
     * @return
     * @throws StreamException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static ManifestData parse(IAbstractFile manifestFile, boolean gatherData, ManifestErrorHandler errorHandler) throws SAXException, IOException, StreamException, ParserConfigurationException {
        if (manifestFile != null) {
            SAXParser parser = sParserFactory.newSAXParser();
            ManifestData data = null;
            if (gatherData) {
                data = new ManifestData();
            }
            ManifestHandler manifestHandler = new ManifestHandler(manifestFile, data, errorHandler);
            parser.parse(new InputSource(manifestFile.getContents()), manifestHandler);
            return data;
        }
        return null;
    }

    /**
     * Parses the Android Manifest, and returns an object containing the result of the parsing.
     *
     * <p/>
     * This is the equivalent of calling <pre>parse(manifestFile, true, null)</pre>
     *
     * @param manifestFile the manifest file to parse.
     * @throws ParserConfigurationException
     * @throws StreamException
     * @throws IOException
     * @throws SAXException
     */
    public static ManifestData parse(IAbstractFile manifestFile) throws SAXException, IOException, StreamException, ParserConfigurationException {
        return parse(manifestFile, true, null);
    }

    public static ManifestData parse(IAbstractFolder projectFolder) throws SAXException, IOException, StreamException, ParserConfigurationException {
        IAbstractFile manifestFile = getManifest(projectFolder);
        if (manifestFile == null) {
            throw new FileNotFoundException();
        }
        return parse(manifestFile, true, null);
    }

    /**
     * Parses the Android Manifest from an {@link InputStream}, and returns a {@link ManifestData}
     * object containing the result of the parsing.
     *
     * @param manifestFileStream the {@link InputStream} representing the manifest file.
     * @return
     * @throws StreamException
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static ManifestData parse(InputStream manifestFileStream) throws SAXException, IOException, StreamException, ParserConfigurationException {
        if (manifestFileStream != null) {
            SAXParser parser = sParserFactory.newSAXParser();
            ManifestData data = new ManifestData();
            ManifestHandler manifestHandler = new ManifestHandler(null, data, null);
            parser.parse(new InputSource(manifestFileStream), manifestHandler);
            return data;
        }
        return null;
    }

    /**
     * Returns an {@link IAbstractFile} object representing the manifest for the given project.
     *
     * @param project The project containing the manifest file.
     * @return An IAbstractFile object pointing to the manifest or null if the manifest
     *         is missing.
     */
    public static IAbstractFile getManifest(IAbstractFolder projectFolder) {
        IAbstractFile file = projectFolder.getFile(SdkConstants.FN_ANDROID_MANIFEST_XML);
        if (file.exists()) {
            return file;
        }
        return null;
    }
}
