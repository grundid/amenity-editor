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
package org.osmsurround.ae.osm;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;

import org.osm.schema.ObjectFactory;
import org.osm.schema.OsmNode;
import org.osm.schema.OsmRoot;
import org.osm.schema.OsmTag;
import org.osmsurround.ae.entity.Amenity;
import org.osmsurround.ae.filter.NodeFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OsmConvertService {

	@Autowired
	private NodeFilterService nodeFilterService;

	private ObjectFactory of = new ObjectFactory();

	public List<Amenity> osmToAmenity(OsmRoot osm) {
		List<Amenity> allAmenities = new ArrayList<Amenity>();
		for (OsmNode node : osm.getNode()) {

			Amenity amenity = osmNodeToAmenity(node);

			if (nodeFilterService.isValidNode(amenity.getKeyValues())) {
				allAmenities.add(amenity);
			}
		}

		return allAmenities;
	}

	public Amenity osmNodeToAmenity(OsmNode node) {
		Amenity amenity = new Amenity(node.getId().longValue(), node.getLon(), node.getLat());
		Map<String, String> data = amenity.getKeyValues();

		for (OsmTag tag : node.getTag()) {

			if (!nodeFilterService.isIgnoreTag(tag.getK().toLowerCase()))
				data.put(tag.getK(), tag.getV());
		}

		return amenity;
	}

	public OsmNode amenityToNode(Amenity amenity) {
		OsmNode node = of.createOsmNode();
		node.setId(BigInteger.valueOf(amenity.getNodeId()));
		node.setLat((float)amenity.getLat());
		node.setLon((float)amenity.getLon());

		node.getTag().clear();
		setNodeTags(node, amenity.getKeyValues());

		return node;
	}

	public void setNodeTags(OsmNode amenity, Map<String, String> data) {
		for (Entry<String, String> entry : data.entrySet()) {
			OsmTag tag = of.createOsmTag();
			tag.setK(entry.getKey());
			tag.setV(entry.getValue());
			amenity.getTag().add(tag);
		}
	}

	public OsmRoot createOsmNode() {
		OsmRoot osm = of.createOsmRoot();
		osm.setVersion(BigDecimal.valueOf(0.6));
		osm.setGenerator("Amenity Editor");
		return osm;
	}

	public JAXBElement<OsmRoot> toJaxbElement(OsmRoot osm) {
		return of.createOsm(osm);
	}

	public JAXBElement<OsmRoot> toJaxbElement(OsmNode node) {
		OsmRoot osm = createOsmNode();
		osm.getNode().add(node);
		return of.createOsm(osm);
	}
}
