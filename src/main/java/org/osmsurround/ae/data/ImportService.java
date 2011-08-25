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
package org.osmsurround.ae.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.osmsurround.ae.dao.InternalDataService;
import org.osmsurround.ae.entity.Amenity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
public class ImportService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	private InternalDataService internalDataService;

	public void run(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		long time = System.currentTimeMillis();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();

		BlockingQueue<Amenity> amenities = new LinkedBlockingDeque<Amenity>(10);
		AmenityWriter amenityWriter = new AmenityWriter(amenities, internalDataService);

		Thread thread = new Thread(amenityWriter);
		thread.start();

		OsmReader osmReader = new OsmReader(amenities);
		parser.parse(is, osmReader);

		amenityWriter.parseFinished();

		log.info("Time needed: " + (System.currentTimeMillis() - time) / (1000 * 60) + " min, Amenities read: "
				+ osmReader.getAmenityCount() + " written: " + amenityWriter.getAmenityCount());
	}
}
