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
package org.osmsurround.ae.dao;

import java.util.List;
import java.util.logging.Logger;

import org.osmsurround.ae.entity.Amenity;

public class InternalDataUpdate implements Runnable {

	private List<Amenity> amenities;
	private Logger log = Logger.getLogger(InternalDataUpdate.class.getName());
	private final InternalDataService internalDataService;

	public InternalDataUpdate(List<Amenity> amenities, InternalDataService internalDataService) {
		this.amenities = amenities;
		this.internalDataService = internalDataService;
	}

	@Override
	public void run() {
		log.info("Running internal data update for " + amenities.size() + " amenities.");
		long time = System.currentTimeMillis();
		internalDataService.updateAmenities(amenities);
		log.info("Done in " + (System.currentTimeMillis() - time) + "ms.");
	}
}
