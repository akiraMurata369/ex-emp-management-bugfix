package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.example.repository.JpaEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

/**
 * 従業員情報を操作するサービス.
 *
 * @author igamasayuki
 *
 */
@Service
@Transactional
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private JpaEmployeeRepository jpaEmployeeRepository;

	/**
	 * 従業員情報を全件取得します.
	 *
	 * @return 従業員情報一覧
	 */
	public List<Employee> showList() {
		List<Employee> employeeList = employeeRepository.findAll();
		return employeeList;
	}

	/**
	 * 従業員情報を取得します.
	 *
	 * @param id ID
	 * @return 従業員情報
	 * @throws org.springframework.dao.DataAccessException 検索されない場合は例外が発生します
	 */
	public Employee showDetail(Integer id) {
		Employee employee = employeeRepository.load(id);
		return employee;
	}

	/**
	 * 従業員情報を更新します.
	 *
	 * @param employee 更新した従業員情報
	 */
	public void update(Employee employee) {
		employeeRepository.update(employee);
	}


	/**
	 * 従業員情報をページングして取得.
	 *
	 * @param page	ページ番号
	 * @param size	サイズ
	 * @return ページングされた従業員情報
	 */
	public Page<Employee> getEmployeePage(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return jpaEmployeeRepository.findAll(pageable);
  }

  	/**
	 * 従業員情報を検索.
	 *
	 * @param name 名前
	 * @param model モデル
	 * @return 検索に一致した従業員のリスト
	 */
	public List<Employee> searchByLikeName(String name, Model model){
		if(name == null) {
			// 入力が空なら全件取得
			return employeeRepository.findAll();
		}
		List<Employee> employeeList = employeeRepository.findByName(name);
		return employeeList;
	}


	/**
	 * 従業員名を部分一致で検索し、指定件数まで取得する（オートコンプリート用）
	 *
	 * @param name  検索文字列
	 * @return 従業員名のリスト
	 */
	public List<String> searchEmployeeNames(String name) {
		return employeeRepository.findNamesByName(name);
  }
  
	/**
	 * 従業員情報を登録.
	 *
	 * @param employee 登録する従業員情報
	 */
	public synchronized void insert(Employee employee, MultipartFile imageFile) throws IOException {
		// 従業員idのmax値を取得
		int maxId = employeeRepository.findMaxId();
		employee.setId(maxId + 1);

		// 画像データをstatic/imgに保存
		Path uploadPath = Paths.get("src/main/resources/static/img/" + employee.getImage());
		Files.copy(imageFile.getInputStream(), uploadPath);
		employeeRepository.insert(employee);
	}
}
