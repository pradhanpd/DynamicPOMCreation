package webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AppPage {

By htmlbodydiv =  By.xpath("/html/body/div");
By title =  By.linkText("{{title}}");
By htmlbodydivpmproducts =  By.xpath("/html/body/div/pm-products");
WebDriver driver;
public AppPage(WebDriver webDriver) { driver = webDriver;  }

 }