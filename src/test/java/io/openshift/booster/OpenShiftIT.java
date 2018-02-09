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

package io.openshift.booster;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.isEmptyString;

import java.net.URL;
import java.util.Collections;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.arquillian.cube.openshift.impl.enricher.AwaitRoute;
import org.arquillian.cube.openshift.impl.enricher.RouteURL;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class OpenShiftIT {

  @AwaitRoute(path = "/health")
  @RouteURL("${app.name}")
  private URL url;

  @Before
  public void setup() {
    RestAssured.baseURI = url + "api/fruits";
  }

  @Test
  public void testPostGetAndDelete() {
    Integer id = given()
      .contentType(ContentType.JSON)
      .body(Collections.singletonMap("name", "Lemon"))
      .when()
      .post()
      .then()
      .statusCode(201)
      .body("id", not(isEmptyString()))
      .body("name", is("Lemon"))
      .extract()
      .response()
      .path("id");

    when().get(id.toString())
      .then()
      .statusCode(200)
      .body("id", is(id))
      .body("name", is("Lemon"));

    when().delete(id.toString())
      .then()
      .statusCode(204);
  }

}

