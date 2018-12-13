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

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    private final String resultFilePath = "./generatedCode/Result.txt";
    private final String existingMethodsFilePath = "./generatedCode/ExistingMethods.txt";
    private final String usedMethodsFilePath = "./generatedCode/UsedMethods.txt";

    private List<String> originalMethodNames;
    private List<String> newMethodNames;

    @Test
    public void initializeFields() throws IOException, InterruptedException, ExecutionException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(existingMethodsFilePath, false));
        // get a list of existing locators
        originalMethodNames = getMethodNames(getLoginPageWithDriver(false));
        writeToTextFile(writer, originalMethodNames);

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
        /*  compare the new locators with the earlier ones
            - if the locator is just updated (order of locator and suffix is same),
             update automation method references and generate the list
            - if a locator is added or removed, then just generate the list
        */
    }

    @Test
    public void initializeMethods() throws IOException, InterruptedException, ExecutionException {
        loginPage = getLoginPage();

        initializePOMList();
        pageObjectGenerator = new PageObjectGenerator(packageName, className, pageObjectModelList,
                pageObjectMethodsClassPath, pageObjectMethodsFilePath, null);
        // generate page methods
        pageObjectGenerator.generatePageMethods(loginPage.getClass().getDeclaredFields());

        copyGeneratedFiles();
    }

    @Test
    public void valdiateLogin() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        loginPage = getLoginPageWithDriver(true);
        newMethodNames = getMethodNames(loginPage);
        originalMethodNames = readFromTextFile();
        Map<String, String> updatedMethodNames = compareMethods(originalMethodNames, newMethodNames);
        List<String> usedMethods = readAllUsedMethods();

        BufferedWriter writer = new BufferedWriter(new FileWriter(usedMethodsFilePath, false));
        String typeUserNameMethod;
        if (updatedMethodNames.containsKey(usedMethods.get(0))) {
            typeUserNameMethod = updatedMethodNames.get(usedMethods.get(0));
        } else {
            typeUserNameMethod = usedMethods.get(0);
        }
        writer.append(typeUserNameMethod);
        writer.newLine();
        Method typeUserName = loginPage.getClass().getMethod(typeUserNameMethod, String.class);
        typeUserName.invoke(loginPage, "pradhan");

        String typePasswordMethod;
        if (updatedMethodNames.containsKey(usedMethods.get(1))) {
            typePasswordMethod = updatedMethodNames.get(usedMethods.get(1));
        } else {
            typePasswordMethod = usedMethods.get(1);
        }
        writer.append(typePasswordMethod);
        writer.newLine();
        Method typePassword = loginPage.getClass().getMethod(typePasswordMethod, String.class);
        typePassword.invoke(loginPage, "pradhan");

        String clickLoginMethod;
        if (updatedMethodNames.containsKey(usedMethods.get(2))) {
            clickLoginMethod = updatedMethodNames.get(usedMethods.get(2));
        } else {
            clickLoginMethod = usedMethods.get(2);
        }
        writer.append(clickLoginMethod);
        writer.newLine();
        Method clickLogin = loginPage.getClass().getMethod(clickLoginMethod);
        clickLogin.invoke(loginPage);

        writer.close();
    }

    private List<String> readAllUsedMethods() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(usedMethodsFilePath));
        List<String> methodNames = new ArrayList<>();
        String strCurrentLine;
        while ((strCurrentLine = reader.readLine()) != null) {
            methodNames.add(strCurrentLine);
        }
        return methodNames;
    }

    private List<String> readFromTextFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(existingMethodsFilePath));
        List<String> methodNames = new ArrayList<>();
        String strCurrentLine;
        while ((strCurrentLine = reader.readLine()) != null) {
            methodNames.add(strCurrentLine);
        }
        return methodNames;
    }

    private void writeToTextFile(BufferedWriter writer, List<String> originalMethodNames) throws IOException {
        for (String methodName : originalMethodNames) {
            writer.append(methodName);
            writer.newLine();
        }
        writer.close();
    }

    private Map<String, String> compareMethods(List<String> originalMethodNames, List<String> newMethodNames) {
        BufferedWriter writer = null;
        Map<String, String> updatedMethodNames = new HashMap<>();

        List<String> origMethodsRemoved = new ArrayList<>(originalMethodNames);
        origMethodsRemoved.removeAll(new HashSet<>(newMethodNames));

        List<String> newMethodsAdded = new ArrayList<>(newMethodNames);
        newMethodsAdded.removeAll(new HashSet<>(originalMethodNames));
        try {
            writer = new BufferedWriter(new FileWriter(resultFilePath, false));
            writer.append("Methods modified:");
            writer.newLine();
            for (int i = 0; i < origMethodsRemoved.size(); i++) {
                for (int j = 0; j < newMethodsAdded.size(); j++) {
                    String[] oldMethodsBits = origMethodsRemoved.get(i).split("_");
                    String[] newMethodsBits = newMethodsAdded.get(j).split("_");
                    if (oldMethodsBits[oldMethodsBits.length - 1].equals(newMethodsBits[newMethodsBits.length - 1])) {
                        // TODO: update automation and text file
                        writer.append(origMethodsRemoved.get(i) + " method name modified to: " + newMethodsAdded.get(j));
                        writer.newLine();
                        updatedMethodNames.put(origMethodsRemoved.get(i), newMethodsAdded.get(j));
                        origMethodsRemoved.remove(i);
                        newMethodsAdded.remove(j);
                    }
                }
            }
            writer.newLine();
            writer.close();
            /* Update text file with:
                - origMethodsRemoved
                - newMethodsAddedpagene
            */

            writer = new BufferedWriter(new FileWriter(resultFilePath, true));
            writer.append("Methods removed:");
            writer.newLine();
            for (String method : origMethodsRemoved) {
                writer.append(method);
                writer.newLine();
            }
            writer.newLine();
            writer.append("Methods added:");
            writer.newLine();
            for (String method : newMethodsAdded) {
                writer.append(method);
                writer.newLine();
            }
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return updatedMethodNames;
    }

    private List<String> getMethodNames(Object object) {
        if (object != null) {
            return Arrays.stream(object.getClass().getMethods())
                    .map(Method::getName)
                    .collect(Collectors.toList());
//            List<String> methodNames = new ArrayList<>();
//            for (Method method : object.getClass().getMethods()) {
//                methodNames.add(method.getName());
//            }
//            return methodNames;
        }
        return null;
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
            } else if (e.tagName().equals("button")) {
                pom.setTagType(TagType.button);
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
                        } else if (attributeValue.equals("checkbox")) {
                            pom.setTagType(TagType.checkbox);
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

    private Object getLoginPage() {
        Class<?> clazz = null;
        Object object;
        try {
            clazz = Class.forName("webpages.LoginPage");
            Constructor<?> ctor = clazz.getConstructor();
            object = ctor.newInstance(new Object[]{});
        } catch (Exception e) {
            return null;
        }
        return object;
    }

    private Object getLoginPageWithDriver(boolean isWebdriverInitialized) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, MalformedURLException {
        Class<?> clazz = Class.forName("webpages.LoginPage");
        if (isWebdriverInitialized) {
            initializeWebdriver();
        }
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
