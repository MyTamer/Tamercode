package com.nanosheep.bikeroute.parser;

import android.util.Log;
import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.utility.route.PGeoPoint;
import com.nanosheep.bikeroute.utility.route.Route;
import com.nanosheep.bikeroute.utility.route.Segment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * @author jono@nanosheep.net
 * @version Jan 20, 2011
 */
public class MapQuestParser extends XMLParser implements Parser {

    /** Distance covered. **/
    private int distance;

    /** MapQuest copyright string. **/
    public static String mqc = "<p>Directions Courtesy of <a href=\"http://open.mapquest.co.uk/\"" + " target=\"_blank\">MapQuest</a></p>";

    /**
	 * 
	 */
    public MapQuestParser() {
    }

    /**
	 * @param feedUrl
	 */
    public MapQuestParser(String feedUrl) {
        super(feedUrl);
    }

    /**
	 * Parses a url pointing to a Google JSON object to a Route object.
	 * @return a Route object based on the JSON object.
	 */
    @Override
    public Route parse() {
        final String result = convertStreamToString(this.getInputStream());
        final Route route = new Route();
        route.setRouter(BikeRouteConsts.MQ);
        final Segment segment = new Segment();
        try {
            final JSONObject json = new JSONObject(result);
            final JSONObject jsonRoute = json.getJSONObject("route");
            final String polyline = jsonRoute.getJSONObject("shape").getString("shapePoints");
            final JSONArray steps = jsonRoute.getJSONArray("legs").getJSONObject(0).getJSONArray("maneuvers");
            final int numSteps = steps.length();
            final JSONArray stepIndexes = jsonRoute.getJSONObject("shape").getJSONArray("maneuverIndexes");
            route.setCopyright(mqc);
            route.setLength((int) (jsonRoute.getDouble("distance") * 1000));
            List<PGeoPoint> points = decodePolyLine(polyline, 5);
            route.addPoints(points);
            String name = steps.getJSONObject(0).getJSONArray("streets").getString(0);
            for (int i = 0; i < numSteps - 1; i++) {
                segment.clearPoints();
                final JSONObject step = steps.getJSONObject(i);
                final int length = (int) (step.getDouble("distance") * 1000);
                distance += length;
                segment.setLength(length);
                segment.setDistance(distance);
                segment.setInstruction(step.getString("narrative"));
                segment.setName(step.getJSONArray("streets").optString(0, segment.getInstruction()));
                int stepIndex = stepIndexes.getInt(i);
                int nextStepIndex = i < stepIndexes.length() ? stepIndexes.getInt(i + 1) : points.size();
                for (int j = stepIndex; j < nextStepIndex; j++) {
                    segment.addPoint(points.get(j));
                }
                route.addSegment(segment.copy());
            }
            name += " to " + segment.getName();
            route.setName(name);
            route.setPolyline(polyline);
        } catch (JSONException e) {
            Log.e(e.getMessage(), "MQ JSON Parser - " + feedUrl);
            return null;
        }
        return route;
    }

    /**
	 * Convert an inputstream to a string.
	 * @param input inputstream to convert.
	 * @return a String of the inputstream.
	 */
    private static String convertStreamToString(final InputStream input) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        final StringBuilder sBuf = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
        } catch (IOException e) {
            Log.e(e.getMessage(), "MQ parser, stream2string");
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), "MQ parser, stream2string");
            }
        }
        return sBuf.toString();
    }

    /**
	 * Decode a mapquest polyline string into a list of PGeoPoints.
	 * @param poly polyline encoded string to decode.
	 * @param precision the level of precision the polyline was encoded to
	 * @return the list of PGeoPoints represented by this polystring.
	 */
    private List<PGeoPoint> decodePolyLine(final String encoded, double precision) {
        precision = Math.pow(10, -precision);
        int len = encoded.length();
        int index = 0;
        double lat = 0;
        double lng = 0;
        List<PGeoPoint> array = new ArrayList<PGeoPoint>();
        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) > 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) > 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            array.add(new PGeoPoint(lat * precision, lng * precision));
        }
        return array;
    }
}
