package Helper;

import Model.PageObjectModel;
import Model.TagAttribute;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class PageObjectGenerator {
    private final String pageObjectMethodsClassPath;
    private final String pageObjectMethodsFilePath;
    private static BufferedWriter outPageCode = null;

    private String packageName;
    private String className;
    private String[] additionalImports;
    private List<PageObjectModel> pageObjectModelList;

    public PageObjectGenerator(String packageName, String className, List<PageObjectModel> pageObjectModelList,
                               String pageObjectMethodsClassPath, String pageObjectMethodsFilePath, String[] additionalImports) {
        this.packageName = packageName;
        this.className = className;
        this.additionalImports = additionalImports;
        this.pageObjectModelList = pageObjectModelList;
        this.pageObjectMethodsClassPath = pageObjectMethodsClassPath;
        this.pageObjectMethodsFilePath = pageObjectMethodsFilePath;
    }

    public void start() throws IOException {
        clearFile();
        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));
        outPageCode.write("package " + packageName + ";");
        outPageCode.newLine();

        outPageCode.newLine();
        outPageCode.write("import org.openqa.selenium.By;");
        outPageCode.newLine();
        outPageCode.write("import org.openqa.selenium.WebDriver;");

        if (additionalImports != null) {
            for (String imp : additionalImports) {
                outPageCode.newLine();
                outPageCode.write("import " + packageName + ";");
            }
        }
        outPageCode.newLine();
        outPageCode.newLine();
        outPageCode.write("public class " + className + " {");
        outPageCode.newLine();

        outPageCode.close();
    }

    public void compile() throws InterruptedException, ExecutionException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, Locale.getDefault(), null);
        List<JavaFileObject> javaObjects = (List<JavaFileObject>) fileManager.getJavaFileObjectsFromFiles(
                Arrays.asList(new File(pageObjectMethodsFilePath)));

        if (javaObjects.size() == 0) {
            System.out.println("There are no source files to compile in " + pageObjectMethodsFilePath);
        }
        String[] compileOptions = new String[]{"-d", pageObjectMethodsClassPath};
        Iterable<String> compilationOptions = Arrays.asList(compileOptions);

        JavaCompiler.CompilationTask compilerTask = compiler.getTask(null, fileManager, diagnostics,
                compilationOptions, null, javaObjects);
        FutureTask<Boolean> futureTask = new FutureTask<>(compilerTask);
        Thread t = new Thread(futureTask);
        t.start();
        if (futureTask.get()) {
            System.out.println("Class compiled");
        } else {
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                System.err.format("Error on line %d in %s", diagnostic.getLineNumber(), diagnostic);
            }
            System.out.println("Could not compile project");
        }
        t.join();
    }

    public void end() throws IOException {
        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));
        outPageCode.newLine();
        outPageCode.write(" }");
        outPageCode.close();
    }

    public void generatePageFields() throws IOException {
        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

        for (PageObjectModel pom : pageObjectModelList) {
            TagAttribute tagAttribute = pom.getTagAttribute();
            String fieldName = "";
            if (pom.getTagType() != null) {
                fieldName = pom.getTagValue().replaceAll("[^\\w\\s\\_]", "") + "_" + pom.getTagType();
            } else {
                fieldName = pom.getTagValue().replaceAll("[^\\w\\s\\_]", "");
            }
            if (tagAttribute == TagAttribute.id) {
                outPageCode.newLine();
                outPageCode.write("By " + fieldName + " = " + " By.id(\"" + pom.getTagValue() + "\");");
            } else if (tagAttribute == TagAttribute.name) {
                outPageCode.newLine();
                outPageCode.write("By " + fieldName + " = " + " By.name(\"" + pom.getTagValue() + "\");");
            } else if (tagAttribute == TagAttribute.linktext) {
                outPageCode.newLine();
                outPageCode.write("By " + fieldName + " = " + " By.linkText(\"" + pom.getTagValue() + "\");");
            } else if (tagAttribute == TagAttribute.xpath) {
                outPageCode.newLine();
                outPageCode.write("By " + fieldName + " = " + " By.xpath(\"" + pom.getTagValue() + "\");");
            }
        }
        outPageCode.newLine();
        outPageCode.close();
    }

    /**
     * To generate code for Page Object Methods in to a file
     *
     * @param fields Fields[] fields or locators for which code to be generated
     */
    public void generatePageMethods(Field[] fields) throws IOException, InterruptedException, ExecutionException {
        start();

        generatePageFields();

        generatePageConstructor();

        for (Field field : fields) {
            System.out.println(field.getName());
            String fieldName = field.getName();

            if (fieldName.endsWith("_text")) {
                addTextBoxMethods(fieldName);
            }
            if (fieldName.endsWith("_dropdown")) {
                addDropDownMethods(fieldName);
            }
            if (fieldName.endsWith("_checkbox")) {
                addCheckBoxMethods(fieldName);
            }
            if (fieldName.endsWith("_radiobutton")) {
                addRadiobuttonMethods(fieldName);
            }
            if (fieldName.endsWith("_link")) {
                addLinkMethods(fieldName);
            }
            if (fieldName.endsWith("_button")) {
                addButtonMethods(fieldName);
            }
            if (fieldName.endsWith("_label")) {
                addLabelMethods(fieldName);
            }
        }

        end();

        compile();
    }

    private void generatePageConstructor() throws IOException {
        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

        outPageCode.write("WebDriver driver;");
        outPageCode.newLine();

        outPageCode.write("public LoginPage(WebDriver webDriver) { driver = webDriver;  }");
        outPageCode.newLine();

        outPageCode.close();
    }

    /**
     * To clear the file in which code will be generated
     */
    private void clearFile() throws IOException {
        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, false));
        outPageCode.close();
    }

    /**
     * TO generate code for Text boxes
     *
     * @param fieldName String
     */
    private void addTextBoxMethods(String fieldName) throws IOException {
        //out= null;

        String labelName = fieldName.substring(0, fieldName.length() - 4);

        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

        List<String> methodType = getMethodTypesToBeGenerated("textBox");
        if (methodType.contains("fill")) {
            //********Fill**************************
            outPageCode.newLine();
            outPageCode.write("public void fill_" + labelName + "_Txt(String inputdata){");
            outPageCode.newLine();

            outPageCode.write("if(inputdata.trim().length()==0){");
            outPageCode.newLine();
            outPageCode.write("clear_" + labelName + "_Txt();");
            outPageCode.write("return;}");
            outPageCode.newLine();
            outPageCode.write("driver.findElement(" + fieldName + ").clear();");
            outPageCode.newLine();
            outPageCode.write("driver.findElement(" + fieldName + ").sendKeys(inputdata);");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        //********Clear**************************
        if (methodType.contains("clear")) {
            outPageCode.newLine();
            outPageCode.write("public  void clear_" + labelName + "_Txt(){");
            outPageCode.newLine();
            outPageCode.write("driver.findElement(" + fieldName + ").clear();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        //********Click**************************
        if (methodType.contains("click")) {
            outPageCode.newLine();
            outPageCode.write("public  void click_" + labelName + "_Txt(){");
            outPageCode.newLine();
            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }

        //********GetText**************************
        if (methodType.contains("gettext")) {
            outPageCode.newLine();
            outPageCode.write("public String getText_" + labelName + "_Txt(){");
            outPageCode.newLine();
            outPageCode.write("return driver.findElement(" + fieldName);
            outPageCode.write(").getAttribute(" + "\"value\"" + ");");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();
    }

    /**
     * TO generate code for Dropdown
     *
     * @param fieldName String
     */

    private void addDropDownMethods(String fieldName) throws IOException {

        //out= null;

        String labelName = fieldName.substring(0, fieldName.length() - 3);

        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

        List<String> methodType = getMethodTypesToBeGenerated("dropDown");
        //****************************Select From DropDown****************************
        if (methodType.contains("select")) {
            outPageCode.newLine();
            outPageCode.write("public void select_" + labelName + "_DD(String inputdata){");
            outPageCode.newLine();
            outPageCode.write("if(inputdata.trim().length()==0)return;");
            outPageCode.newLine();

            outPageCode.write("new Select (driver.findElement(" + fieldName + ")).selectByVisibleText(inputdata);");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        //*******************************UnSelect DropDown*********************
        if (methodType.contains("deselect")) {
            outPageCode.newLine();
            outPageCode.write("public void deSelect_" + labelName + "_DD(String inputdata){");
            outPageCode.newLine();
            outPageCode.write("new Select (driver.findElement(" + fieldName + ")).deselectByVisibleText(inputdata);");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        //*******************************GetSelected DropDown*********************
        if (methodType.contains("getselectedtext")) {
            outPageCode.newLine();
            outPageCode.write("public String getSelectedText_" + labelName + "_DD(){");
            outPageCode.newLine();
            outPageCode.write("return new Select (driver.findElement(" + fieldName + ")).getFirstSelectedOption().getText();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        //*******************************GetSelected ID DropDown*********************
        if (methodType.contains("getselectedid")) {
            outPageCode.newLine();
            outPageCode.write("public String getSelectedID_" + labelName + "_DD(){");
            outPageCode.newLine();


            outPageCode.write("return new Select (driver.findElement(" + fieldName + ")).getFirstSelectedOption().getAttribute(\"value\");");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();
    }

    /**
     * TO generate code for Check box
     *
     * @param fieldName String
     */
    private void addCheckBoxMethods(String fieldName) throws IOException {

        //out= null;
        String labelName = fieldName.substring(0, fieldName.length() - 4);
        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

        List<String> methodType = getMethodTypesToBeGenerated("checkBox");
        //****************************Select Checkbox****************************
        if (methodType.contains("select")) {
            outPageCode.newLine();
            outPageCode.write("public void select_" + labelName + "_Chk(){");
            outPageCode.newLine();


            outPageCode.write("if(driver.findElement(" + fieldName + ").isSelected()){}else{");
            outPageCode.newLine();
            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        //****************************DeSelect Checkbox
        if (methodType.contains("deselect")) {
            outPageCode.newLine();
            outPageCode.write("public void deSelect_" + labelName + "_Chk(){");
            outPageCode.newLine();

            outPageCode.write("if(driver.findElement(" + fieldName + ").isSelected()){");
            outPageCode.newLine();
            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        //************IsSelected Checkbox


        if (methodType.contains("isselected")) {
            outPageCode.newLine();
            outPageCode.write("public boolean isChecked_" + labelName + "_Chk(){");
            outPageCode.newLine();
            outPageCode.write("return driver.findElement(" + fieldName);
            outPageCode.write(").isSelected();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();

    }

    /**
     * TO generate code for Radio button
     *
     * @param fieldName String
     * @throws IOException if particular file path is not available
     */
    private void addRadiobuttonMethods(String fieldName) throws IOException {
        //out= null;

        String labelName = fieldName.substring(0, fieldName.length() - 3);

        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

        List<String> methodType = getMethodTypesToBeGenerated("radioButton");
        //********************************Radio button Select
        if (methodType.contains("select")) {
            outPageCode.newLine();
            outPageCode.write("public void select_" + labelName + "_Rd(){");
            outPageCode.newLine();
            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }


        //************IsSelected


        if (methodType.contains("isselected")) {
            outPageCode.newLine();
            outPageCode.write("public boolean isChecked_" + labelName + "_Rd(){");
            outPageCode.newLine();
            outPageCode.write("return driver.findElement(" + fieldName);
            outPageCode.write(").isSelected();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();


    }

    /**
     * TO generate code for Link
     *
     * @param fieldName String
     * @throws IOException if particular file path is not available
     */
    private void addLinkMethods(String fieldName) throws IOException {
        //out= null;

        String labelName = fieldName.substring(0, fieldName.length() - 4);
        outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

        List<String> methodType = getMethodTypesToBeGenerated("link");
        //********************************Link Click
        if (methodType.contains("click")) {
            outPageCode.newLine();
            outPageCode.write("public void click_" + labelName + "_Lnk(){");
            outPageCode.newLine();

            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }


        //************GetText
        if (methodType.contains("gettext")) {
            outPageCode.newLine();
            outPageCode.write("public String getLinkText_" + labelName + "_Lnk(){");
            outPageCode.newLine();
            outPageCode.write("return driver.findElement(" + fieldName);
            outPageCode.write(").getText();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();

    }

    /**
     * TO generate code for button
     *
     * @param fieldName String
     * @throws IOException if particular file path is not available
     */
    private void addButtonMethods(String fieldName) throws IOException {
        //out= null;

        String labelName = fieldName.substring(0, fieldName.length() - 4);

        List<String> methodType = getMethodTypesToBeGenerated("button");
        //********************************Button Click
        if (methodType.contains("click")) {
            outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));

            outPageCode.newLine();
            outPageCode.write("public void click_" + labelName + "_Btn(){");
            outPageCode.newLine();


            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();
    }

    /**
     * TO generate code for label
     *
     * @param fieldName String
     * @throws IOException if particular file path is not available
     */
    private void addLabelMethods(String fieldName) throws IOException {
        //out= null;
        String labelName;

        labelName = fieldName.substring(0, fieldName.length() - 4);
        List<String> methodType = getMethodTypesToBeGenerated("label");
        //********************************Label Click
        if (methodType.contains("click")) {
            outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));


            outPageCode.newLine();
            outPageCode.write("public void click_" + labelName + "_Lbl(){");
            outPageCode.newLine();


            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }


        //************GetText


        if (methodType.contains("gettext")) {
            outPageCode.newLine();
            outPageCode.write("public String getLabelText_" + labelName + "_Lbl(){");
            outPageCode.newLine();

            outPageCode.write("return driver.findElement(" + fieldName);
            outPageCode.write(").getText();");
            outPageCode.newLine();

            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();

    }

    /**
     * TO generate code for Web Element
     *
     * @param fieldName String
     * @throws IOException if particular file path is not available
     */
    private void addWebElementMethods(String fieldName) throws IOException {
        //out= null;

        String labelName = fieldName.substring(0, fieldName.length() - 3);
        List<String> methodType = getMethodTypesToBeGenerated("webElement");
        //********************************WebElement Click
        if (methodType.contains("click")) {
            outPageCode = new BufferedWriter(new FileWriter(pageObjectMethodsFilePath, true));


            outPageCode.newLine();
            outPageCode.write("public void click_" + labelName + "_WE(){");
            outPageCode.newLine();


            outPageCode.write("driver.findElement(" + fieldName + ").click();");
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }


        //************GetText

        if (methodType.contains("gettext")) {

            outPageCode.newLine();
            outPageCode.write("public String getElementText_" + labelName + "_WE(){");
            outPageCode.newLine();

            outPageCode.write("return driver.findElement(" + fieldName);
            outPageCode.write(").getText();");
            outPageCode.newLine();
            outPageCode.newLine();
            outPageCode.write("}");
            outPageCode.newLine();
        }
        outPageCode.close();
    }

    /**
     * TO get particular property from config
     *
     * @param propertyName String
     */

    private String getPropertyValue(String propertyName) {
        String propFileName = "./src/config.properties";
        File file = new File(propFileName);
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties prop = new Properties();

        try {
            prop.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String propertyValue = prop.getProperty(propertyName);
        return propertyValue;
    }

    /**
     * To get method types for particular element type in to list
     *
     * @param methodType String
     */
    private List<String> getMethodTypesToBeGenerated(String methodType) {
        List<String> methodTypesList = new ArrayList<String>();
        String methods = getPropertyValue(methodType);
        String[] methodTypesString = methods.toLowerCase().split(",");
        for (int i = 0; i < methodTypesString.length; i++) {
            methodTypesList.add(methodTypesString[i]);
        }
        return methodTypesList;
    }
}
