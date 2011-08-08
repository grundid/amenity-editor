package org.osmsurround.ae.oauth;

import static org.junit.Assert.*;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.osmsurround.ae.TestBase;
import org.osmsurround.ae.oauth.OauthCookieService;
import org.osmsurround.ae.oauth.OauthTokens;
import org.springframework.beans.factory.annotation.Autowired;


public class OauthCookieServiceTest extends TestBase {

	@Autowired
	private OauthCookieService oauthCookieService;

	@Test
	public void testCreateOauthCookie() throws Exception {
		Cookie cookie = oauthCookieService.createOauthCookie(new OauthTokens("token", "secret"));
		assertEquals("token####secret", cookie.getValue());
	}

	@Test
	public void testInitOauthServiceFromCookies() throws Exception {
		Cookie cookie = new Cookie("oauth_token", "token####secret");
		boolean result = oauthCookieService.initOauthServiceFromCookies(new Cookie[] { cookie });
		assertTrue(result);
	}
}
