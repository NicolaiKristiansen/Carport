package app;

import app.persistence.StatusPageMapper;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinThymeleaf;
import app.config.SessionConfig;
import app.config.ThymeleafConfig;

public class Main {
    public static void main(String[] args) {

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
    }
}