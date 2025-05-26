package app.services;


import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
class CalculatorTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("password");
    private static final String URL = "jdbc:postgresql://" + System.getenv("ip") + ":5432/%s?currentSchema=public";
    private static final String DB = "Carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);

    Calculator calculator = new Calculator(600, 780, connectionPool);

    @BeforeAll
    static void setUp() {

    }

    @Test
    void calcPostQuantity() {
        int expected = 6;
        int actual = calculator.calcPostQuantity();
        assertEquals(expected, actual);
    }

    @Test
    void calcBeamQuantity() {
        int expected = 4;
        int actual = calculator.calcBeamQuantity();
        assertEquals(expected, actual);
    }


    @Test
    void calcRafterQuantity() {
        int expected = 13;
        int actual = calculator.calcRafterQuantity();
        assertEquals(expected, actual);
    }

    @Test
    void calcPostPrice() throws DatabaseException {
        int expected = 8580;
        int actual = calculator.calcPostPrice();
        assertEquals(expected, actual);
    }

    @Test
    void calcBeamPrice()  {
        int expected = 4800;
        int actual = calculator.calcBeamPrice();
        assertEquals(expected, actual);
    }

    @Test
    void calcRafterPrice()  {
        int expected = 15600;
        int actual = calculator.calcRafterPrice();
        assertEquals(expected, actual);
    }
}