package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.persistent.BasicHealthMetrics;
import edu.ncsu.csc.itrust2.models.persistent.User;

public class BasicHealthMetricsTest {

    @Test
    public void test () {

        final BasicHealthMetrics bhm = new BasicHealthMetrics();
        assertTrue( bhm.equals( bhm ) );
        assertFalse( bhm.equals( null ) );
        assertFalse( bhm.equals( "123" ) );
        final BasicHealthMetrics bhm1 = new BasicHealthMetrics();
        assertTrue( bhm1.equals( bhm ) );
        assertTrue( bhm.hashCode() == bhm1.hashCode() );

        bhm.setDiastolic( null );
        bhm1.setDiastolic( 12 );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setDiastolic( 12 );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setHcp( null );
        bhm1.setHcp( User.getHCPs().get( 0 ) );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setHcp( User.getHCPs().get( 0 ) );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setHdl( null );
        try {
            bhm.setHdl( -1 );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( null == bhm.getHdl() );
        }
        try {
            bhm.setHdl( 91 );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( null == bhm.getHdl() );
        }
        bhm1.setHdl( 1 );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setHdl( 1 );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setHeadCircumference( null );
        bhm1.setHeadCircumference( new Float( "1.3" ) );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setHeadCircumference( new Float( "1.3" ) );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setHeight( null );
        bhm1.setHeight( new Float( "1.3" ) );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setHeight( new Float( "1.3" ) );
        assertTrue( bhm1.equals( bhm ) );

        bhm1.setHouseSmokingStatus( HouseholdSmokingStatus.INDOOR );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.NONAPPLICABLE );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setHouseSmokingStatus( HouseholdSmokingStatus.INDOOR );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setLdl( null );
        try {
            bhm.setLdl( -1 );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( null == bhm.getLdl() );
        }
        try {
            bhm.setLdl( 601 );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( null == bhm.getLdl() );
        }
        bhm1.setLdl( 3 );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setLdl( 3 );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setPatient( null );
        bhm1.setPatient( User.getPatients().get( 0 ) );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setPatient( User.getPatients().get( 0 ) );
        assertTrue( bhm1.equals( bhm ) );
        bhm.setPatientSmokingStatus( PatientSmokingStatus.EVERYDAY );
        bhm1.setPatientSmokingStatus( PatientSmokingStatus.CURRENT_BUT_UNKNOWN );
        assertFalse( bhm1.equals( bhm ) );
        bhm1.setPatientSmokingStatus( PatientSmokingStatus.EVERYDAY );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setSystolic( null );
        bhm1.setSystolic( 5 );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setSystolic( 5 );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setTri( null );
        try {
            bhm.setTri( 3 );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( null == bhm.getTri() );
        }
        try {
            bhm.setTri( 603 );
            fail();
        }
        catch ( final Exception e ) {
            assertTrue( null == bhm.getTri() );
        }
        bhm1.setTri( 300 );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setTri( 300 );
        assertTrue( bhm1.equals( bhm ) );

        bhm.setWeight( null );
        bhm1.setWeight( new Float( "60" ) );
        assertFalse( bhm.equals( bhm1 ) );
        assertFalse( bhm1.equals( bhm ) );
        bhm.setWeight( new Float( "60" ) );
        assertTrue( bhm1.equals( bhm ) );

        assertTrue( bhm.hashCode() == bhm1.hashCode() );
    }

}
