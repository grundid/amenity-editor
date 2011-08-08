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
package org.osmsurround.ae.osm;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.osmsurround.ae.ResultMessage;
import org.osmsurround.ae.entity.Amenity;
import org.osmsurround.ae.filter.FilterSet;
import org.osmsurround.ae.filter.NodeFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class OsmUpdateController {

	@Autowired
	private OsmUpdateService osmUpdateService;
	@Autowired
	private NodeFilterService nodeFilterService;

	@RequestMapping(value = "/osmUpdate", method = RequestMethod.GET)
	public @ResponseBody
	List<Amenity> get(BoundingBox boundingBox, HttpServletRequest request) {

		List<Amenity> amenities = osmUpdateService.getOsmDataAsAmenities(boundingBox);
		osmUpdateService.startUpdateThread(new ArrayList<Amenity>(amenities));

		String[] show = request.getParameterValues("show");
		String[] hide = request.getParameterValues("hide");

		nodeFilterService.filterAmenitiesForUser(amenities, new FilterSet(show, hide));
		return amenities;
	}

	@ExceptionHandler
	public @ResponseBody
	ResultMessage handleException(Exception exception) {
		return new ResultMessage(exception);
	}

}
