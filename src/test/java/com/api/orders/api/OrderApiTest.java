package com.api.orders.api;

import com.api.orders.model.Order;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderApiTest {


    @Before
    public void setup(){
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/api/v1";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build();
    }


    @Test
    public void getAllOrders(){
        given()
            .get("orders")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body(notNullValue());
    }

    @Test
    public void getOrderById(){
        given()
            .get("orders/1")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(1));
    }

    @Test
    public void saveOrder(){
        given()
            .body("{\n" +
                    "   \"orderDate\": \"2022-05-30\",\n" +
                    "   \"totalAmount\": \"25000\",\n" +
                    "   \"customerId\": \"1\"\n" +
                        "}")
            .post("orders")
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body(notNullValue());
    }

    @Test
    public void deleteOrder(){
        given()
                .delete("orders/delete/20")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void updateOrder(){
        BigDecimal totalAmountUpdated =  given()
                .body("{\n" +
                        "   \"totalAmount\": \"33000\"\n" +
                        "}")
                .put("orders/edit/21")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getObject("totalAmount", BigDecimal.class);

        assertThat(totalAmountUpdated, equalTo(BigDecimal.valueOf(33000)));
    }

    @Test
    public void getOrderByCustomerId(){
        given()
                .get("orders/customer/1")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue());
    }
}
