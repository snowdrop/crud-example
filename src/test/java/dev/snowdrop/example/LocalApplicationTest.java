/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.snowdrop.example;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.snowdrop.example.service.Fruit;
import dev.snowdrop.example.service.FruitRepository;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LocalApplicationTest extends AbstractApplicationTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private FruitRepository fruitRepository;

    @Override
    protected String baseURI() {
        return String.format("http://localhost:%d", port);
    }

    @BeforeEach
    public void beforeTest() {
        fruitRepository.deleteAll();
    }

    @Test
    public void testGetAll() {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        Fruit apple = fruitRepository.save(new Fruit("Apple"));
        requestSpecification()
                .get()
                .then()
                .statusCode(200)
                .body("id", hasItems(cherry.getId(), apple.getId()))
                .body("name", hasItems(cherry.getName(), apple.getName()));
    }

    @Test
    public void testGetEmptyArray() {
        requestSpecification()
                .get()
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

    @Test
    public void testGetOne() {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        requestSpecification()
                .get(String.valueOf(cherry.getId()))
                .then()
                .statusCode(200)
                .body("id", is(cherry.getId()))
                .body("name", is(cherry.getName()));
    }

    @Test
    public void testGetNotExisting() {
        requestSpecification()
                .get("0")
                .then()
                .statusCode(404);
    }

    @Test
    public void testPost() {
        requestSpecification()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("name", "Cherry"))
                .post()
                .then()
                .statusCode(201)
                .body("id", is(not(emptyString())))
                .body("name", is("Cherry"));
    }

    @Test
    public void testPostWithWrongPayload() {
        requestSpecification()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("id", 0))
                .when()
                .post()
                .then()
                .statusCode(422)
                .body("message", is("The name is required!"));
    }

    @Test
    public void testPostWithNonJsonPayload() {
        requestSpecification()
                .contentType(ContentType.XML)
                .when()
                .post()
                .then()
                .statusCode(415);
    }

    @Test
    public void testPostWithEmptyPayload() {
        requestSpecification()
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(415);
    }

    @Test
    public void testPut() {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        requestSpecification()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("name", "Lemon"))
                .when()
                .put(String.valueOf(cherry.getId()))
                .then()
                .statusCode(200)
                .body("id", is(cherry.getId()))
                .body("name", is("Lemon"));

    }

    @Test
    public void testPutNotExisting() {
        requestSpecification()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("name", "Lemon"))
                .when()
                .put("/0")
                .then()
                .statusCode(404);
    }

    @Test
    public void testPutWithWrongPayload() {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        requestSpecification()
                .contentType(ContentType.JSON)
                .body(Collections.singletonMap("id", 0))
                .when()
                .put(String.valueOf(cherry.getId()))
                .then()
                .statusCode(422);
    }

    @Test
    public void testPutWithNonJsonPayload() {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        requestSpecification()
                .contentType(ContentType.XML)
                .when()
                .put(String.valueOf(cherry.getId()))
                .then()
                .statusCode(415);
    }

    @Test
    public void testPutWithEmptyPayload() {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        requestSpecification()
                .contentType(ContentType.JSON)
                .when()
                .put(String.valueOf(cherry.getId()))
                .then()
                .statusCode(415);
    }

    @Test
    public void testDelete() {
        Fruit cherry = fruitRepository.save(new Fruit("Cherry"));
        requestSpecification()
                .delete(String.valueOf(cherry.getId()))
                .then()
                .statusCode(204);
        assertFalse(fruitRepository.existsById(cherry.getId()));
    }

    @Test
    public void testDeleteNotExisting() {
        requestSpecification()
                .delete("/0")
                .then()
                .statusCode(404);
    }

    private RequestSpecification requestSpecification() {
        return given().baseUri(baseURI() + FRUITS_PATH);
    }
}
