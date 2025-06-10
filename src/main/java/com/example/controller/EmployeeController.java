package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.domain.Employee;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@GetMapping("/showList")
	public String showList(@RequestParam(defaultValue = "1") int page, Model model) {
		int pageSize = 10;
		Page<Employee> employeePage = employeeService.getEmployeePage(page - 1, pageSize);

		model.addAttribute("employeeList", employeePage.getContent());
		model.addAttribute("employeePage", employeePage);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", employeePage.getTotalPages());

		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@GetMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@PostMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}


	/**
	 * 従業員情報を検索する.
	 *
	 * @param employeeName 名前
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@PostMapping("/search")
	public String search(String employeeName, Model model) {
		List<Employee> employeeList = employeeService.searchByLikeName(employeeName, model);

		if (employeeList.isEmpty()) {
			// 検索結果が0件なら全件取得
			model.addAttribute("emptyEmployeeMessage", "検索条件がありません。全件表示します。");
			employeeList = employeeService.showList();
		}
		
		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}


	/**
	 * 従業員名のオートコンプリート用に部分一致検索をする.
	 *
	 * @param term 検索文字列（クエリパラメータ）
	 * @return 検索結果の従業員名リスト（JSON）
	 */
	@ResponseBody
	@GetMapping("/autocomplete")
	public List<String> autocomplete(@RequestParam String term) {
		int limit = 10;
		return employeeService.searchEmployeeNames(term);
	}
}
