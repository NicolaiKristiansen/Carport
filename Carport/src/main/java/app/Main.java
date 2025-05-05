package app;

import app.controllers.IndexController;
import app.persistence.ConnectionPool;
import app.persistence.StatusPageMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;

import java.util.logging.Logger;

public class Main {
    public static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "postgres";
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

   public static void main(String[] args) {
    //TODO: Delete ConnectionPool

        //TODO: Delete later
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);


        // Routing

        IndexController indexController = new IndexController();
        indexController.addRoutes(app, connectionPool);
    }
}