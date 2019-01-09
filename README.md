Creating a POM dynamically:
1) Parse through all HTML elements of a page
- Using any of the open source libraries (ex: Jsoup) parse through the HTML file
- Get a list of all HTML elements with the page
- initializePOMList() creates pageObjectModelList i.e a list of PageObjectModel

2) Create the fields (locators) in the POM class with using the above list of HTML elements
- For each HTML element, get all its tags:
	ID, Name, LinkText – if exists
	Xpath – always
- Check if the tag list contains:
	ID/ Name/ LinkText/ Xpath – in that order, and that attribute and value for creation of locator
- Field name would be something like this:
	Tagvalue + “_” + TagType
- initializeFields() uses PageObjectGeneratorHelper and pageObjectModelList to generate POM fields (locators)

3) Maintain a config containing the list of methods to be created for specific HTML element types
- config.properties contains the methods that need to be generated for the all HTML element events

4) Creating the corresponding methods for each locators in the POM class
- initializeMethods() uses config.properties and the already generated POM fields above to generate the POM methods

5) Validate the POM class methods created against test cases
- Compare the list of method signatures which the Automation test uses (UsedMethods.txt) vs the list of method signatures which is dynamically generated in the POM class
- If locators are created/ deleted, notify the changes to the user (Result.txt). This would give the developer an opportunity to fix the Automation test/ UI even before and well ahead of the nightly automation run.
- If locators are updated(ex: ID newly added), verified by validating against existing methods (ExistingMethods.txt), then notify the change (Result.txt) + enable automation to use the newly created method
- valdiateLogin() would run the test case for the HTML page using the POM class generated in the above steps.
