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

import java.io.ByteArrayOutputStream;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.osm.schema.OsmNode;
import org.springframework.stereotype.Service;

@Service
public class OsmDeleteRequest extends OsmSignedRequestTemplate {

	protected OsmDeleteRequest() {
		super("Amenity Editor Delete");
	}

	@Override
	protected HttpRequestBase createRequest(OsmNode amenity, int changesetId) throws Exception {
		ByteArrayOutputStream baos = marshallIntoBaos(amenity, changesetId);

		HttpEntityEnclosingRequestBase request = new HttpDeleteWithBody(osmApiBaseUrl + "/api/0.6/node/"
				+ amenity.getId());
		request.setEntity(new ByteArrayEntity(baos.toByteArray()));

		return request;
	}

}
