package app.services;

import app.persistence.ConnectionPool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CalculatorTest {

    private static final String USER = "postgres";
    private static final String PASSWORD = System.getenv("password");
    private static final String URL = "jdbc:postgresql://" + System.getenv("ip") + ":5432/%s?currentSchema=public";
    private static final String DB = "Carport";

    private static final ConnectionPool connectionPool = ConnectionPool.getInstance(USER, PASSWORD, URL, DB);


    @BeforeAll
    static void setUp() {

    }

    //grænseværdianalyse
    @Test
    void calcPostQuantity() {
        Calculator calculator = new Calculator(600, 780, connectionPool);
        int expected = 6;
        int actual = calculator.calcPostQuantity();
        assertEquals(expected, actual);
    }

    @Test
    void calcBeamQuantity() {
        Calculator calculator = new Calculator(600, 780, connectionPool);
        int expected = 4;
        int actual = calculator.calcBeamQuantity();
        assertEquals(expected, actual);
    }


    @Test
    void calcRafterQuantity() {
        Calculator calculator = new Calculator(600, 780, connectionPool);
        int expected = 13;
        int actual = calculator.calcRafterQuantity();
        assertEquals(expected, actual);
    }
}