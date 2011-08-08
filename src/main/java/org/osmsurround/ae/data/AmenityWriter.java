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

import java.util.concurrent.BlockingQueue;

import org.osmsurround.ae.dao.InternalDataService;
import org.osmsurround.ae.entity.Amenity;


public class AmenityWriter implements Runnable {

	private BlockingQueue<Amenity> amenities;
	private boolean finishWhenDone;
	private InternalDataService internalDataService;
	private int amenityCount;

	public AmenityWriter(BlockingQueue<Amenity> amenities, InternalDataService internalDataService) {
		this.amenities = amenities;
		this.internalDataService = internalDataService;

	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				if (amenities.isEmpty() && finishWhenDone)
					break;
				else
					Thread.sleep(10);

				while (!amenities.isEmpty()) {
					Amenity amenity = amenities.take();
					internalDataService.insertInternalData(amenity.getNodeId(), amenity);
					amenityCount++;
				}
			}
		}
		catch (InterruptedException ex) {
		}
	}

	public void setFinishWhenDone(boolean finishWhenDone) {
		this.finishWhenDone = finishWhenDone;
	}

	public int getAmenityCount() {
		return amenityCount;
	}
}
