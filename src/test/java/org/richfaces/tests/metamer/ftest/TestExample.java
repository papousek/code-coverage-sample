package org.richfaces.tests.metamer.ftest;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

public class TestExample extends Arquillian {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    protected URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        WebArchive archive = ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/showcase.war"));
        Map<ArchivePath, Node> jars = archive.getContent(Filters.include(".*\\.jar"));
        for (Entry<ArchivePath, Node> jar : jars.entrySet()) {
            if (!jar.getKey().get().contains("richfaces")) {
                continue;
            }
            JavaArchive newJar = ShrinkWrap.createFromZipFile(JavaArchive.class, new File("target/" + new File(jar.getKey().get()).getName()));
            archive.delete(jar.getKey());
            archive.addAsLibrary(newJar);
            System.out.println(jar.getKey().toString());
        }
        return archive;
    }

    @Test
    public void test() {
        browser.get(contextPath.toString());
    }

}
