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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FruitRepository {

	private JdbcTemplate jdbcTemplate;

	private RowMapper<Fruit> rowMapper = (rs, rowNum) -> new Fruit(rs.getInt("id"), rs.getString("fname"));

	@Autowired
	public FruitRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Fruit findById(long id) {
		return jdbcTemplate.queryForObject("SELECT * FROM fruit WHERE id = " + id, rowMapper);
	}

	public List<Fruit> list() {
    	return jdbcTemplate.query("SELECT * FROM fruit", rowMapper);
	}

	public void insert(Fruit fruit) {
		jdbcTemplate.update("INSERT INTO fruit (fname) VALUES (?)", fruit.getName());
	}

	public boolean update(Fruit fruit) {
		int update = jdbcTemplate.update("UPDATE fruit SET fname = ? WHERE id = ? ", fruit.getName(), fruit.getId());
		return (update > 0);
	}

	public boolean delete(long id) {
		int update = jdbcTemplate.update("DELETE FROM fruit WHERE id = " + id);
		return (update > 0);
	}
}
