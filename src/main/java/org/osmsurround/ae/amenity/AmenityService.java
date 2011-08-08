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
package org.osmsurround.ae.amenity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.osm.schema.OsmNode;
import org.osmsurround.ae.dao.InternalDataService;
import org.osmsurround.ae.entity.Amenity;
import org.osmsurround.ae.model.NewPosition;
import org.osmsurround.ae.osm.OsmConvertService;
import org.osmsurround.ae.osmrequest.OsmDeleteRequest;
import org.osmsurround.ae.osmrequest.OsmInsertRequest;
import org.osmsurround.ae.osmrequest.OsmOperations;
import org.osmsurround.ae.osmrequest.OsmUpdateRequest;
import org.osmsurround.ae.osmrequest.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmenityService {

	private Logger log = Logger.getLogger(AmenityService.class);

	@Autowired
	private OsmDeleteRequest osmDeleteRequest;
	@Autowired
	private OsmUpdateRequest osmUpdateRequest;
	@Autowired
	private OsmInsertRequest osmInsertRequest;
	@Autowired
	private InternalDataService internalDataService;
	@Autowired
	private OsmConvertService osmConvertService;
	@Autowired
	private OsmOperations osmOperations;

	public Amenity getAmenity(long nodeId) {
		return osmConvertService.osmToAmenity(osmOperations.getForNode(nodeId)).get(0);
	}

	private OsmNode getNode(long nodeId) {
		return osmOperations.getForNode(nodeId).getNode().get(0);
	}

	public void deleteAmenity(long nodeId) {
		HttpResponse httpResponse = osmDeleteRequest.execute(getNode(nodeId));
		if (httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
			internalDataService.deleteInternalData(nodeId);
		}
		else {
			throw RequestUtils.createExceptionFromHttpResponse(httpResponse);
		}
	}

	public void updateAmenity(long nodeId, Map<String, String> data, NewPosition newPosition) {
		OsmNode amenity = getNode(nodeId);

		// save the original amenity position
		newPosition.setLat(Double.valueOf(amenity.getLat()));
		newPosition.setLon(Double.valueOf(amenity.getLon()));

		updateAmenityValues(amenity, data, newPosition);

		HttpResponse httpResponse = osmUpdateRequest.execute(amenity);
		if (httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
			Amenity amenityFromOsm = getAmenity(nodeId);
			internalDataService.updateInternalData(nodeId, amenityFromOsm);
		}
		else {
			throw RequestUtils.createExceptionFromHttpResponse(httpResponse);
		}
	}

	private void updateAmenityValues(OsmNode amenity, Map<String, String> data, NewPosition newPosition) {

		amenity.getTag().clear();
		osmConvertService.setNodeTags(amenity, data);

		if (newPosition.hasNewPosition()) {
			amenity.setLon(newPosition.getNewlon().floatValue());
			amenity.setLat(newPosition.getNewlat().floatValue());
		}
		else if (newPosition.hasPosition()) {
			amenity.setLon(newPosition.getLon().floatValue());
			amenity.setLat(newPosition.getLat().floatValue());
		}
		else {
			throw new RuntimeException("amenity has no position");
		}
	}

	public void insertAmenity(Map<String, String> data, NewPosition newPosition) {
		OsmNode amenity = osmConvertService.amenityToNode(new Amenity());
		updateAmenityValues(amenity, data, newPosition);

		HttpResponse httpResponse = osmInsertRequest.execute(amenity);
		if (httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
			long nodeId;
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
				nodeId = Long.parseLong(reader.readLine());
				internalDataService.insertInternalData(nodeId, getAmenity(nodeId));
			}
			catch (Exception e) {
				log.warn("", e);
			}
		}
		else {
			throw RequestUtils.createExceptionFromHttpResponse(httpResponse);
		}
	}
}
