package org.osmsurround.ae.ac;

import static org.junit.Assert.*;

import org.junit.Test;
import org.osmsurround.ae.TestBase;
import org.osmsurround.ae.ac.AutoCompleteService;
import org.springframework.beans.factory.annotation.Autowired;


public class AutoCompleteServiceTest extends TestBase {

	@Autowired
	private AutoCompleteService autoCompleteService;

	@Test
	public void testSearchByKey() throws Exception {
		assertEquals(1, autoCompleteService.searchByKey("key_a").size());
		assertEquals(1, autoCompleteService.searchByKey("key_b").size());
		assertEquals(2, autoCompleteService.searchByKey("key").size());
	}

	@Test
	public void testSearchByKeyAndValue() throws Exception {
		assertEquals(2, autoCompleteService.searchByKeyAndValue("key_a", "").size());
		assertEquals(2, autoCompleteService.searchByKeyAndValue("key_a", "val").size());
		assertEquals(3, autoCompleteService.searchByKeyAndValue("key_b", "").size());
		assertEquals(2, autoCompleteService.searchByKeyAndValue("key_b", "val").size());
		assertEquals(1, autoCompleteService.searchByKeyAndValue("key_b", "other").size());
		assertEquals(0, autoCompleteService.searchByKeyAndValue("key", "other").size());
	}
}
