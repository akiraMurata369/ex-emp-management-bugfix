package com.example.controller;

import java.io.IOException;
import java.util.List;

import com.example.domain.Administrator;
import com.example.form.InsertEmployeeForm;
import com.example.service.AdministratorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private HttpSession session;

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
		// 認証済みユーザー情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mailAddress = authentication.getName(); // ログインID（メールアドレス）
		Administrator administrator = administratorService.findByMailAddress(mailAddress);
		session.setAttribute("administratorName", administrator.getName());

		// オートコンプリート候補を全部取得
		List<Employee> employeeList = employeeService.showList();
		model.addAttribute("employeeNameList", employeeList);


		// ページング処理
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
	 * 従業員登録画面を表示.
	 *
	 * @return 従業員登録画面
	 */
	@GetMapping("/toInsert")
	public String toInsert(InsertEmployeeForm form){
		return "employee/insert";
	}

	/**
	 * 従業員を登録.
	 *
	 * @param form 従業員のフォーム
	 * @param result バリデーション結果
	 * @param model モデル
	 * @return 従業員一覧画面
	 * @throws IOException 画像ファイルの読み込みに失敗した場合
	 */
	@PostMapping("/insert")
	public String insert(@Validated InsertEmployeeForm form, BindingResult result, Model model) throws IOException {
		Employee employee = new Employee();
		BeanUtils.copyProperties(form, employee);

		// imageフィールドにファイル名を設定
		String fileName = form.getImage().getOriginalFilename();
		employee.setImage(fileName);

		if (result.hasErrors()) {
			return "employee/insert";
		}
		employeeService.insert(employee, form.getImage());
		return "redirect:/employee/showList";
	}
}
