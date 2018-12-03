package tests;

import Helper.PageObjectGenerator;
import Model.PageObjectModel;
import Model.TagAttribute;
import Model.TagType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TestLoginPage {
    List<PageObjectModel> pageObjectModelList = new ArrayList<PageObjectModel>(); //record tags
    Object loginPage;
    WebDriver webDriver;
    PageObjectGenerator pageObjectGenerator;

    private final String pageObjectMethodsClassPath = "./generatedCode/";
    private final String pageObjectMethodsFilePath = "./generatedCode/LoginPage.java";

    private final String sourceFilePath = "./generatedCode/LoginPage.java";
    private final String sourceClassPath = "./generatedCode/webpages/LoginPage.class";
    private final String destinationFilePath = "./src/test/java/webpages/LoginPage.java";
    private final String destinationClassPath = "./target/test-classes/webpages/LoginPage.class";

    private final String packageName = "webpages";
    private final String className = "LoginPage";

    @Test
    public void initializeFields() throws IOException, InterruptedException, ExecutionException {
        initializePOMList();

        pageObjectGenerator = new PageObjectGenerator(packageName, className, pageObjectModelList,
                pageObjectMethodsClassPath, pageObjectMethodsFilePath, null);
        // create/ start class composition
        pageObjectGenerator.start();
        // generate page fields
        pageObjectGenerator.generatePageFields();
        // close/ end class composition
        pageObjectGenerator.end();
        pageObjectGenerator.compile();

        copyGeneratedFiles();
    }

    @Test
    public void initializeMethods() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException, InterruptedException, ExecutionException {
        loginPage = getLoginPage();

        initializePOMList();
        pageObjectGenerator = new PageObjectGenerator(packageName, className, pageObjectModelList,
                pageObjectMethodsClassPath, pageObjectMethodsFilePath, null);
        // generate page methods
        pageObjectGenerator.generatePageMethods(loginPage.getClass().getDeclaredFields());

        copyGeneratedFiles();
    }

    @Test
    public void valdiateLogin() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException {
        loginPage = getLoginPageWithDriver();

        Method typeUserName = loginPage.getClass().getMethod("fill_username__Txt", String.class);
        typeUserName.invoke(loginPage, "pradhan");
        Method typePassword = loginPage.getClass().getMethod("fill_password__Txt", String.class);
        typePassword.invoke(loginPage, "pradhan");
        Method clickLogin = loginPage.getClass().getMethod("click_login_bu_Btn");
        clickLogin.invoke(loginPage);
    }

    private void copyGeneratedFiles() throws IOException {
        Path sourceFile = Paths.get(sourceFilePath);
        Path sourceClass = Paths.get(sourceClassPath);
        Path destinationFile = Paths.get(destinationFilePath);
        Path destinationClass = Paths.get(destinationClassPath);

        //copy source to target using Files Class
        Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(sourceClass, destinationClass, StandardCopyOption.REPLACE_EXISTING);
    }

    private void initializeWebdriver() {
        System.setProperty("webdriver.gecko.driver", "D:\\Softwares\\geckodriver-v0.23.0-win64\\geckodriver.exe");

        webDriver = new FirefoxDriver();
        webDriver.manage().window().maximize();
        webDriver.get("http://localhost:8082/login");
    }

    private void initializePOMList() throws IOException {
        pageObjectModelList = new ArrayList<>();
        File file = new File("./src/main/resources/freemarker/login.ftl");
        Document doc = Jsoup.parse(file, "UTF-8");

        // all elements in html
        for (Element e : doc.getAllElements()) {
            PageObjectModel pom = new PageObjectModel();
            // add each tag name from List
            pom.setTagName(e.tagName().toLowerCase());

            Map<TagAttribute, String> tags = new HashMap<>();

            // get linktext
            String linktext = e.ownText().trim();
            if (!linktext.isEmpty()) {
                tags.put(TagAttribute.linktext, linktext);
            }

            // get xpath
            String xPath = getXPath(e);
            tags.put(TagAttribute.xpath, xPath);

            // get type
            if (e.classNames().contains("label")) {
                pom.setTagType(TagType.label);
            } else if (e.tagName().equals("a")) {
                pom.setTagType(TagType.link);
            }

            // for each tag get all attributes
            for (Attribute attribute : e.attributes().asList()) {
                // get tag type
                String attributeKey = attribute.getKey();
                // get tag value
                String attributeValue = attribute.getValue();

                switch (attributeKey) {
                    case "id":
                        tags.put(TagAttribute.id, attributeValue);
                        break;
                    case "name":
                        tags.put(TagAttribute.name, attributeValue);
                        break;
                    case "type":
                        if (attributeValue.equals("text") || attributeValue.equals("password")) {
                            pom.setTagType(TagType.text);
                        } else if (attributeValue.equals("submit") || attributeValue.equals("button")) {
                            pom.setTagType(TagType.button);
                        }
                        break;
                    default:
                        System.out.println("No tags matched for: " + e.tagName() + "!!!");
                        break;
                }
            }

            if (tags.containsKey(TagAttribute.id)) {
                pom.setTagAttribute(TagAttribute.id);
                pom.setTagValue(tags.get(TagAttribute.id));
            } else if (tags.containsKey(TagAttribute.name)) {
                pom.setTagAttribute(TagAttribute.name);
                pom.setTagValue(tags.get(TagAttribute.name));
            } else if (tags.containsKey(TagAttribute.linktext)) {
                pom.setTagAttribute(TagAttribute.linktext);
                pom.setTagValue(tags.get(TagAttribute.linktext));
            } else if (tags.containsKey(TagAttribute.xpath)) {
                pom.setTagAttribute(TagAttribute.xpath);
                pom.setTagValue(tags.get(TagAttribute.xpath));
            }
            if (pom.getTagName() != "#root" && pom.getTagName() != "html"
                    && pom.getTagName() != "head" && pom.getTagName() != "body") {
                pageObjectModelList.add(pom);
            }
        }

        for (PageObjectModel pom : pageObjectModelList) {
            System.out.print("TagName: ");
            System.out.print(pom.getTagName());
            System.out.print(" TagAttribute: ");
            System.out.print(pom.getTagAttribute());
            System.out.print(" TagValue: ");
            System.out.println(pom.getTagValue());
            System.out.print(" TagType: ");
            System.out.println(pom.getTagType());
        }
    }

    private Object getLoginPage() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, MalformedURLException {
        Class<?> clazz = Class.forName("webpages.LoginPage");
        Constructor<?> ctor = clazz.getConstructor();
        Object object = ctor.newInstance(new Object[]{});

        return object;
    }

    private Object getLoginPageWithDriver() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, MalformedURLException {
        Class<?> clazz = Class.forName("webpages.LoginPage");
        initializeWebdriver();
        Constructor<?> ctor = clazz.getConstructor(WebDriver.class);
        Object object = ctor.newInstance(new Object[]{webDriver});

        return object;
    }

    private String getXPath(Element element) {
        String xpath = "";
        while (element.parent() != null) {
            String xname = "";
            int elementIndex = getElementIndex(element);
            xname = element.tagName();
            if (elementIndex > 1) {
                xname += "[" + elementIndex + "]";
            }
            xpath = "/" + xname + xpath;
            element = element.parent();
        }
        return xpath;
    }

    private int getElementIndex(Element element) {
        int count = 1;
        Element previousElement = element.previousElementSibling();
        while (previousElement != null) {
            if (element.tagName() == previousElement.tagName()) {
                count++;
            }
            previousElement = previousElement.previousElementSibling();
        }
        return count;
    }
}
