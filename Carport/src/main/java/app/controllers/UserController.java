package app.controllers;

import app.PasswordUtil;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {
    static User user;

    public static void addRoutes(Javalin app, ConnectionPool connectionPool){

    }


    public static void createAccount(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String password2 = ctx.formParam("password2");
        String hashedPassword = PasswordUtil.hashPassword(password);
        String role = "Customer";
        String phone = ctx.formParam("phone");
        String address = ctx.formParam("address");
        User user = new User(email, hashedPassword, role, phone, address);

        if (password.equals(password2)) {
            try {
                UserMapper.insertUser(user, connectionPool);
                ctx.attribute("message", "Du er hermed oprettet med email " + email + "." + " Nu skal du logge på.");
                ctx.render("loginpage.html");
            } catch (DatabaseException e) {
                ctx.attribute("message", "Dit brugernavn findes allerede, Prøv igen, eller log ind");
                ctx.render("createaccountpage.html");
            }

        } else {
            ctx.attribute("message", "Dine to passwords matcher ikke! Prøv igen");
            ctx.render("createaccountpage.html");
        }

    }

    public static void login(Context ctx, ConnectionPool connectionPool) {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            switch (user.getRole()) {
                case "Customer" -> ctx.redirect("frontpage.html");
                case "Seller" -> ctx.render("listofquery.html");
                case "Admin" -> ctx.render("empty.html");
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.render("loginpage.html");
        }
    }

    public static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }
}
