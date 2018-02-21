#Author Natalie Landsberg nslandsb
#Author Hannah Morrison hmorris3

Feature: Change or Reset password send email
	As an iTrust2 user
	I want to view access logs
	And make sure an email is sent to me

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
#	|pwtestuser2|123456  | csc326s18.203.2@gmail.com    |
	
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
And I am returned to the homepage
Then I see access logs with 4 log entries
Examples:
	|username   |password|newPassword|email	|
	|csc326     |redball	|notredball	|csc326s18.203.2@gmail.com	|
	|pwtestuser2|123456  |123456         |csc326s18.203.2@gmail.com    |
	
Scenario Outline: View logs within specified range
Given I am able to log in to iTrust as <username> with password <password>
When I go to the access log page
When I enter a valid starting date <startdate> and a valid end date <enddate>
Then I see access logs with 4 entries
Examples:
	|username	|password	|startdate	|enddate	 	|
	|csc326		|redball		|02/05/18	|02/06/18	|
	|pwtestuser2|123456  	|02/06/18	|02/27/18	|
	
Scenario Outline: View logs with invalid date
Given I am able to log in to iTrust as <username> with password <password>
When I go to the access log page
When I enter an invalid starting date <startdate> and a valid end date <enddate>
Then I do not see any log entries
And I see an option to re-select the range of dates
Examples:
	|username	|password	|startdate		|enddate	 |
	|csc326		|redball		|abcdefghijklmno	|02/06/18|
    |pwtestuser2|123456  	|123456			|asdfjsjfjfjdk   |

Scenario Outline: View logs with invalid range
Given I am able to log in to iTrust as <username> with password <password>
When I go to the access log page
When I enter a valid starting date <startdate> and a valid end date <enddate>
Then I do not see any log entries
And I see an option to re-select the range of dates
Examples:
	|username	|password	|startdate	|enddate	 |
	|csc326		|redball		|02/06/18	|02/05/18|
	|pwtestuser2|123456  	|123456		|csc326s18.203.2@gmail.com    |
	
Scenario Outline: View more than 10 logs
Given I am able to log in to iTrust as <username> with password <password>
When I go to the change password page
When I fill out the page's form with current password <password> and new password <newPassword>
Then My password is updated successfully
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
When I go to the Request Appointment page as a patient
#When I fill in values for the Appointment Request Fields
#Then The appointment is requested successfully
#And The appointment can be found within the list
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
And I sign out of the system
Given I am able to log in to iTrust as <username> with password <newPassword>
Then I go to my access log
Then I view my demographics
And I edit my preferred name field
Then I return to the homepage and view the access log entries
Examples:
	|username	|password	|newPassword |
	|csc326		|redball		|yellowball  |
	
Scenario Outline: User cannot see admin code in access log
Given The user does not already exist in the database
When I log in as admin
When I go to the Add User page
When I fill in the values in the Add User form with <username> and <password> 
Then The user was created successfully
And I sign out as admin
Given I am able to log in to iTrust as <username> with password <password>
Then I do not see the admin code in my access log
Examples:
	|username	|password	|
	|csc326		|redball		|