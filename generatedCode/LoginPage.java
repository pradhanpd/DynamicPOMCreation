package webpages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

By title =  By.name("title");
By htmlheadstyle =  By.xpath("/html/head/style");
By Signup_link =  By.linkText("Signup");
By htmlbodyp =  By.xpath("/html/body/p");
By Login =  By.linkText("Login");
By htmlbodyform =  By.xpath("/html/body/form");
By htmlbodyformtable =  By.xpath("/html/body/form/table");
By htmlbodyformtabletbody =  By.xpath("/html/body/form/table/tbody");
By htmlbodyformtabletbodytr =  By.xpath("/html/body/form/table/tbody/tr");
By Username_label =  By.linkText("Username");
By htmlbodyformtabletbodytrtd2 =  By.xpath("/html/body/form/table/tbody/tr/td[2]");
By username_text =  By.id("username");
By htmlbodyformtabletbodytrtd3 =  By.xpath("/html/body/form/table/tbody/tr/td[3]");
By htmlbodyformtabletbodytr2 =  By.xpath("/html/body/form/table/tbody/tr[2]");
By Password_label =  By.linkText("Password");
By htmlbodyformtabletbodytr2td2 =  By.xpath("/html/body/form/table/tbody/tr[2]/td[2]");
By password_text =  By.id("password");
By loginerror =  By.linkText("${login_error}");
By submit_button =  By.name("submit");
WebDriver driver;
public LoginPage(WebDriver webDriver) { driver = webDriver;  }

public void click_Signup__Lnk(){
driver.findElement(Signup_link).click();
}

public String getLinkText_Signup__Lnk(){
return driver.findElement(Signup_link).getText();
}

public void click_Username_l_Lbl(){
driver.findElement(Username_label).click();
}

public String getLabelText_Username_l_Lbl(){
return driver.findElement(Username_label).getText();
}

public void fill_username__Txt(String inputdata){
if(inputdata.trim().length()==0){
clear_username__Txt();return;}
driver.findElement(username_text).clear();
driver.findElement(username_text).sendKeys(inputdata);
}

public  void clear_username__Txt(){
driver.findElement(username_text).clear();
}

public  void click_username__Txt(){
driver.findElement(username_text).click();
}

public String getText_username__Txt(){
return driver.findElement(username_text).getAttribute("value");
}

public void click_Password_l_Lbl(){
driver.findElement(Password_label).click();
}

public String getLabelText_Password_l_Lbl(){
return driver.findElement(Password_label).getText();
}

public void fill_password__Txt(String inputdata){
if(inputdata.trim().length()==0){
clear_password__Txt();return;}
driver.findElement(password_text).clear();
driver.findElement(password_text).sendKeys(inputdata);
}

public  void clear_password__Txt(){
driver.findElement(password_text).clear();
}

public  void click_password__Txt(){
driver.findElement(password_text).click();
}

public String getText_password__Txt(){
return driver.findElement(password_text).getAttribute("value");
}

public void click_submit_bu_Btn(){
driver.findElement(submit_button).click();
}

 }