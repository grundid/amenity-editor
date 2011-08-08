package org.osmsurround.ae.osm;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.osmsurround.ae.TestBase;
import org.osmsurround.ae.entity.Amenity;
import org.osmsurround.ae.osm.BoundingBox;
import org.osmsurround.ae.osm.OsmUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.social.test.client.MockRestServiceServer;
import org.springframework.social.test.client.RequestMatchers;
import org.springframework.social.test.client.ResponseCreators;
import org.springframework.web.client.RestTemplate;


public class OsmServerUpdateServiceTest extends TestBase {

	@Autowired
	private OsmUpdateService osmServerUpdateService;
	@Autowired
	private RestTemplate restTemplate;
	private MockRestServiceServer mockServer;
	@Value("${osmApiBaseUrl}")
	protected String osmApiBaseUrl;

	@Before
	public void setup() {
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	public void testGetOsmData() throws Exception {

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_XML);
		mockServer
				.expect(RequestMatchers.requestTo(osmApiBaseUrl + "/api/0.6/map?bbox=9.15,49.27,9.19,49.3"))
				.andExpect(RequestMatchers.method(HttpMethod.GET))
				.andRespond(
						ResponseCreators.withResponse(new ClassPathResource("/map.osm", getClass()), responseHeaders));

		BoundingBox boundingBox = new BoundingBox();
		boundingBox.setWest(9.15);
		boundingBox.setEast(9.19);
		boundingBox.setNorth(49.30);
		boundingBox.setSouth(49.27);

		List<Amenity> amenities = osmServerUpdateService.getOsmDataAsAmenities(boundingBox);
		assertEquals(99, amenities.size());
	}
}
