package org.fudaa.dodico.corba.geometrie;

/**
* org/fudaa/dodico/corba/geometrie/ILineaireFini_Tie.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/general/geometrie.idl
* mercredi 6 ao�t 2008 22 h 27 CEST
*/
public class ILineaireFini_Tie extends ILineaireFiniPOA {

    public ILineaireFini_Tie(org.fudaa.dodico.corba.geometrie.ILineaireFiniOperations delegate) {
        this._impl = delegate;
    }

    public ILineaireFini_Tie(org.fudaa.dodico.corba.geometrie.ILineaireFiniOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.geometrie.ILineaireFiniOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.geometrie.ILineaireFiniOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public double longueur() {
        return _impl.longueur();
    }

    public double longueurXY() {
        return _impl.longueurXY();
    }

    public double[][] h4() {
        return _impl.h4();
    }

    public void h4(double[][] newH4) {
        _impl.h4(newH4);
    }

    public org.fudaa.dodico.corba.geometrie.IBoite boite() {
        return _impl.boite();
    }

    public org.fudaa.dodico.corba.geometrie.IPoint barycentre() {
        return _impl.barycentre();
    }

    public org.fudaa.dodico.corba.geometrie.IPoint centreApparent() {
        return _impl.centreApparent();
    }

    public int dimension() {
        return _impl.dimension();
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

    private org.fudaa.dodico.corba.geometrie.ILineaireFiniOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
