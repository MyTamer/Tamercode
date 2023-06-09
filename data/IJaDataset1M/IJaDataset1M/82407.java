package eu.jacquet80.rds.app.oda;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import eu.jacquet80.rds.log.RDSTime;

public class RTPlus extends ODA {

    public static int AID = 0x4BD7;

    private static String[] classNames = { "DUMMY_CLASS", "ITEM.TITLE", "ITEM.ALBUM", "ITEM.TRACKNUMBER", "ITEM.ARTIST", "ITEM.COMPOSITION", "ITEM.MOVEMENT", "ITEM.CONDUCTOR", "ITEM.COMPOSER", "ITEM.BAND", "ITEM.COMMENT", "ITEM.GENRE", "INFO.NEWS", "INFO.NEWS.LOCAL", "INFO.STOCKMARKET", "INFO.SPORT", "INFO.LOTTERY", "INFO.HOROSCOPE", "INFO.DAILY_DIVERSION", "INFO.HEALTH", "INFO.EVENT", "INFO.SCENE", "INFO.CINEMA", "INFO.TV", "INFO.DATE_TIME", "INFO.WEATHER", "INFO.TRAFFIC", "INFO.ALARM", "INFO.ADVERTISEMENT", "INFO.URL", "INFO.OTHER", "STATIONNAME.SHORT", "STATIONNAME.LONG", "PROGRAMME.NOW", "PROGRAMME.NEXT", "PROGRAMME.PART", "PROGRAMME.HOST", "PROGRAMME.EDITORIAL_STAFF", "PROGRAMME.FREQUENCY", "PROGRAMME.HOMEPAGE", "PROGRAMME.SUBCHANNEL", "PHONE.HOTLINE", "PHONE.STUDIO", "PHONE.OTHER", "SMS.STUDIO", "SMS.OTHER", "EMAIL.HOTLINE", "EMAIL.STUDIO", "EMAIL.OTHER", "MMS.OTHER", "CHAT", "CHAT.CENTRE", "VOTE.QUESTION", "VOTE.CENTRE", "RFU-54", "RFU-55", "PRIVATE-56", "PRIVATE-57", "PRIVATE-58", "PLACE", "APPOINTMENT", "IDENTIFIER", "PURCHASE", "GET_DATA" };

    private List<RTPlusItem> history = new ArrayList<RTPlusItem>();

    @Override
    public String getName() {
        return "RT+";
    }

    @Override
    public void receiveGroup(PrintWriter console, int type, int version, int[] blocks, boolean[] blocksOk, RDSTime time) {
        if (type == 3 && version == 0 && blocksOk[2]) {
            int ert = (blocks[2] >> 13) & 1;
            console.print("Applies to " + (ert == 1 ? "eRT" : "RT") + ", ");
            int cb = (blocks[2] >> 12) & 1;
            console.print((cb == 0 ? "NO" : "Using") + " template");
            if (cb == 1) {
                int scb = (blocks[2] >> 8) & 0xF;
                int tn = blocks[2] & 0xFF;
                console.printf(", SCB=%01X, template #%02X", scb, tn);
            }
        }
        if (version == 0 && type != 3) {
            int running = (blocks[1] >> 4) & 1;
            int toggle = (blocks[1] >> 3) & 1;
            console.printf("Running=%d, Toggle=%d, ", running, toggle);
            int[] ctype = new int[] { 0, 0 }, start = new int[2], len = new int[2];
            if (blocksOk[2]) {
                ctype[0] = ((blocks[1] & 7) << 3) | ((blocks[2] >> 13) & 7);
                start[0] = (blocks[2] >> 7) & 0x3F;
                len[0] = (blocks[2] >> 1) & 0x3F;
            }
            if (blocksOk[2] && blocksOk[3]) {
                ctype[1] = ((blocks[2] & 1) << 5) | ((blocks[3] >> 11) & 0x1F);
                start[1] = (blocks[3] >> 5) & 0x3F;
                len[1] = blocks[3] & 0x1F;
            }
            for (int i = 0; i < 2; i++) {
                if (ctype[i] != 0) {
                    String rt = station == null ? null : station.getRT().toString();
                    String text = null;
                    if (rt != null) {
                        if (start[i] < rt.length()) {
                            int endIndex = start[i] + len[i] + 1;
                            if (rt.length() < endIndex) endIndex = rt.length();
                            text = rt.substring(start[i], endIndex);
                        }
                    }
                    console.print(ctype[i] + "/" + classNames[ctype[i]] + "@" + start[i] + ":" + len[i]);
                    if (text != null) {
                        console.print(" = \"" + text + "\"");
                    }
                    console.print("    ");
                    if (len[i] > 0) {
                        addToHistory(station.getRT().getCurrentIndex(), ctype[i], start[i], len[i]);
                    }
                }
            }
        }
    }

    private void addToHistory(int textIndex, int type, int start, int len) {
        for (RTPlusItem i : history) {
            if (i.textIndex == textIndex && i.type == type) return;
        }
        history.add(new RTPlusItem(textIndex, type, start, len));
    }

    public String getHistoryForIndex(int textIndex, String text) {
        StringBuilder res = new StringBuilder();
        for (RTPlusItem i : history) {
            if (i.textIndex == textIndex) {
                int endIndex = i.start + i.len + 1;
                if (text.length() < endIndex) endIndex = text.length();
                res.append(classNames[i.type]).append("=\"").append(text.substring(i.start, endIndex)).append("\"&nbsp;&nbsp;&nbsp;&nbsp;");
            }
        }
        return res.toString();
    }

    @Override
    public int getAID() {
        return AID;
    }

    private static class RTPlusItem {

        public final int textIndex;

        public final int type;

        public final int start;

        public final int len;

        public RTPlusItem(int textIndex, int type, int start, int len) {
            this.textIndex = textIndex;
            this.type = type;
            this.start = start;
            this.len = len;
        }
    }
}
