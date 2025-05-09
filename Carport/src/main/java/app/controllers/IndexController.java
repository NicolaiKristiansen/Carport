    package app.controllers;


    import app.entities.Order;
    import app.entities.User;
    import app.persistence.ConnectionPool;

    import app.services.Calculator;
    import app.services.SVG;
    import io.javalin.Javalin;
    import io.javalin.http.Context;

    import java.util.ArrayList;
    import java.util.List;



    public class IndexController {
        private static UserController userController = new UserController();
//comment
        private static String universalStyle = "stroke:black; fill:white";
        private static String arrowStyle = "stroke:black; marker-start: url(#beginArrow); marker-end: url(#endArrow);";
        //Delete later
        private static User user = new User("Nicolai", "Password", "customer", "+45 66 66 66 66", "Buckingham Palace 5");
        private static Order order = new Order(1, 900, 700, 1000, user);



        public static void addRoutes(Javalin app, ConnectionPool connectionPool){
            app.get("/", ctx -> ctx.render("customer/frontPage.html"));
            app.get("/login", ctx -> ctx.render("loginPage.html"));
            app.post("/login", ctx -> userController.login(ctx, connectionPool));
            app.get("/createAccount", ctx -> ctx.render("createAccountPage.html"));
            app.post("/createAccount", ctx -> userController.createAccount(ctx, connectionPool));
            app.get("/status", ctx -> statuspage(ctx));
            app.post("/status", ctx -> statuspage(ctx));
            //Delete Later
            app.get("/SVG", ctx -> makeSVG(order, ctx, connectionPool));
        }

        public static void statuspage(Context ctx){

            //add an attribute
            ctx.render("statusPage.html");

        }

        public static void makeSVG(Order order, Context ctx, ConnectionPool connectionPool){
            Calculator calculator = new Calculator(order.getCarportWidth(), order.getCarportLength(), connectionPool);
            SVG svg = new SVG(0, 0, "0 0 900 900", "900", "900");


            makeRafter(order, calculator, svg);
            makeBeams(order, calculator, svg);
            makePost(order, calculator, svg);




            ctx.attribute("sketch", svg);
            ctx.render("SVGLearning.html");

        }

        private static void makeBeams(Order order, Calculator calculator, SVG svg){
            int beamsQuantity = calculator.calcBeamQuantity();
            int totalY = order.getCarportLength();
            int totalX = order.getCarportWidth();

            int beamsAmountPerSide = beamsQuantity / 2;
            int x = 0;
            int y = 0;

            for(int side = 0; side < 2; side++){
                y = (side == 0) ? 20: totalY - 20;

                if(beamsQuantity == 2){
                    svg.addRectangle(x, y-15, 30, totalX, universalStyle);
                    svg.addLine(x, y, totalX, y, arrowStyle);
                    if(y == 20) {
                        //Line not appearing
                        svg.addLine(x + 20, y, x + 20, totalY - 20, arrowStyle);

                    }
                } else if(beamsQuantity == 4){
                    int midpoint = totalX/2;
                    svg.addRectangle(x, y-15, 30, midpoint, universalStyle);
                    svg.addRectangle(midpoint, y-15, 30, midpoint, universalStyle);

                    svg.addLine(x, y, midpoint, y, arrowStyle);
                    svg.addLine(midpoint, y, totalX, y, arrowStyle);

                    svg.addText(x + (midpoint/2), y-10, 0, String.valueOf(totalX/2) + "cm");
                    svg.addText(x + midpoint + (midpoint/2), y-10, 0, String.valueOf(totalX/2) + "cm");

                    if(y == 20) {
                        svg.addLine(x + 20, y, x + 20, totalY - 20, arrowStyle);
                        int beamToBeam = (totalY-20)-y;
                        svg.addText(x+10, (totalY-20)/2, -90, String.valueOf(beamToBeam) + "cm");
                    }
                }

            }

        }

        private static void makeRafter(Order order, Calculator calculator, SVG svg){
            int totalY = order.getCarportLength();
            int totalX = order.getCarportWidth();
            int y = 0;
            int rafterQuantity = calculator.calcRafterQuantity();
            int gapBetweenRafters = totalX/(rafterQuantity + 2);

            for (int i = 0; i < rafterQuantity; i++) {
                //They all just need to start at y = 0
                int x = (i + 1) * gapBetweenRafters;
                svg.addRectangle(x-10, y, totalY, 20, universalStyle);
                svg.addLine(x, totalY/2,x+gapBetweenRafters, totalY/2, arrowStyle );
                svg.addText(x+(gapBetweenRafters/2), totalY/2, 0, String.valueOf(gapBetweenRafters + "cm"));
                svg.addLine(totalX-20, 0, totalX-20, totalY, arrowStyle);
                svg.addText(totalX-20, totalY/2, -90, String.valueOf(totalY) + "cm");
            }
        }

        private static void makePost(Order order, Calculator calculator, SVG svg){
            int totalY = order.getCarportLength();
            int totalX = order.getCarportWidth();
            int y = 0;
            int postQuantity = calculator.calcPostQuantity();
            int postAmountPerSide = postQuantity / 2;
            int gapBetween = totalX/(postAmountPerSide + 2);
            for (int i = 0; i < 2; i++) {
                //Shorter version of an if statment. Short Hand if... else (https://www.w3schools.com/java/java_conditions_shorthand.asp)
                y = (i == 0) ? 20: totalY - 20;
                for (int j = 0; j < postAmountPerSide; j++) {

                    int x = (j + 1) * gapBetween;
                    svg.addRectangle(x-20, y-20, 40, 40, universalStyle);

                    svg.addLine(x, y+20, x+gapBetween, y+20, arrowStyle);
                    svg.addText(x + (gapBetween / 2), y + 20, 0, String.valueOf(gapBetween) + "cm");

                }
            }
        }

        public static void SVGLearning(Context ctx){
            SVG svg = new SVG(0,0,"0,0,850 600", "1000", "900");
            svg.addText(20, 300, -90, "400cm" );
            svg.addText(430, 750, 0, "300cm" );
            ctx.attribute("sketch", svg);
            ctx.render("SVGLearning.html");
        }
    }
