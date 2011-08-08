package org.osmsurround.ae.search;

import static org.junit.Assert.*;

import org.junit.Test;
import org.osmsurround.ae.TestBase;
import org.osmsurround.ae.filter.FilterSet;
import org.osmsurround.ae.osm.BoundingBox;
import org.osmsurround.ae.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;


public class SearchServiceTest extends TestBase {

	@Autowired
	private SearchService searchService;

	private FilterSet restuarantFilterSet = new FilterSet(new String[] { "restaurant" }, null);

	@Test
	public void testFindAmenities() throws Exception {
		assertEquals(2, searchService.findAmenities(createBoundingBox(180, -180, 90, -90), restuarantFilterSet).size());
		assertEquals(1, searchService.findAmenities(createBoundingBox(20, 0, 50, 40), restuarantFilterSet).size());
		assertEquals(1, searchService.findAmenities(createBoundingBox(0, -20, 0, -20), restuarantFilterSet).size());
	}

	private BoundingBox createBoundingBox(double east, double west, double north, double south) {
		BoundingBox boundingBox = new BoundingBox();
		boundingBox.setEast(east);
		boundingBox.setWest(west);
		boundingBox.setNorth(north);
		boundingBox.setSouth(south);
		return boundingBox;
	}
}
