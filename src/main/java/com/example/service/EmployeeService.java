package com.example.service;

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
}
