/*
 * Copyright 2021 Red Hat, Inc, and individual contributors.
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

import java.util.Collections;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public abstract class AbstractApplicationTest {

    protected static final String FRUITS_PATH = "/api/fruits";

    private static final String INDEX_HTML = "index.html";

    @Test
    public void testGetIndex() {
        given()
                .baseUri(baseURI())
                .get(INDEX_HTML)
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testPostGetAndDelete() {
        Integer id =
                given()
                        .baseUri(baseURI())
                        .contentType(ContentType.JSON)
                        .body(Collections.singletonMap("name", "Lemon"))
                        .post(FRUITS_PATH)
                        .then()
                        .statusCode(201)
                        .body("id", is(not(emptyString())))
                        .body("name", is("Lemon"))
                        .extract()
                        .response()
                        .path("id");

        given()
                .baseUri(baseURI())
                .get(String.format("%s/%d",FRUITS_PATH, id))
                .then()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("Lemon"));

        given()
                .baseUri(baseURI())
                .delete(String.format("%s/%d",FRUITS_PATH, id))
                .then()
                .statusCode(204);
    }

    protected abstract String baseURI();
}
