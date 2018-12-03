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
By login_error =  By.linkText("${login_error}");
By login_button =  By.id("login");

 }