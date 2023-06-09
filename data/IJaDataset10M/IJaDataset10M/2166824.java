/* 
 * This file is part of Quelea, free projection software for churches.
 * Copyright (C) 2011 Michael Berry
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.quelea;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quelea.displayable.Song;
import org.quelea.displayable.TextSection;
import org.quelea.lucene.SearchIndex;
import org.quelea.lucene.SongSearchIndex;
import org.quelea.utils.DatabaseListener;
import org.quelea.utils.LoggerUtils;
import org.quelea.utils.QueleaProperties;

/**
 * The database that contains all the songs and their appropriate properties.
 * Calling methods on this class updates the persistent database used by Quelea
 * to store songs. Only one instance can be created since only one lock can be
 * held on the database at any one time.
 *
 * @author Michael
 */
public final class SongDatabase {

    private static final Logger LOGGER = LoggerUtils.getLogger();
    private static final SongDatabase INSTANCE = new SongDatabase();
    private SearchIndex<Song> index;
    private boolean addedToIndex;
    private Connection conn;
    private boolean error;
    private final Set<DatabaseListener> listeners;

    /**
     * Initialise the song database.
     */
    private SongDatabase() {
        listeners = new HashSet<>();
        addedToIndex = false;
        index = new SongSearchIndex();
        try {
            LOGGER.log(Level.INFO, "Loading database");
            Class.forName("org.hsqldb.jdbcDriver");
            String location = new File(new File(QueleaProperties.getQueleaUserHome(), "database"), "database").getAbsolutePath();
            conn = DriverManager.getConnection("jdbc:hsqldb:" + location, "", "");
            try(Statement stat = conn.createStatement()) {
                try {
                    stat.executeUpdate("CREATE TABLE Songs (id INTEGER IDENTITY,"
                            + "title varchar_ignorecase(256),"
                            + "author varchar_ignorecase(256),"
                            + "lyrics varchar_ignorecase(" + Integer.MAX_VALUE + "),"
                            + "background varchar(256),"
                            + ")");
                }
                catch(SQLException ex) { //Horrible but only way with hsqldb
                    LOGGER.log(Level.INFO, "Songs table already exists.");
                }
            }
            addColumns();
            LOGGER.log(Level.INFO, "Loaded database.");
        }
        catch(ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Couldn't find the database library.", ex);
            error = true;
        }
        catch(SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQL excpetion - hopefully this is just because Quelea is already running", ex);
            error = true;
        }
    }

    /**
     * Add any extra columns into the database that may not already be there.
     * This way we can silently upgrade the database if we need to add in new
     * columns later.
     */
    private void addColumns() {
        addColumn("ccli", "varchar_ignorecase(256)");
        addColumn("copyright", "varchar_ignorecase(256)");
        addColumn("year", "varchar_ignorecase(256)");
        addColumn("publisher", "varchar_ignorecase(256)");
        addColumn("tags", "varchar_ignorecase(256)");
        addColumn("key", "varchar_ignorecase(256)");
        addColumn("capo", "varchar_ignorecase(256)");
        addColumn("info", "varchar_ignorecase(20000)");
    }

    /**
     * Add a column into the database, silently fail since if it already exists
     * this means the database already contains the column.
     *
     * @param name the name of the column.
     * @param dataType the data type of the column.
     */
    private void addColumn(String name, String dataType) {
        String nameDataType = name + " " + dataType;
        try(Statement stat = conn.createStatement()) {
            stat.executeUpdate("ALTER TABLE Songs ADD COLUMN " + nameDataType);
            LOGGER.log(Level.INFO, "Added {0}", nameDataType);
        }
        catch(SQLException ex) {
            LOGGER.log(Level.INFO, "{0} already exists", nameDataType);
        }
    }

    /**
     * Get the singleton instance of this class.
     *
     * @return the singleton instance of this class.
     */
    public static SongDatabase get() {
        return INSTANCE;
    }

    /**
     * Get the underlying search index used by this database.
     *
     * @return the search index.
     */
    public SearchIndex<Song> getIndex() {
        return index;
    }

    /**
     * Determine if an error occurred initialising the database.
     *
     * @return true if an error occurred, false if all is ok.
     */
    public boolean errorOccurred() {
        return error;
    }

    /**
     * Register a database listener with this database.
     *
     * @param listener the listener.
     */
    public void registerDatabaseListener(DatabaseListener listener) {
        listeners.add(listener);
    }

    /**
     * Fire off the database listeners.
     */
    public void fireUpdate() {
        for(DatabaseListener listener : listeners) {
            listener.update();
        }
    }

    /**
     * Run a select expression (query) that returns a result set.
     *
     * @param expression the select expression to run.
     * @return the result set returned from the SQL query.
     * @throws SQLException if the query fails for some reason.
     */
    private ResultSet runSelectExpression(String expression) throws SQLException {
        try(Statement stat = conn.createStatement()) {
            return stat.executeQuery(expression);
        }
    }

    /**
     * Get all the songs in the database.
     *
     * @return an array of all the songs in the database.
     */
    public Song[] getSongs() {
        try(ResultSet rs = runSelectExpression("select * from songs")) {
            List<Song> songs = new ArrayList<>();
            while(rs.next()) {
                Song song = new Song.Builder(rs.getString("title"), rs.getString("author")).lyrics(rs.getString("lyrics")).ccli(rs.getString("ccli")).year(rs.getString("year")).tags(rs.getString("tags")).publisher(rs.getString("publisher")).copyright(rs.getString("copyright")).key(rs.getString("key")).info(rs.getString("info")).capo(rs.getString("capo")).id(rs.getInt("id")).get();
                for(TextSection section : song.getSections()) {
                    section.setTheme(Theme.parseDBString(rs.getString("background")));
                }
                songs.add(song);
            }
            if(!addedToIndex) {
                addedToIndex = true;
                LOGGER.log(Level.INFO, "Adding songs to index");
                index.addAll(songs);
            }
            return songs.toArray(new Song[songs.size()]);
        }
        catch(SQLException ex) {
            LOGGER.log(Level.WARNING, "Couldn't get the songs", ex);
            return null;
        }
    }

    /**
     * Add a song to the database.
     *
     * @param song the song to add.
     * @param fireUpdate true if the update should be fired to listeners when
     * adding this song, false otherwise.
     * @return true if the operation succeeded, false otherwise.
     */
    public boolean addSong(Song song, boolean fireUpdate) {
        try(PreparedStatement stat = conn.prepareStatement("insert into songs(title, author, lyrics, background, "
                        + "ccli, tags, publisher, year, copyright, key, capo, info) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            stat.setString(1, song.getTitle());
            stat.setString(2, song.getAuthor());
            stat.setString(3, song.getLyrics(true, true));
            String theme = "";
            if(song.getSections().length > 0 && song.getSections()[0].getTheme() != null) {
                theme = song.getSections()[0].getTheme().toDBString();
            }
            stat.setString(4, theme);
            stat.setString(5, song.getCcli());
            stat.setString(6, song.getTagsAsString());
            stat.setString(7, song.getPublisher());
            stat.setString(8, song.getYear());
            stat.setString(9, song.getCopyright());
            stat.setString(10, song.getKey());
            stat.setString(11, song.getCapo());
            stat.setString(12, song.getInfo());
            stat.executeUpdate();
            int id = -1;
            ResultSet resultSet;
            try(Statement stId = conn.createStatement()) {
                stId.execute("call IDENTITY()");
                resultSet = stId.getResultSet();
            }
            try {
                while(resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
            finally {
                resultSet.close();
            }
            song.setID(id);
            index.add(song);
            return true;
        }
        catch(SQLException ex) {
            LOGGER.log(Level.WARNING, "SQL exception occured adding the song: " + song, ex);
            return false;
        }
        finally {
            if(fireUpdate) {
                fireUpdate();
            }
        }
    }

    /**
     * Update a song in the database.
     *
     * @param song the song to update.
     * @return true if the operation succeeded, false otherwise.
     */
    public boolean updateSong(Song song) {
        try {
            if(song.getID() == -1) {
                LOGGER.log(Level.INFO, "Updating song that doesn't exist, adding instead");
                return addSong(song, true);
            }
            else {
                LOGGER.log(Level.INFO, "Updating song");
                try(PreparedStatement stat = conn.prepareStatement("update songs set title=?, author=?, lyrics=?, background=?,"
                                + "ccli=?, tags=?, publisher=?, year=?, copyright=?, key=?, capo=?, info=? where id=?")) {
                    stat.setString(1, song.getTitle());
                    stat.setString(2, song.getAuthor());
                    stat.setString(3, song.getLyrics(true, true));
                    String theme = "";
                    if(song.getSections().length > 0 && song.getSections()[0].getTheme() != null) {
                        theme = song.getSections()[0].getTheme().toDBString();
                    }
                    stat.setString(4, theme);
                    stat.setString(5, song.getCcli());
                    stat.setString(6, song.getTagsAsString());
                    stat.setString(7, song.getPublisher());
                    stat.setString(8, song.getYear());
                    stat.setString(9, song.getCopyright());
                    stat.setString(10, song.getKey());
                    stat.setString(11, song.getCapo());
                    stat.setString(12, song.getInfo());
                    stat.setInt(13, song.getID());
                    stat.executeUpdate();
                    index.update(song);
                    return true;
                }
                finally {
                    fireUpdate();
                }
            }
        }
        catch(SQLException ex) {
            LOGGER.log(Level.WARNING, "SQL exception occured updating the song: " + song, ex);
            return false;
        }
    }

    /**
     * Remove a song from the database.
     *
     * @param song the song to remove.
     * @return true if the operation succeeded, false otherwise.
     */
    public boolean removeSong(Song song) {
        try(PreparedStatement stat = conn.prepareStatement("delete from songs where id=?")) {
            stat.setInt(1, song.getID());
            stat.executeUpdate();
            index.remove(song);
            return true;
        }
        catch(SQLException ex) {
            LOGGER.log(Level.WARNING, "SQL exception occured removing the song: " + song, ex);
            return false;
        }
        finally {
            fireUpdate();
        }

    }
}
