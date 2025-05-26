package app.controllers;

import app.entities.Order;
import app.entities.OrderItem;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.Calculator;
import app.services.SVG;
import app.util.MailUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HandlerType;
import java.io.IOException;
import java.util.List;

public class IndexController {
    private static String universalStyle = "stroke:black; fill:white";
    private static String arrowStyle = "stroke:black; marker-start: url(#beginArrow); marker-end: url(#endArrow);";

    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("/status", ctx -> statuspage(ctx, connectionPool));
        app.post("/status", ctx -> boughtcarport(ctx, connectionPool));
        app.get("/partslistevaluation", ctx -> partslistevaluation(ctx, connectionPool));
        app.post("/partslistevaluation", ctx -> partslistevaluation(ctx, connectionPool));
        app.get("/listofquery", ctx -> listofquery(ctx, connectionPool));
    }

    public static void statuspage(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        User user = ctx.sessionAttribute("currentUser");

        List<Order> order = OrderMapper.getAllOrdersForUser(user, connectionPool);
        ctx.attribute("orders", order);
        ctx.render("statusPage.html");
    }

    public static void boughtcarport(Context ctx, ConnectionPool connectionPool) throws IOException, DatabaseException {
        int id = Integer.parseInt(ctx.formParam("orderId"));
        System.out.println(id);
        Order order = OrderMapper.getOrderById(id, connectionPool);

        Calculator calculator = new Calculator(order.getCarportWidth(), order.getCarportLength(), connectionPool);
        calculator.calcCarport(order);
        SVG svg = new SVG(0, 0, "0 0 900 900", "900", "900");

        makeRafter(order, calculator, svg);
        makeBeams(order, calculator, svg);
        makePost(order, calculator, svg);

        order.setSvg(svg);

        String email = order.getUser().getEmail();
        String password = order.getUser().getPassword();
        SVG svgForMail = order.getSvg();


        List<OrderItem> partslist = calculator.getOrderItems();
        System.out.println("In boughtcarport: " + partslist);

        MailUtil mailUtil = new MailUtil();
        mailUtil.sendMail(email, email, svgForMail, partslist);

        OrderMapper.updateOrder(order.getTotalPrice(), 4, order.getOrderId(), connectionPool);

        ctx.render("frontpage.html");
    }

    public static void partslistevaluation(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        if (ctx.method() == HandlerType.GET) {
            int id = Integer.parseInt(ctx.queryParam("orderID"));
            Order order = OrderMapper.getOrderById(id, connectionPool);
            OrderMapper.updateOrder(order.getTotalPrice(), 2, id, connectionPool);
            Calculator calculator = new Calculator(order.getCarportWidth(), order.getCarportLength(), connectionPool);
            calculator.calcCarport(order);
            List<OrderItem> parts = calculator.getOrderItems();
            SVG svg = makeSVG(id, ctx, connectionPool);
            order.setSvg(svg);
            ctx.attribute("parts", parts);

            System.out.println(parts);

            ctx.attribute("order", order);
            ctx.render("partslistevaluation.html");
        } else if (ctx.method() == HandlerType.POST) {
            String price = ctx.formParam("price");
            String orderID = ctx.formParam("orderId");
            int integerPrice = Integer.parseInt(price);
            int integerOrderID = Integer.parseInt(orderID);
            int result = OrderMapper.updateOrder(integerPrice, 3, integerOrderID, connectionPool);
            if (result == 1) {
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

    public static SVG makeSVG(int id, Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        Order order = OrderMapper.getOrderById(id, connectionPool);
        Calculator calculator = new Calculator(order.getCarportWidth(), order.getCarportLength(), connectionPool);
        SVG svg = new SVG(0, 0, "0 0 900 900", "900", "900");

        makeRafter(order, calculator, svg);
        makeBeams(order, calculator, svg);
        makePost(order, calculator, svg);

        return svg;
    }

    private static void makeBeams(Order order, Calculator calculator, SVG svg) {
        int beamsQuantity = calculator.calcBeamQuantity();
        int totalX = order.getCarportLength();
        int totalY = order.getCarportWidth();

        int x = 0;
        int y = 0;
        int height = 20;

        for (int side = 0; side < 2; side++) {
            y = (side == 0) ? 15 : totalY - 15;

            if (beamsQuantity == 2) {
                // Draw beam and line indicating length
                svg.addRectangle(x, y - (height / 2), height, totalX, universalStyle);
                svg.addLine(x, y, totalX, y, arrowStyle);
                svg.addText(totalX / 2, y + 10, 0, totalX + " cm");  // Move text down by changing `y + 10`
            } else if (beamsQuantity == 4) {
                int midpoint = totalX / 2;
                svg.addRectangle(x, y - (height / 2), height, midpoint, universalStyle);
                svg.addRectangle(midpoint, y - (height / 2), height, midpoint, universalStyle);

                // Draw lines and labels for beams
                svg.addLine(x, y, midpoint, y, arrowStyle);
                svg.addLine(midpoint, y, totalX, y, arrowStyle);

                svg.addText(x + (midpoint / 2), y + 10, 0, (totalX / 2) + " cm");  // Move text down by changing `y + 10`
                svg.addText(x + midpoint + (midpoint / 2), y + 10, 0, (totalX / 2) + " cm");  // Move text down by changing `y + 10`
            }
        }
    }


    private static void makeRafter(Order order, Calculator calculator, SVG svg) {
        int totalX = order.getCarportLength();
        int totalY = order.getCarportWidth();
        int y = 0;
        int rafterQuantity = calculator.calcRafterQuantity();
        int gapBetweenRafters = 55;
        int width = 10;

        for (int i = 0; i < rafterQuantity; i++) {
            int x = (i + 1) * gapBetweenRafters;
            svg.addRectangle(x - (width / 2), y, totalY, width, universalStyle);
            if (i != rafterQuantity - 1) {
                svg.addLine(x, totalY / 2, x + gapBetweenRafters, totalY / 2, arrowStyle);
                svg.addText(x + (gapBetweenRafters / 2), totalY / 2, 0, (gapBetweenRafters + "cm"));
            }

            svg.addLine(totalX - 20, 0, totalX - 20, totalY, arrowStyle);
            svg.addText(totalX, totalY / 2, -90, (totalY) + "cm");
        }
    }

    private static void makePost(Order order, Calculator calculator, SVG svg) {
        int heightAndWidth = 20;
        int totalX = order.getCarportLength();
        int totalY = order.getCarportWidth();
        int postQuantity = calculator.calcPostQuantity();
        int postAmountPerSide = postQuantity / 2;

        int firstX = 100;
        int lastX = totalX - 30;

        double gap = (double) (lastX - firstX) / (postAmountPerSide - 1);

        for (int i = 0; i < 2; i++) {
            int y = (i == 0) ? 15 : totalY - 15;
            int previousX = -1;

            for (int j = 0; j < postAmountPerSide; j++) {
                int x = (int) Math.round(firstX + (j * gap));

                svg.addRectangle(x - (heightAndWidth / 2), y - (heightAndWidth / 2),
                        heightAndWidth, heightAndWidth, universalStyle);

                if (previousX != -1) {
                    int distance = x - previousX;
                    svg.addLine(previousX, y + 30, x, y + 30, arrowStyle);
                    svg.addText(previousX + (distance / 2), y + 40, 0, distance + " cm");
                }

                previousX = x;
            }
        }
    }
}
