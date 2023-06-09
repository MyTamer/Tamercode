package gate.creole.gazetteer;

/**OntoGazetteer
 * <br>
 * A gazetter that exploits a linear gazetter to generate annotations
 * according to a mapping definition between lists and ontology classes*/
public interface OntoGazetteer extends Gazetteer {

    /**Sets name of the gazzetteer
   * @param name the name to be set   */
    public void setGazetteerName(String name);

    /** Gets the name of the gazetteer
   *  @return the name of the gazetteer  */
    public String getGazetteerName();

    /**Gets the linear gazetteer associated with this onto gazetteer
   * @return the linear gazetteer */
    public Gazetteer getGazetteer();

    /**Associates a linear gazetteer with an onto gazetteer
   * @param gaze the linear gazetteer to be associated with this onto gazetteer */
    public void setGazetteer(Gazetteer gaze);

    /**Sets the url of the mapping definition
   * @param url the url of the mapping definition  */
    public void setMappingURL(java.net.URL url);

    /**Gets the url of the mapping definition
   * @return the url of the mapping definition  */
    public java.net.URL getMappingURL();
}
