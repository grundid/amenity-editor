package org.osmsurround.ae.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.osmsurround.ae.TestBase;
import org.osmsurround.ae.dao.InternalDataService;
import org.osmsurround.ae.entity.Amenity;
import org.springframework.beans.factory.annotation.Autowired;


public class InternalDataServiceTest extends TestBase {

	@Autowired
	private InternalDataService internalDataService;

	private Map<String, String> newDataMap;

	@Before
	public void setup() throws Exception {
		newDataMap = new HashMap<String, String>();
		newDataMap.put("key", "value");
		newDataMap.put("name", "myname");
	}

	@Test
	public void testUpdateInternalData() throws Exception {
		Amenity amenity = createAmenity(1);
		internalDataService.updateInternalData(1, amenity);
	}

	@Test
	public void testInsertInternalData() throws Exception {
		Amenity amenity = new Amenity(200, 49.1, 10.0, newDataMap);
		amenity.setVersion(2);
		internalDataService.insertInternalData(amenity.getNodeId(), amenity);
	}

	@Test
	public void testDeleteInternalData() throws Exception {
		internalDataService.deleteInternalData(1);
		internalDataService.deleteInternalData(2);
	}

	@Test
	public void testUpdateAmenities() throws Exception {
		List<Amenity> amenities = new ArrayList<Amenity>();
		amenities.add(createAmenity(1));
		amenities.add(createAmenity(2));
		amenities.add(createAmenity(3));
		internalDataService.updateAmenities(amenities);

	}

	private Amenity createAmenity(long nodeId) {
		Amenity amenity = new Amenity(nodeId, 49.1, 10.0, newDataMap);
		amenity.setVersion(2);
		return amenity;
	}

}
