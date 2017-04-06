package org.obsidiantoaster.quickstart.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
