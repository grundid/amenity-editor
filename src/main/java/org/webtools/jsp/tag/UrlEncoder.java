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
package org.webtools.jsp.tag;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

public class UrlEncoder {

	private String servletName;
	private Pattern pattern;
	private boolean ignoreContextPath = false;

	public UrlEncoder() {
		ResourceBundle rb = ResourceBundle.getBundle("webtools");
		servletName = rb.getString("urlEncoder.servletName");
		pattern = Pattern.compile(rb.getString("urlEncoder.pattern"));
	}

	public UrlEncoder(String servletName, String pattern) {
		this.servletName = servletName;
		this.pattern = Pattern.compile(pattern);
	}

	public String encodeUrl(HttpServletRequest request, HttpServletResponse response, String url,
			boolean ignoreServletName) {
		return encodeUrl(request, response, url, ignoreServletName, true);
	}

	public String encodeUrl(PageContext pageContext, String url, boolean ignoreServletName) {
		return encodeUrl((HttpServletRequest)pageContext.getRequest(), (HttpServletResponse)pageContext.getResponse(),
				url, ignoreServletName);
	}

	public String encodeUrl(HttpServletRequest request, HttpServletResponse response, String url,
			boolean ignoreServletName, boolean encodeUrl) {
		if (!ignoreServletName && !url.contains(servletName) && !pattern.matcher(url).matches())
			url = servletName + url;
		String returnUrl = (ignoreContextPath ? "" : request.getContextPath()) + url;
		if (encodeUrl)
			return response.encodeURL(returnUrl);
		else
			return returnUrl;
	}

}
