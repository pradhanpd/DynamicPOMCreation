package ApplicationPageObjectGenerator;

import Helper.PageObjectGeneratorHelper;
import Model.PageObjectModel;
import Model.TagAttribute;
import Model.TagType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

public class ApplicationTestAngularPage {
    Object page;

    List<PageObjectModel> indexPageObjectModelList = new ArrayList<PageObjectModel>();
    List<PageObjectModel> appPageObjectModelList = new ArrayList<PageObjectModel>();
    List<PageObjectModel> productPageObjectModelList = new ArrayList<PageObjectModel>();

    PageObjectGeneratorHelper indexPageObjectGeneratorHelper;
    PageObjectGeneratorHelper appPageObjectGeneratorHelper;
    PageObjectGeneratorHelper productPageObjectGeneratorHelper;

    private final String pageObjectMethodsClassPath = "./generatedCode/";

    private final String indexPageObjectMethodsFilePath = "./generatedCode/IndexPage.java";
    private final String appPageObjectMethodsFilePath = "./generatedCode/AppPage.java";
    private final String productPageObjectMethodsFilePath = "./generatedCode/ProductPage.java";

    private final String indexPageSourceFilePath = "./generatedCode/IndexPage.java";
    private final String indexPageSourceClassPath = "./generatedCode/webpages/IndexPage.class";
    private final String indexPageDestinationFilePath = "./src/test/java/webpages/IndexPage.java";
    private final String indexPageDestinationClassPath = "./target/test-classes/webpages/IndexPage.class";

    private final String appPageSourceFilePath = "./generatedCode/AppPage.java";
    private final String appPageSourceClassPath = "./generatedCode/webpages/AppPage.class";
    private final String appPageDestinationFilePath = "./src/test/java/webpages/AppPage.java";
    private final String appPageDestinationClassPath = "./target/test-classes/webpages/AppPage.class";

    private final String productPageSourceFilePath = "./generatedCode/ProductPage.java";
    private final String productPageSourceClassPath = "./generatedCode/webpages/ProductPage.class";
    private final String productPageDestinationFilePath = "./src/test/java/webpages/ProductPage.java";
    private final String productPageDestinationClassPath = "./target/test-classes/webpages/ProductPage.class";

    private final String packageName = "webpages";

    private final String indexPageClassName = "IndexPage";
    private final String appPageClassName = "AppPage";
    private final String productPageClassName = "ProductPage";

    @Test
    public void initializeFields() throws IOException, InterruptedException, ExecutionException {
        initializeIndexPageFields();

        initializeAppPageFields();

        initializeProductPageFields();

        copyGeneratedFiles();
    }

    @Test
    public void initializeMethods() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException, InterruptedException, ExecutionException {
        initializeIndexPageMethods();

        initializeAppPageMethods();

        initializeProductPageMethods();

        copyGeneratedFiles();
    }

    private void initializeIndexPageMethods() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException, InterruptedException, ExecutionException {
        page = getPageObject("webpages.IndexPage");

        indexPageObjectModelList = initializePOMList("D:\\Workshop\\Angular-GettingStarted-master\\APM\\src\\index.html");
        indexPageObjectGeneratorHelper = new PageObjectGeneratorHelper(packageName, indexPageClassName, indexPageObjectModelList,
                pageObjectMethodsClassPath, indexPageObjectMethodsFilePath, null);
        // generate page methods
        indexPageObjectGeneratorHelper.generatePageMethods(page.getClass().getDeclaredFields());
    }

    private void initializeAppPageMethods() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException, InterruptedException, ExecutionException {
        page = getPageObject("webpages.AppPage");

        appPageObjectModelList = initializePOMList("D:\\Workshop\\Angular-GettingStarted-master\\APM\\src\\app\\app.component.html");
        appPageObjectGeneratorHelper = new PageObjectGeneratorHelper(packageName, appPageClassName, appPageObjectModelList,
                pageObjectMethodsClassPath, appPageObjectMethodsFilePath, null);
        // generate page methods
        appPageObjectGeneratorHelper.generatePageMethods(page.getClass().getDeclaredFields());
    }

    private void initializeProductPageMethods() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException, InterruptedException, ExecutionException {
        page = getPageObject("webpages.ProductPage");

        productPageObjectModelList = initializePOMList("D:\\Workshop\\Angular-GettingStarted-master\\APM\\src\\app\\products\\product-list.component.html");
        productPageObjectGeneratorHelper = new PageObjectGeneratorHelper(packageName, productPageClassName, productPageObjectModelList,
                pageObjectMethodsClassPath, productPageObjectMethodsFilePath, null);
        // generate page methods
        productPageObjectGeneratorHelper.generatePageMethods(page.getClass().getDeclaredFields());
    }

    private Object getPageObject(String className) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, MalformedURLException {
        Class<?> clazz = Class.forName(className);
        Constructor<?> ctor = clazz.getConstructor();
        Object object = ctor.newInstance(new Object[]{});

        return object;
    }

    private void initializeIndexPageFields() throws IOException, InterruptedException, ExecutionException {
        indexPageObjectModelList = initializePOMList("D:\\Workshop\\Angular-GettingStarted-master\\APM\\src\\index.html");
        indexPageObjectGeneratorHelper = new PageObjectGeneratorHelper(packageName, indexPageClassName, indexPageObjectModelList,
                pageObjectMethodsClassPath, indexPageObjectMethodsFilePath, null);
        // create/ start class composition
        indexPageObjectGeneratorHelper.start();
        // generate page fields
        indexPageObjectGeneratorHelper.generatePageFields();
        // close/ end class composition
        indexPageObjectGeneratorHelper.end();
        indexPageObjectGeneratorHelper.compile();
    }

    private void initializeAppPageFields() throws IOException, InterruptedException, ExecutionException {
        appPageObjectModelList = initializePOMList("D:\\Workshop\\Angular-GettingStarted-master\\APM\\src\\app\\app.component.html");
        appPageObjectGeneratorHelper = new PageObjectGeneratorHelper(packageName, appPageClassName, appPageObjectModelList,
                pageObjectMethodsClassPath, appPageObjectMethodsFilePath, null);
        // create/ start class composition
        appPageObjectGeneratorHelper.start();
        // generate page fields
        appPageObjectGeneratorHelper.generatePageFields();
        // close/ end class composition
        appPageObjectGeneratorHelper.end();
        appPageObjectGeneratorHelper.compile();
    }

    private void initializeProductPageFields() throws IOException, InterruptedException, ExecutionException {
        productPageObjectModelList = initializePOMList("D:\\Workshop\\Angular-GettingStarted-master\\APM\\src\\app\\products\\product-list.component.html");
        productPageObjectGeneratorHelper = new PageObjectGeneratorHelper(packageName, productPageClassName, productPageObjectModelList,
                pageObjectMethodsClassPath, productPageObjectMethodsFilePath, null);
        // create/ start class composition
        productPageObjectGeneratorHelper.start();
        // generate page fields
        productPageObjectGeneratorHelper.generatePageFields();
        // close/ end class composition
        productPageObjectGeneratorHelper.end();
        productPageObjectGeneratorHelper.compile();
    }

    private List<PageObjectModel> initializePOMList(String filePath) throws IOException {
        List<PageObjectModel> pageObjectModelList = new ArrayList<>();
        File file = new File(filePath);
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
                    && pom.getTagName() != "head" && pom.getTagName() != "body"
                    && pom.getTagName() != "base" && pom.getTagName() != "meta"
                    && pom.getTagName() != "link") {
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
        return pageObjectModelList;
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

    private void copyGeneratedFiles() throws IOException {
        copyGeneratedIndexPageFiles();
        copyGeneratedAppPageFiles();
        copyGeneratedProductPageFiles();
    }

    private void copyGeneratedIndexPageFiles() throws IOException {
        Path sourceFile = Paths.get(indexPageSourceFilePath);
        Path sourceClass = Paths.get(indexPageSourceClassPath);
        Path destinationFile = Paths.get(indexPageDestinationFilePath);
        Path destinationClass = Paths.get(indexPageDestinationClassPath);

        //copy source to target using Files Class
        Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(sourceClass, destinationClass, StandardCopyOption.REPLACE_EXISTING);
    }

    private void copyGeneratedAppPageFiles() throws IOException {
        Path sourceFile = Paths.get(appPageSourceFilePath);
        Path sourceClass = Paths.get(appPageSourceClassPath);
        Path destinationFile = Paths.get(appPageDestinationFilePath);
        Path destinationClass = Paths.get(appPageDestinationClassPath);

        //copy source to target using Files Class
        Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(sourceClass, destinationClass, StandardCopyOption.REPLACE_EXISTING);
    }

    private void copyGeneratedProductPageFiles() throws IOException {
        Path sourceFile = Paths.get(productPageSourceFilePath);
        Path sourceClass = Paths.get(productPageSourceClassPath);
        Path destinationFile = Paths.get(productPageDestinationFilePath);
        Path destinationClass = Paths.get(productPageDestinationClassPath);

        //copy source to target using Files Class
        Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(sourceClass, destinationClass, StandardCopyOption.REPLACE_EXISTING);
    }
}

