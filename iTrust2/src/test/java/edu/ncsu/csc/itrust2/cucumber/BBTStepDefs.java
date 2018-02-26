package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import edu.ncsu.csc.itrust2.models.persistent.PasswordResetToken;
import edu.ncsu.csc.itrust2.models.persistent.User;
import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * Step definitions for the BBT feature file.
 *
 * @author Natalie Landsberg
 * @author Hannah Morrison
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
        // driver = new HtmlUnitDriver( true );

        ChromeDriverManager.getInstance().setup();
        final ChromeOptions options = new ChromeOptions();
        // options.addArguments( "headless" );
        options.addArguments( "window-size=1200x600" );
        options.addArguments( "blink-settings=imagesEnabled=false" );
        driver = new ChromeDriver( options );
        wait = new WebDriverWait( driver, 10 );

    }

    @After
    public void tearDown () {
        driver.close();
        driver.quit();
    }

    private void setTextField ( final By byval, final Object value ) {
        final WebElement elem = driver.findElement( byval );
        elem.clear();
        elem.sendKeys( value.toString() );
    }

    @Given ( "I am able to log in to iTrust as (.+) with password (.+)" )
    public void login ( final String username, final String password ) {
        driver.get( baseUrl );
        try {
            driver.findElement( By.id( "logout" ) ).click();
        }
        catch ( final NoSuchElementException nsee ) {
            // user is not logged in, we good
        }

        wait.until( ExpectedConditions.titleIs( "iTrust2 :: Login" ) );
        setTextField( By.name( "username" ), username );
        setTextField( By.name( "password" ), password );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        wait.until( ExpectedConditions.not( ExpectedConditions.titleIs( "iTrust2 :: Login" ) ) );
    }

    @When ( "I login as hcp" )
    public void loginHcp () {
        driver.get( baseUrl );
        try {
            driver.findElement( By.id( "logout" ) ).click();
        }
        catch ( final NoSuchElementException nsee ) {
            // user is not logged in, we good
        }
        wait.until( ExpectedConditions.titleIs( "iTrust2 :: Login" ) );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "hcp" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @When ( "I go to the change password page" )
    public void navChangePassword () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('changePassword').click();" );
    }

    @When ( "I fill out the page's form with current password (.+) and new password (.+)" )
    public void fillChangePassword ( final String password, final String newPassword ) {
        // Wait until page loads
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "currentPW" ) ) );

        setTextField( By.name( "currentPW" ), password );
        setTextField( By.name( "newPW" ), newPassword );
        setTextField( By.name( "confirmPW" ), newPassword );

        final WebElement submit = driver.findElement( By.name( "submitButton" ) );
        submit.click();

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "message" ) ) );

    }

    @Then ( "My password is updated successfully" )
    public void verifyPass () {
        try {
            wait.until( ExpectedConditions.textToBePresentInElementLocated( By.name( "message" ),
                    "Password changed successfully" ) );
            assertFalse( driver.getPageSource().contains( "Could not change" ) );
        }
        catch ( final Exception e ) {
            fail( driver.findElement( By.name( "message" ) ).getText() + "\n" + token.getId() + "\n"
                    + token.getTempPasswordPlaintext() );
        }
        // driver.findElement( By.id( "logout" ) ).click();
    }

    @Then ( "there is a log stating no email on file" )
    public void checkforEmailLog () {
        // unsure how to check this since it's generated with JS
        driver.getPageSource().contains( "activitylog" );
    }

    @Then ( "a password email is sent to the patient with address (.+)" )
    public void verifyEmailPassword ( final String emailAddress ) {
        final String username = emailAddress;
        final String password = "greenball";
        final String host = "pop.gmail.com";
        final PasswordResetToken token = null;
        boolean containsSubject = false;
        try {
            // create properties field
            final Properties properties = new Properties();
            properties.put( "mail.store.protocol", "pop3" );
            properties.put( "mail.pop3.host", host );
            properties.put( "mail.pop3.port", "995" );
            properties.put( "mail.pop3.starttls.enable", "true" );
            final Session emailSession = Session.getDefaultInstance( properties );
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            final Store store = emailSession.getStore( "pop3s" );

            store.connect( host, username, password );

            // create the folder object and open it
            final Folder emailFolder = store.getFolder( "INBOX" );
            emailFolder.open( Folder.READ_WRITE );

            // retrieve the messages from the folder in an array and print it
            final Message[] messages = emailFolder.getMessages();
            Arrays.sort( messages, ( x, y ) -> {
                try {
                    return y.getSentDate().compareTo( x.getSentDate() );
                }
                catch ( final MessagingException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 0;
            } );
            for ( final Message message : messages ) {
                // SUBJECT
                if ( containsSubject == false && message.getSubject() != null
                        && message.getSubject().contains( "iTrust2 Password Reset" ) ) {
                    containsSubject = true;
                }
            }
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
        if ( containsSubject == false ) {
            fail( "Failed to receive email." );
        }

    }

    /*
     * Credit for checking email:
     * https://www.tutorialspoint.com/javamail_api/javamail_api_checking_emails.
     * htm
     */
    // TODO verify this works
    @Then ( "an approved email is sent to the user with address (.+)" )
    public void verifyEmailApproved ( final String emailAddress ) {
        final String username = emailAddress;
        final String password = "greenball";
        final String host = "pop.gmail.com";
        final PasswordResetToken token = null;
        boolean containsSubject = false;
        try {
            // create properties field
            final Properties properties = new Properties();
            properties.put( "mail.store.protocol", "pop3" );
            properties.put( "mail.pop3.host", host );
            properties.put( "mail.pop3.port", "995" );
            properties.put( "mail.pop3.starttls.enable", "true" );
            final Session emailSession = Session.getDefaultInstance( properties );
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            final Store store = emailSession.getStore( "pop3s" );

            store.connect( host, username, password );

            // create the folder object and open it
            final Folder emailFolder = store.getFolder( "INBOX" );
            emailFolder.open( Folder.READ_WRITE );

            // retrieve the messages from the folder in an array and print it
            final Message[] messages = emailFolder.getMessages();
            Arrays.sort( messages, ( x, y ) -> {
                try {
                    return y.getSentDate().compareTo( x.getSentDate() );
                }
                catch ( final MessagingException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 0;
            } );
            for ( final Message message : messages ) {
                // SUBJECT
                if ( containsSubject == false && message.getSubject() != null
                        && message.getSubject().contains( "iTrust2 Appointment Request" ) ) {
                    containsSubject = true;
                }
            }
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
        if ( containsSubject == false ) {
            fail( "Failed to receive email." );
        }
    }

    @Then ( "a declined email is sent to the user with address (.+)" )
    public void verifyEmailDeclined ( final String emailAddress ) {
        final String username = emailAddress;
        final String password = "greenball";
        final String host = "pop.gmail.com";
        final PasswordResetToken token = null;
        boolean containsSubject = false;
        try {
            // create properties field
            final Properties properties = new Properties();
            properties.put( "mail.store.protocol", "pop3" );
            properties.put( "mail.pop3.host", host );
            properties.put( "mail.pop3.port", "995" );
            properties.put( "mail.pop3.starttls.enable", "true" );
            final Session emailSession = Session.getDefaultInstance( properties );
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            final Store store = emailSession.getStore( "pop3s" );

            store.connect( host, username, password );

            // create the folder object and open it
            final Folder emailFolder = store.getFolder( "INBOX" );
            emailFolder.open( Folder.READ_WRITE );

            // retrieve the messages from the folder in an array and print it
            final Message[] messages = emailFolder.getMessages();
            Arrays.sort( messages, ( x, y ) -> {
                try {
                    return y.getSentDate().compareTo( x.getSentDate() );
                }
                catch ( final MessagingException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 0;
            } );
            for ( final Message message : messages ) {
                // SUBJECT
                if ( containsSubject == false && message.getSubject() != null
                        && message.getSubject().contains( "iTrust2 Appointment Request" ) ) {
                    containsSubject = true;
                }
            }
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
        if ( containsSubject == false ) {
            fail( "Failed to receive email." );
        }
    }

    @When ( "I go to the Request Appointment page" )
    public void navReqAppt () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('requestappointment').click();" );
    }

    @When ( "I fill in values for the Appointment Request Fields" )
    public void fillValsApptReq () {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.id( "date" ) ) );
        final WebElement date = driver.findElement( By.id( "date" ) );
        date.clear();
        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        final Long value = Calendar.getInstance().getTimeInMillis()
                + 1000 * 60 * 60 * 24 * 14; /* Two weeks */
        final Calendar future = Calendar.getInstance();
        future.setTimeInMillis( value );
        date.sendKeys( sdf.format( future.getTime() ) );
        final WebElement time = driver.findElement( By.id( "time" ) );
        time.clear();
        time.sendKeys( "12:30 PM" );
        final WebElement comments = driver.findElement( By.id( "comments" ) );
        comments.clear();
        comments.sendKeys( "Test appointment please ignore" );
        driver.findElement( By.className( "btn" ) ).click();
    }

    @Then ( "The appointment was requested successfully" )
    public void verifyApptReq () {
        assertTrue( driver.getPageSource().contains( "Your appointment has been requested successfully" ) );

    }

    @Then ( "The appointment can be found within the list" )
    public void foundAppt () {
        driver.findElement( By.linkText( "iTrust2" ) ).click();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewrequests-patient').click();" );

        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        final Long value = Calendar.getInstance().getTimeInMillis()
                + 1000 * 60 * 60 * 24 * 14; /* Two weeks */
        final Calendar future = Calendar.getInstance();
        future.setTimeInMillis( value );
        final String dateString = sdf.format( future.getTime() );
        assertTrue( driver.getPageSource().contains( dateString ) );
    }

    @When ( "I go to the View Requests page" )
    public void navViewReqs () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewrequests').click();" );
    }

    @When ( "I go to the View Requests page as a patient" )
    public void navViewReqsPatient () {
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewrequests-patients').click();" );
    }

    @When ( "I approve the appointment request" )
    public void approveApptReq () {
        driver.findElement( By.name( "appointment" ) ).click();
        driver.findElement( By.className( "btn" ) ).click();

    }

    @Then ( "The request was successfully updated" )
    public void reqSuccUpd () {
        assertTrue( driver.getPageSource().contains( "Appointment request was successfully updated" ) );

    }

    @Then ( "The appointment is within the list of upcoming events" )
    public void apptInList () {
        driver.findElement( By.linkText( "iTrust2" ) ).click();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('upcomingrequests').click();" );

        final SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.ENGLISH );
        final Long value = Calendar.getInstance().getTimeInMillis()
                + 1000 * 60 * 60 * 24 * 14; /* Two weeks */
        final Calendar future = Calendar.getInstance();
        future.setTimeInMillis( value );
        final String dateString = sdf.format( future.getTime() );
        assertTrue( driver.getPageSource().contains( dateString ) );
        assertTrue( driver.getPageSource().contains( "patient" ) );

    }

    @Given ( "The user (.+) with password (.+) and the current machine does not have failed login attempts" )
    public void noLoginAttempts ( final String username, final String correct ) {
        // attempts cleared by logging in
        driver.get( baseUrl );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "username" ) ) );

        final WebElement usernameField = driver.findElement( By.name( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( username );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( correct );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.id( "logout" ) ) );
        driver.findElement( By.id( "logout" ) ).click();
    }

    @When ( "I try to login to iTrust as (.+) with password (.+)" )
    public void tryToLogin ( final String username, final String password1 ) {
        driver.get( baseUrl );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "username" ) ) );

        final WebElement usernameField = driver.findElement( By.name( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( username );
        final WebElement passwordField = driver.findElement( By.name( "password" ) );
        passwordField.clear();
        passwordField.sendKeys( password1 );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @When ( "My credentials are not correct" )
    public void credentialsWrong () {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.className( "alert-error" ) ) );
        assertTrue( driver.findElement( By.className( "alert-error" ) ).getText()
                .contains( "Invalid username and password." ) );
    }

    @When ( "I try a second time to login as (.+) with password (.+)" )
    public void tryToLoginSecondTime ( final String username, final String password2 ) {
        driver.get( baseUrl );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "username" ) ) );

        final WebElement usernameField = driver.findElement( By.name( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( username );
        final WebElement passwordField = driver.findElement( By.name( "password" ) );
        passwordField.clear();
        passwordField.sendKeys( password2 );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @When ( "My credentials are again not correct" )
    public void credentialsWrongSecondTime () {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.className( "alert-error" ) ) );
        assertTrue( driver.findElement( By.className( "alert-error" ) ).getText()
                .contains( "Invalid username and password." ) );
    }

    @When ( "I try a third time to login as (.+) with password (.+)" )
    public void tryToLoginThirdTime ( final String username, final String password3 ) {
        driver.get( baseUrl );

        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "username" ) ) );

        final WebElement usernameField = driver.findElement( By.name( "username" ) );
        usernameField.clear();
        usernameField.sendKeys( username );
        final WebElement passwordField = driver.findElement( By.name( "password" ) );
        passwordField.clear();
        passwordField.sendKeys( password3 );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

    @Then ( "The account is locked for one hour" )
    public void acctLockedOneHr () {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.className( "alert-error" ) ) );
        assertTrue( driver.findElement( By.className( "alert-error" ) ).getText()
                .contains( "Too many invalid logins. Account locked for 1 hour." ) );
    }

    @Then ( "a lockout email is sent to the user with address (.+)" )
    public void lockoutEmailSent ( final String emailAddress ) {
        final String username = emailAddress;
        final String password = "greenball";
        final String host = "pop.gmail.com";
        final PasswordResetToken token = null;
        boolean containsSubject = false;
        try {
            // create properties field
            final Properties properties = new Properties();
            properties.put( "mail.store.protocol", "pop3" );
            properties.put( "mail.pop3.host", host );
            properties.put( "mail.pop3.port", "995" );
            properties.put( "mail.pop3.starttls.enable", "true" );
            final Session emailSession = Session.getDefaultInstance( properties );
            // emailSession.setDebug(true);

            // create the POP3 store object and connect with the pop server
            final Store store = emailSession.getStore( "pop3s" );

            store.connect( host, username, password );

            // create the folder object and open it
            final Folder emailFolder = store.getFolder( "INBOX" );
            emailFolder.open( Folder.READ_WRITE );

            // retrieve the messages from the folder in an array and print it
            final Message[] messages = emailFolder.getMessages();
            Arrays.sort( messages, ( x, y ) -> {
                try {
                    return y.getSentDate().compareTo( x.getSentDate() );
                }
                catch ( final MessagingException e ) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 0;
            } );
            for ( final Message message : messages ) {
                // SUBJECT
                if ( containsSubject == false && message.getSubject() != null
                        && message.getSubject().contains( "iTrust2 Lockout Notification" ) ) {
                    containsSubject = true;
                }
            }
        }
        catch ( final Exception e ) {
            e.printStackTrace();
        }
        if ( containsSubject == false ) {
            fail( "Failed to receive email." );
        }

    }

    @Given ( "The user (.+) does not already exist in my database" )
    public void userNotInDatabase ( final String username ) {
        final List<User> users = User.getUsers();
        for ( final User user : users ) {
            if ( user.getUsername().equals( username ) ) {
                try {
                    user.delete();
                }
                catch ( final Exception e ) {
                    Assert.fail();
                }
            }
        }
    }

    @When ( "I login as admin" )
    public void loginAdmin () {
        driver.get( baseUrl );
        final WebElement username = driver.findElement( By.name( "username" ) );
        username.clear();
        username.sendKeys( "admin" );
        final WebElement password = driver.findElement( By.name( "password" ) );
        password.clear();
        password.sendKeys( "123456" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();

    }

    @When ( "I go to the Add User page" )
    public void navAddUser () {
        try {
            ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('addnewuser').click();" );
        }
        catch ( final Exception e ) {
            driver.get( "http://localhost:8080/iTrust2/addUser" );
        }
    }

    @When ( "I fill in values in the Add User form with (.+) and (.+)" )
    public void fillAddUserForm ( final String un, final String pw ) {

        final WebElement username = driver.findElement( By.id( "username" ) );
        username.clear();
        username.sendKeys( un );

        final WebElement password = driver.findElement( By.id( "password" ) );
        password.clear();
        password.sendKeys( pw );

        final WebElement password2 = driver.findElement( By.id( "password2" ) );
        password2.clear();
        password2.sendKeys( pw );

        final Select role = new Select( driver.findElement( By.id( "role" ) ) );
        role.selectByVisibleText( "ROLE_HCP" );

        final WebElement enabled = driver.findElement( By.className( "checkbox" ) );
        enabled.click();

        driver.findElement( By.className( "btn" ) ).click();
    }

    @Then ( "The user was created successfully" )
    public void userCreateSucc () {
        assertTrue( driver.getPageSource().contains( "User added successfully" ) );

    }

    @Then ( "My password is updated unsuccessfully" )
    public void unsuccPassUpd () {
        assertFalse( driver.getPageSource().contains( "Password changed successfully" ) );

    }

    @Then ( "I return to the homepage" )
    public void etPhoneHome () {
        // click iTrust2 in the upper righthand corner
        driver.findElement( By.xpath( "//a[contains(@href, 'iTrust2')]" ) ).click();
        assertTrue( driver.getPageSource().contains( "Welcome to iTrust2" ) );

    }

    @Then ( "I see access logs with (.+) log entries" )
    public void showSixLogs ( final int numLogs ) {
        // if the log is on the home page, then it will show up in the home
        // page's source
        driver.getPageSource().contains( "activitylog" );
        final List<WebElement> tableRows = driver.findElements( By.cssSelector( "tbody > tr" ) );
        assertTrue( tableRows.size() == numLogs );
        // doesn't work, just generates JS code
        // final WebElement table_element = driver.findElement( By.id(
        // "logtable" ) );
        // final List<WebElement> tr_collection = table_element.findElements(
        // By.xpath( "id('logtable')/tbody/tr" ) );
        //
        // int row_num, col_num;
        // row_num = 1;
        // for ( final WebElement trElement : tr_collection ) {
        // final List<WebElement> td_collection = trElement.findElements(
        // By.xpath( "td" ) );
        // System.out.println( "NUMBER OF COLUMNS=" + td_collection.size() );
        // col_num = 1;
        // for ( final WebElement tdElement : td_collection ) {
        // System.out.println( "row # " + row_num + ", col # " + col_num +
        // "text=" + tdElement.getText() );
        // assertTrue( tdElement.getText().contains( "LOGIN_SUCCESS" )
        // || tdElement.getText().contains( "PASSWORD_UPDATE_SUCCESS" )
        // || tdElement.getText().contains( "EDIT_DEMOGRAPHICS" )
        // || tdElement.getText().contains( "VIEW_DEMOGRAPHICS" )
        // || tdElement.getText().contains( "APPOINTMENT_REQUEST_VIEWED" )
        // || tdElement.getText().contains( "APPOINTMENT_REQUEST_SUBMITTED" ) );
        //
        // col_num++;
        // }
        // row_num++;
        // }
        // try {
        // Thread.sleep( 500000 );
        // }
        // catch ( final InterruptedException e ) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    @When ( "I go to the access log page" )
    public void goToAccessLogPage () {
        try {
            ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('viewLogs').click();" );
        }
        catch ( final Exception e ) {
            driver.get( "http://localhost:8080/iTrust2/activitylog" );
        }
    }

    @When ( "I enter a valid starting date (.+) and a valid end date (.+)" )
    public void enterDates ( final String startDate, final String endDate ) {
        final WebElement from = driver.findElement( By.name( "startdate" ) );
        final String startMonth = startDate.substring( 0, 2 );
        final String startDay = startDate.substring( 3, 5 );
        final String startYear = startDate.substring( 5, 9 );
        from.sendKeys( startMonth + startDay + startYear );

        final WebElement to = driver.findElement( By.name( "startdate" ) );
        final String endMonth = startDate.substring( 0, 2 );
        final String endDay = startDate.substring( 3, 5 );
        final String endYear = startDate.substring( 5, 9 );
        to.sendKeys( endMonth + endDay + endYear );
    }

    @When ( "I enter an invalid starting date (.+) and a valid end date (.+)" )
    public void enterInvalidDates ( final String startDate, final String endDate ) {
        final WebElement from = driver.findElement( By.name( "startdate" ) );
        final String startMonth = startDate.substring( 0, 2 );
        final String startDay = startDate.substring( 3, 5 );
        final String startYear = startDate.substring( 5, 9 );
        from.sendKeys( startMonth + startDay + startYear );

        final WebElement to = driver.findElement( By.name( "startdate" ) );
        final String endMonth = startDate.substring( 0, 2 );
        final String endDay = startDate.substring( 3, 5 );
        final String endYear = startDate.substring( 5, 9 );
        to.sendKeys( endMonth + endDay + endYear );
        final List<WebElement> tableRows = driver.findElements( By.cssSelector( "tbody > tr" ) );
        assertTrue( tableRows.size() == 0 );

    }

    @Then ( "I do not see any log entries" )
    public void noLogEntries () {
        assertTrue( driver.getPageSource().contains( "Error Message: The start date should be before the end date" ) );

    }

    @Then ( "I sign out of the system" )
    public void signOut () {
        final WebElement logout = driver.findElement( By.id( "logout" ) );
        logout.click();
    }

    @Then ( "I view my demographics" )
    public void viewDemos () {
        try {
            ( (JavascriptExecutor) driver )
                    .executeScript( "document.getElementById('editdemographics-patient').click();" );
        }
        catch ( final Exception e ) {
            driver.get( "http://localhost:8080/iTrust2/patient/editDemographics" );
        }
    }

    @Then ( "I edit my preferred name field" )
    public void editNickname () {
        wait.until( ExpectedConditions.visibilityOfElementLocated( By.name( "preferredName" ) ) );
        final WebElement prefName = driver.findElement( By.id( "preferredName" ) );
        prefName.clear();
        prefName.sendKeys( "newNickname" );
        final WebElement submit = driver.findElement( By.className( "btn" ) );
        submit.click();
    }

}
