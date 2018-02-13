package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.hcp.OfficeVisitForm;
import edu.ncsu.csc.itrust2.models.persistent.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.persistent.Drug;
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
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

}
