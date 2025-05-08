    package app.controllers;

    import app.persistence.ConnectionPool;
    import app.persistence.StatusPageMapper;
    import app.services.SVG;
    import io.javalin.Javalin;
    import io.javalin.http.Context;

    import static app.persistence.StatusPageMapper.statuspage;

    public class IndexController {
        private static UserController userController = new UserController();

        private static String universalStyle = "stroke:black; fill:white";



        public static void addRoutes(Javalin app, ConnectionPool connectionPool){
            app.get("/", ctx -> ctx.render("frontPage.html"));
            app.get("/login", ctx -> ctx.render("loginPage.html"));
            app.post("/login", ctx -> userController.login(ctx, connectionPool));
            app.get("/createAccount", ctx -> ctx.render("createAccountPage.html"));
            app.post("/createAccount", ctx -> userController.createAccount(ctx, connectionPool));
            app.get("/status", ctx -> statuspage(ctx));
            app.post("/status", ctx -> statuspage(ctx));
            //Delete Later
            app.get("/SVG", ctx -> SVGLearning(ctx));
        }

        public static void SVGLearning(Context ctx){
            SVG svg = new SVG(0,0,"0,0,850 600", "1000", "900");
            svg.addText(20, 300, -90, "400cm" );
            svg.addText(430, 750, 0, "300cm" );
            ctx.attribute("sketch", svg);
            ctx.render("SVGLearning.html");
        }
    }
