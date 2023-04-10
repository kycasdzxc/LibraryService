package util;

import domain.User;

public class StringUtil {
	
	/**
	 * 계정정보의 이름을 축약하는 메서드. 이름이 6글자가 넘어가면, 5번째 글자 이후는 ... 으로 출력된다.
	 * @param user : 이름을 축약할 계정의 정보(User 타입)
	 * @return 반각문자, 전각문자 상관없이 최대 5글자까지 출력이 가능하다.
	 * 6글자가 넘어가면 ... 으로 출력된다.
	 */
	public static String userNameLength(User user) {
		if (user.getUserName().length() > 5) {
			return user.getUserName().substring(0, 5) + "...";
		} else {
			return user.getUserName();
		}
	}

	/**
	 * 생년월일의 길이를 확인하는 메서드. 생년월일은 6자리로 제한한다.
	 * @param birth : 길이를 확인할 생년월일
	 * @return 조건이 true면 생년월일 반환, false면 문구 출력
	 */
	public static String birthLength(String birth) {
		if (birth.length() == 6) {
			return birth;
		} else {
			throw new RuntimeException("6자리로 입력해주세요.[ex)921024]");
		}
	}

	/**
	 * 전화번호의 길이를 확인하는 메서드. 전화번호 입력은 11자리로 제한한다.
	 * @param birth : 길이를 확인할 생년월일
	 * @return 조건이 true면 생년월일 반환, false면 문구 출력
	 */
	public static String pnLength(String pn) {
		if (pn.length() == 11) {
			return pn.substring(0, 3) + "-" + pn.substring(3, 7) + "-" + pn.substring(7, 11);
		} else {
			throw new RuntimeException("11자리로 입력해주세요.[ex)01086940273]");
		}
	}
}
