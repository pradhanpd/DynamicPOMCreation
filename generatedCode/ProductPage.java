package webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage {

By htmlbodydiv =  By.xpath("/html/body/div");
By ProductList =  By.linkText("Product List:");
By htmlbodydivdiv2 =  By.xpath("/html/body/div/div[2]");
By htmlbodydivdiv2div =  By.xpath("/html/body/div/div[2]/div");
By Filterby =  By.linkText("Filter by:");
By htmlbodydivdiv2divdiv2 =  By.xpath("/html/body/div/div[2]/div/div[2]");
By htmlbodydivdiv2divdiv2input_text =  By.xpath("/html/body/div/div[2]/div/div[2]/input");
By htmlbodydivdiv2div2 =  By.xpath("/html/body/div/div[2]/div[2]");
By htmlbodydivdiv2div2div =  By.xpath("/html/body/div/div[2]/div[2]/div");
By Filteredby =  By.linkText("Filtered by:");
By htmlbodydivdiv2div3 =  By.xpath("/html/body/div/div[2]/div[3]");
By table =  By.name("table");
By htmlbodydivdiv2div3tablethead =  By.xpath("/html/body/div/div[2]/div[3]/table/thead");
By htmlbodydivdiv2div3tabletheadtr =  By.xpath("/html/body/div/div[2]/div[3]/table/thead/tr");
By htmlbodydivdiv2div3tabletheadtrth =  By.xpath("/html/body/div/div[2]/div[3]/table/thead/tr/th");
By showImageHideShowImage_button =  By.linkText("{{showImage ? 'Hide' : 'Show'}} Image");
By Product =  By.linkText("Product");
By Code =  By.linkText("Code");
By Available =  By.linkText("Available");
By Price =  By.linkText("Price");
By Rating =  By.linkText("Rating");
By htmlbodydivdiv2div3tabletbody =  By.xpath("/html/body/div/div[2]/div[3]/table/tbody");
By htmlbodydivdiv2div3tabletbodytr =  By.xpath("/html/body/div/div[2]/div[3]/table/tbody/tr");
By htmlbodydivdiv2div3tabletbodytrtd =  By.xpath("/html/body/div/div[2]/div[3]/table/tbody/tr/td");
By htmlbodydivdiv2div3tabletbodytrtdimg =  By.xpath("/html/body/div/div[2]/div[3]/table/tbody/tr/td/img");
By productproductName =  By.linkText("{{product.productName}}");
By productproductCode =  By.linkText("{{product.productCode}}");
By productreleaseDate =  By.linkText("{{product.releaseDate}}");
By productprice =  By.linkText("{{product.price}}");
By productstarRating =  By.linkText("{{product.starRating}}");
WebDriver driver;
public ProductPage(WebDriver webDriver) { driver = webDriver;  }

public void fill_htmlbodydivdiv2divdiv2input__Txt(String inputdata){
if(inputdata.trim().length()==0){
clear_htmlbodydivdiv2divdiv2input__Txt();return;}
driver.findElement(htmlbodydivdiv2divdiv2input_text).clear();
driver.findElement(htmlbodydivdiv2divdiv2input_text).sendKeys(inputdata);
}

public  void clear_htmlbodydivdiv2divdiv2input__Txt(){
driver.findElement(htmlbodydivdiv2divdiv2input_text).clear();
}

public  void click_htmlbodydivdiv2divdiv2input__Txt(){
driver.findElement(htmlbodydivdiv2divdiv2input_text).click();
}

public String getText_htmlbodydivdiv2divdiv2input__Txt(){
return driver.findElement(htmlbodydivdiv2divdiv2input_text).getAttribute("value");
}

public void click_showImageHideShowImage_bu_Btn(){
driver.findElement(showImageHideShowImage_button).click();
}

 }