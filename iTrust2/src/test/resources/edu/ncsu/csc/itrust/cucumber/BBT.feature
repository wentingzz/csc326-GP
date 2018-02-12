#Author Natalie Landsberg nslandsb

Feature: Change or Reset password send email
	As an iTrust2 user
	I want to view access logs
	And make sure an email is sent to me

Scenario Outline: Change password correctly
Given I can log in to iTrust as <username> with password <password>
When I navigate to the change password page
When I fill out the form with current password <password> and new password <newPassword>
Then My password is updated successfully
And an email is sent to the patient
Examples:
	|username   |password|newPassword|email	|
	|csc326     |redball	|yellowball	|csc3260GP-203-2@gmail.com	|

Scenario: Appointment declined
Given I can log in to iTrust as <username> with password <password>
When I navigate to the Request Appointment page
When I fill in values in the Appointment Request Fields
Then The appointment is requested successfully
And The appointment can be found in the list
When I log in as hcp
And I navigate to the View Requests page
And I approve the Appointment Request
Then The request is successfully updated
And The appointment is in the list of upcoming events 
And a declined email is sent to the user

Scenario: Appointment approved
Given I can log in to iTrust as <username> with password <password>
When I navigate to the Request Appointment page
When I fill in values in the Appointment Request Fields
Then The appointment is requested successfully
And The appointment can be found in the list
When I log in as hcp
And I navigate to the View Requests page
And I approve the Appointment Request
Then The request is successfully updated
And The appointment is in the list of upcoming events 
And an approved email is sent to the user

Scenario Outline: Email sent to User locked out after 3 failed attempts
Given The user <username> with password <correct> and the current machine have no failed login attempts
When I try to login as <username> with password <password1>
Then My credentials are incorrect
When I try to login as <username> with password <password2>
Then My credentials are incorrect
When I try to login as <username> with password <password3>
Then My account is locked for one hour
And a lockout email is sent to the user
	Examples:
	|username   |password1|password2	|password3	|correct		|
	|csc326		|redball  |redBALL	|reDBALL		|yellowball	|

Scenario Outline: Change password without email on file
Given The user does not already exist in the database
When I log in as admin
When I navigate to the Add User page
When I fill in the values in the Add User form with <username> and <password> 
Then The user is created successfully
Given I can log in to iTrust as <username> with password <password>
When I navigate to the change password page
When I fill out the form with current password <password> and new password <newPassword>
Then My password is updated unsuccessfully
And I am returned to the homepage
Examples:
	|username  |password|newPassword|
	|rando     |redball	|notredball	|
	
Scenario Outline: View logs
Given I can log in to iTrust as <username> with password <password>
When I navigate to the change password page
When I fill out the form with current password <password> and new password <newPassword>
Then My password is updated successfully
And the homepage shows the access logs with 4 log entries
Examples:
	|username   |password|newPassword|email	|
	|csc326     |redball	|notredball	|csc3260GP-203-2@gmail.com	|
	
Scenario Outline: View logs within specified range
Given I can log in to iTrust as <username> with password <password>
When I enter a valid starting date <startdate> and a valid end date <enddate>
Then I see access logs with 4 entries
Examples:
	|username	|password	|startdate	|enddate	 |
	|csc326		|notredball	|02/05/18	|02/06/18|
	
Scenario Outline: View logs with invalid date
Given I can log in to iTrust as <username> with password <password>
When I enter an valid starting date <startdate> and a valid end date <enddate>
Then I do not see any log entries
And I see an option to re-select the range of dates
Examples:
	|username	|password	|startdate		|enddate	 |
	|csc326		|notredball	|abcdefghijklmno	|02/06/18|

Scenario Outline: View logs with invalid range
Given I can log in to iTrust as <username> with password <password>
When I enter a valid starting date <startdate> and a valid end date <enddate>
Then I do not see any log entries
And I see an option to re-select the range of dates
Examples:
	|username	|password	|startdate	|enddate	 |
	|csc326		|notredball	|02/06/18	|02/05/18|
	
Scenario Outline: View more than 10 logs
Given I can log in to iTrust as <username> with password <password>
When I navigate to the change password page
When I fill out the form with current password <password> and new password <newPassword>
Then My password is updated successfully
And I sign out of the system
Given I can log in to iTrust as <username> with password <password>
When I fill in values in the Appointment Request Fields
Then The appointment is requested successfully
And The appointment can be found in the list
And I sign out of the system
Given I can log in to iTrust as <username> with password <password>
And I sign out of the system
Given I can log in to iTrust as <username> with password <password>
Then I navigate to my access log
Then I view my demographics
And I edit my preferred name field
Then I return to the homepage and view the access log entries
Examples:
	|username	|password	|
	|csc326		|notredball1	|
	
Scenario Outline: User cannot see admin code in access log
Given The user does not already exist in the database
When I log in as admin
When I navigate to the Add User page
When I fill in the values in the Add User form with <username> and <password> 
Then The user is created successfully
And I sign out as admin
Given I can log in to iTrust as <username> with password <password>
Then I do not see the admin code in my access log
Examples:
	|username	|password		|
	|cscstudent	|sectionthree	|