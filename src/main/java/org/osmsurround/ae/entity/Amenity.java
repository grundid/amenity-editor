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
package org.osmsurround.ae.entity;

import java.util.Map;
import java.util.TreeMap;

public class Amenity extends Node {

	private long version;
	private String matchedIcon;
	private Map<String, String> keyValues = new TreeMap<String, String>();

	public Amenity(long id, double longitude, double latitude, Map<String, String> keyValues) {
		super(id, longitude, latitude);
		this.keyValues = keyValues;
	}

	public Amenity(long id) {
		super(id, 0, 0);
	}

	public Amenity() {
		super(0, 0, 0);
	}

	public Amenity(long id, double longitude, double latitude) {
		super(id, longitude, latitude);
	}

	public String getName() {
		return keyValues.get("name");
	}

	public Map<String, String> getKeyValues() {
		return keyValues;
	}

	public void addKeyValue(String key, String value) {
		keyValues.put(key, value);
	}

	public String getMatchedIcon() {
		return matchedIcon;
	}

	public void setMatchedIcon(String matchedIcon) {
		this.matchedIcon = matchedIcon;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
