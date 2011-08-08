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

import org.apache.log4j.Logger;
import org.osmsurround.ae.entity.Amenity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InternalDataService {

	private Logger log = Logger.getLogger(InternalDataService.class);

	@Autowired
	private NodeUpdate nodeUpdate;
	@Autowired
	private NodeInsert nodeInsert;
	@Autowired
	private NodeDelete nodeDelete;
	@Autowired
	private NodeTagInsert nodeTagInsert;
	@Autowired
	private NodeTagDelete nodeTagDelete;

	public void updateInternalData(long nodeId, Amenity amenity) {
		log.debug("Internal Update with nodeId: " + nodeId + " amenity: " + amenity);
		nodeUpdate.updateNode(amenity);
		updateNodeTags(nodeId, amenity);
	}

	private void updateNodeTags(long nodeId, Amenity amenity) {
		nodeTagDelete.delete(nodeId);
		nodeTagInsert.insert(nodeId, amenity.getKeyValues());
	}

	public void insertInternalData(long nodeId, Amenity amenity) {
		log.debug("Internal Update with nodeId: " + nodeId + " amenity: " + amenity);
		nodeInsert.insert(amenity);
		updateNodeTags(nodeId, amenity);
	}

	public void deleteInternalData(long nodeId) {
		nodeDelete.delete(nodeId);
		nodeTagDelete.delete(nodeId);
	}

	public void updateAmenities(Iterable<Amenity> amenities) {
		for (Amenity amenity : amenities) {
			deleteInternalData(amenity.getNodeId());
			nodeInsert.insert(amenity);
			nodeTagInsert.insert(amenity.getNodeId(), amenity.getKeyValues());
		}
	}

}
