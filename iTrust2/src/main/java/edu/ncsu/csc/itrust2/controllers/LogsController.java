package edu.ncsu.csc.itrust2.controllers;

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
     * Create a page for the user to view all logs
     *
     * @param model
     *            data for front end
     * @return The page for the patient to view their diagnoses
     */
    @GetMapping ( value = "/activitylog" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT') or hasRole('ROLE_ADMIN') or hasRole('ROLE_HCP')" )
    public String viewLogsPatient ( final Model model ) {
        return "/activitylog";
    }

}
