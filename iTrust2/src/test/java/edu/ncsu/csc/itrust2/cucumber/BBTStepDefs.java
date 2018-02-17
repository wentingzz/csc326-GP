package edu.ncsu.csc.itrust2.cucumber;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.persistent.PasswordResetToken;
import edu.ncsu.csc.itrust2.utils.HibernateDataGenerator;

/**
 * Step definitions for the BBT feature file.
 *
 * @author Natalie Landsberg
 *
 */
public class BBTStepDefs {
    private WebDriver                driver;
    private final String             baseUrl = "http://localhost:8080/iTrust2";

    // Token for testing
    private final PasswordResetToken token   = null;
    WebDriverWait                    wait;

    @Before
    public void setup () {
        driver = new HtmlUnitDriver( true );
        wait = new WebDriverWait( driver, 30 );

        HibernateDataGenerator.generateUsers();
    }

    @After
    public void tearDown () {
        driver.close();
    }

    private void setTextField ( final By byval, final Object value ) {
        final WebElement elem = driver.findElement( byval );
        elem.clear();
        elem.sendKeys( value.toString() );
    }

    // need to login as admin and create user
    @Given ( "I am able to log in to iTrust as (.+) with password (.+)" )
    public void login ( final String username, final String password ) {
        // driver.get( baseUrl );
        // setTextField( By.name( "username" ), username );
        // setTextField( By.name( "password" ), password );
        // final WebElement submit = driver.findElement( By.className( "btn" )
        // );
        // submit.click();
        //
        // wait.until( ExpectedConditions.not( ExpectedConditions.titleIs(
        // "iTrust2 :: Login" ) ) );
    }

    @When ( "I go to the change password page" )
    public void navChangePassword () {
        // ( (JavascriptExecutor) driver ).executeScript(
        // "document.getElementById('changePassword').click();" );
    }

    @When ( "I fill out the page's form with current password (.+) and new password (.+)" )
    public void fillChangePassword ( final String password, final String newPassword ) {
        // // Wait until page loads
        // wait.until( ExpectedConditions.visibilityOfElementLocated( By.name(
        // "currentPW" ) ) );
        //
        // setTextField( By.name( "currentPW" ), password );
        // setTextField( By.name( "newPW" ), newPassword );
        // setTextField( By.name( "confirmPW" ), newPassword );
        //
        // final WebElement submit = driver.findElement( By.name( "submitButton"
        // ) );
        // submit.click();
    }

    @Then ( "The password is updated successfully" )
    public void verifyPass () {
        // try {
        // wait.until( ExpectedConditions.textToBePresentInElementLocated(
        // By.name( "message" ),
        // "Password changed successfully" ) );
        // }
        // catch ( final Exception e ) {
        // fail( driver.findElement( By.name( "message" ) ).getText() + "\n" +
        // token.getId() + "\n"
        // + token.getTempPasswordPlaintext() );
        // }
        // driver.findElement( By.id( "logout" ) ).click();
    }

    /*
     * Credit for checking email:
     * https://www.tutorialspoint.com/javamail_api/javamail_api_checking_emails.
     * htm
     */
    // TODO verify this works
    @Then ( "an approved email is sent to the user" )
    public void verifyEmailApproved () {
        // final String username = "csc326s18.203.2@gmail.com";
        // final String password = "greenball";
        // final String host = "pop.gmail.com";
        // final PasswordResetToken token = null;
        // boolean containsSubject = false;
        // try {
        // // create properties field
        // final Properties properties = new Properties();
        // properties.put( "mail.store.protocol", "pop3" );
        // properties.put( "mail.pop3.host", host );
        // properties.put( "mail.pop3.port", "995" );
        // properties.put( "mail.pop3.starttls.enable", "true" );
        // final Session emailSession = Session.getDefaultInstance( properties
        // );
        // // emailSession.setDebug(true);
        //
        // // create the POP3 store object and connect with the pop server
        // final Store store = emailSession.getStore( "pop3s" );
        //
        // store.connect( host, username, password );
        //
        // // create the folder object and open it
        // final Folder emailFolder = store.getFolder( "INBOX" );
        // emailFolder.open( Folder.READ_WRITE );
        //
        // // retrieve the messages from the folder in an array and print it
        // final Message[] messages = emailFolder.getMessages();
        // Arrays.sort( messages, ( x, y ) -> {
        // try {
        // return y.getSentDate().compareTo( x.getSentDate() );
        // }
        // catch ( final MessagingException e ) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return 0;
        // } );
        // for ( final Message message : messages ) {
        // // SUBJECT
        // if ( containsSubject == false && message.getSubject() != null
        // && message.getSubject().contains( "iTrust2 Appointment Request" ) ) {
        // containsSubject = true;
        // }
        // }
        // }
        // catch ( final Exception e ) {
        // e.printStackTrace();
        // }
        // if ( containsSubject == false ) {
        // fail( "Failed to receive email." );
        // }
    }

    @Then ( "a lockout email is sent to the user" )
    public void verifyEmailLockout () {
        // final String username = "csc326s18.203.2@gmail.com";
        // final String password = "greenball";
        // final String host = "pop.gmail.com";
        // final PasswordResetToken token = null;
        // boolean containsSubject = false;
        // int index = 0;
        // try {
        // // create properties field
        // final Properties properties = new Properties();
        // properties.put( "mail.store.protocol", "pop3" );
        // properties.put( "mail.pop3.host", host );
        // properties.put( "mail.pop3.port", "995" );
        // properties.put( "mail.pop3.starttls.enable", "true" );
        // final Session emailSession = Session.getDefaultInstance( properties
        // );
        // // emailSession.setDebug(true);
        //
        // // create the POP3 store object and connect with the pop server
        // final Store store = emailSession.getStore( "pop3s" );
        //
        // store.connect( host, username, password );
        //
        // // create the folder object and open it
        // final Folder emailFolder = store.getFolder( "INBOX" );
        // emailFolder.open( Folder.READ_WRITE );
        //
        // // retrieve the messages from the folder in an array and print it
        // final Message[] messages = emailFolder.getMessages();
        // Arrays.sort( messages, ( x, y ) -> {
        // try {
        // return y.getSentDate().compareTo( x.getSentDate() );
        // }
        // catch ( final MessagingException e ) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return 0;
        // } );
        // Message message;
        // // go through emails, looking for the subject line we want;
        // // if it exists, then containsSubject is true
        // while ( containsSubject == false ) {
        // message = messages[index];
        // if ( message.getSubject() != null && message.getSubject().contains(
        // "iTrust2 Appointment Request" ) ) {
        // containsSubject = true;
        // }
        // index++;
        // }
        // }
        // catch ( final Exception e ) {
        // e.printStackTrace();
        // }
        // if ( containsSubject == false ) {
        // fail( "Failed to receive email." );
        // }
    }

    @Then ( "a declined email is sent to the user" )
    public void verifyEmailDeclined () {
        // final String username = "csc326s18.203.2@gmail.com";
        // final String password = "greenball";
        // final String host = "pop.gmail.com";
        // final PasswordResetToken token = null;
        // boolean containsSubject = false;
        // try {
        // // create properties field
        // final Properties properties = new Properties();
        // properties.put( "mail.store.protocol", "pop3" );
        // properties.put( "mail.pop3.host", host );
        // properties.put( "mail.pop3.port", "995" );
        // properties.put( "mail.pop3.starttls.enable", "true" );
        // final Session emailSession = Session.getDefaultInstance( properties
        // );
        // // emailSession.setDebug(true);
        //
        // // create the POP3 store object and connect with the pop server
        // final Store store = emailSession.getStore( "pop3s" );
        //
        // store.connect( host, username, password );
        //
        // // create the folder object and open it
        // final Folder emailFolder = store.getFolder( "INBOX" );
        // emailFolder.open( Folder.READ_WRITE );
        //
        // // retrieve the messages from the folder in an array and print it
        // final Message[] messages = emailFolder.getMessages();
        // Arrays.sort( messages, ( x, y ) -> {
        // try {
        // return y.getSentDate().compareTo( x.getSentDate() );
        // }
        // catch ( final MessagingException e ) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return 0;
        // } );
        // for ( final Message message : messages ) {
        // // SUBJECT
        // if ( containsSubject == false && message.getSubject() != null
        // && message.getSubject().contains( "iTrust2 Appointment Request" ) ) {
        // containsSubject = true;
        // }
        // }
        // }
        // catch ( final Exception e ) {
        // e.printStackTrace();
        // }
        // if ( containsSubject == false ) {
        // fail( "Failed to receive email." );
        // }
    }

    @Then ( "a password email is sent to the patient" )
    public void verifyEmailPassword () {
        // final String username = "csc326s18.203.2@gmail.com";
        // final String password = "greenball";
        // final String host = "pop.gmail.com";
        // final PasswordResetToken token = null;
        // boolean containsSubject = false;
        // try {
        // // create properties field
        // final Properties properties = new Properties();
        // properties.put( "mail.store.protocol", "pop3" );
        // properties.put( "mail.pop3.host", host );
        // properties.put( "mail.pop3.port", "995" );
        // properties.put( "mail.pop3.starttls.enable", "true" );
        // final Session emailSession = Session.getDefaultInstance( properties
        // );
        // // emailSession.setDebug(true);
        //
        // // create the POP3 store object and connect with the pop server
        // final Store store = emailSession.getStore( "pop3s" );
        //
        // store.connect( host, username, password );
        //
        // // create the folder object and open it
        // final Folder emailFolder = store.getFolder( "INBOX" );
        // emailFolder.open( Folder.READ_WRITE );
        //
        // // retrieve the messages from the folder in an array and print it
        // final Message[] messages = emailFolder.getMessages();
        // Arrays.sort( messages, ( x, y ) -> {
        // try {
        // return y.getSentDate().compareTo( x.getSentDate() );
        // }
        // catch ( final MessagingException e ) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // return 0;
        // } );
        // for ( final Message message : messages ) {
        // // SUBJECT
        // if ( containsSubject == false && message.getSubject() != null
        // && message.getSubject().contains( "iTrust2 Password Reset" ) ) {
        // containsSubject = true;
        // }
        // }
        // }
        // catch ( final Exception e ) {
        // e.printStackTrace();
        // }
        // if ( containsSubject == false ) {
        // fail( "Failed to receive email." );
        // }

    }

    @When ( "I go to the Request Appointment page" )
    public void navReqAppt () {
        // ( (JavascriptExecutor) driver ).executeScript(
        // "document.getElementById('requestappointment').click();" );
        // assertTrue( true );
    }

    @When ( "I fill in values for the Appointment Request Fields" )
    public void fillValsApptReq () {
        // final WebElement date = driver.findElement( By.id( "date" ) );
        // date.clear();
        // final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy",
        // Locale.ENGLISH );
        // final Long value = Calendar.getInstance().getTimeInMillis()
        // + 1000 * 60 * 60 * 24 * 14; /* Two weeks */
        // final Calendar future = Calendar.getInstance();
        // future.setTimeInMillis( value );
        // date.sendKeys( sdf.format( future.getTime() ) );
        // final WebElement time = driver.findElement( By.id( "time" ) );
        // time.clear();
        // time.sendKeys( "12:30 PM" );
        // final WebElement comments = driver.findElement( By.id( "comments" )
        // );
        // comments.clear();
        // comments.sendKeys( "Test appointment please ignore" );
        // driver.findElement( By.className( "btn" ) ).click();
    }

    @Then ( "The appointment was requested successfully" )
    public void verifyApptReq () {
        // assertTrue( driver.getPageSource().contains( "Your appointment has
        // been requested successfully" ) );

    }

    @Then ( "The appointment can be found within the list" )
    public void foundAppt () {
        // driver.findElement( By.linkText( "iTrust2" ) ).click();
        // ( (JavascriptExecutor) driver ).executeScript(
        // "document.getElementById('viewrequests-patient').click();" );
        //
        // final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy",
        // Locale.ENGLISH );
        // final Long value = Calendar.getInstance().getTimeInMillis()
        // + 1000 * 60 * 60 * 24 * 14; /* Two weeks */
        // final Calendar future = Calendar.getInstance();
        // future.setTimeInMillis( value );
        // final String dateString = sdf.format( future.getTime() );
        // assertTrue( driver.getPageSource().contains( dateString ) );

    }

    @When ( "I login as hcp" )
    public void loginHCP () {

    }

    @When ( "I go to the View Requests page" )
    public void navViewReqs () {

    }

    @When ( "I approve the appointment request" )
    public void approveApptReq () {

    }

    @Then ( "The request was successfully updated" )
    public void reqSuccUpd () {

    }

    @Then ( "The appointment is within the list of upcoming events" )
    public void apptInList () {

    }

    @Given ( "The user (.+) with password (.+) and the current machine has no failed login attempts\n" )
    public void noLoginAttempts () {

    }

    @When ( "I try to login to iTrust as (.+) with password (.+)" )
    public void tryToLogin () {

    }

    @Then ( "My credentials are not correct" )
    public void credentialsWrong () {

    }

    @Then ( "The account is locked for one hour" )
    public void acctLockedOneHr () {

    }

    @Given ( "The user does not already exist in my database" )
    public void userNotInDatabase () {

    }

    @When ( "I login as admin" )
    public void loginAdmin () {

    }

    @When ( "I go to the add user page" )
    public void navAddUser () {

    }

    @When ( "I fill in values in the Add User form with (.+) and (.+)" )
    public void fillAddUserForm ( final String username, final String password ) {

    }

    @Then ( "The user was created successfully" )
    public void userCreateSucc () {

    }

    @Then ( "My password is updated unsuccessfully" )
    public void unsuccPassUpd () {

    }

    @Then ( "I am returned to the homepage" )
    public void etPhoneHome () {

    }

    @Then ( "the homepage shows the access logs with 4 log entries" )
    public void showFourLogs () {

    }

    @When ( "I enter a valid starting date (.+) and a valid end date (.+)" )
    public void enterDates ( final String startDate, final String endDate ) {

    }

    @When ( "I enter an invalid starting date (.+) and a valid end date (.+)" )
    public void enterInvalidDates ( final String startDate, final String endDate ) {

    }

    @Then ( "I do not see any log entries" )
    public void noLogEntries () {

    }

    @Then ( "I see an option to re-select the range of dates" )
    public void reselectDates () {

    }

    @Then ( "I sign out of the system" )
    public void signOut () {

    }
}
