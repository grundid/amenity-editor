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
package org.osmsurround.ae;

import org.osmsurround.ae.model.StartModel;

public class StartParameters {

	private GeoLocation geoLocation;
	private int zoom;
	private boolean permalink;
	private boolean oauthTokenAvailable;
	private String version;

	public StartParameters(StartModel startModel, String version) {
		this.version = version;
		zoom = 8;

		if (startModel.hasZoom()) {
			zoom = startModel.getZoom().intValue();
		}

		if (startModel.hasPosition()) {
			geoLocation = new GeoLocation(startModel.getLon().doubleValue(), startModel.getLat().doubleValue());
			permalink = true;
		}
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public int getZoom() {
		return zoom;
	}

	/**
	 * @return the permalink
	 */
	public boolean isPermalink() {
		return permalink;
	}

	public boolean isOauthTokenAvailable() {
		return oauthTokenAvailable;
	}

	public void setOauthTokenAvailable(boolean oauthTokenAvailable) {
		this.oauthTokenAvailable = oauthTokenAvailable;
	}

	public String getVersion() {
		return version;
	}
}
