package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.ncsu.csc.itrust2.forms.patient.AppointmentForm;

/**
 * Tests the methods for the AppointmentForm class
 *
 * @author Hannah Morrison (hmorris3)
 */
public class AppointmentFormTest {
    /**
     * Tests the getter and setter methods for the AppointmentForm object
     */
    @Test
    public void testGettersAndSetters () {
        final AppointmentForm form = new AppointmentForm();
        // test setting and getting the appointment string
        form.setAppointment( "General Checkup" );
        assertTrue( form.getAppointment().equals( "General Checkup" ) );
        // test setting and getting the action string
        form.setAction( "APPROVE" );
        assertTrue( form.getAction().equals( "APPROVE" ) );
    }
}
