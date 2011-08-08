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
package org.osmsurround.ae.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OauthConfig {

	@Value("${consumerKey}")
	private String consumerKey;
	@Value("${consumerSecret}")
	private String consumerSecret;
	@Value("${requestTokenEndpointUrl}")
	private String requestTokenEndpointUrl;
	@Value("${accessTokenEndpointUrl}")
	private String accessTokenEndpointUrl;
	@Value("${authorizeWebsiteUrl}")
	private String authorizeWebsiteUrl;
	@Value("${oauthCallbackUrl}")
	private String oauthCallbackUrl;

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public String getRequestTokenEndpointUrl() {
		return requestTokenEndpointUrl;
	}

	public String getAccessTokenEndpointUrl() {
		return accessTokenEndpointUrl;
	}

	public String getAuthorizeWebsiteUrl() {
		return authorizeWebsiteUrl;
	}

	public String getOauthCallbackUrl() {
		return oauthCallbackUrl;
	}

}
