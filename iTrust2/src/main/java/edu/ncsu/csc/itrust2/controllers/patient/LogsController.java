package edu.ncsu.csc.itrust2.controllers.patient;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This controller enables Patients to view their log entries
 *
 * @author Natalie Landsberg
 *
 */

@Controller

public class LogsController {
    /**
     * Create a page for the patient to view all logs
     *
     * @param model
     *            data for front end
     * @return The page for the patient to view their diagnoses
     */
    @GetMapping ( value = "patient/activitylog" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public String viewLogsPatient ( final Model model ) {
        return "/patient/activitylog";
    }

    /**
     * Create a page for the admin to view all logs
     *
     * @param model
     *            data for front end
     * @return The page for the patient to view their diagnoses
     */
    @GetMapping ( value = "admin/activitylog" )
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    public String viewLogsAdmin ( final Model model ) {
        return "/admin/activitylog";
    }

    /**
     * Create a page for the patient to view all logs
     *
     * @param model
     *            data for front end
     * @return The page for the patient to view their diagnoses
     */
    @GetMapping ( value = "hcp/activitylog" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public String viewLogsHcp ( final Model model ) {
        return "/hcp/activitylog";
    }

}
