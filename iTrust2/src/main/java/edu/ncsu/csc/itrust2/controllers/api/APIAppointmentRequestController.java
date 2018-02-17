package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.patient.AppointmentRequestForm;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.persistent.DomainObject;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.EmailUtil;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class that provides REST API endpoints for the AppointmentRequest model. In
 * all requests made to this controller, the {id} provided is a numeric ID that
 * is the primary key of the appointment request in question
 *
 * @author Kai Presler-Marshall
 * @author Natalie Landsberg
 * @author Hannah Morrison
 *
 */
@RestController
@SuppressWarnings ( { "unchecked", "rawtypes" } )
public class APIAppointmentRequestController extends APIController {

    /**
     * Retrieves a list of all AppointmentRequests in the database
     *
     * @return list of appointment requests
     */
    @GetMapping ( BASE_PATH + "/appointmentrequests" )
    public List<AppointmentRequest> getAppointmentRequests () {
        return AppointmentRequest.getAppointmentRequests();
    }

    /**
     * Retrieves the AppointmentRequest specified by the ID provided
     *
     * @param id
     *            The (numeric) ID of the AppointmentRequest desired
     * @return The AppointmentRequest corresponding to the ID provided or
     *         HttpStatus.NOT_FOUND if no such AppointmentRequest could be found
     */
    @GetMapping ( BASE_PATH + "/appointmentrequests/{id}" )
    public ResponseEntity getAppointmentRequest ( @PathVariable ( "id" ) final Long id ) {
        final AppointmentRequest request = AppointmentRequest.getById( id );
        if ( request != null ) {
            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_VIEWED, request.getPatient(), request.getHcp() );
        }
        return null == request
                ? new ResponseEntity( errorResponse( "No AppointmentRequest found for id " + id ),
                        HttpStatus.NOT_FOUND )
                : new ResponseEntity( request, HttpStatus.OK );
    }

    /**
     * Creates an AppointmentRequest from the RequestBody provided. Record is
     * automatically saved in the database.
     *
     * @param requestF
     *            The AppointmentRequestForm to be parsed into an
     *            AppointmentRequest and stored
     * @return The parsed and validated AppointmentRequest created from the Form
     *         provided, HttpStatus.CONFLICT if a Request already exists with
     *         the ID of the provided request, or HttpStatus.BAD_REQUEST if
     *         another error occurred while parsing or saving the Request
     *         provided
     */
    @PostMapping ( BASE_PATH + "/appointmentrequests" )
    public ResponseEntity createAppointmentRequest ( @RequestBody final AppointmentRequestForm requestF ) {

        try {
            final AppointmentRequest request = new AppointmentRequest( requestF );
            if ( null != AppointmentRequest.getById( request.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "AppointmentRequest with the id " + request.getId() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
            request.save();
            try {
                final String name = requestF.getPatient();
                final User user = User.getByName( name );
                String addr = "";
                String firstName = "";
                final Personnel person = Personnel.getByName( user );
                if ( person != null ) {
                    addr = person.getEmail();
                    firstName = person.getFirstName();
                }
                else {
                    final Patient patient = Patient.getPatient( user );
                    if ( patient != null ) {
                        addr = patient.getEmail();
                        firstName = patient.getFirstName();
                    }
                    else {
                        return new ResponseEntity(
                                errorResponse( "No Patient or Personnel on file for " + user.getId() ),
                                HttpStatus.NOT_FOUND );
                    }

                    if ( addr == null ) {
                        LoggerUtil.log( TransactionType.NOTIFICATION_EMAIL_NOT_SENT,
                                "An email should have been sent to you, but there is no email associated with your account." );
                    }
                    else {
                        String body = "Dear " + firstName + ", \n\nWe receieved your request to make an appointment.\n";
                        body += "--iTrust2 Admin";
                        EmailUtil.sendEmail( addr, "iTrust2 Appointment Request", body );
                        LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_EMAIL_SENT,
                                "An email regarding your appointment request has been sent." );
                    }
                }
            }
            catch ( final NullPointerException e ) {
                // don't send email, move on
            }

            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_SUBMITTED, request.getPatient(), request.getHcp() );
            return new ResponseEntity( request, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity( errorResponse( "Error occured while validating or saving " + requestF.toString()
                    + " because of " + e.getMessage() ), HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Deletes the AppointmentRequest with the id provided. This will remove all
     * traces from the system and cannot be reversed.
     *
     * @param id
     *            The id of the AppointmentRequest to delete
     * @return response
     */
    @DeleteMapping ( BASE_PATH + "/appointmentrequests/{id}" )
    public ResponseEntity deleteAppointmentRequest ( @PathVariable final Long id ) {
        final AppointmentRequest request = AppointmentRequest.getById( id );

        if ( request == null ) {
            return new ResponseEntity( errorResponse( "No appointmentrequest found for id " + id ),
                    HttpStatus.NOT_FOUND );
        }

        try {
            try {
                final String name = request.getPatient().getUsername();
                final User user = User.getByName( name );
                String addr = "";
                String firstName = "";
                final Personnel person = Personnel.getByName( user );
                if ( person != null ) {
                    addr = person.getEmail();
                    firstName = person.getFirstName();
                }
                else {
                    final Patient patient = Patient.getPatient( user );
                    if ( patient != null ) {
                        addr = patient.getEmail();
                        firstName = patient.getFirstName();
                    }
                    else {
                        LoggerUtil.log( TransactionType.NOTIFICATION_EMAIL_NOT_SENT,
                                "An email should have been sent to you, but there is no email associated with your account." );
                        return new ResponseEntity(
                                errorResponse( "No Patient or Personnel on file for " + user.getId() ),
                                HttpStatus.NOT_FOUND );
                    }
                }

                String body = "Dear " + firstName + ", \n\nWe receieved your request to delete your appointment.\n";
                body += "--iTrust2 Admin";
                EmailUtil.sendEmail( addr, "iTrust2 Request to Delete Appointment", body );
                LoggerUtil.log( TransactionType.CHANGE_EMAIL_SENT,
                        "An email regarding your appointment has been sent." );
            }
            catch ( final NullPointerException npe ) {
                // don't send email, continue on
            }

            request.delete();
            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_DELETED, request.getPatient(), request.getHcp() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not delete " + request.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Updates the AppointmentRequest with the id provided by overwriting it
     * with the new AppointmentRequest that is provided. If the ID provided does
     * not match the ID set in the AppointmentRequest provided, the update will
     * not take place
     *
     * @param id
     *            The ID of the AppointmentRequest to be updated
     * @param requestF
     *            The updated AppointmentRequestForm to parse, validate, and
     *            save
     * @return The AppointmentRequest that is created from the Form that is
     *         provided
     */
    @PutMapping ( BASE_PATH + "/appointmentrequests/{id}" )
    public ResponseEntity updateAppointmentRequest ( @PathVariable final Long id,
            @RequestBody final AppointmentRequestForm requestF ) {

        try {
            final AppointmentRequest request = new AppointmentRequest( requestF );

            if ( request.getId() != null && !id.equals( request.getId() ) ) {
                return new ResponseEntity(
                        errorResponse( "The ID provided does not match the ID of the AppointmentRequest provided" ),
                        HttpStatus.CONFLICT );
            }
            final AppointmentRequest dbRequest = AppointmentRequest.getById( id );
            if ( dbRequest == null ) {
                return new ResponseEntity( errorResponse( "No appointmentrequest found for id " + id ),
                        HttpStatus.NOT_FOUND );
            }

            request.save();

            try {
                final String name = request.getPatient().getUsername();
                final User user = User.getByName( name );
                String addr = "";
                String firstName = "";
                final Personnel person = Personnel.getByName( user );
                if ( person != null ) {
                    addr = person.getEmail();
                    firstName = person.getFirstName();
                }
                else {
                    final Patient patient = Patient.getPatient( user );
                    if ( patient != null ) {
                        addr = patient.getEmail();
                        firstName = patient.getFirstName();
                    }
                    else {
                        LoggerUtil.log( TransactionType.NOTIFICATION_EMAIL_NOT_SENT,
                                "An email should have been sent to you, but there is no email associated with your account." );
                        return new ResponseEntity(
                                errorResponse( "No Patient or Personnel on file for " + user.getId() ),
                                HttpStatus.NOT_FOUND );
                    }
                }

                String body = "Dear " + firstName + ", \n\nThe status of your appointment has updated.\n";
                body += "--iTrust2 Admin";
                EmailUtil.sendEmail( addr, "iTrust2 Updated Appointment", body );
                LoggerUtil.log( TransactionType.CHANGE_EMAIL_SENT,
                        "An email regarding your appointment has been sent." );
                LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_UPDATED, request.getPatient(), request.getHcp() );
                return new ResponseEntity( request, HttpStatus.OK );
            }
            catch ( final NullPointerException npe ) {
                // don't send email, continue on
            }

            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_UPDATED, request.getPatient(), request.getHcp() );
            return new ResponseEntity( request, HttpStatus.OK );

        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not update " + requestF.toString() + " because of " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }

    }

    /**
     * Deletes _all_ of the AppointmentRequests stored in the system. Exercise
     * caution before calling this method.
     *
     * @return reponse
     */
    @DeleteMapping ( BASE_PATH + "/appointmentrequests" )
    public ResponseEntity deleteAppointmentRequests () {

        try {
            DomainObject.deleteAll( AppointmentRequest.class );
            try {
                final String name = SecurityContextHolder.getContext().getAuthentication().getName();
                final User user = User.getByName( name );
                String addr = "";
                String firstName = "";
                final Personnel person = Personnel.getByName( user );
                if ( person != null ) {
                    addr = person.getEmail();
                    firstName = person.getFirstName();
                }
                else {
                    final Patient patient = Patient.getPatient( user );
                    if ( patient != null ) {
                        addr = patient.getEmail();
                        firstName = patient.getFirstName();
                    }
                    else {
                        LoggerUtil.log( TransactionType.NOTIFICATION_EMAIL_NOT_SENT,
                                "An email should have been sent to you, but there is no email associated with your account." );
                        return new ResponseEntity(
                                errorResponse( "No Patient or Personnel on file for " + user.getId() ),
                                HttpStatus.NOT_FOUND );
                    }
                }

                String body = "Dear " + firstName
                        + ", \n\nWe receieved your request to delete all your appointments.\n";
                body += "--iTrust2 Admin";
                EmailUtil.sendEmail( addr, "iTrust2 Deleting Appointments", body );
                LoggerUtil.log( TransactionType.CHANGE_EMAIL_SENT,
                        "An email regarding your appointments has been sent." );
            }
            catch ( final NullPointerException npe ) {
                // don't send email, continue on
            }

            return new ResponseEntity( successResponse( "Successfully deleted all AppointmentRequests" ),
                    HttpStatus.OK );
        }
        catch ( final Exception e ) {
            return new ResponseEntity(
                    errorResponse( "Could not delete one or more AppointmentRequests " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

}
