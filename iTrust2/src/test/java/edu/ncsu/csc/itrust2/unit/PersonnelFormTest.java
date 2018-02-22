package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.personnel.PersonnelForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;

/**
 * Tests for the PersonnelForm class
 *
 * @author jshore
 *
 */
public class PersonnelFormTest {

    /**
     * Test the PersonnelForm class.
     */
    @Test
    public void testPersonnelForm () {
        final Personnel person = new Personnel();
        person.setSelf( new User( "username", "password", Role.ROLE_PATIENT, 1 ) );
        person.setFirstName( "first" );
        person.setLastName( "last" );
        person.setAddress1( "address1" );
        person.setAddress2( "address2" );
        person.setCity( "city" );
        person.setState( State.NC );
        person.setZip( "27606" );
        person.setPhone( "111-111-1111" );
        person.setSpecialty( "special" );
        person.setEmail( "email@email.com" );
        person.setId( 1L );
        final PersonnelForm form = new PersonnelForm( person );
        assertEquals( "username", form.getSelf() );
        assertEquals( "first", form.getFirstName() );
        assertEquals( "last", form.getLastName() );
        assertEquals( "address1", form.getAddress1() );
        assertEquals( "address2", form.getAddress2() );
        assertEquals( "city", form.getCity() );
        assertEquals( State.NC.getAbbrev(), form.getState() );
        assertEquals( "27606", form.getZip() );
        assertEquals( "111-111-1111", form.getPhone() );
        assertEquals( "special", form.getSpecialty() );
        assertEquals( "email@email.com", form.getEmail() );
        assertEquals( "1", form.getId() );
    }

    @Test
    public void testPatientForm () {
        User u = new User();
        u.setUsername( "name" );
        PersonnelForm pf = null;
        Personnel p = null;
        try {
            pf = new PersonnelForm( p );
        }
        catch ( final Exception e ) {
            fail();
        }
        p = new Personnel();
        p.setId( (long) 123 );
        p.setSelf( null );
        p.setState( State.NC );
        try {
            pf = new PersonnelForm( p );
        }
        catch ( final Exception e ) {
            fail();
        }
        p.setSelf( u );
        p.setEnabled( 0 );
        try {
            pf = new PersonnelForm( p );
        }
        catch ( final Exception e ) {
            fail();
        }
        assertEquals( "0", pf.getEnabled() );
        pf.setEnabled( "1" );
        assertEquals( "1", pf.getEnabled() );

        p.setState( null );
        try {
            pf = new PersonnelForm( p );
        }
        catch ( final Exception e ) {
            fail();
        }

        pf = new PersonnelForm( u );
        assertEquals( u.getUsername(), pf.getSelf() );
        u = null;
        pf = null;
        pf = new PersonnelForm( u );
        assertEquals( null, pf.getSelf() );

        p.setState( State.NC );
        pf = new PersonnelForm( p );
        pf.setEnabled( "0" );
        p = new Personnel( pf );

        assertTrue( 1 == p.getEnabled() );
        pf.setEnabled( null );
        p = new Personnel( pf );
        assertTrue( 0 == p.getEnabled() );

        pf.setId( "1" );
        p = new Personnel( pf );
        assertTrue( 1 == p.getId() );
    }

}
