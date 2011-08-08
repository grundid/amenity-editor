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
import java.util.Set;

public class FilterManager {

	private List<Filter> filtersPositive = new ArrayList<Filter>();
	private List<Filter> filtersNegative = new ArrayList<Filter>();

	private static FilterManager instance = new FilterManager();

	private FilterManager() {
		addSimpleFilter("bakery", "map/bread.png", "shop", "bakery");
		addSimpleFilter("restaurant", "map/restaurant.png", "amenity", "restaurant");
		addSimpleFilter("fast_food", "map/fastfood.png", "amenity", "fast_food");
		//		addSimpleFilter("biergarten", "map/.png", "amenity", "biergarten");
		addSimpleFilter("cafe", "map/coffee.png", "amenity", "cafe");
		//		addSimpleFilter("pub", "map/.png", "amenity", "pub");
		addSimpleFilter("nightclub", "map/club.png", "amenity", "nightclub");
		addSimpleFilter("fuel", "map/gazstation.png", "amenity", "fuel");
		addSimpleFilter("parking", "map/parking.png", "amenity", "parking");
		addSimpleFilter("supermarket", "map/supermarket.png", "shop", "supermarket");
		addSimpleFilter("shop-pharmacy", "map/drugs.png", "shop", "pharmacy");
		addSimpleFilter("amenity-pharmacy", "map/drugs.png", "amenity", "pharmacy");
		addSimpleFilter("doctor", "map/doctor.png", "amenity", "doctor");
		addSimpleFilter("butcher", "map/butcher.png", "shop", "butcher");
		//		addSimpleFilter("beverages", "map/.png", "shop", "beverages");
		addSimpleFilter("car", "map/car.png", "shop", "car");
		addSimpleFilter("car_repair", "map/carrepair.png", "shop", "car_repair");
		//		addSimpleFilter("bicycle", "map/.png", "shop", "bicycle");
		addSimpleFilter("clothes", "map/clothes.png", "shop", "clothes");
		addSimpleFilter("florist", "map/flowers.png", "shop", "florist");
		//		addSimpleFilter("doityourself", "map/.png", "shop", "doityourself");
		//		addSimpleFilter("electronics", "map/.png", "shop", "electronics");
		addSimpleFilter("laundry", "map/laundromat.png", "shop", "laundry");
		addSimpleFilter("hairdresser", "map/hairsalon.png", "shop", "hairdresser");
		addSimpleFilter("place_of_worship", "map/church2.png", "amenity", "place_of_worship");
		addSimpleFilter("postbox", "map/postal.png", "amenity", "post_box");
		addSimpleFilter("post_office", "map/postal.png", "amenity", "post_office");
		addSimpleFilter("bank", "map/bank.png", "amenity", "bank");
		addSimpleFilter("playground", "map/playground.png", "leisure", "playground");
		addSimpleFilter("bench", "map/bench.png", "amenity", "bench");
		addSimpleFilter("hunting", "map/hunting.png", "amenity", "hunting_stand");
		addSimpleFilter("shelter", "map/shelter-picnic.png", "amenity", "shelter");
		addSimpleFilter("atm", "map/atm.png", "amenity", "atm");
		addSimpleFilter("pool", "map/pool.png", "amenity", "swimming_pool");
		addSimpleFilter("recycle", "map/recycle.png", "amenity", "recycling");
		addSimpleFilter("fitness_studio", "map/fitnesscenter.png", "amenity", "fitness_studio");
		addSimpleFilter("maxspeed", "map/photoright.png", "enforcement", "maxspeed");
		addSimpleFilter("hotel", "map/hotel.png", "tourism", "hotel");
		addSimpleFilter("hostel", "map/hostel.png", "tourism", "hostel");
		addSimpleFilter("bus", "map/bus.png", "amenity", "bus_station");
		addSimpleFilter("telephone", "map/telephone.png", "amenity", "telephone");
		addSimpleFilter("school", "map/school.png", "amenity", "school");
		addSimpleFilter("university", "map/university.png", "amenity", "university");
		addSimpleFilter("bicycle_parking", "map/bicycleparking.png", "amenity", "bicycle_parking");

		Filter filter = new Filter("amenity", "marker-red.png");
		filter.addKeyValueFilter(new KeyValueFilter("amenity", ".*"));
		filtersPositive.add(filter);

		filter = new Filter("shop", "map/shoppingmall.png");
		filter.addKeyValueFilter(new KeyValueFilter("shop", ".*"));
		filtersPositive.add(filter);

		filter = new Filter("place", "marker-fuchsia.png");
		filter.addKeyValueFilter(new KeyValueFilter("place", ".*"));
		filtersPositive.add(filter);

		filter = new Filter("man_made", "marker-teal.png");
		filter.addKeyValueFilter(new KeyValueFilter("man_made", ".*"));
		filtersPositive.add(filter);

		filter = new Filter("tourism", "marker-aqua.png");
		filter.addKeyValueFilter(new KeyValueFilter("tourism", ".*"));
		filtersPositive.add(filter);

		filter = new Filter("other", "marker-yellow.png", false);
		filtersPositive.add(filter);

		filter = new Filter("opening_hours");
		filter.addKeyValueFilter(new KeyValueFilter("opening_hours", ".*"));
		filtersNegative.add(filter);

		filter = new Filter("address");
		filter.addKeyValueFilter(new KeyValueFilter("addr:street", ".*"));
		filter.addKeyValueFilter(new KeyValueFilter("addr:housenumber", ".*"));
		filter.addKeyValueFilter(new KeyValueFilter("addr:postcode", ".*"));
		filter.addKeyValueFilter(new KeyValueFilter("addr:city", ".*"));
		filtersNegative.add(filter);

		filter = new Filter("benches");
		filter.addKeyValueFilter(new KeyValueFilter("amenity", "bench.*"));
		filter.addKeyValueFilter(new KeyValueFilter("amenity", "parking.*"));
		filter.addKeyValueFilter(new KeyValueFilter("amenity", "post_box.*"));
		filter.addKeyValueFilter(new KeyValueFilter("amenity", "telephone.*"));
		filtersNegative.add(filter);

	}

	private void addSimpleFilter(String name, String icon, String key, String value) {
		Filter filter = new Filter(name, icon);
		filter.addKeyValueFilter(new KeyValueFilter(key, value));
		filtersPositive.add(filter);
	}

	public static FilterManager getInstance() {
		return instance;
	}

	private static Filter matches(List<Filter> filters, Map<String, String> keyValues, Set<String> filterNames) {
		for (Filter filter : filters) {
			boolean matches = filter.matches(keyValues);

			if (matches) {
				return filterNames.contains(filter.getName()) ? filter : null;
			}
		}
		return null;
	}

	public Filter matchesPositive(Map<String, String> keyValues, Set<String> filterNames) {
		return matches(filtersPositive, keyValues, filterNames);
	}

	public Filter matchesNegative(Map<String, String> keyValues, Set<String> filterNames) {
		return matches(filtersNegative, keyValues, filterNames);
	}

	public List<Filter> getFiltersPositive() {
		return filtersPositive;
	}

	public List<Filter> getFiltersNegative() {
		return filtersNegative;
	}
}
