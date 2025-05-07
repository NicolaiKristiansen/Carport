package app.controllers;

import app.entities.Order;
import app.entities.OrderItem;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.OrderMapper;
import app.services.Calculator;
import app.services.CarportSVG;
import app.services.SVG;
import io.javalin.http.Context;

import java.util.List;
import java.util.Locale;

public class OrderController {

    private static void sendRequest(Context ctx, ConnectionPool connectionPool) {
        Order order = new Order(0, status, width, length, totalprice, user);

        //TODO: Insert order in database

        try {
            order = OrderMapper.insertOrder(order, connectionPool);

            //TODO: Claculate order items (Stykliste)
            Calculator calculator = new Calculator(width, length, connectionPool);
            calculator.calcCarport(order);

            //TODO: Save order items in database (stykliste)
            OrderMapper.insertOrderItem(calculator.getOrderItems(), connectionPool);

            //TODO: Create message to customer and render order / request conformation

            ctx.render("orderbekræftelse");

        } catch(DatabaseException e){
            throw new RuntimeException(e);
        }

    }

    private static void showSketch(Context ctx, ConnectionPool connectionPool) {}

    private static void showBom (Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int orderId = Integer.parseInt(ctx.pathParam("order_id"));
        try{
            List<OrderItem> orderitems = OrderMapper.getOrderItemsByOrderID(orderId, connectionPool);

            if(orderitems.size() == 0){
                ctx.render("");
                return;
            }

            OrderItem orderItem = orderitems.get(0);

            ctx.attribute("width", orderItem.getOrder().getCarportWidth());
            ctx.attribute("length", orderItem.getOrder().getCarportLength());
            ctx.attribute("orderItems", orderItem);
            ctx.render("");
        }catch(DatabaseException e){
            throw new RuntimeException(e);
        }
    }

    public void sendSketch(Context ctx, ConnectionPool connectionPool) throws DatabaseException {

        CarportSVG svg = new CarportSVG(600, 700);

        Locale.setDefault(new Locale("US"));

        String universalStyle = "stroke:black; fill:white";

        SVG carportSvg = new SVG(0, 0, "0 0 855 690", "100%", "auto");
        carportSvg.addRectangle(0,0,600,780, universalStyle);

        //This String should    be replaced with a sketch based on our calculations


        ctx.attribute("sketch", svg.toString());
        ctx.render("SVGLearning");//Where it should be sent to
    }
}
