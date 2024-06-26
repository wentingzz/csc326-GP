package edu.ncsu.csc.itrust2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Class for sending email. Used for the Password Reset emails.
 *
 * @author Kai Presler-Marshall
 *
 */
public class EmailUtil {

    /**
     * Send an email from the email account in the system's `email.properties`
     * file
     *
     * @param addr
     *            Address to send to
     * @param subject
     *            Subject of the email
     * @param body
     *            Body of the message to send
     * @throws MessagingException
     *             Results from JavaX Mail functionality
     */
    public static void sendEmail ( final String addr, final String subject, final String body )
            throws MessagingException {

        InputStream input = null;

        final String to = addr;
        final String from;
        final String username;
        final String password;
        final String host;

        final Properties properties = new Properties();
        final String filename = "src/main/java/email.properties";
        final File emailFile = new File( filename );
        try {
            input = new FileInputStream( emailFile );
        }
        catch ( final FileNotFoundException e1 ) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if ( null != input ) {
            try {
                properties.load( input );
            }
            catch ( final IOException e ) {
                e.printStackTrace();
            }
        }
        from = properties.getProperty( "from" );
        username = properties.getProperty( "username" );
        password = properties.getProperty( "password" );
        host = properties.getProperty( "host" );

        /*
         * Source for java mail code:
         * https://www.tutorialspoint.com/javamail_api/
         * javamail_api_gmail_smtp_server.htm
         */

        final Properties props = new Properties();
        props.put( "mail.smtp.auth", "true" );
        props.put( "mail.smtp.starttls.enable", "true" );
        props.put( "mail.smtp.host", host );
        props.put( "mail.smtp.port", "587" );

        final Session session = Session.getInstance( props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication () {
                return new PasswordAuthentication( username, password );
            }
        } );

        try {
            final Message message = new MimeMessage( session );
            message.setFrom( new InternetAddress( from ) );
            message.setRecipients( Message.RecipientType.TO, InternetAddress.parse( to ) );
            message.setSubject( subject );
            message.setText( body );
            Transport.send( message );
        }
        catch ( final MessagingException e ) {
            e.printStackTrace();
            throw e;
        }
    }

}
