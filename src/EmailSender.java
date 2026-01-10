import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import java.io.File;
import java.util.Properties;

/**
 * EmailSender class to send emails with attachments.
 */
public class EmailSender {

    private final String username;
    private final String password;

    public EmailSender(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void sendEmailWithAttachment(String to, String subject, String body, File file) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);

        // Body part
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);

        // Attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(file.getName());

        // Combine
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        Transport.send(message);
    }
}
