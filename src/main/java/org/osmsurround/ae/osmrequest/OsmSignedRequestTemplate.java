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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.osm.schema.OsmNode;
import org.osm.schema.OsmRoot;
import org.osmsurround.ae.oauth.OauthService;
import org.osmsurround.ae.osm.OsmConvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class OsmSignedRequestTemplate extends OsmRequestTemplate {

	protected String requestComment;

	@Autowired
	protected OauthService oAuthService;
	@Autowired
	protected SchemaService schemaService;
	@Autowired
	protected OsmConvertService osmConvertService;
	@Autowired
	protected OsmEditorService osmEditorService;
	@Value("${osmApiBaseUrl}")
	protected String osmApiBaseUrl;

	protected OsmSignedRequestTemplate(String requestComment) {
		this.requestComment = requestComment;
	}

	@Override
	public HttpResponse execute(OsmNode amenity) {
		try {
			int changesetId = openChangeset(requestComment);
			try {

				DefaultHttpClient httpClient = createHttpClient();

				HttpRequestBase request = createRequest(amenity, changesetId);

				oAuthService.signRequest(request);
				HttpResponse httpResponse = httpClient.execute(request);

				log.info("Result: " + httpResponse.getStatusLine().getStatusCode());
				return httpResponse;
			}
			finally {
				closeChangeset(changesetId);
			}
		}
		catch (Exception e) {
			throw new OsmRequestException(e);
		}
	}

	private int openChangeset(String comment) throws JAXBException, HttpException, IOException {

		DefaultHttpClient httpClient = createHttpClient();

		HttpEntityEnclosingRequestBase request = new HttpPut(osmApiBaseUrl + "/api/0.6/changeset/create");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OsmRoot node = osmConvertService.createOsmNode();
		osmEditorService.addChangeset(node, comment);

		schemaService.createOsmMarshaller().marshal(osmConvertService.toJaxbElement(node), baos);
		request.setEntity(new ByteArrayEntity(baos.toByteArray()));

		oAuthService.signRequest(request);
		HttpResponse httpResponse = httpClient.execute(request);
		log.info("Create result: " + httpResponse.getStatusLine().getStatusCode());
		if (httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			return Integer.parseInt(reader.readLine());
		}
		else {
			throw RequestUtils.createExceptionFromHttpResponse(httpResponse);
		}

	}

	private HttpResponse closeChangeset(int changesetId) throws JAXBException, HttpException, IOException {
		DefaultHttpClient httpClient = createHttpClient();

		HttpEntityEnclosingRequestBase request = new HttpPut(osmApiBaseUrl + "/api/0.6/changeset/" + changesetId
				+ "/close");

		oAuthService.signRequest(request);
		HttpResponse httpResponse = httpClient.execute(request);
		log.info("Close result: " + httpResponse.getStatusLine().getStatusCode());
		return httpResponse;
	}

	protected ByteArrayOutputStream marshallIntoBaos(OsmNode amenity, int changesetId) throws JAXBException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		amenity.setChangeset(BigInteger.valueOf(changesetId));
		schemaService.createOsmMarshaller().marshal(osmConvertService.toJaxbElement(amenity), baos);
		return baos;
	}

}
