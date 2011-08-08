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

public class NodeTemplate {

	private String titleCode;
	private Map<String, String> tagsAndValues;

	public NodeTemplate(String titleCode, Map<String, String> tagsAndValues) {
		this.titleCode = titleCode;
		this.tagsAndValues = tagsAndValues;
	}

	public String getTitleCode() {
		return titleCode;
	}

	public Map<String, String> getTagsAndValues() {
		return tagsAndValues;
	}
}
