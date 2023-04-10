package domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 도서관에 등록된 계정 데이터. '아이디'로 관리
 */
@Getter @Setter
@NoArgsConstructor @ToString
public class User implements Serializable {
	
	private static final long serialVersionUID = 8922971924230604317L;
	
	// 변수
	private String name;        // 이름
	private String birth;       // 생년월일
	private String phone;       // 전화번호
	
	private int id;             // 아이디 : User의 기본키
	private String pw;          // 비밀번호
	
	// 관리자와 블랙리스트는 중첩될 수 없다.
	private boolean admin;      // 관리자 여부[true = 관리자]
	private boolean blacklist;  // 블랙리스트 여부[true = 블랙리스트]
	
	
	// 생성자
	public User(String name, String birth, String phone, int id, String pw, boolean admin) {
		this.name = name;
		this.birth = birth;
		this.phone = phone;
		this.id = id;
		this.pw = pw;
		this.admin = admin;
	}
	
}