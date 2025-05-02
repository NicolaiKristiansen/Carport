package app.controllers;

import app.persistence.ConnectionPool;
import app.persistence.StatusPageMapper;
import io.javalin.Javalin;

import static app.persistence.StatusPageMapper.statuspage;

public class IndexController {
    StatusPageMapper statusPageMapper = new StatusPageMapper();


    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.get("/", ctx -> ctx.render("frontPage.html"));
        app.get("/login", ctx -> ctx.render("loginPage.html"));
        app.get("/createAccount", ctx -> ctx.render("createAccountPage.html"));
        app.get("/status", ctx -> statuspage(ctx));
        app.post("/status", ctx -> statuspage(ctx));
    }
}
