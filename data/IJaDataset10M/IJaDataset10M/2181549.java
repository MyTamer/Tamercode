package org.fudaa.dodico.corba.mascaret;

/**
* org/fudaa/dodico/corba/mascaret/ICalculMascaret_Tie.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/devel/fudaa/fudaa_devel/dodico/idl/code/mascaret.idl
* mercredi 14 janvier 2009 02 h 05 CET
*/
public class ICalculMascaret_Tie extends ICalculMascaretPOA {

    public ICalculMascaret_Tie(org.fudaa.dodico.corba.mascaret.ICalculMascaretOperations delegate) {
        this._impl = delegate;
    }

    public ICalculMascaret_Tie(org.fudaa.dodico.corba.mascaret.ICalculMascaretOperations delegate, org.omg.PortableServer.POA poa) {
        this._impl = delegate;
        this._poa = poa;
    }

    public org.fudaa.dodico.corba.mascaret.ICalculMascaretOperations _delegate() {
        return this._impl;
    }

    public void _delegate(org.fudaa.dodico.corba.mascaret.ICalculMascaretOperations delegate) {
        this._impl = delegate;
    }

    public org.omg.PortableServer.POA _default_POA() {
        if (_poa != null) {
            return _poa;
        } else {
            return super._default_POA();
        }
    }

    public String getAvertissements() {
        return _impl.getAvertissements();
    }

    public String getCheminNoyau() {
        return _impl.getCheminNoyau();
    }

    public void setNoyau(boolean noyau5_2) {
        _impl.setNoyau(noyau5_2);
    }

    public boolean IsAfficherResultats() {
        return _impl.IsAfficherResultats();
    }

    public void ArreterCalcul(boolean bInterrompu, boolean bRecupererResultats) {
        _impl.ArreterCalcul(bInterrompu, bRecupererResultats);
    }

    public org.fudaa.dodico.corba.calcul.IParametres parametres(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.parametres(c);
    }

    public org.fudaa.dodico.corba.calcul.IResultats resultats(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.resultats(c);
    }

    public void calcul(org.fudaa.dodico.corba.objet.IConnexion c) {
        _impl.calcul(c);
    }

    public int dureeEstimee(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.dureeEstimee(c);
    }

    public int creation() {
        return _impl.creation();
    }

    public int derniereUtilisation() {
        return _impl.derniereUtilisation();
    }

    public String description() {
        return _impl.description();
    }

    public org.fudaa.dodico.corba.objet.IPersonne responsable() {
        return _impl.responsable();
    }

    public org.fudaa.dodico.corba.objet.IConnexion[] connexions() {
        return _impl.connexions();
    }

    public org.fudaa.dodico.corba.objet.IConnexion connexion(org.fudaa.dodico.corba.objet.IPersonne p) {
        return _impl.connexion(p);
    }

    public boolean deconnexion(org.fudaa.dodico.corba.objet.IConnexion c) {
        return _impl.deconnexion(c);
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

    /**
     * Retourne l'operation en cours et un pourcentage d'achevement.
     * De la forme "operation":"%%"
     */
    public org.fudaa.dodico.corba.calcul.SProgression progression() {
        return _impl.progression();
    }

    private org.fudaa.dodico.corba.mascaret.ICalculMascaretOperations _impl;

    private org.omg.PortableServer.POA _poa;
}
