package uk.org.toot.misc;

import java.util.Observer;

public interface IObservable {

    void addObserver(Observer o);

    int countObservers();

    void deleteObserver(Observer o);

    void deleteObservers();

    boolean hasChanged();

    void notifyObservers();

    void notifyObservers(Object arg);
}
