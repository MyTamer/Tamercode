package jfreerails.world.top;

import jfreerails.world.accounts.Transaction;
import jfreerails.world.common.Activity;
import jfreerails.world.common.FreerailsSerializable;
import jfreerails.world.common.GameTime;
import jfreerails.world.player.FreerailsPrincipal;
import jfreerails.world.player.Player;

/**
 * <p>
 * This class implements methods which can be used to alter the world. Notice
 * that incontrast to, say, <CODE>java.util.List</CODE> there is no remove()
 * method that shifts any subsequent elements to the left (subtracts one from
 * their indices). This means that an elements' position in a list can be used
 * as an address space independent way to reference the element. If you want to
 * remove an element from a list, you should set it to null, e.g.
 * </p>
 * <p>
 * <CODE>world.set(KEY.TRAINS, 5, null, player);</CODE>
 * </P>
 * <p>
 * Code that loops through lists should handle null values gracefully
 * </p>
 * 
 * @author Luke
 * @author rob
 */
public interface World extends ReadOnlyWorld {

    int addActiveEntity(FreerailsPrincipal principal, Activity element);

    void add(FreerailsPrincipal principal, int index, Activity element);

    /**
     * Appends the specified element to the end of the specifed list and returns
     * the index that can be used to retrieve it.
     */
    int add(FreerailsPrincipal principal, KEY key, FreerailsSerializable element);

    /**
     * Appends the specified element to the end of the specifed list and returns
     * the index that can be used to retrieve it.
     * 
     */
    int add(SKEY key, FreerailsSerializable element);

    int addPlayer(Player player);

    /**
     * Adds the specified transaction to the specified principal's bank account.
     */
    void addTransaction(FreerailsPrincipal p, Transaction t);

    /**
     * Returns a copy of this world object - making changes to this copy will
     * not change this object.
     */
    World defensiveCopy();

    Activity removeLastActiveEntity(FreerailsPrincipal principal);

    Activity removeLastActivity(FreerailsPrincipal principal, int index);

    /**
     * Removes the last element from the specified list.
     */
    FreerailsSerializable removeLast(FreerailsPrincipal principal, KEY key);

    /**
     * Removes the last element from the specified list.
     * 
     */
    FreerailsSerializable removeLast(SKEY key);

    /**
     * Removes and returns the last transaction added the the specified
     * principal's bank account. This method is only here so that moves that add
     * transactions can be undone.
     */
    Transaction removeLastTransaction(FreerailsPrincipal p);

    Player removeLastPlayer();

    /**
     * Replaces the element mapped to the specified item with the specified
     * element.
     * 
     */
    void set(ITEM item, FreerailsSerializable element);

    /**
     * Replaces the element at the specified position in the specified list with
     * the specified element.
     */
    void set(FreerailsPrincipal principal, KEY key, int index, FreerailsSerializable element);

    /**
     * Replaces the element at the specified position in the specified list with
     * the specified element.
     * 
     */
    void set(SKEY key, int index, FreerailsSerializable element);

    /**
     * Replaces the tile at the specified position on the map with the specified
     * tile.
     * 
     */
    void setTile(int x, int y, FreerailsSerializable tile);

    void setTime(GameTime t);
}
