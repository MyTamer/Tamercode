package org.fudaa.dodico.corba.mathematiques;

/**
* org/fudaa/dodico/corba/mathematiques/IFonctionComplexe_Tie.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/general/mathematiques.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public class IFonctionComplexe_Tie extends IFonctionComplexePOA {

    public IFonctionComplexe_Tie(org.fudaa.dodico.corba.mathematiques.IFonctionComplexeOperations delegate) {
        this._impl = delegate;
    }

    public IFonctionComplexe_Tie(org.fudaa.dodico.corba.mathematiques.IFonctionComplexeOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.mathematiques.IFonctionComplexeOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.mathematiques.IFonctionComplexeOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public String formule() {
        return _impl.formule();
    }

    public void formule(String newFormule) {
        _impl.formule(newFormule);
    }

    public String[] variables() {
        return _impl.variables();
    }

    public void variables(String[] newVariables) {
        _impl.variables(newVariables);
    }

    public org.fudaa.dodico.corba.mathematiques.SComplexe valeur(org.fudaa.dodico.corba.mathematiques.SComplexe[] x) {
        return _impl.valeur(x);
    }

    public org.fudaa.dodico.corba.mathematiques.SComplexe[] valeurs(org.fudaa.dodico.corba.mathematiques.SComplexe[][] vx) {
        return _impl.valeurs(vx);
    }

    public void dispose() {
        _impl.dispose();
    }

    public void initialise(org.fudaa.dodico.corba.objet.IObjet o) {
        _impl.initialise(o);
    }

    public void reconnecte(String nom) {
        _impl.reconnecte(nom);
    }

    public org.fudaa.dodico.corba.objet.IObjet creeClone() {
        return _impl.creeClone();
    }

    public String moduleCorba() {
        return _impl.moduleCorba();
    }

    public String[] interfacesCorba() {
        return _impl.interfacesCorba();
    }

    public boolean egale(org.fudaa.dodico.corba.objet.IObjet o) {
        return _impl.egale(o);
    }

    public int codeHachage() {
        return _impl.codeHachage();
    }

    public String enChaine() {
        return _impl.enChaine();
    }

    private org.fudaa.dodico.corba.mathematiques.IFonctionComplexeOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
