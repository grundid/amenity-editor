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
package org.osmsurround.ae.search;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.osmsurround.ae.entity.Amenity;
import org.osmsurround.ae.filter.FilterSet;
import org.osmsurround.ae.osm.BoundingBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SearchController {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private SearchService searchService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public @ResponseBody
	List<Amenity> get(BoundingBox boundingBox, HttpServletRequest request, HttpServletResponse resp) throws IOException {
		long time = System.currentTimeMillis();

		String[] show = request.getParameterValues("show");
		String[] hide = request.getParameterValues("hide");

		List<Amenity> amenities = searchService.findAmenities(boundingBox, new FilterSet(show, hide));
		log.info("found: " + amenities.size() + " in " + (System.currentTimeMillis() - time) + " ms");

		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.addHeader("Cache-Control", "no-cache");
		resp.addHeader("Pragma", "no-cache");
		return amenities;
	}
}
