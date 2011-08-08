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
package org.osmsurround.ae.ac;

import java.util.List;

import org.apache.log4j.Logger;
import org.osmsurround.ae.dao.NodeValueResult;
import org.osmsurround.ae.dao.NodeValueSearchByKey;
import org.osmsurround.ae.dao.NodeValueSearchByKeyAndValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoCompleteService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private NodeValueSearchByKey nodeValueSearchByKey;
	@Autowired
	private NodeValueSearchByKeyAndValue nodeValueSearchByKeyAndValue;

	public List<NodeValueResult> searchByKey(String key) {
		log.debug("Searching [" + key + "]");
		return nodeValueSearchByKey.execute(key + "%");
	}

	public List<NodeValueResult> searchByKeyAndValue(String key, String value) {
		log.debug("Searching [" + key + "] [" + value + "]");
		return nodeValueSearchByKeyAndValue.execute(key, value + "%");
	}
}
