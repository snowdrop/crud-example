/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.openshift.booster;

import java.util.Collections;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import io.openshift.booster.service.Fruit;
import io.openshift.booster.service.FruitRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestJdbcApplicationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private FruitRepository fruitRepository;

    @Before
    public void beforeTest() {
        RestAssured.baseURI = String.format("http://localhost:%d/api/fruits", port);
        initDatabase();
    }

    @Test
    public void testGetAll() {
        when().get()
                .then()
                .statusCode(200)
                .body("id", hasItems(1, 2, 3))
                .body("name", hasItems("Cherry", "Apple", "Banana"));
    }

    @Test
    public void testGetEmptyArray() {
        fruitRepository.delete();
        when().get()
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    public void testGetOne() {
        when().get("/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Cherry"));
    }

    @Ignore
    @Test
    public void testGetNotExisting() {
        when().get("/0")
                .then()
                .statusCode(404);
    }

    @Ignore
    @Test
    public void testPost() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("name", "Lemon"))
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", not(isEmptyString()))
                .body("name", is("Lemon"));
    }

    @Ignore
    @Test
    public void testPostWithWrongPayload() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("id", 0))
                .when()
                .post()
                .then()
                .statusCode(422);
    }

    @Ignore
    @Test
    public void testPostWithNonJsonPayload() {
        given().contentType(ContentType.XML)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostWithEmptyPayload() {
        given().contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Ignore
    @Test
    public void testPut() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("name", "Lemon"))
                .when()
                .put("/1")
                .then()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Lemon"));

    }

    @Test
    public void testPutNotExisting() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("name", "Lemon"))
                .when()
                .put("/0")
                .then()
                .statusCode(404);
    }

    @Ignore
    @Test
    public void testPutWithWrongPayload() {
        given().contentType(ContentType.JSON)
                .body(Collections.singletonMap("id", 0))
                .when()
                .put()
                .then()
                .statusCode(422);
    }

    @Ignore
    @Test
    public void testPutWithNonJsonPayload() {
        given().contentType(ContentType.XML)
                .when()
                .put()
                .then()
                .statusCode(400);
    }

    @Ignore
    @Test
    public void testPutWithEmptyPayload() {
        given().contentType(ContentType.JSON)
                .when()
                .put()
                .then()
                .statusCode(400);
    }

    @Ignore
    @Test
    public void testDelete() {
        when().delete("/1")
                .then()
                .statusCode(204);
        when().get("/1")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteNotExisting() {
        when().delete("/0")
                .then()
                .statusCode(404);
    }

    private void initDatabase() {
        fruitRepository.delete();
        fruitRepository.insert(new Fruit(1, "Cherry"));
        fruitRepository.insert(new Fruit(2, "Apple"));
        fruitRepository.insert(new Fruit(3, "Banana"));
    }

}
