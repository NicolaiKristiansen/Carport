package app.util;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;

public class MailUtil {

    public boolean sendMail(String to, String email,  String password  /*, String svg*/) throws IOException {
        // Erstat xyx@gmail.com med din egen email, som er afsender
        Email from = new Email("sofus@k7c.dk");
        from.setName("Johannes Fog Byggemarked");

        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

        /* Erstat kunde@gmail.com, name, email og zip med egne værdier ****/
        /* I test-fasen - brug din egen email, så du kan modtage beskeden */
        personalization.addTo(new Email(to));
        personalization.addDynamicTemplateData("email", email);
        personalization.addDynamicTemplateData("password", password);
        //personalization.addDynamicTemplateData("SVG", svg);
        mail.addPersonalization(personalization);

        mail.addCategory("carportapp");

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try
        {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            // indsæt dit skabelonid herunder
            mail.templateId = "d-3fe651994385461a929ac79c0413817a";
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            return true;
        }
        catch (IOException ex)
        {
            System.out.println("Error sending mail");
            throw ex;
        }
    }

}
