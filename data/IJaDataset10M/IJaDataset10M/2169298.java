package foobar1914.server;

/**
 * The IDGenerator is, like the name implies, used to generate IDs which are unique identification strings
 * that are used as an independant method of recognizing units on the map.
 *
 * The IDs will be generated by simply having a running counter of created IDs and then calculating the
 * hexadecimal string of this counter's value.
 * The values for game ids will be static, because they are needed on the server level
 *
 * @author Marc
 */
public class IDGenerator {

    /**
   * The array used for static prefixes used on server level
   *
   * 0: game prefix
   * 1: player prefix
   */
    private static char[] staPrefixes = new char[] { 'G', 'P' };

    /**
   * The array used for static fields used on server level
   *
   * 0: game counter
   * 1: player counter
   */
    private static short[] staFields = new short[2];

    /**
   * The array used to find the prefixes to indicate each kind of ID.
   *
   * 0: mobile prefix
   * 1: stationary prefix
   * 2: bot prefix
   * 3: resource prefix
   * 4: land prefix
   */
    private char[] prefixes = new char[5];

    /**
   * The array used to store the values for each kind of ID.
   *
   * 0: mobile counter
   * 1: stationary counter
   * 2: bot counter
   * 3: resource counter
   * 4: land counter
   */
    private short[] fields = new short[5];

    /**
   * This is the normal constructor because every object of an IDGenerator should have
   * the prefixes set up, so as not to create potentially non-unique IDs because of a lack of prefixes.
   *
   * @param newMobilePrefix
   * @param newStationaryPrefix
   * @param newPlayerPrefix
   * @param newBotPrefix
   */
    public IDGenerator(char newMobilePrefix, char newStationaryPrefix, char newBotPrefix, char newResourcePrefix, char newLandPrefix) {
        this.prefixes[0] = newMobilePrefix;
        this.prefixes[1] = newStationaryPrefix;
        this.prefixes[2] = newBotPrefix;
        this.prefixes[3] = newResourcePrefix;
        this.prefixes[4] = newLandPrefix;
        initMinimum();
    }

    /**
   * Set all values to their minimum
   */
    private void initMinimum() {
        for (int i = 0; i < this.fields.length; i += 1) {
            this.fields[i] = -32768;
        }
    }

    /**
   * Empty generator. As stated in the description of the above generator; this shouldn't be used.
   */
    public IDGenerator() {
        initMinimum();
        this.prefixes[0] = 'M';
        this.prefixes[1] = 'S';
        this.prefixes[2] = 'B';
        this.prefixes[3] = 'R';
        this.prefixes[4] = 'L';
    }

    /**
   * Return a new ID for a game
   */
    public static String getGameID() {
        return serverIDGen(0);
    }

    /**
   * Create an id at the given index by calculating the hexadecimal string
   * of the value in the counter.
   * 
   * @param field
   * @return
   */
    private String generalIDGen(int field, int type) {
        this.fields[field] += 1;
        StringBuilder sb = new StringBuilder();
        sb.append(this.prefixes[field]);
        sb.append(type);
        sb.append(";");
        sb.append(this.fields[field]);
        return sb.toString();
    }

    /**
   * Just like generalIDGen, just for IDs on the server level
   *
   * @param field
   * @return
   */
    private static String serverIDGen(int field) {
        staFields[field] += 1;
        StringBuilder sb = new StringBuilder();
        sb.append(staPrefixes[field]);
        sb.append(";");
        sb.append(staFields[field]);
        return sb.toString();
    }

    /**
   * Return the ID for a mobile unit.
   *
   * @return
   */
    public String getMobileID(int typeInt) {
        return this.generalIDGen(0, typeInt);
    }

    /**
   * Return the ID for a stationary unit.
   *
   * @return
   */
    public String getStationaryID(int typeInt) {
        return this.generalIDGen(1, typeInt);
    }

    /**
   * Return the ID for a player.
   *
   * @return
   */
    public static String getPlayerID() {
        return serverIDGen(1);
    }

    /**
   * Return the ID for a bot.
   * 
   * @return
   */
    public static String getBotID() {
        return serverIDGen(2);
    }

    /**
   * Return the ID for a resource
   *
   * @return
   */
    public String getResourceID(int typeInt) {
        return generalIDGen(3, typeInt);
    }

    /**
   * Return the ID for a land
   *
   * @return
   */
    public String getLandID() {
        return generalIDGen(4, 0);
    }

    private static char getServerPrefix(int field) {
        return staPrefixes[field];
    }

    private static short getServerField(int field) {
        return staFields[field];
    }

    private static void setServerField(int field, short newField) {
        staFields[field] = newField;
    }

    private static void setServerPrefix(int field, char newPrefix) {
        staPrefixes[field] = newPrefix;
    }

    private char getGeneralPrefix(int field) {
        return this.prefixes[field];
    }

    private void setGeneralPrefix(int field, char newPrefix) {
        this.prefixes[field] = newPrefix;
    }

    private short getGeneralField(int field) {
        return this.fields[field];
    }

    private void setGeneralField(int field, short newField) {
        this.fields[field] = newField;
    }

    public static void setGamePrefix(char newPrefix) {
        setServerPrefix(0, newPrefix);
    }

    public static char getGamePrefix() {
        return getServerPrefix(0);
    }

    public static void setPlayerPrefix(char newPrefix) {
        setServerPrefix(1, newPrefix);
    }

    public static char getPlayerPrefix() {
        return getServerPrefix(1);
    }

    public short getMobileField() {
        return getGeneralField(0);
    }

    public void setMobileField(short newField) {
        setGeneralField(0, newField);
    }

    public short getStationaryField() {
        return getGeneralField(1);
    }

    public void setStationaryField(short newField) {
        setGeneralField(1, newField);
    }

    public short getBotField() {
        return getGeneralField(2);
    }

    public void setBotField(short newField) {
        setGeneralField(2, newField);
    }

    public short getResourceField() {
        return getGeneralField(3);
    }

    public void setResourceField(short newField) {
        setGeneralField(3, newField);
    }

    public short getLandField() {
        return getGeneralField(4);
    }

    public void setLandField(short newField) {
        setGeneralField(4, newField);
    }

    public char getMobilePrefix() {
        return getGeneralPrefix(0);
    }

    public void setMobilePrefix(char newPrefix) {
        setGeneralPrefix(0, newPrefix);
    }

    public char getStationaryPrefix() {
        return getGeneralPrefix(1);
    }

    public void setStationaryPrefix(char newPrefix) {
        setGeneralPrefix(1, newPrefix);
    }

    public char getBotPrefix() {
        return getGeneralPrefix(2);
    }

    public void setBotPrefix(char newPrefix) {
        setGeneralPrefix(2, newPrefix);
    }

    public char getResourcePrefix() {
        return getGeneralPrefix(3);
    }

    public void setResourcePrefix(char newPrefix) {
        setGeneralPrefix(3, newPrefix);
    }

    public char getLandPrefix() {
        return getGeneralPrefix(4);
    }

    public void setLandPrefix(char newPrefix) {
        setGeneralPrefix(4, newPrefix);
    }

    public short[] getFields() {
        return this.fields;
    }

    public void setFields(short[] newFields) {
        this.fields = newFields;
    }

    public static short[] getStaticFields() {
        return staFields;
    }

    public static void setStaticFields(short[] newStaFields) {
        staFields = newStaFields;
    }

    public char[] getPrefixes() {
        return this.prefixes;
    }

    public void setPrefixes(char[] newPrefixes) {
        this.prefixes = newPrefixes;
    }

    public static char[] getStaticPrefixes() {
        return staPrefixes;
    }

    public static void setStaticPrefixes(char[] newStaPrefixes) {
        staPrefixes = newStaPrefixes;
    }
}
