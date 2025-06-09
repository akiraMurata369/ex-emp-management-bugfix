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
	 * システム内で例外発生を行うメソッド.
	 * ここで発生した例外はGlobalExceptionHandlerが捕獲し処理をします
	 * 
	 * @throws ArithmeticException このメソッドは必ずArithmeticExceptionを発生します
	 */
	@GetMapping("/exception")
	public String throwsException() {
		// 0で除算、非検査例外であるArithmeticExceptionが発生！
		System.out.println("例外発生前");
		System.out.println(10 / 0); // ←このタイミングでGlobalExceptionHandlerに処理が飛ぶ
		System.out.println("例外発生後");

		return "通常はここにHTML名を書くが、ここまで処理は来ない";
	}



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
