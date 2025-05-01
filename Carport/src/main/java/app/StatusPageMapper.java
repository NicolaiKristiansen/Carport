package app;

import io.javalin.Javalin;
import org.postgresql.jdbc2.optional.ConnectionPool;
import io.javalin.http.Context;


import java.util.ArrayList;
import java.util.List;

public class StatusPageMapper {



    public static void addRoutes(Javalin app){
        app.get("/", ctx -> statuspage(ctx));
        app.post("/", ctx -> statuspage(ctx));
    }

    //TODO: Replace with function to get data from database
    public static ArrayList<CustomerOrders> makeOrders(){
        ArrayList<CustomerOrders> customerOrders = new ArrayList<>();
        for(int i = 0; i <= 5; i++){
            double length = 5.3;
            double width = 5;
            int id = i;
            int price = 1000;
            int status = 4;
            CustomerOrders customerOrder = new CustomerOrders(length, width, id, price, status);
            customerOrders.add(customerOrder);
        }
        return customerOrders;
    }

    public static void statuspage(Context ctx){
        List<CustomerOrders> orders = makeOrders();
        System.out.println(orders);
        ctx.attribute("orders", orders);
        ctx.render("status page.html");

    }

    public static void updateOrderToBought(Context ctx){

    }


}
