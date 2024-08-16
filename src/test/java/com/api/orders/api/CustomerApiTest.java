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

public class CustomerApiTest {


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
    public void getAllCustomers(){
        given()
                .get("customers")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(notNullValue());
    }

    @Test
    public void getCustomerById(){
        given()
                .get("customers/31")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(31))
                .body("firstName", equalTo("Test3"));
    }

    @Test
    public void createCustomer(){
        given()
                .body("{\n" +
                        "   \"firstName\": \"Emilio\",\n" +
                        "   \"lastName\": \"Test\",\n" +
                        "   \"email\": \"test@gmail.com\",\n" +
                        "   \"phone\": \"0180004567\"\n" +
                        "}")
                .post("customers")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(notNullValue());
    }

    @Test
    public void deleteCustomer(){
        given()
                .delete("customers/delete/33")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void updateCustomer(){
        String firstNameUpdated =  given()
                .body("{\n" +
                        "   \"firstName\": \"TEST\"\n" +
                        "}")
                .patch("customers/edit/32")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("firstName");

        assertThat(firstNameUpdated, equalTo("TEST"));
    }

}
