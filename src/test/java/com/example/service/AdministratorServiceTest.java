package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AdministratorServiceTest {

    // モック(偽物)のリポジトリを作成
    @Mock
    private AdministratorRepository administratorRepository;

    @Mock // パスワードエンコーダーのモック
    private PasswordEncoder passwordEncoder;

    // モックリポジトリを注入したサービスを作成
    @InjectMocks
    private AdministratorService administratorService;



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


    /**
     * 管理者登録.
     */
    @Test
    void testInsert() {
        Administrator administrator = new Administrator();
        administrator.setName("山田太郎");
        administrator.setMailAddress("yamada@example.com");
        administrator.setPassword("password");

        // パスワードエンコーダの振る舞いを定義 : パスワードをハッシュ化する
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // 実行
        administratorService.insert(administrator);

        // Assert: repositoryのinsert()が正しく1回呼ばれたか確認
        verify(administratorRepository, times(1)).insert(administrator);

        // パスワードがハッシュされたことを確認
        assertEquals("hashedPassword", administrator.getPassword());
    }



    /**
     * ログイン(成功時).
     */
    @Test
    void testLogin_Success() {
        String mailAddress = "success@example.com";
        String rawPassword = "successPassword";
        String hashedPassword = "hashedPassword";

        Administrator administrator = new Administrator();
        administrator.setId(1);
        administrator.setName("山田太郎");
        administrator.setMailAddress(mailAddress);
        administrator.setPassword(hashedPassword);

        // リポジトリの振る舞いを定義 : 管理者情報を返す
        when(administratorRepository.findByMailAddress(mailAddress)).thenReturn(administrator);
        // パスワードエンコーダの振る舞いを定義 : パスワードが一致する
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        // 実行
        Administrator result = administratorService.login(mailAddress, rawPassword);

        // 検証
        assertNotNull(result);
        assertEquals(administrator.getId(), result.getId());
        assertEquals(administrator.getName(), result.getName());
        assertEquals(mailAddress, result.getMailAddress());
        assertEquals(hashedPassword, result.getPassword());
    }



    /**
     * ログイン(失敗時).
     *
     * 管理者が見つからない場合(パスワード照合自体は行われない)
     */
    @Test
    void testLogin_FailureWhenAdminNotFound() {
        String mailAddress = "notfound@example.com";
        String password = "password";

        // リポジトリの振る舞いを定義 : nullを返す（該当ユーザーなし）
        when(administratorRepository.findByMailAddress(mailAddress)).thenReturn(null);

        // 実行
        Administrator result = administratorService.login(mailAddress, password);

        // 検証 : nullが返されることを確認
        assertNull(result);
    }



    /**
     * ログイン(失敗時).
     *
     * パスワードが一致しない場合
     */
    @Test
    void testLogin_FailureWhenPasswordMismatch() {
        String email = "mismatch@example.com";
        String rawPassword = "wrongPassword";
        String hashedPassword = "hashedPassword";

        Administrator mockAdmin = new Administrator();
        mockAdmin.setMailAddress(email);
        mockAdmin.setPassword(hashedPassword);

        // リポジトリの振る舞いを定義 : 管理者情報を返す
        when(administratorRepository.findByMailAddress(email)).thenReturn(mockAdmin);
        // パスワードエンコーダの振る舞いを定義 : パスワードが一致しない
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        // 実行
        Administrator result = administratorService.login(email, rawPassword);

        // 検証 : nullが返されることを確認
        assertNull(result);
    }
}