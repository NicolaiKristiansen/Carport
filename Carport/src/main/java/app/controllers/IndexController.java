    package app.controllers;


    import app.entities.Order;
    import app.entities.OrderItem;
    import app.entities.User;
    import app.exceptions.DatabaseException;
    import app.persistence.ConnectionPool;

    import app.persistence.OrderMapper;
    import app.services.Calculator;
    import app.services.SVG;
    import io.javalin.Javalin;
    import io.javalin.http.Context;
    import io.javalin.http.HandlerType;

    import java.util.ArrayList;
    import java.util.List;



    public class IndexController {
        private static UserController userController = new UserController();
        private static String universalStyle = "stroke:black; fill:white";
        private static String arrowStyle = "stroke:black; marker-start: url(#beginArrow); marker-end: url(#endArrow);";

        private static User user = new User("Nicolai", "password", "Customer", "+45 77 66 44 33", "vej");
        private static Order order = new Order(2, 900, 900, 1000, user);




        public static void addRoutes(Javalin app, ConnectionPool connectionPool) throws DatabaseException {
            app.get("/status", ctx -> statuspage(ctx, connectionPool));
            app.post("/status", ctx -> statuspage(ctx, connectionPool));
            app.get("/partslistevaluation", ctx -> partslistevaluation(ctx, connectionPool));
            app.post("/partslistevaluation", ctx  -> partslistevaluation(ctx, connectionPool));
            app.get("/listofquery", ctx -> listofquery(ctx, connectionPool));
            //Delete Later

           app.get("/SVG", ctx -> makeSVG(order, ctx, connectionPool));
        }

        public static void statuspage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

            //add an attribute
            User user = ctx.sessionAttribute("currentUser");

            List<Order> order = OrderMapper.getAllOrdersForUser(user, connectionPool);
            ctx.attribute("orders", order);
            ctx.render("statusPage.html");

        }

        public static void partslistevaluation(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
            //TODO: Test later
            if(ctx.method() == HandlerType.GET) {
                int id = Integer.parseInt(ctx.queryParam("OrderID"));
                Order order = OrderMapper.getOrderById(id, connectionPool);
                Calculator calculator = new Calculator(order.getCarportWidth(), order.getCarportLength(), connectionPool);
                calculator.calcCarport(order);
                List<OrderItem> parts = calculator.getOrderItems();
                ctx.attribute("parts", parts);
                ctx.attribute("order", order);
                ctx.render("partslistevaluation.html");
            } else if (ctx.method() == HandlerType.POST) {
                String price = ctx.formParam("price");
                String orderID = ctx.formParam("orderID");
                int integerPrice = Integer.parseInt(price);
                int integerOrderID = Integer.parseInt(orderID);
                int result = OrderMapper.updateOrder(integerPrice, 3, integerOrderID, connectionPool);
                if(result == 1){

                    ctx.sessionAttribute("message", "The orders total price has now been updated");
                    ctx.redirect("/listofquery");
                } else if (result == 0) {
                    ctx.sessionAttribute("message", "Something went wrong. The update was not carried out");
                    ctx.render("/partslistevaluation");

                }
            }
        }

        public static void listofquery(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
            List<Order> orders = OrderMapper.getAllOrders(connectionPool);
            ctx.attribute("customerQueryInformation", orders);
            ctx.render("listofquery.html");
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
            int totalX = order.getCarportLength();
            int totalY = order.getCarportWidth();

            int beamsAmountPerSide = beamsQuantity / 2;
            int x = 0;
            int y = 0;
            int height = 20;

            for(int side = 0; side < 2; side++){
                y = (side == 0) ? 15: totalY - 15;

                if(beamsQuantity == 2){
                    svg.addRectangle(x, y-(height/2), height, totalX, universalStyle);
                    svg.addLine(x, y, totalX, y, arrowStyle);
                    if(y == 20) {
                        svg.addLine(x + 20, y, x + 20, totalY - 20, arrowStyle);
                        svg.addText(totalX/2, y, 0, totalX + "cm");
                    }
                } else if(beamsQuantity == 4){
                    int midpoint = totalX/2;
                    svg.addRectangle(x, y-(height/2), height, midpoint, universalStyle);
                    svg.addRectangle(midpoint, y-(height/2), height, midpoint, universalStyle);

                    svg.addLine(x, y, midpoint, y, arrowStyle);
                    svg.addLine(midpoint, y, totalX, y, arrowStyle);

                    svg.addText(x + (midpoint/2), y, 0, String.valueOf(totalX/2) + "cm");
                    svg.addText(x + midpoint + (midpoint/2), y, 0, String.valueOf(totalX/2) + "cm");

                    if(y == 20) {
                        svg.addLine(x + 20, y, x + 20, totalY - 20, arrowStyle);
                        int beamToBeam = (totalY-20)-y;
                        svg.addText(x+10, (totalY-20)/2, -90, String.valueOf(beamToBeam) + "cm");
                    }
                }

            }

        }

        private static void makeRafter(Order order, Calculator calculator, SVG svg){
            int totalX = order.getCarportLength();
            int totalY = order.getCarportWidth();
            int y = 0;
            int rafterQuantity = calculator.calcRafterQuantity();
            int gapBetweenRafters = 55;
            int width = 10;

            for (int i = 0; i < rafterQuantity; i++) {
                //They all just need to start at y = 0
                int x = (i + 1) * gapBetweenRafters;
                svg.addRectangle(x-(width/2), y, totalY, width, universalStyle);
                if(i != rafterQuantity - 1){
                    svg.addLine(x, totalY/2,x+gapBetweenRafters, totalY/2, arrowStyle );
                    svg.addText(x+(gapBetweenRafters/2), totalY/2, 0, String.valueOf(gapBetweenRafters + "cm"));
                }

                svg.addLine(totalX-20, 0, totalX-20, totalY, arrowStyle);
                svg.addText(totalX-20, totalY/2, -90, String.valueOf(totalY) + "cm");
            }
        }

        private static void makePost(Order order, Calculator calculator, SVG svg) {
            int heightAndWidth = 20;
            int totalX = order.getCarportLength();
            int totalY = order.getCarportWidth();
            int postQuantity = calculator.calcPostQuantity();
            int postAmountPerSide = postQuantity / 2;

            // Fixed edge positions
            int firstX = 100;
            int lastX = totalX - 30;

            // Calculate even gap between posts
            double gap = (double) (lastX - firstX) / (postAmountPerSide - 1);

            for (int i = 0; i < 2; i++) {
                int y = (i == 0) ? 15 : totalY - 15;
                int previousX = -1;

                for (int j = 0; j < postAmountPerSide; j++) {
                    int x = (int) Math.round(firstX + (j * gap));

                    svg.addRectangle(x - (heightAndWidth / 2), y - (heightAndWidth / 2),
                            heightAndWidth, heightAndWidth, universalStyle);

                    // Optionally draw measurement between posts
                    if (previousX != -1) {
                        int distance = x - previousX;
                        svg.addLine(previousX, y + 30, x, y + 30, arrowStyle);
                        svg.addText(previousX + (distance / 2), y + 40, 0, distance + " cm");
                    }

                    previousX = x;
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
