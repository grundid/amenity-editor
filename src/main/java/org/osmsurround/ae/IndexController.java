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
package org.osmsurround.ae;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.osmsurround.ae.model.StartModel;
import org.osmsurround.ae.oauth.OauthCookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController {

	private static final GeoLocation DEFAULT_LOCATION = new GeoLocation(9.98, 51.15);
	@Autowired
	private OauthCookieService oauthCookieService;
	@Value("${version}")
	private String version;

	@RequestMapping("/index")
	public ModelAndView index(@ModelAttribute StartModel startModel, HttpServletRequest request) {

		StartParameters startParameters = new StartParameters(startModel, version);

		if (startParameters.getGeoLocation() == null) {
			startParameters.setGeoLocation(DEFAULT_LOCATION);
		}

		Cookie[] cookies = request.getCookies();
		startParameters.setOauthTokenAvailable(oauthCookieService.initOauthServiceFromCookies(cookies));

		return new ModelAndView("index", "startParameters", startParameters);
	}

}
