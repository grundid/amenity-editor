/**
 * This file is part of Amenity Editor for OSM.
 * Copyright (c) 2001 by Adrian Stabiszewski, as@grundid.de
 *
 * Amenity Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Amenity Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Amenity Editor.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.osmsurround.ae.data;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.osmsurround.ae.entity.Amenity;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class OsmReader extends DefaultHandler {

	private BlockingQueue<Amenity> amenities;
	private Set<String> ignoreKeys = new HashSet<String>();

	private Amenity node;
	private int amenityCount;

	public OsmReader(BlockingQueue<Amenity> amenities) {
		this.amenities = amenities;
		ignoreKeys.add("created_by");
		ignoreKeys.add("source");

	}

	private int parseIntSafe(String s) {
		try {
			return Integer.parseInt(s);
		}
		catch (Exception e) {
			return 0;
		}
	}

	private long parseLongSafe(String s) {
		try {
			return Long.parseLong(s);
		}
		catch (Exception e) {
			return 0;
		}
	}

	private double parseDoubleSafe(String s) {
		try {
			return Double.parseDouble(s);
		}
		catch (Exception e) {
			return 0;
		}
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		char c = name.charAt(0);
		if (c == 't') // tag
		{
			if (node != null) {
				String k = attributes.getValue("k");
				String v = attributes.getValue("v");
				if (!ignoreKeys.contains(k))
					node.getKeyValues().put(k, v);
			}
		}
		else if (c == 'n') // node
		{
			node = new Amenity(parseLongSafe(attributes.getValue("id")), parseDoubleSafe(attributes.getValue("lon")),
					parseDoubleSafe(attributes.getValue("lat")));
			node.setVersion(parseIntSafe(attributes.getValue("version")));
		}
		else if (c == 'r') // relation
		{
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if (node == null)
			return;

		char c = name.charAt(0);
		if (c == 'n') {
			if (node != null) {

				if (!node.getKeyValues().isEmpty()
						&& (!node.getKeyValues().containsKey("highway") || node.getKeyValues()
								.containsValue("bus_stop"))) {
					if (node.getKeyValues().containsKey("amenity")) {
						try {
							amenities.put(node);
							amenityCount++;
						}
						catch (InterruptedException e) {
						}
					}
				}
				node = null;
			}
			else
				throw new RuntimeException("unexpected node end tag");
		}
		else if (c == 'r') {

		}

	}

	public int getAmenityCount() {
		return amenityCount;
	}
}
