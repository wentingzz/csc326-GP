#Author Natalie Landsberg nslandsb
#Author Hannah Morrison hmorris3

Feature: Change or Reset password send email
	As an iTrust2 user
	I want to view access logs
	And make sure an email is sent to me

Scenario Outline: Change password without email on file
Given The user <username> does not already exist in my database
When I login as admin
When I go to the Add User page
When I fill in values in the Add User form with <username> and <password>
Then The user was created successfully
Given I am able to log in to iTrust as <username> with password <password>
When I go to the change password page
When I fill out the page's form with current password <password> and new password <newPassword>
Then My password is updated successfully
And there is a log stating no email on file
Examples:
	|username  |password|newPassword|
	|rando     |redball	|notredball	|
	
Scenario Outline: View logs
Given I am able to log in to iTrust as <username> with password <password>
When I go to the change password page
When I fill out the page's form with current password <password> and new password <newPassword>
Then My password is updated successfully
And I return to the homepage
Then I see access logs with 2 log entries
Examples:
	|username   		 |password	|newPassword	|
	|csc326Logs1     |redball	|notredball	|
	
Scenario Outline: View logs within specified range
Given I am able to log in to iTrust as <username> with password <password>
When I go to the access log page
When I enter a valid starting date <startdate> and a valid end date <enddate>
Then I see access logs with 3 log entries
Examples:
	|username		|password		|startdate	|enddate	 	|
	|csc326Logs1		|redball			|02/05/2018	|02/27/2018	|
	
Scenario Outline: View logs with invalid range
Given I am able to log in to iTrust as <username> with password <password>
When I go to the access log page
When I enter a valid starting date <startdate> and a valid end date <enddate>
Then I do not see any log entries
Examples:
	|username	|password	|startdate	|enddate	 |
	|csc326		|redball		|02/02/2020	|02/05/2018|
	
Scenario Outline: View only 10 logs
Given I am able to log in to iTrust as <username> with password <password>
When I go to the change password page
When I fill out the page's form with current password <password> and new password <newPassword>
Then My password is updated successfully
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
Then I go to the access log page
Then I view my demographics
And I edit my preferred name field
Then I return to the homepage
And I see access logs with 10 log entries
Examples:
	|username		|password	|newPassword |
	|csc326Logs2		|redball		|yellowball  |
	
Scenario Outline: User cannot see admin code in access log
Given I am able to log in to iTrust as <username> with password <password>
Then I do not see the admin code in my access log
Examples:
	|username	|password	|
	|csc326.2	|redball		|
	
Scenario Outline: Change password correctly
Given I am able to log in to iTrust as <username> with password <password>
When I go to the change password page
When I fill out the page's form with current password <password> and new password <newPassword>
Then My password is updated successfully
And a password email is sent to the patient with address <email>
Examples:
	|username   |password|newPassword|email	|
	|csc326     |redball	|yellowball	|csc326s18.203.2@gmail.com	|
	|pwtestuser2|123456  | 654321    | csc326s18.203.2@gmail.com    |

Scenario Outline: Appointment declined
Given I am able to log in to iTrust as <username> with password <password>
When I go to the Request Appointment page as a patient
When I fill in values for the Appointment Request Fields
Then The appointment was requested successfully
And The appointment can be found within the list
When I login as hcp
When I go to the View Requests page
And I approve the appointment request
Then The request was successfully updated
And The appointment is within the list of upcoming events
And a declined email is sent to the user with address <email>
Examples:
	|username   |password|email	|
	|csc326     |redball	|csc326s18.203.2@gmail.com	|
	|pwtestuser2|123456  | csc326s18.203.2@gmail.com    |
	
Scenario Outline: Appointment approved
Given I am able to log in to iTrust as <username> with password <password>
When I go to the Request Appointment page as a patient
When I fill in values for the Appointment Request Fields
Then The appointment was requested successfully
And The appointment can be found within the list
When I login as hcp
And I go to the View Requests page
And I approve the appointment request
Then The request was successfully updated
Then The appointment is within the list of upcoming events 
And an approved email is sent to the user with address <email>
Examples:
	|username   |password|email	|
	|csc326     |redball	|csc326s18.203.2@gmail.com	|
    
Scenario Outline: Email sent to User locked out after 3 failed attempts
Given The user <username> with password <correct> and the current machine does not have failed login attempts
When I try to login to iTrust as <username> with password <password1>
Then My credentials are not correct
When I try a second time to login as <username> with password <password2>
Then My credentials are again not correct 
When I try a third time to login as <username> with password <password3>
Then The account is locked for one hour
And a lockout email is sent to the user with address <email>
Examples:
	|username   |password1|password2	|password3	|correct	    | email |
	|csc326		|yellow   |redBALL	|reDBALL		|redball		|csc326s18.203.2@gmail.com |