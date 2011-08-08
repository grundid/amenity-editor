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

import java.util.HashSet;
import java.util.Set;

public class FilterSet {

	private Set<String> positiveFilter = new HashSet<String>();
	private Set<String> negativeFilter = new HashSet<String>();

	public FilterSet(String[] showFilterNames, String[] hideFilterNames) {
		if (showFilterNames != null) {
			for (String string : showFilterNames) {
				positiveFilter.add(string);
			}
		}
		if (hideFilterNames != null) {
			for (String string : hideFilterNames) {
				negativeFilter.add(string);
			}
		}
	}

	public Set<String> getPositiveFilter() {
		return positiveFilter;
	}

	public Set<String> getNegativeFilter() {
		return negativeFilter;
	}

}
