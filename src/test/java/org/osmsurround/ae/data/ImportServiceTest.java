package org.osmsurround.ae.data;

import org.junit.Test;
import org.osmsurround.ae.TestBase;
import org.osmsurround.ae.data.ImportService;
import org.springframework.beans.factory.annotation.Autowired;


public class ImportServiceTest extends TestBase {

	@Autowired
	private ImportService importService;

	@Test
	public void testRun() throws Exception {
		importService.run(ClassLoader.getSystemResourceAsStream("map.osm"));
	}
}
