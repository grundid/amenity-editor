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
package org.osmsurround.ae.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.osmsurround.ae.entity.Amenity;
import org.osmsurround.ae.filter.FilterSet;
import org.osmsurround.ae.filter.NodeFilterService;
import org.osmsurround.ae.osm.BoundingBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SearchService {

	@Autowired
	private NodeFilterService nodeFilterService;
	@Autowired
	private SearchResultRepository searchResultRepository;

	public List<Amenity> findAmenities(BoundingBox boundingBox, FilterSet filterSet) {
		List<Amenity> amenities = new ArrayList<Amenity>();
		List<SearchResult> searchResult = searchResultRepository.search(boundingBox);
		mapReduce(amenities, searchResult);
		nodeFilterService.filterAmenitiesForUser(amenities, filterSet);
		return amenities;
	}

	@SuppressWarnings("null")
	private void mapReduce(List<Amenity> amenities, List<SearchResult> searchResult) {
		long mapId = -1;
		Amenity amenity = null;
		for (Iterator<SearchResult> it = searchResult.iterator(); it.hasNext();) {
			SearchResult sr = it.next();

			if (mapId != sr.getNodeId()) {
				addAmenityIfValid(amenities, amenity);
				mapId = sr.getNodeId();
				amenity = new Amenity(mapId, sr.getLon(), sr.getLat());
			}

			if (!nodeFilterService.isIgnoreTag(sr.getKey().toLowerCase()))
				amenity.addKeyValue(sr.getKey(), sr.getValue());

			if (!it.hasNext()) {
				addAmenityIfValid(amenities, amenity);
			}
		}
	}

	private void addAmenityIfValid(List<Amenity> amenities, Amenity amenity) {
		if (amenity != null && nodeFilterService.isValidNode(amenity.getKeyValues()))
			amenities.add(amenity);
	}
}
