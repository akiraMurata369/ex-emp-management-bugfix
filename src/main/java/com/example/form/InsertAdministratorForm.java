package com.example.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 管理者情報登録時に使用するフォーム.
 * 
 * @author igamasayuki
 * 
 */
public class InsertAdministratorForm {
	/** 名前 */
	@NotBlank(message = "名前を入力してください")
	@Size(max = 20, message = "20文字以内で入力してください")
	private String name;

	/** メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	@Size(max = 30, message = "30文字以内で入力してください")
	private String mailAddress;

	/** パスワード */
	@NotBlank(message = "パスワードを入力してください")
	@Size(max = 20, message = "20文字以内で入力してください")
	private String password;

	/** 確認用パスワード */
	@NotBlank(message = "確認用パスワードを入力してください")
	@Size(max = 20, message = "20文字以内で入力してください")
	private String confirmationPassword;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmationPassword() {
		return confirmationPassword;
	}

	public void setConfirmationPassword(String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
	}


	@Override
	public String toString() {
		return "InsertAdministratorForm{" +
				"name='" + name + '\'' +
				", mailAddress='" + mailAddress + '\'' +
				", password='" + password + '\'' +
				", confirmationPassword='" + confirmationPassword + '\'' +
				'}';
	}
}
