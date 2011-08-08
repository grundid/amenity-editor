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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.osmsurround.ae.entity.Amenity;
import org.springframework.stereotype.Service;


@Service
public class NodeFilterService {

	public static final Set<String> ignoreTags = new HashSet<String>();

	/**
	 * Knoten mit diesen Tags sollen nicht angezeigt werden
	 */
	public static final Set<String> blockerTags = new HashSet<String>();

	/**
	 * Knoten, die nur aus diesen Tags bestehen sollen nicht angezeigt werden.
	 */
	public static final Set<String> stopperTags = new HashSet<String>();

	static {

		// TODO move into some properties file
		ignoreTags.add("created_by");

		stopperTags.add("created_by");
		stopperTags.add("source");
		stopperTags.add("fixme");
		stopperTags.add("note");
		stopperTags.add("ele");
		stopperTags.add("incline");
		stopperTags.add("noexit");

		blockerTags.add("highway");
		blockerTags.add("barrier");
		blockerTags.add("power");
		blockerTags.add("railway");
	}

	public boolean isValidNode(Map<String, String> tags) {
		int stopperCount = 0;
		for (Map.Entry<String, String> entry : tags.entrySet()) {
			String key = entry.getKey().toLowerCase();

			if (blockerTags.contains(key))
				return false;

			if (stopperTags.contains(key))
				stopperCount++;
		}
		// nur dann true, wenn nicht nur stopper tags enthalten sind
		return stopperCount != tags.size();
	}

	public boolean isIgnoreTag(String tagName) {
		return ignoreTags.contains(tagName);
	}

	private Filter checkFilter(FilterSet filterSet, Map<String, String> data) {
		Filter positiveMatch = FilterManager.getInstance().matchesPositive(data, filterSet.getPositiveFilter());
		if (positiveMatch != null) {
			if (FilterManager.getInstance().matchesNegative(data, filterSet.getNegativeFilter()) == null)
				return positiveMatch;
			else
				return null;
		}
		else
			return null;
	}

	public void filterAmenitiesForUser(Iterable<Amenity> amenities, FilterSet userFilterSet) {
		for (Iterator<Amenity> it = amenities.iterator(); it.hasNext();) {
			Amenity amenity = it.next();
			Filter filter = checkFilter(userFilterSet, amenity.getKeyValues());
			if (filter == null) {
				it.remove();
			}
			else
				amenity.setMatchedIcon(filter.getIcon());
		}
	}

}
