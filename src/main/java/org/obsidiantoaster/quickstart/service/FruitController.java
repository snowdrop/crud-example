/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.obsidiantoaster.quickstart.service;

import java.util.List;

import org.obsidiantoaster.quickstart.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/api")
public class FruitController {

	private FruitRepository repository;

	private void log(String format, Object... agrs) {
		System.out.println(String.format(format, agrs));
	}

	@Autowired
	public FruitController(FruitRepository repository) {
		this.repository = repository;
	}

	@ResponseBody
	@GetMapping("/fruits")
	public List<Fruit> list() {
		return repository.list();
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/fruits")
	public void post(@RequestBody Fruit fruit) {
		log("Saving fruit: %s", fruit.getName());
		this.repository.insert(fruit);
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping("/fruits/{id}")
	public void put(@PathVariable("id") Long id, @RequestBody Fruit fruit) {
		fruit.setId(id);
		log("Updating fruit: %s", fruit.getName());
		if (!this.repository.update(fruit)) {
			throw new NotFoundException("No such fruit.id = " + id);
		}
	}

	@DeleteMapping("/fruits/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		log("Deleting fruit: %s", id);
		if (!this.repository.delete(id)) {
			throw new NotFoundException("No such fruit.id = " + id);
		}
	}

	@ResponseBody
	@GetMapping("/fruits/{id}")
	public Fruit get(@PathVariable("id") Long id) {
		return this.repository.findById(id);
	}
}
