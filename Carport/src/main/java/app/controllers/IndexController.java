package app.controllers;

import app.persistence.ConnectionPool;
import app.persistence.StatusPageMapper;
import io.javalin.Javalin;

import static app.persistence.StatusPageMapper.statuspage;

public class IndexController {
    static UserController userController = new UserController();



    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.get("/", ctx -> ctx.render("frontPage.html"));
        app.get("/login", ctx -> ctx.render("loginPage.html"));
        app.post("/login", ctx -> userController.login(ctx, connectionPool));
        app.get("/createAccount", ctx -> ctx.render("createAccountPage.html"));
        app.post("/createAccount", ctx -> userController.createAccount(ctx, connectionPool));
        app.get("/status", ctx -> statuspage(ctx));
        app.post("/status", ctx -> statuspage(ctx));
    }
}
