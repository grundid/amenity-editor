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

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthCookieService {

	private static final String OAUTH_TOKEN_COOKIE = "oauth_token";

	@Autowired
	private OauthService oauthService;

	public boolean initOauthServiceFromCookies(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (OAUTH_TOKEN_COOKIE.equals(cookie.getName())) {
					String data[] = cookie.getValue().split("####");
					if (data != null && data.length == 2) {
						oauthService.getConsumer().setTokenWithSecret(data[0], data[1]);
						return true;
					}
				}
			}
		}
		return false;
	}

	public Cookie createOauthCookie(OauthTokens oauthTokens) {
		return createOneYearCookie(OAUTH_TOKEN_COOKIE, oauthTokens.getToken() + "####" + oauthTokens.getTokenSecret());
	}

	private Cookie createOneYearCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(60 * 60 * 24 * 365);
		return cookie;
	}
}
