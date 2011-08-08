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

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.osm.schema.OsmNode;

public abstract class OsmRequestTemplate {

	protected Logger log = Logger.getLogger(OsmRequestTemplate.class);

	protected abstract HttpRequestBase createRequest(OsmNode amenity, int changesetId) throws Exception;

	public HttpResponse execute(OsmNode amenity) {
		try {
			DefaultHttpClient httpClient = createHttpClient();

			HttpRequestBase request = createRequest(amenity, 0);

			log.info("Request: " + request.getURI());

			HttpResponse httpResponse = httpClient.execute(request);

			log.info("Result: " + httpResponse.getStatusLine().getStatusCode());
			return httpResponse;
		}
		catch (Exception e) {
			throw new OsmRequestException(e);
		}
	}

	protected DefaultHttpClient createHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		httpClient.getParams().setBooleanParameter("http.authentication.preemptive", true);
		return httpClient;
	}
}
