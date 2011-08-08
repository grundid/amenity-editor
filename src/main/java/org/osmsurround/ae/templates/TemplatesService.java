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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TemplatesService {

	private List<TagValueTemplate> tagValueTemplates = new ArrayList<TagValueTemplate>();
	private List<NodeTemplate> nodeTemplates = new ArrayList<NodeTemplate>();

	@Autowired
	private MessageSource messageSource;

	public TemplatesService() {
		initTagValueTemplates();
		initNodeTemplates();
	}

	private void initTagValueTemplates() {

		tagValueTemplates.add(new TagValueTemplate("/img/icons/add.png", "eb.tag.button.add.label", map("", "")));
		tagValueTemplates.add(new TagValueTemplate("/img/icons/time_add.png", "eb.oh.label", map("opening_hours", "")));
		tagValueTemplates.add(new TagValueTemplate("/img/icons/cart_add.png", "eb.shop.label", map("shop", "", "name",
				"", "opening_hours", "")));
		tagValueTemplates.add(new TagValueTemplate("/img/icons/book_addresses.png", "eb.add.label", map("addr:street",
				"", "addr:housenumber", "", "addr:postcode", "", "addr:city", "", "addr:country", "")));
		tagValueTemplates.add(new TagValueTemplate("/img/icons/telephone_add.png", "eb.tel.label", map("phone", "")));
		tagValueTemplates.add(new TagValueTemplate("/img/icons/car_add.png", "eb.fuel.label", map("fuel:diesel", "yes",
				"fuel:octane_95", "yes", "fuel:octane_98", "yes", "fuel:lpg", "")));
	}

	private void initNodeTemplates() {

		nodeTemplates.add(new NodeTemplate("template.01", map("", "")));
		nodeTemplates.add(new NodeTemplate("template.02", map("amenity", "", "name", "")));
		nodeTemplates.add(new NodeTemplate("template.03", map("shop", "", "name", "", "opening_hours", "")));
		nodeTemplates.add(new NodeTemplate("template.04", map("tourism", "", "name", "")));
		nodeTemplates.add(new NodeTemplate("template.05", map("amenity", "restaurant", "name", "", "cuisine", "",
				"opening_hours", "", "addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "",
				"phone", "+49", "url", "")));
		nodeTemplates.add(new NodeTemplate("template.06", map("amenity", "fast_food", "name", "", "cuisine", "",
				"opening_hours", "", "addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "",
				"phone", "+49", "url", "")));
		nodeTemplates.add(new NodeTemplate("template.07", map("amenity", "biergarten", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "", "phone", "+49", "url",
				"")));
		nodeTemplates.add(new NodeTemplate("template.08", map("amenity", "cafe", "name", "", "cuisine", "",
				"opening_hours", "", "addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "",
				"phone", "+49", "url", "")));
		nodeTemplates.add(new NodeTemplate("template.09", map("amenity", "pub", "name", "", "cuisine", "",
				"opening_hours", "", "addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "",
				"phone", "+49", "url", "")));
		nodeTemplates.add(new NodeTemplate("template.10", map("amenity", "nightclub", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "", "phone", "+49", "url",
				"")));
		nodeTemplates.add(new NodeTemplate("template.11", map("amenity", "fuel", "name", "", "operator", "",
				"opening_hours", "", "addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "",
				"fuel:diesel", "yes", "fuel:octane_95", "yes", "fuel:octane_98", "yes", "fuel:lpg", "")));
		nodeTemplates.add(new NodeTemplate("template.12", map("amenity", "parking", "name", "", "fee", "", "park_ride",
				"")));
		nodeTemplates.add(new NodeTemplate("template.13", map("shop", "supermarket", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.14", map("shop", "pharmacy", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.15", map("amenity", "doctor", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "", "phone", "+49")));
		nodeTemplates.add(new NodeTemplate("template.16", map("shop", "bakery", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.17", map("shop", "butcher", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.18", map("shop", "beverages", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.19", map("shop", "car", "name", "", "operator", "", "service", "",
				"opening_hours", "", "addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.20", map("shop", "car_repair", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.21", map("shop", "bicycle", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.22", map("shop", "clothes", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.23", map("shop", "florist", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.24", map("shop", "doityourself", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.25", map("shop", "electronics", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.26", map("shop", "laundry", "name", "", "self_service", "no",
				"opening_hours", "", "addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.27", map("shop", "hairdresser", "name", "", "opening_hours", "",
				"addr:street", "", "addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
		nodeTemplates.add(new NodeTemplate("template.28",
				map("amenity", "place_of_worship", "name", "", "religion", "", "denomination", "", "addr:street", "",
						"addr:housenumber", "", "addr:postcode", "", "addr:city", "")));
	}

	private Map<String, String> map(String... keyValues) {
		Map<String, String> map = new LinkedHashMap<String, String>(keyValues.length / 2);
		for (int x = 0; x < keyValues.length; x += 2) {
			map.put(keyValues[x], keyValues[x + 1]);
		}
		return map;
	}

	public List<TagValueTemplate> getLocalizedTagValueTemplates() {
		List<TagValueTemplate> result = new ArrayList<TagValueTemplate>();

		Locale locale = LocaleContextHolder.getLocale();
		for (TagValueTemplate tvt : tagValueTemplates) {
			result.add(new TagValueTemplate(tvt.getIconUrl(),
					messageSource.getMessage(tvt.getIconTitle(), null, locale), tvt.getTags()));
		}

		return result;
	}

	public Map<String, Map<String, String>> getLocalizedNodeTemplates() {
		Map<String, Map<String, String>> result = new LinkedHashMap<String, Map<String, String>>();

		Locale locale = LocaleContextHolder.getLocale();
		for (NodeTemplate nt : nodeTemplates) {
			result.put(messageSource.getMessage(nt.getTitleCode(), null, locale), nt.getTagsAndValues());
		}

		return result;
	}
}
