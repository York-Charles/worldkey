package com.worldkey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManageController {
	@RequestMapping({"/","/manage"})
	public String manage() {
		return "manage";
	}
	@RequestMapping("manage/welcome")
	public String welcome() {
		return "welcome";
	}
	@RequestMapping("manage/action")
	public String action(String msg,Model model) {
		model.addAttribute("msg", msg);
		return "manage/action";
	}
}
