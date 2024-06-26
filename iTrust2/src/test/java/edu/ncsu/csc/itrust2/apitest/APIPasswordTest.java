package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.forms.personnel.PasswordChangeForm;
import edu.ncsu.csc.itrust2.forms.personnel.PersonnelForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIPasswordTest {

    private MockMvc               mvc;
    PasswordEncoder               pe = new BCryptPasswordEncoder();

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    private void changePassword ( final User user, final String password, final String newP ) throws Exception {
        final PasswordChangeForm form = new PasswordChangeForm();
        form.setCurrentPassword( password );
        form.setNewPassword( newP );
        form.setNewPassword2( newP );
        mvc.perform( post( "/api/v1/changePassword" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isOk() );
    }

    // Save auto-formatter wont let this be a javadoc comment
    // Create user. Starts with password 123456.
    // Changes to 654321.
    // Reset to 98765.
    // Delete user
    @WithMockUser ( username = "patientPW", roles = { "USER", "ADMIN" } )
    @Test
    public void testValidPasswordChanges () throws Exception {

        final UserForm patient = new UserForm( "patientPW", "123456", Role.ROLE_PATIENT, 1 );

        User user = new User( patient );
        user.save();

        user = User.getByName( "patientPW" ); // ensure they exist

        final PersonnelForm personnel = new PersonnelForm();
        personnel.setAddress1( "1 Test Street" );
        personnel.setAddress2( "Address Part 2" );
        personnel.setCity( "Prag" );
        personnel.setEmail( "csc326.201.1@gmail.com" );
        personnel.setFirstName( "Test" );
        personnel.setLastName( "HCP" );
        personnel.setPhone( "123-456-7890" );
        personnel.setSelf( user.getUsername() );
        personnel.setState( State.NC.toString() );
        personnel.setZip( "27514" );
        mvc.perform( post( "/api/v1/personnel" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( personnel ) ) );

        assertTrue( pe.matches( "123456", user.getPassword() ) );
        changePassword( user, "123456", "654321" );
        user = User.getByName( "patientPW" ); // reload so changes are visible
        assertFalse( pe.matches( "123456", user.getPassword() ) );
        assertTrue( pe.matches( "654321", user.getPassword() ) );

        final Personnel p = Personnel.getByName( user );
        p.delete();
        user.delete();

    }

    /**
     * This tests that invalid api requests fail. Invalid passwords and
     * expiration testing handled in unit tests.
     *
     * @throws Exception
     */
    @WithMockUser ( username = "patientPW", roles = { "USER", "ADMIN" } )
    @Test
    public void testInvalidPasswordChanges () throws Exception {

        // test unknown user
        final String pw = "123456";
        final String newP = "654321";
        final PasswordChangeForm form = new PasswordChangeForm();
        form.setCurrentPassword( pw );
        form.setNewPassword( newP );
        form.setNewPassword2( newP );
        mvc.perform( post( "/api/v1/changePassword" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( form ) ) ).andExpect( status().isBadRequest() );

        mvc.perform( post( "/api/v1/requestPasswordReset" ).contentType( MediaType.APPLICATION_JSON )
                .content( "patientPW" ) ).andExpect( status().isBadRequest() );

        final UserForm patient = new UserForm( "patientPW", "123456", Role.ROLE_PATIENT, 1 );

        User user = new User( patient );
        user.save();

        user = User.getByName( "patientPW" ); // ensure they exist

        final PersonnelForm personnel = new PersonnelForm();
        personnel.setAddress1( "1 Test Street" );
        personnel.setAddress2( "Address Part 2" );
        personnel.setCity( "Prag" );
        personnel.setEmail( "csc326s18.203.2@gmail.com" );
        personnel.setFirstName( "Test" );
        personnel.setLastName( "HCP" );
        personnel.setPhone( "123-456-7890" );
        personnel.setSelf( user.getUsername() );
        personnel.setState( State.NC.toString() );
        personnel.setZip( "27514" );
        mvc.perform( post( "/api/v1/personnel" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( personnel ) ) );

        assertTrue( pe.matches( "123456", user.getPassword() ) );
        changePassword( user, "123456", "654321" );
        user = User.getByName( "patientPW" ); // reload so changes are visible
        assertFalse( pe.matches( "123456", user.getPassword() ) );
        assertTrue( pe.matches( "654321", user.getPassword() ) );

        final Personnel p = Personnel.getByName( user );
        p.delete();
        user.delete();

    }

    /**
     * This tests the resetPassword method
     *
     * @throws Exception
     */
    @WithMockUser ( username = "patientPW1", roles = { "USER", "ADMIN" } )
    @Test
    public void testResetPassword () throws Exception {

        final UserForm patient = new UserForm( "patientPW1", "123456", Role.ROLE_PATIENT, 1 );

        // test the reset request for an unknown user
        mvc.perform( post( "/api/v1/resetPassword/111111" ).contentType( MediaType.APPLICATION_JSON )
                .content( "patientPW1" ) ).andExpect( status().is4xxClientError() );
        // make a user and form
        User user = new User( patient );
        user = User.getByName( "patientPW1" ); // ensure they exist
        // assertNotNull( user );
        // test the reset request for a known user, invalid token
        mvc.perform( post( "/api/v1/resetPassword/111111" ).contentType( MediaType.APPLICATION_JSON )
                .content( "patientPW1" ) ).andExpect( status().is4xxClientError() );
        // user.delete();
    }

    // Use patient form instead of personnel form
    // Create user. Starts with password 123456.
    // Changes to 654321.
    // Reset to 98765.
    // Don't delete user
    @WithMockUser ( username = "patientPW2", roles = { "USER", "ADMIN" } )
    @Test
    public void testMoreValidPasswordChanges () throws Exception {

        final UserForm patientUserForm = new UserForm( "patientPW2", "123456", Role.ROLE_PATIENT, 1 );

        User user = new User( patientUserForm );
        user.save();

        user = User.getByName( "patientPW2" ); // ensure they exist

        final PatientForm patient = new PatientForm();
        patient.setAddress1( "1 Test Street" );
        patient.setAddress2( "Address Part 2" );
        patient.setCity( "Prag" );
        patient.setEmail( "csc326s18.203.2@gmail.com" );
        patient.setFirstName( "Test" );
        patient.setLastName( "HCP" );
        patient.setPhone( "123-456-7890" );
        patient.setSelf( user.getUsername() );
        patient.setState( State.NC.toString() );
        patient.setZip( "27514" );
        mvc.perform( post( "/api/v1/personnel" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( patient ) ) );

        assertTrue( pe.matches( "123456", user.getPassword() ) );
        changePassword( user, "123456", "654321" );
        user = User.getByName( "patientPW2" ); // reload so changes are
        // visible
        assertFalse( pe.matches( "123456", user.getPassword() ) );
        assertTrue( pe.matches( "654321", user.getPassword() ) );

        // test the reset request for a known user
        mvc.perform( post( "/api/v1/requestPasswordReset" ).contentType( MediaType.APPLICATION_JSON )
                .content( "patientPW2" ) ).andExpect( status().isOk() );

        final Personnel p = Personnel.getByName( user );
        p.delete();
        user.delete();

    }

}
