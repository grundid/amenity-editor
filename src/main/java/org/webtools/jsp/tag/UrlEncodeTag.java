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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class UrlEncodeTag extends BodyTagSupport {

	private UrlEncoder urlEncoder = new UrlEncoder();
	private boolean ignoreServletName = false;
	private boolean encodeUrl = true;
	private boolean appendBaseUrl = false;

	@Override
	public int doEndTag() throws JspTagException {
		try {
			HttpServletResponse res = (HttpServletResponse)pageContext.getResponse();
			HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();

			String baseUrl = "";
			if (appendBaseUrl)
				baseUrl = createBaseUrl(req);

			if (bodyContent != null) {
				bodyContent
						.getEnclosingWriter()
						.print(baseUrl
								+ urlEncoder.encodeUrl(req, res, bodyContent.getString(), ignoreServletName, encodeUrl));
			}
			else
				this.pageContext.getOut().print(
						baseUrl + urlEncoder.encodeUrl(req, res, "", ignoreServletName, encodeUrl));
		}
		catch (java.io.IOException e) {
			throw new JspTagException("IO Error: " + e.getMessage());
		}
		setEncodeUrl(true);
		setIgnoreServletName(false);
		setAppendBaseUrl(false);
		return EVAL_PAGE;
	}

	public void setIgnoreServletName(boolean ignoreServletName) {
		this.ignoreServletName = ignoreServletName;
	}

	public void setEncodeUrl(boolean encodeUrl) {
		this.encodeUrl = encodeUrl;
	}

	public void setAppendBaseUrl(boolean appendBaseUrl) {
		this.appendBaseUrl = appendBaseUrl;
	}

	public static String createBaseUrl(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName()
				+ (request.getServerPort() != 80 ? ":" + request.getServerPort() : "");
	}
}
