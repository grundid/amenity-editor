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
package org.osmsurround.ae.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Filter {

	private String name;
	private String icon;
	private transient List<KeyValueFilter> filters = new ArrayList<KeyValueFilter>();
	private boolean defaultSelected = true;

	public Filter(String name, String icon, boolean defaultSelected) {
		this.name = name;
		this.icon = icon;
		this.defaultSelected = defaultSelected;
	}

	public Filter(String name, String icon) {
		this.name = name;
		this.icon = icon;
	}

	public Filter(String name) {
		this(name, null);
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}

	public void addKeyValueFilter(KeyValueFilter keyValueFilter) {
		filters.add(keyValueFilter);
	}

	public boolean matches(Map<String, String> keyValues) {
		if (filters.isEmpty()) // empty filter matches all
			return true;

		for (KeyValueFilter filter : filters) {
			if (keyValues.containsKey(filter.getKey())) {
				if (filter.getPattern().matcher(keyValues.get(filter.getKey())).matches())
					return true;
			}
		}
		return false;
	}

	public boolean isDefaultSelected() {
		return defaultSelected;
	}

}
