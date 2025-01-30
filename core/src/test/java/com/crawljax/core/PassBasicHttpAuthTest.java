package com.crawljax.core;

import com.crawljax.test.BrowserTest;
import org.eclipse.jetty.server.Server;
import org.junit.experimental.categories.Category;

@Category(BrowserTest.class)
public class PassBasicHttpAuthTest {

    private static final String USERNAME = "test";
    private static final String PASSWORD = "test#&";
    private static final String USER_ROLE = "user";

    private Server server;
    private int port;

    //    @Before
    //    public void setup() throws Exception {
    //        server = new Server(0);
    //        ResourceHandler handler = new ResourceHandler();
    //        handler.setBaseResource(Resource.newClassPathResource("/site"));
    //
    //        ConstraintSecurityHandler csh = newSecurityHandler(handler);
    //
    //        server.setHandler(csh);
    //        server.start();
    //
    //        this.port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
    //    }
    //
    //    private ConstraintSecurityHandler newSecurityHandler(ResourceHandler handler) {
    //        HashLoginService login = new HashLoginService();
    //        login.setConfig(
    //                PassBasicHttpAuthTest.class.getResource("/realm.properties").getPath());
    //
    //        Constraint constraint = new Constraint();
    //        constraint.setName(Constraint.__BASIC_AUTH);
    //        constraint.setRoles(new String[] {USER_ROLE});
    //        constraint.setAuthenticate(true);
    //
    //        ConstraintMapping cm = new ConstraintMapping();
    //        cm.setConstraint(constraint);
    //        cm.setPathSpec("/*");
    //
    //        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
    //        csh.setAuthenticator(new BasicAuthenticator());
    //        csh.addConstraintMapping(cm);
    //        csh.setLoginService(login);
    //        csh.setHandler(handler);
    //        return csh;
    //    }
    //
    //    @Test
    //    public void testProvidedCredentialsAreUsedInBasicAuth() {
    //        String url = "http://localhost:" + port + "/infinite.html";
    //        CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
    //        builder.setMaximumStates(3);
    //        builder.setBasicAuth(USERNAME, PASSWORD);
    //        builder.setBrowserConfig(new BrowserConfiguration(BrowserProvider.getBrowserType()));
    //        CrawlSession session = new CrawljaxRunner(builder.build()).call();
    //
    //        assertThat(session.getStateFlowGraph(), hasStates(3));
    //    }
    //
    //    @Test
    //    public void testRegisterCredentialsWithBiDi() {
    //        String host = "localhost";
    //        String url = "http://" + host + ":" + port + "/infinite.html";
    //        CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(url);
    //
    //        builder.addPlugin((OnBrowserCreatedPlugin) browser -> {
    //            Predicate<URI> uriPredicate = uri -> uri.getHost().contains(host);
    //            ((HasAuthentication) browser.getWebDriver())
    //                    .register(uriPredicate, UsernameAndPassword.of(USERNAME, PASSWORD));
    //        });
    //
    //        builder.setMaximumStates(3);
    //        builder.setBrowserConfig(new BrowserConfiguration(EmbeddedBrowser.BrowserType.CHROME_HEADLESS, 1));
    //        CrawlSession session = new CrawljaxRunner(builder.build()).call();
    //
    //        assertThat(session.getStateFlowGraph(), hasStates(3));
    //    }
    //
    //    @After
    //    public void shutDown() throws Exception {
    //        server.stop();
    //    }
}
