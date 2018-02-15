package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.hcp_patient.PatientForm;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.persistent.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.persistent.Drug;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Prescription;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class OfficeVisitTest {

    @Test
    public void test () {
        final OfficeVisit ov = new OfficeVisit();
        final User patient = new User();
        final User hcp = new User();

        final List<Prescription> prelist = new ArrayList<Prescription>();
        final Prescription pre = new Prescription();
        final Drug d = new Drug();
        d.setCode( "code" );
        pre.setDrug( d );
        prelist.add( pre );
        pre.setStartDate( Calendar.getInstance() );
        pre.setEndDate( Calendar.getInstance() );
        pre.setRenewals( 10 );
        pre.setPatient( patient );

        final Calendar c = Calendar.getInstance();
        patient.setUsername( "p-user" );
        hcp.setUsername( "hcp-user" );
        ov.setPatient( patient );
        ov.setHcp( hcp );
        ov.setDate( c );
        ov.setNotes( "test notes" );
        ov.setId( (long) 315 );

        ov.setAppointment( new AppointmentRequest() );
        ov.setPrescriptions( prelist );

        final OfficeVisitForm ovf = new OfficeVisitForm( ov );
        assertEquals( "p-user", ovf.getPatient() );
        assertEquals( "hcp-user", ovf.getHcp() );
        assertEquals( "test notes", ovf.getNotes() );
        assertEquals( "315", ovf.getId() );

        ovf.setHeadCircumference( null );
        assertEquals( null, ovf.getHeadCircumference() );
        ovf.setPrescriptions( null );
        assertEquals( null, ovf.getPrescriptions() );

        final User hcp1 = User.getHCPs().get( 0 );
        final User user2 = User.getPatients().get( 0 );
        assertEquals( new ArrayList<OfficeVisit>(), OfficeVisit.getForHCP( hcp1.getUsername() ) );
        assertEquals( new ArrayList<OfficeVisit>(),
                OfficeVisit.getForHCPAndPatient( hcp1.getUsername(), user2.getUsername() ) );
        assertTrue( ov.getDate().compareTo( Calendar.getInstance() ) < 0 );
        assertEquals( null, ov.getType() );
        assertEquals( null, ov.getHospital() );
        assertEquals( "test notes", ov.getNotes() );
        assertTrue( null != ov.getAppointment() );
        assertTrue( null != ov.getPrescriptions() );
    }

    private Patient createPatientUnder3 () throws Exception {
        final User patient = new User( "patientTestPatient", "123456", Role.ROLE_PATIENT, 1 );
        patient.save();
        final User mom = new User( "patientTestMom", "123456", Role.ROLE_PATIENT, 1 );
        mom.save();
        final User dad = new User( "patientTestDad", "123456", Role.ROLE_PATIENT, 1 );
        dad.save();
        final PatientForm form = new PatientForm();
        form.setMother( mom.getUsername() );
        form.setFather( dad.getUsername() );
        form.setFirstName( "patient" );
        form.setPreferredName( "patient" );
        form.setLastName( "mcpatientface" );
        form.setEmail( "bademail@ncsu.edu" );
        form.setAddress1( "Some town" );
        form.setAddress2( "Somewhere" );
        form.setCity( "placecity" );
        form.setState( State.AL.getName() );
        form.setZip( "27606" );
        form.setPhone( "111-111-1111" );
        form.setDateOfBirth( "01/01/1901" );
        form.setDateOfDeath( "01/01/1902" );
        form.setCauseOfDeath( "Hit by a truck" );
        form.setBloodType( BloodType.ABPos.getName() );
        form.setEthnicity( Ethnicity.Asian.getName() );
        form.setGender( Gender.Male.getName() );
        form.setSelf( patient.getUsername() );
        final Patient testPatient = new Patient( form );
        testPatient.save();

        return testPatient;
    }

    private Patient createPatientUnder12 () throws Exception {
        final User patient = new User( "patientTestPatient", "123456", Role.ROLE_PATIENT, 1 );
        patient.save();
        final User mom = new User( "patientTestMom", "123456", Role.ROLE_PATIENT, 1 );
        mom.save();
        final User dad = new User( "patientTestDad", "123456", Role.ROLE_PATIENT, 1 );
        dad.save();
        final PatientForm form = new PatientForm();
        form.setMother( mom.getUsername() );
        form.setFather( dad.getUsername() );
        form.setFirstName( "patient" );
        form.setPreferredName( "patient" );
        form.setLastName( "mcpatientface" );
        form.setEmail( "bademail@ncsu.edu" );
        form.setAddress1( "Some town" );
        form.setAddress2( "Somewhere" );
        form.setCity( "placecity" );
        form.setState( State.AL.getName() );
        form.setZip( "27606" );
        form.setPhone( "111-111-1111" );
        form.setDateOfBirth( "01/01/1901" );
        form.setDateOfDeath( "01/01/1907" );
        form.setCauseOfDeath( "Hit by a truck" );
        form.setBloodType( BloodType.ABPos.getName() );
        form.setEthnicity( Ethnicity.Asian.getName() );
        form.setGender( Gender.Male.getName() );
        form.setSelf( patient.getUsername() );
        final Patient testPatient = new Patient( form );
        testPatient.save();

        return testPatient;
    }

    @Test
    public void moreTests () throws Exception {
        final OfficeVisit ov = new OfficeVisit();
        final Patient patient = createPatientUnder3();
        final User hcp = new User();

        final Calendar c = Calendar.getInstance();
        hcp.setUsername( "hcp-user" );
        ov.setPatient( User.getByName( "patientTestPatient" ) );
        ov.setHcp( hcp );
        ov.setDate( c );
        ov.setNotes( "test notes" );
        ov.setId( (long) 315 );

        ov.setAppointment( new AppointmentRequest() );
        // ov.setPrescriptions( prelist );

        final OfficeVisitForm ovf = new OfficeVisitForm( ov );
        assertEquals( "patientTestPatient", ovf.getPatient() );
        assertEquals( "hcp-user", ovf.getHcp() );
        assertEquals( "test notes", ovf.getNotes() );
        assertEquals( "315", ovf.getId() );

        ovf.setHeadCircumference( null );
        assertEquals( null, ovf.getHeadCircumference() );
        ovf.setPrescriptions( null );
        assertEquals( null, ovf.getPrescriptions() );

        final User hcp1 = User.getHCPs().get( 0 );
        final User user2 = User.getPatients().get( 0 );
        assertEquals( new ArrayList<OfficeVisit>(), OfficeVisit.getForHCP( hcp1.getUsername() ) );
        assertEquals( new ArrayList<OfficeVisit>(),
                OfficeVisit.getForHCPAndPatient( hcp1.getUsername(), user2.getUsername() ) );
        assertTrue( ov.getDate().compareTo( Calendar.getInstance() ) < 0 );
        assertEquals( null, ov.getType() );
        assertEquals( null, ov.getHospital() );
        assertEquals( "test notes", ov.getNotes() );
        assertTrue( null != ov.getAppointment() );
        assertTrue( null != ov.getPrescriptions() );
    }

}
