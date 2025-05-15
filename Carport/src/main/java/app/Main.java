package app;

import app.controllers.IndexController;
import app.controllers.UserController;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;

import app.persistence.UserMapper;
import app.services.Calculator;
import app.util.MailUtil;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("password");
    private static final String URL = "jdbc:postgresql://" + System.getenv("ip") +  ":5432/%s?currentSchema=public";
    private static final String DB = "Carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) {
        //Message to commit and updateeeeee

        // mail method
        //MailUtil mailUtil = new MailUtil();
        //mailUtil.sendMail("sofus@k7c.dk", "anders@and.dk", "1234");


        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        UserController.addRoutes(app, connectionPool);
        IndexController.addRoutes(app, connectionPool);
    }
}