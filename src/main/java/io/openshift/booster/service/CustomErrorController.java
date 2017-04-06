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
package io.openshift.booster.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.openshift.booster.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/error")
public class CustomErrorController extends AbstractErrorController {

	private ErrorAttributes errorAttributes;

	@Autowired
	public CustomErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes);
		this.errorAttributes = errorAttributes;
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

	@RequestMapping
	@ResponseBody
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> errorAttributes = getErrorAttributes(request, false);
		HttpStatus status = getStatus(request);

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("error", errorAttributes.get("message"));
		Integer code = code(request);
		body.put("code", (code != null ? code : status.value()));

		return new ResponseEntity<>(body, status);
	}

	private Integer code(HttpServletRequest request) {
		RequestAttributes requestAttributes = new ServletRequestAttributes(request);
		Throwable ex = errorAttributes.getError(requestAttributes);
		if (ex != null) {
			ErrorCode code = AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ErrorCode.class);
			return (code != null ? code.value() : null);
		} else {
			return -1; // system / unknown error?
		}
	}
}
