package webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IndexPage {

By APM =  By.linkText("APM");
By root =  By.id("root");
WebDriver driver;
public IndexPage(WebDriver webDriver) { driver = webDriver;  }

 }