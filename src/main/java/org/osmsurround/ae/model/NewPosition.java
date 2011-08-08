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
package org.osmsurround.ae.model;

public class NewPosition {

	private Double newlon;
	private Double newlat;

	private Double lon;
	private Double lat;

	public boolean hasNewPosition() {
		return newlon != null && newlat != null;
	}

	public boolean hasPosition() {
		return lon != null && lat != null;
	}

	public Double getNewlon() {
		return newlon;
	}

	public void setNewlon(Double newlon) {
		this.newlon = newlon;
	}

	public Double getNewlat() {
		return newlat;
	}

	public void setNewlat(Double newlat) {
		this.newlat = newlat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

}
