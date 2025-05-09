package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;

import app.persistence.UserMapper;
import io.javalin.http.Context;

public class UserController {
        UserMapper userMapper = new UserMapper();

    public void createAccount(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        String role = "Customer";
        String phone = ctx.formParam("phone");
        String address = ctx.formParam("address");
        User user = new User(email, password, role, phone, address);
        userMapper.insertUser(user, connectionPool);
        ctx.render("loginPage.html");
    }

    public void login(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");
        userMapper.login(email, password, connectionPool);
    }
}
