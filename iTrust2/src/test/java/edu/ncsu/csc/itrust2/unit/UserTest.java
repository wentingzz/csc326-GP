package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.admin.UserForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Unit tests for the User class.
 *
 * @author jshore
 *
 */
public class UserTest {

    /**
     * Tests equals comparison of two user objects. Also verifies getters and
     * setters of the used properties.
     */
    @Test
    public void testEqualsAndProperties () {
        final User u1 = new User();
        final User u2 = new User();

        assertFalse( u1.equals( new Object() ) );
        assertFalse( u1.equals( null ) );
        assertTrue( u1.equals( u1 ) );

        u1.setEnabled( 1 );
        assertTrue( 1 == u1.getEnabled() );
        u2.setEnabled( 1 );

        u1.setPassword( "abcdefg" );
        assertEquals( "abcdefg", u1.getPassword() );
        u2.setPassword( "abcdefg" );

        u1.setRole( Role.valueOf( "ROLE_PATIENT" ) );
        assertEquals( Role.valueOf( "ROLE_PATIENT" ), u1.getRole() );
        u2.setRole( Role.valueOf( "ROLE_PATIENT" ) );

        u1.setUsername( "abcdefg" );
        assertEquals( "abcdefg", u1.getUsername() );
        u2.setUsername( "abcdefg" );

        assertTrue( u1.equals( u2 ) );

        u1.setEnabled( null );
        assertFalse( u1.equals( u2 ) );
        assertFalse( u2.equals( u1 ) );
        u2.setEnabled( null );
        assertTrue( u1.equals( u2 ) );

        u1.setPassword( null );
        assertFalse( u1.equals( u2 ) );
        assertFalse( u2.equals( u1 ) );
        u2.setPassword( null );
        assertTrue( u1.equals( u2 ) );

        u1.setUsername( null );
        assertFalse( u1.equals( u2 ) );
        assertFalse( u2.equals( u1 ) );
        u2.setUsername( null );
        assertTrue( u1.equals( u2 ) );

        u1.setRole( Role.ROLE_HCP );
        assertFalse( u1.equals( u2 ) );

    }

    /**
     * Tests UserForm constructor and Getters in User
     */
    @Test
    public void testUserFormAndGets () {
        User u = new User();
        UserForm uf = null;
        u.setUsername( "user1" );
        u.setRole( Role.ROLE_PATIENT );
        u.setEnabled( 0 );
        uf = new UserForm( u );
        assertEquals( "user1", uf.getUsername() );
        assertEquals( Role.ROLE_PATIENT.toString(), uf.getRole() );
        assertEquals( "0", uf.getEnabled() );

        uf.setPassword( "123" );
        uf.setPassword2( "234" );
        try {
            u = new User( uf );
            fail();
        }
        catch ( final Exception e ) {
            assertEquals( "Passwords do not match!", e.getMessage() );
        }

        u = User.getHCPs().get( 0 );
        assertEquals( Role.ROLE_HCP, u.getRole() );
        u = User.getPatients().get( 0 );
        assertEquals( Role.ROLE_PATIENT, u.getRole() );
        u = User.getByRole( Role.ROLE_ADMIN ).get( 0 );
        assertEquals( Role.ROLE_ADMIN, u.getRole() );

    }

}
