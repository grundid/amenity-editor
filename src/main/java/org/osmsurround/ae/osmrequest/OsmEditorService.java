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
package org.osmsurround.ae.osmrequest;

import org.osm.schema.ObjectFactory;
import org.osm.schema.OsmChangeset;
import org.osm.schema.OsmRoot;
import org.osm.schema.OsmTag;
import org.springframework.stereotype.Service;

@Service
public class OsmEditorService {

	private ObjectFactory of = new ObjectFactory();

	public void addChangeset(OsmRoot osm, String comment) {
		OsmChangeset changeset = of.createOsmChangeset();
		osm.getChangeset().add(changeset);
		OsmTag tag = of.createOsmTag();
		tag.setK("comment");
		tag.setV(comment);
		changeset.getTag().add(tag);
	}
}
