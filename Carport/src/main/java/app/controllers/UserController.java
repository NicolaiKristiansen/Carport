package app.controllers;

import app.PasswordUtil;
import app.entities.Order;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.OrderMapper;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {
    static User user;

    public static void addRoutes(Javalin app, ConnectionPool connectionPool){
        app.get("/", ctx -> ctx.render("frontpage.html"));
        app.post("/frontPage", ctx -> ctx.render("frontpage.html"));
        app.get("/login", ctx -> ctx.render("loginpage.html"));
        app.post("/login", ctx -> login(ctx, connectionPool));
        app.get("/createaccount", ctx -> ctx.render("createaccountpage.html"));
        app.post("/createaccount", ctx -> createAccount(ctx, connectionPool));
        app.get("/createquery", ctx -> ctx.render("createquery.html"));
        app.post("/frontPageHandle", ctx -> handleQuerySubmission(ctx, connectionPool));
        app.get("/logout", ctx -> logout(ctx));
    }


    public static void createAccount(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String password2 = ctx.formParam("password2");
        String role = "Customer";
        String phone = ctx.formParam("phone");
        String address = ctx.formParam("address");
        User user = new User(email, password, role, phone, address);

        if (password.equals(password2)) {
            try {
                String hashedPassword = PasswordUtil.hashPassword(password);
                User userHashedPassword = new User(email, hashedPassword, role, phone, address);
                UserMapper.insertUser(userHashedPassword, connectionPool);

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
            User user = UserMapper.login(email, password, connectionPool);
            ctx.sessionAttribute("currentUser", user);
            switch (user.getRole()) {
                case "Customer" -> ctx.redirect("/");
                case "Seller" -> ctx.redirect("/listofquery");
                case "Admin" -> ctx.render("empty.html");
                case "default" -> ctx.render("/login");
            }
        } catch (DatabaseException e) {
            ctx.attribute("message", e.getMessage());
            ctx.redirect("/login");
        }
    }

    public static void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/");
    }

    public static void handleQuerySubmission(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        // Check if the user is logged in
        User user = ctx.sessionAttribute("currentUser");
        if (user == null) {
            ctx.attribute("message", "Du skal være logget ind for at indsende en forespørgsel!");
            ctx.render("loginpage.html");
            return;
        }

        // Retrieve form parameters
        int length = Integer.parseInt(ctx.formParam("length"));
        int width = Integer.parseInt(ctx.formParam("width"));

        // Save data (store it in session or database as needed)
        ctx.sessionAttribute("queryLength", length);
        ctx.sessionAttribute("queryWidth", width);

        Order order = new Order(1, length, width, 0, user);

        OrderMapper.insertOrder(order, connectionPool);

        // Redirect or render confirmation page
        ctx.render("frontpage.html");
    }
}
