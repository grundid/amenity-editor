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
package org.osmsurround.ae.templates;

import java.util.Map;

public class TagValueTemplate {

	private String iconUrl;
	private String iconTitle;
	private Map<String, String> tags;

	public TagValueTemplate(String iconUrl, String iconTitle, Map<String, String> tags) {
		this.iconUrl = iconUrl;
		this.iconTitle = iconTitle;
		this.tags = tags;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public String getIconTitle() {
		return iconTitle;
	}

	public Map<String, String> getTags() {
		return tags;
	}

}