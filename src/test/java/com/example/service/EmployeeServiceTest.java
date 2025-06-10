
package com.example.service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @BeforeAll
    static void beforeAll() {
    }

    @AfterAll
    static void afterAll() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    // モック(偽物)のリポジトリを作成
    @Mock
    private EmployeeRepository employeeRepository;

    // モックリポジトリを注入したサービスを作成
    @InjectMocks
    private EmployeeService employeeService;


    @Test
    void testShowList() {
        // モックの戻り値設定
        Employee employee1 = new Employee();
        employee1.setId(1);
        employee1.setName("山田太郎");
        employee1.setImage("e1.png");
        employee1.setGender("男性");
        employee1.setHireDate(new Date(110, 0, 15)); // 2010年1月15日
        employee1.setMailAddress("yamada.taro@example.com");
        employee1.setZipCode("100-0001");
        employee1.setAddress("東京都XXXXXXXX");
        employee1.setTelephone("123-4567-8912");
        employee1.setSalary(500000);
        employee1.setCharacteristics("責任感が強い");
        employee1.setDependentsCount(2);

        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setName("山田花子");
        employee2.setImage("e2.png");
        employee2.setGender("女性");
        employee1.setHireDate(new Date(111, 6, 30)); // 2011年5月30日
        employee2.setMailAddress("yamada.hanako@example.com");
        employee2.setZipCode("100-0002");
        employee2.setAddress("東京都YYYYYYYY");
        employee2.setTelephone("123-4567-8913");
        employee2.setSalary(480000);
        employee2.setCharacteristics("明るく社交的");
        employee2.setDependentsCount(1);

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

        // 実行
        List<Employee> result = employeeService.showList();

        // 検証
        assertEquals(2, result.size());

        // 1人目の検証
        Employee e1 = result.get(0);
        assertEquals(1, e1.getId());
        assertEquals("山田太郎", e1.getName());
        assertEquals("e1.png", e1.getImage());
        assertEquals("男性", e1.getGender());
        assertEquals(new Date(110, 0, 15), e1.getHireDate());
        assertEquals("yamada.taro@example.com", e1.getMailAddress());
        assertEquals("100-0001", e1.getZipCode());
        assertEquals("東京都XXXXXXXX", e1.getAddress());
        assertEquals("123-4567-8912", e1.getTelephone());
        assertEquals(500000, e1.getSalary());
        assertEquals("責任感が強い", e1.getCharacteristics());
        assertEquals(2, e1.getDependentsCount());

        // 2人目の検証
        Employee e2 = result.get(1);
        assertEquals(2, e2.getId());
        assertEquals("山田花子", e2.getName());
        assertEquals("e2.png", e2.getImage());
        assertEquals("女性", e2.getGender());
        assertEquals(new Date(111, 0, 15), e2.getHireDate());
        assertEquals("yamada.hanako@example.com", e2.getMailAddress());
        assertEquals("100-0002", e2.getZipCode());
        assertEquals("東京都YYYYYYYY", e2.getAddress());
        assertEquals("123-4567-8913", e2.getTelephone());
        assertEquals(480000, e2.getSalary());
        assertEquals("明るく社交的", e2.getCharacteristics());
        assertEquals(1, e2.getDependentsCount());

        verify(employeeRepository, times(1)).findAll();
    }


    @Test
    void testShowDetail() {
        // モックの戻り値設定
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("山田太郎");
        employee.setImage("e1.png");
        employee.setGender("男性");
        employee.setHireDate(new Date(110, 0, 15)); // 2010年1月15日
        employee.setMailAddress("yamada.taro@example.com");
        employee.setZipCode("100-0001");
        employee.setAddress("東京都XXXXXXXX");
        employee.setTelephone("123-4567-8912");
        employee.setSalary(500000);
        employee.setCharacteristics("責任感が強い");
        employee.setDependentsCount(2);

        when(employeeRepository.load(1)).thenReturn(employee);

        // 実行
        Employee result = employeeService.showDetail(1);

        // 検証
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("山田太郎", result.getName());
        assertEquals("e1.png", result.getImage());
        assertEquals("男性", result.getGender());
        assertEquals(new Date(110, 0, 15), result.getHireDate());
        assertEquals("yamada.taro@example.com", result.getMailAddress());
        assertEquals("100-0001", result.getZipCode());
        assertEquals("東京都XXXXXXXX", result.getAddress());
        assertEquals("123-4567-8912", result.getTelephone());
        assertEquals(500000, result.getSalary());
        assertEquals("責任感が強い", result.getCharacteristics());
        assertEquals(2, result.getDependentsCount());

        verify(employeeRepository, times(1)).load(1);
    }

}
