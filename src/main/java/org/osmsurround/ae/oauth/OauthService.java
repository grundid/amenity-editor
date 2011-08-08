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

import javax.servlet.http.HttpServletResponse;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OauthService implements InitializingBean {

	// create a consumer object and configure it with the access
	// token and token secret obtained from the service provider
	private OAuthConsumer consumer;

	// create a new service provider object and configure it with
	// the URLs which provide request tokens, access tokens, and
	// the URL to which users are sent in order to grant permission
	// to your application to access protected resources
	private OAuthProvider provider;

	private String callbackUrl;

	@Autowired
	private OauthConfig oauthConfig;

	@Override
	public void afterPropertiesSet() throws Exception {
		consumer = new CommonsHttpOAuthConsumer(oauthConfig.getConsumerKey(), oauthConfig.getConsumerSecret());
		provider = new DefaultOAuthProvider(oauthConfig.getRequestTokenEndpointUrl(),
				oauthConfig.getAccessTokenEndpointUrl(), oauthConfig.getAuthorizeWebsiteUrl());

		callbackUrl = oauthConfig.getOauthCallbackUrl();
	}

	public String getRequestTokenUrl(HttpServletResponse response) {

		try {

			// fetches a request token from the service provider and builds
			// a url based on AUTHORIZE_WEBSITE_URL and CALLBACK_URL to
			// which your app must now send the user
			return provider.retrieveRequestToken(consumer, response.encodeURL(callbackUrl));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public OauthTokens retrieveAccessToken(String oauthVerifier) {
		try {
			provider.retrieveAccessToken(consumer, oauthVerifier);
			return new OauthTokens(consumer.getToken(), consumer.getTokenSecret());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public OAuthConsumer getConsumer() {
		return consumer;
	}

	public void signRequest(HttpRequestBase requestBase) {
		try {
			consumer.sign(requestBase);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
