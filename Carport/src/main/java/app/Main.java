package app;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.StatusPageMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/%s?currentSchema=public";
    private static final String DB = "Carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    public static void main(String[] args) throws DatabaseException {
        //Message to comit and update
        UserMapper userMapper = new UserMapper();

        //TODO: Delete later
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
            config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
            config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
        }).start(7070);

        // Routing
        StatusPageMapper statusPageMapper = new StatusPageMapper();
        statusPageMapper.addRoutes(app);
        //dddd
        //Comments are nice qggg
        userMapper.insertUser(new User("mail1", "1234", "Customer"), connectionPool);


        System.out.println(userMapper.getAllUsers(connectionPool));

    }
}