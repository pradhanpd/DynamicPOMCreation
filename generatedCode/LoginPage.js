var LoginPagePageObject = function() {

this.title = element(by.name('title'));
this.Signup_link = element(by.linkText('Signup'));
this.Login = element(by.linkText('Login'));
this.Iagreetothetermsandconditions = element(by.linkText('I agree to the terms and conditions'));
this.Username_label = element(by.linkText('Username'));
this.username_text = element(by.id('username'));
this.Password_label = element(by.linkText('Password'));
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