package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ロギング処理のサンプルを動かすためのコントローラ.
 */
@Controller
@RequestMapping("/")
public class LogController {

	/**
	 *  ログイン画面に遷移する.
	 *
	 * @return ログイン画面
	 */
	@GetMapping("/toLogin")
	public String toLogin() {
		return "administrator/login";
	}
}
