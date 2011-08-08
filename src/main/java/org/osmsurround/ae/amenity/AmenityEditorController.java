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
package org.osmsurround.ae.amenity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osmsurround.ae.ResultMessage;
import org.osmsurround.ae.model.NewPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/amenity")
public class AmenityEditorController {

	@Autowired
	private AmenityService amenityService;

	@RequestMapping(method = RequestMethod.PUT)
	public @ResponseBody
	ResultMessage put(@RequestParam(value = "_nodeId") long nodeId, @ModelAttribute NewPosition newPosition,
			HttpServletRequest request) throws IOException {
		Map<String, String> dataFromRequest = loadDataFromRequest(request);
		amenityService.updateAmenity(nodeId, dataFromRequest, newPosition);
		return new ResultMessage("Ok.");
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody
	ResultMessage post(@ModelAttribute NewPosition newPosition, HttpServletRequest request) throws IOException {
		Map<String, String> dataFromRequest = loadDataFromRequest(request);
		amenityService.insertAmenity(dataFromRequest, newPosition);
		return new ResultMessage("Ok.");
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public @ResponseBody
	ResultMessage delete(@RequestParam(value = "_nodeId") long nodeId) throws IOException {
		amenityService.deleteAmenity(nodeId);
		return new ResultMessage("Ok.");
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Object get(@RequestParam(value = "nodeId") long nodeId) throws IOException {
		return amenityService.getAmenity(nodeId);
	}

	@ExceptionHandler
	public @ResponseBody
	ResultMessage handleException(Exception exception) {
		exception.printStackTrace();
		return new ResultMessage(exception);
	}

	private Map<String, String> loadDataFromRequest(HttpServletRequest req) {
		Map<String, String> data = new HashMap<String, String>();

		String[] keys = req.getParameterValues("key");
		String[] values = req.getParameterValues("value");

		if (keys == null || values == null || keys.length != values.length) {
			throw new RuntimeException("Bad Request");
		}
		else {
			for (int x = 0; x < keys.length; x++) {
				String k = keys[x];
				String v = values[x];

				if (v != null && !v.isEmpty())
					data.put(k.trim(), v.trim());
			}
		}
		return data;
	}
}
