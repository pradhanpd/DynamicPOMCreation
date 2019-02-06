var LoginPagePageObject = function() {

this.title = element(by.name('title'));
this.htmlheadstyle = element(by.xpath('/html/head/style'));
this.Signup_link = element(by.linkText('Signup'));
this.htmlbodyp = element(by.xpath('/html/body/p'));
this.Login = element(by.linkText('Login'));
this.Iagreetothetermsandconditions = element(by.linkText('I agree to the terms and conditions'));
this.htmlbodyformtable = element(by.xpath('/html/body/form/table'));
this.htmlbodyformtabletbody = element(by.xpath('/html/body/form/table/tbody'));
this.htmlbodyformtabletbodytr = element(by.xpath('/html/body/form/table/tbody/tr'));
this.Username_label = element(by.linkText('Username'));
this.htmlbodyformtabletbodytrtd2 = element(by.xpath('/html/body/form/table/tbody/tr/td[2]'));
this.username_text = element(by.id('username'));
this.htmlbodyformtabletbodytrtd3 = element(by.xpath('/html/body/form/table/tbody/tr/td[3]'));
this.htmlbodyformtabletbodytr2 = element(by.xpath('/html/body/form/table/tbody/tr[2]'));
this.Password_label = element(by.linkText('Password'));
this.htmlbodyformtabletbodytr2td2 = element(by.xpath('/html/body/form/table/tbody/tr[2]/td[2]'));
this.password_text = element(by.id('password'));
this.loginerror = element(by.linkText('${login_error}'));
this.login_button = element(by.id('login'));
this.conditions_checkbox = element(by.name('conditions'));

this.click_Signup__Lnk = function() {
this.Signup_link.click();
};

this.fill_username__Txt = function(value){
this.username_text.clear();
this.username_text.sendKeys(value);
};

this.fill_password__Txt = function(value){
this.password_text.clear();
this.password_text.sendKeys(value);
};

this_login_bu_Btn = function() {
this.login_button.click();
};

this.select_conditions_chec_Chk = function() {
this.conditions_checkbox.click();
};

 };
module.exports = new LoginPagePageObject();