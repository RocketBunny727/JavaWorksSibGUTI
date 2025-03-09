package com.example.WebInfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class WebInfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebInfoApplication.class, args);
	}

	@Controller
	public static class PageController {
		@RequestMapping("/")
		public ModelAndView getPage() {
			return new ModelAndView("index");
		}

		@RequestMapping("/info")
		public ModelAndView getInfoPage() {
			return new ModelAndView("info");
		}
	}
}