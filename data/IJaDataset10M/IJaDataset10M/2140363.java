package net.sf.dvstar.transmission.protocol;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import net.sf.dvstar.transmission.protocol.ProtocolConstants.ResponseTag;
import net.sf.dvstar.transmission.utils.ConfigStorage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.openide.util.Exceptions;

/**
 *
 * @author dstarzhynskyi
 */
public class Requests implements ProtocolConstants {

    private boolean connected = false;

    private Logger loggerProvider;

    public Requests(Logger loggerProvider) {
        this.loggerProvider = loggerProvider;
    }

    public boolean produceConnect() {
        boolean ret = false;
        ConfigStorage config = new ConfigStorage();
        if (config.loadConfig()) {
            try {
                String serverURL = config.getProfileProperty(CONFIG_SERVER_URL);
                XmlRpcClientConfigImpl xmlRpcConfig = new XmlRpcClientConfigImpl();
                xmlRpcConfig.setServerURL(new URL(serverURL));
                XmlRpcClient client = new XmlRpcClient();
                client.setConfig(xmlRpcConfig);
                ret = connected = true;
            } catch (MalformedURLException ex) {
                Exceptions.printStackTrace(ex);
                Logger.getLogger(Requests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ret = true;
        return (ret);
    }

    public JSONObject generic(String method, JSONArray ids) {
        JSONObject request = createBasicObject(method);
        JSONObject args = request.getJSONObject(ProtocolConstants.KEY_ARGUMENTS);
        args.put(ProtocolConstants.KEY_IDS, ids);
        request.put(ProtocolConstants.KEY_ARGUMENTS, args);
        return request;
    }

    public JSONObject sessionGet() {
        return createBasicObject(ProtocolConstants.METHOD_SESSIONGET, ResponseTag.SessionGet);
    }

    public JSONObject sessionStats() {
        return createBasicObject(ProtocolConstants.METHOD_SESSIONSTATS, ResponseTag.SessionStats);
    }

    public JSONObject torrentGet() {
        return torrentGet(null, null, METHOD_TORRENTGET_LIST);
    }

    public JSONObject torrentGet(List<Integer> tblTorrentListModel, TorrentsTableModel modelTorrentsList, int modeRefresh) {
        JSONObject request = createBasicObject(ProtocolConstants.METHOD_TORRENTGET, ResponseTag.TorrentGet);
        JSONArray ids = null;
        if (tblTorrentListModel != null && modelTorrentsList != null) {
            if (tblTorrentListModel.size() > 0) {
                ids = new JSONArray();
                {
                    for (int i = 0; i < tblTorrentListModel.size(); i++) {
                        int modelRow = (Integer) tblTorrentListModel.get(i);
                        Torrent torrent = modelTorrentsList.getTableDataTorrents().get(modelRow);
                        ids.add(torrent.getValue(FIELD_ID));
                    }
                }
            }
        }
        Object[] olist;
        olist = new Object[] { ProtocolConstants.FIELD_ID, ProtocolConstants.FIELD_NAME, ProtocolConstants.FIELD_TOTALSIZE, ProtocolConstants.FIELD_STATUS, ProtocolConstants.FIELD_SEEDERS, ProtocolConstants.FIELD_LEECHERS, ProtocolConstants.FIELD_PROGRESS, ProtocolConstants.FIELD_RECHECKPROGRESS, ProtocolConstants.FIELD_ADDEDDATE, ProtocolConstants.FIELD_HAVEVALID, ProtocolConstants.FIELD_HAVEUNCHECKED, ProtocolConstants.FIELD_ETA, ProtocolConstants.FIELD_RATEDOWNLOAD, ProtocolConstants.FIELD_RATEUPLOAD, ProtocolConstants.FIELD_UPLOADEDEVER, ProtocolConstants.FIELD_LEFTUNTILDONE, ProtocolConstants.FIELD_ANNOUNCEURL, ProtocolConstants.FIELD_DOWNLOADLIMIT, ProtocolConstants.FIELD_DOWNLOADLIMITMODE, ProtocolConstants.FIELD_UPLOADLIMIT, ProtocolConstants.FIELD_UPLOADLIMITED, ProtocolConstants.FIELD_UPLOADLIMITMODE, ProtocolConstants.FIELD_SPEEDLIMITDOWN, ProtocolConstants.FIELD_DOWNLOADLIMITED, ProtocolConstants.FIELD_SPEEDLIMITDOWNENABLED, ProtocolConstants.FIELD_SPEEDLIMITUP, ProtocolConstants.FIELD_SPEEDLIMITUPENABLED, ProtocolConstants.FIELD_ERRORSTRING, ProtocolConstants.FIELD_PEERSGETTINGFROMUS, ProtocolConstants.FIELD_PEERSSENDINGTOUS, ProtocolConstants.FIELD_PIECECOUNT, ProtocolConstants.FIELD_MAXCONNECTEDPEERS, ProtocolConstants.FIELD_COMMENT, ProtocolConstants.FIELD_SWARMSPEED, ProtocolConstants.FIELD_DATECREATED, ProtocolConstants.FIELD_CREATOR, ProtocolConstants.FIELD_HASHSTRING, ProtocolConstants.FIELD_DOWNLOADDIR, ProtocolConstants.FIELD_SEEDRATIOLIMIT, ProtocolConstants.FIELD_BANDWIDTHPRIORITY, ProtocolConstants.FIELD_SEEDRATIOMODE, ProtocolConstants.FIELD_HONORSSESSIONLIMITS, ProtocolConstants.FIELD_DONEDATE, ProtocolConstants.FIELD_SIZEWHENDONE };
        switch(modeRefresh) {
            case METHOD_TORRENTGET_LIST:
                {
                }
                break;
            case METHOD_TORRENTGET_DTLS:
                {
                    olist = new Object[] { ProtocolConstants.FIELD_ID, ProtocolConstants.FIELD_NAME, ProtocolConstants.FIELD_TOTALSIZE, ProtocolConstants.FIELD_STATUS, ProtocolConstants.FIELD_SEEDERS, ProtocolConstants.FIELD_LEECHERS, ProtocolConstants.FIELD_PROGRESS, ProtocolConstants.FIELD_RECHECKPROGRESS, ProtocolConstants.FIELD_ADDEDDATE, ProtocolConstants.FIELD_HAVEVALID, ProtocolConstants.FIELD_HAVEUNCHECKED, ProtocolConstants.FIELD_ETA, ProtocolConstants.FIELD_RATEDOWNLOAD, ProtocolConstants.FIELD_RATEUPLOAD, ProtocolConstants.FIELD_UPLOADEDEVER, ProtocolConstants.FIELD_LEFTUNTILDONE, ProtocolConstants.FIELD_ANNOUNCEURL, ProtocolConstants.FIELD_DOWNLOADLIMIT, ProtocolConstants.FIELD_DOWNLOADLIMITMODE, ProtocolConstants.FIELD_UPLOADLIMIT, ProtocolConstants.FIELD_UPLOADLIMITED, ProtocolConstants.FIELD_UPLOADLIMITMODE, ProtocolConstants.FIELD_SPEEDLIMITDOWN, ProtocolConstants.FIELD_DOWNLOADLIMITED, ProtocolConstants.FIELD_SPEEDLIMITDOWNENABLED, ProtocolConstants.FIELD_SPEEDLIMITUP, ProtocolConstants.FIELD_SPEEDLIMITUPENABLED, ProtocolConstants.FIELD_ERRORSTRING, ProtocolConstants.FIELD_PEERSGETTINGFROMUS, ProtocolConstants.FIELD_PEERSSENDINGTOUS, ProtocolConstants.FIELD_PIECECOUNT, ProtocolConstants.FIELD_PIECES, ProtocolConstants.FIELD_MAXCONNECTEDPEERS, ProtocolConstants.FIELD_COMMENT, ProtocolConstants.FIELD_SWARMSPEED, ProtocolConstants.FIELD_DATECREATED, ProtocolConstants.FIELD_CREATOR, ProtocolConstants.FIELD_TRACKERS, ProtocolConstants.FIELD_HASHSTRING, ProtocolConstants.FIELD_DOWNLOADDIR, ProtocolConstants.FIELD_SEEDRATIOLIMIT, ProtocolConstants.FIELD_BANDWIDTHPRIORITY, ProtocolConstants.FIELD_SEEDRATIOMODE, ProtocolConstants.FIELD_HONORSSESSIONLIMITS, ProtocolConstants.FIELD_DONEDATE, ProtocolConstants.FIELD_FILES, ProtocolConstants.FIELD_PEERS, ProtocolConstants.FIELD_SIZEWHENDONE };
                }
                break;
            default:
                {
                }
                break;
        }
        JSONArray fields = JSONArray.fromObject(olist);
        JSONObject args = request.getJSONObject(ProtocolConstants.KEY_ARGUMENTS);
        args.put(ProtocolConstants.KEY_FIELDS, fields);
        if (ids != null) {
            args.put(ProtocolConstants.KEY_IDS, ids);
        }
        request.put(ProtocolConstants.KEY_ARGUMENTS, args);
        return request;
    }

    public JSONObject createBasicObject(String method, ResponseTag tag) {
        Map map = new HashMap();
        map.put(ProtocolConstants.KEY_TAG, tag);
        map.put(ProtocolConstants.KEY_METHOD, method);
        map.put(ProtocolConstants.KEY_ARGUMENTS, new JSONObject());
        JSONObject jsonObject = JSONObject.fromObject(map);
        return jsonObject;
    }

    public JSONObject createBasicObject(String method) {
        return createBasicObject(method, ResponseTag.DoNothing);
    }

    public JSONObject torrentSetLocation(JSONArray ids, String location, boolean move) {
        JSONObject request = createBasicObject(ProtocolConstants.METHOD_TORRENT_SET_LOCATION, ResponseTag.DoNothing);
        JSONObject args = request.getJSONObject(ProtocolConstants.KEY_ARGUMENTS);
        args.put(ProtocolConstants.KEY_IDS, ids);
        args.put(ProtocolConstants.FIELD_LOCATION, location);
        args.put(ProtocolConstants.FIELD_MOVE, move);
        request.put(ProtocolConstants.KEY_ARGUMENTS, args);
        return request;
    }

    public JSONObject torrentAddByFile(File file, boolean deleteAfter, String destination, int peerLimit) {
        JSONObject request = null;
        BufferedInputStream bis = null;
        try {
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
            } else {
                loggerProvider.log(Level.INFO, "Opening: " + file.getName());
                byte[] bytes = new byte[(int) length];
                bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytes);
                String baseEncoded = new String(Base64.encodeBase64(bytes));
                loggerProvider.log(Level.INFO, "Opening: " + baseEncoded);
                request = createBasicObject(ProtocolConstants.METHOD_TORRENTADD);
                JSONObject args = request.getJSONObject(ProtocolConstants.KEY_ARGUMENTS);
                args.put(ProtocolConstants.FIELD_METAINFO, baseEncoded);
                args.put(ProtocolConstants.FIELD_PAUSED, true);
                if (destination != null) {
                    args.put(ProtocolConstants.FIELD_DOWNLOAD_DIR, destination);
                }
                if (peerLimit > 0) {
                    args.put(ProtocolConstants.FIELD_PEERLIMIT, peerLimit);
                }
                request.put(ProtocolConstants.KEY_ARGUMENTS, args);
                if (deleteAfter && file.exists()) {
                    try {
                        file.delete();
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            loggerProvider.log(Level.WARNING, "", ex);
        } catch (IOException ex) {
            loggerProvider.log(Level.WARNING, "", ex);
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                loggerProvider.log(Level.WARNING, "", ex);
            }
        }
        return request;
    }

    /**
     * @return the connected
     */
    public boolean isConnected() {
        return connected;
    }
}
