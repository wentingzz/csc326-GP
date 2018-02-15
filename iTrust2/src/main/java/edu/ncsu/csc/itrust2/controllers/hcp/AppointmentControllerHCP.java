package edu.ncsu.csc.itrust2.controllers.hcp;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import edu.ncsu.csc.itrust2.forms.patient.AppointmentForm;
import edu.ncsu.csc.itrust2.forms.patient.AppointmentRequestForm;
import edu.ncsu.csc.itrust2.models.enums.Status;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.AppointmentRequest;
import edu.ncsu.csc.itrust2.models.persistent.Patient;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.utils.EmailUtil;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Class for managing Appointments for a HCP
 *
 * @author Kai Presler-Marshall
 *
 */
@Controller
public class AppointmentControllerHCP {

    /**
     * Method responsible for HCP's Accept/Reject requested appointment
     * functionality. This prepares the page.
     *
     * @param model
     *            Data for the front end
     * @return The page to display to the user
     */
    @GetMapping ( "/hcp/viewAppointmentRequests" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String requestAppointmentForm ( final Model model ) {
        final List<AppointmentRequest> appointment = AppointmentRequest
                .getAppointmentRequestsForHCP( SecurityContextHolder.getContext().getAuthentication().getName() )
                .stream().filter( e -> e.getStatus().equals( Status.PENDING ) ).collect( Collectors.toList() );
        final List<AppointmentRequestForm> appointments = new Vector<AppointmentRequestForm>();
        for ( final AppointmentRequest ar : appointment ) {
            appointments.add( new AppointmentRequestForm( ar ) );
        }

        /* Log the event */
        appointment.stream().map( AppointmentRequest::getPatient )
                .forEach( e -> LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_VIEWED,
                        SecurityContextHolder.getContext().getAuthentication().getName(), e.getUsername() ) );

        model.addAttribute( "appointments", appointments );
        model.addAttribute( "appointmentForm", new AppointmentForm() );
        final List<String> allActions = new Vector<String>();
        allActions.add( "accept" );
        allActions.add( "reject" );
        model.addAttribute( "allActions", allActions );
        return "hcp/viewAppointmentRequests";
    }

    /**
     * Responsible for Accept/Reject appointment functionality. This takes
     * action based on user parameters.
     *
     * @param form
     *            Form from the user to parse
     * @return Page to display to the user
     */
    @PostMapping ( "/hcp/viewAppointmentRequests" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String appointmentActionSubmit ( @ModelAttribute final AppointmentForm form ) {
        final int id = Integer.parseInt( form.getAppointment() );
        final String action = form.getAction();
        final AppointmentRequest ar = AppointmentRequest.getById( Long.valueOf( id ) );
        final boolean aptAction = action.equals( "reject" );
        ar.setStatus( aptAction ? Status.REJECTED : Status.APPROVED );
        ar.save();
        String addr;
        String firstName;
        String body = "";
        final User patientUser = ar.getPatient();
        final Patient patient = Patient.getPatient( patientUser );
        try {
            if ( patient != null ) {
                addr = patient.getEmail();
                firstName = patient.getFirstName();
            }
            else {
                LoggerUtil.log( TransactionType.NOTIFICATION_EMAIL_NOT_SENT,
                        "An email should have been sent to you, but there is no email associated with your account." );
                throw new Exception( "No Patient on file for " + patientUser.getId() );
            }
            // if the appointment was rejected, state in the body that it was
            // rejected.
            // else, state it was accepted
            if ( ar.getStatus().equals( Status.REJECTED ) ) {
                body = "Dear " + firstName
                        + ", \n\nThis email is to notify you that your appointment has been declined by your healthcare provider. \n";
                body += "--iTrust2 Staff";
            }
            else {
                body = "Dear " + firstName
                        + ", \n\nThis email is to notify you that your appointment has been approved by your healthcare provider.  \n";
                body += "--iTrust2 Staff";
            }
            EmailUtil.sendEmail( addr, "iTrust2 Appointment Request", body );
            LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_EMAIL_SENT, patientUser.getUsername(),
                    "An email regarding the status of your appointment has been sent to you." );
        }
        catch ( final Exception e ) {
            System.out.println( "Patient doesn't exist." );
        }
        LoggerUtil.log(
                aptAction ? TransactionType.APPOINTMENT_REQUEST_DENIED : TransactionType.APPOINTMENT_REQUEST_APPROVED,
                ar.getHcp().getUsername(), ar.getPatient().getUsername() );
        return "hcp/viewAppointmentRequestsResult";
    }

    /**
     * View Appointments will retrieve and display all appointments for the
     * logged-in HCP that are in "approved" status
     *
     * @param model
     *            The data from the front-end to display
     * @return The page to display for the user
     */
    @GetMapping ( "/hcp/viewAppointments" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String upcomingAppointments ( final Model model ) {
        final List<AppointmentRequest> appointment = AppointmentRequest
                .getAppointmentRequestsForHCP( SecurityContextHolder.getContext().getAuthentication().getName() )
                .stream().filter( e -> e.getStatus().equals( Status.APPROVED ) ).collect( Collectors.toList() );
        /* Log the event */
        appointment.stream().map( AppointmentRequest::getPatient )
                .forEach( e -> LoggerUtil.log( TransactionType.APPOINTMENT_REQUEST_VIEWED,
                        SecurityContextHolder.getContext().getAuthentication().getName(), e.getUsername() ) );
        final List<AppointmentRequestForm> appointments = new Vector<AppointmentRequestForm>();
        for ( final AppointmentRequest ar : appointment ) {
            appointments.add( new AppointmentRequestForm( ar ) );
        }
        model.addAttribute( "appointments", appointments );

        return "hcp/viewAppointments";
    }

}
