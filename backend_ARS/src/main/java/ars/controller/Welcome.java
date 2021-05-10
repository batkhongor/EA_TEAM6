package ars.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author A
 *
 */
@RestController
// @RestController This also can be used instead of @Controller so that we don't
// have to user @ResponseBody annotation.

public class Welcome {
	
	@RequestMapping("/")
	public String welcome() {
		return "Welcome to Page";
	}
}
