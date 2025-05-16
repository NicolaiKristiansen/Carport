package app.util;

import app.entities.OrderItem;

import app.services.SVG;
import app.services.SVGConverter;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.bouncycastle.util.encoders.Base64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class MailUtil {

    public boolean sendMail(String to, String email, String password, SVG svg, List<OrderItem> partlist) throws IOException {
        Email from = new Email("sofus@k7c.dk");
        from.setName("Johannes Fog Byggemarked");

        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

       String partlistAsString = partlist.toString();



        personalization.addTo(new Email(to));
        personalization.addDynamicTemplateData("email", email);
        personalization.addDynamicTemplateData("password", password);
        personalization.addDynamicTemplateData("partslist", partlistAsString);
        mail.addPersonalization(personalization);

        mail.addCategory("carportapp");

        try{
            String svgString = svg.toString();
            System.out.println(svgString);
            ByteArrayOutputStream pngStream = SVGConverter.convertSvgToPng(svgString);
            String base64Image = Base64.getEncoder().encodeToString(pngStream.toByteArray());

            Attachments attachments = new Attachments();
            attachments.setContent(base64Image);
            attachments.setType("image/png");
            attachments.setFilename("carportImage.png");
            attachments.setDisposition("attachment");
            mail.addAttachments(attachments);

        } catch (Exception e){
            System.out.println("Error when trying to convert the SVG to PNG: " + e.getMessage() );
            return false;
        }

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try
        {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

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