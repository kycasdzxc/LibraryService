package service.user;

import static util.LibUtil.convert;
import static util.LibUtil.convertLeft;
import static util.LibUtil.nextInt;
import static util.LibUtil.nextLine;

import java.util.ArrayList;
import java.util.List;

import domain.Rent;
import domain.User;
import service.rent.RentService;
import service.rent.RentServiceImpl;
import util.DataUtil;
import util.LibUtil;

public class UserServiceImpl implements UserService {
	
	private RentService rentService = RentServiceImpl.getInstance();
	private List<Rent> rents = rentService.getRents();
	
	// Singleton 패턴 적용
	private static UserService userService;
	
	private UserServiceImpl() {}
	
	public static UserService getInstance() {
		return userService == null ? new UserServiceImpl() : userService;
	}
	
	private List<User> users = new ArrayList<User>();
	
	// User 데이터 초기화
	{ DataUtil.initUsers(users); }
	
	@Override
	public List<User> getUsers() { return users; }
	
	/**
	 * 해당 계정이 '관리자'인지 '블랙리스트'인지 확인하는 메서드.
	 * isAdmin()이 true면 '관리자', isBlacklist()가 true면 '블랙리스트'를 문자열로 반환한다.
	 * '관리자'와 '블랙리스트'는 중첩될 수 없다.
	 * @param userID : 조회할 계정의 ID(User 클래스의 id)
	 * @return isAdmin()이 true면 '관리자', isBlacklist()가 true면 '블랙리스트'를 문자열로 반환
	 */
	private String userState(int userID) {
		if (findUserByID(userID).isAdmin() == true) {
			return "  관리자";
		} else if (findUserByID(userID).isBlacklist() == true) {
			return "블랙리스트";
		} else {
			return "";
		}
	}

	/**
	 * 가입된 계정의 목록을 보여주는 메서드.
	 */
	public void userList() {
		LibUtil.userIndex();
		for (User u : users) {
			System.out.print("│  " + convertLeft(userNameLength(u), 13) + "   │    ");
			System.out.print(convert(u.getUserBirth(), 6) + "    │ ");
			System.out.print(convert(u.getUserPN(), 13) + " │  ");
			System.out.println(convertLeft(userState(u.getId()), 10) + "  │");
			System.out.printf("──────────────────────────────────────────────────────────────────%n");
		}
	}

	/**
	 * 신규 계정의 데이터를 등록하는 메서드. 생년월일은 6글자, 전화번호는 11글자를 입력해야 한다.
	 */
	public void regUser() {
		users.add(new User(nextLine("이름 > ", true, false), birthLength(nextLine("생년월일[ex)921024] > ", false, true)),
				pnLength(nextLine("전화번호[ex)01086940273] > ", false, true)), nextInt("ID > "), nextLine("PW > "),
				false));

		System.out.println("계정생성이 완료되었습니다.");
	}

	/**
	 * 계정 데이터를 수정하는 메서드. 계정 ID로 수정할 계정의 정보를 찾는다.
	 */
	public void modifyUser() {
		User user = findUserByID(nextInt("수정할 계정의 ID > "));
		if (user != null) {
			System.out.println(user);

			user.setUserName(nextLine("Enter the name to be replaced. > ", true, false));
			user.setUserBirth(nextLine("Enter the birth to be replaced. > ", false, true));
			user.setUserPN(nextLine("Enter the P.N to be replaced. >", false, true));
			System.out.println("등록 완료 되었습니다.");
			System.out.println(user);

		} else {
			System.out.println("등록된 적 없는 ID입니다.");
		}
	}

	/**
	 * 계정 데이터를 삭제하는 메서드. 해당 계정이 대여 중인 도서가 있거나, 관리자 계정일 경우 삭제가 불가하다.
	 */
	public void removeUser() {
		User user = findUserByID(nextInt("삭제할 회원의 ID를 입력하세요 > "));
		int userID = 0;
//		for (Rent r : rents) {
//			if (user.getId() == r.getUserID()) {
//				userID = r.getUserID();
//				break;
//			}
//		}
		// userID가 없는 아이디일 경우
		if (user == null) {
			System.out.println("잘못된 ID입니다.");
		// 대여 중인 도서가 있는 경우
		} else if (userID != 0) {
			System.out.println("대여 중인 도서가 있습니다.");
		// 관리자 계정일 경우
		} else if (user.isAdmin() == true) {
			System.out.println("해당 계정은 관리자 계정입니다.");
		} else {
			users.remove(user);
			System.out.println("**삭제완료**");
		}
	}

	/**
	 * 관리자 권한을 부여·회수하는 메서드. '관리자'는 '블랙리스트'와 중첩될 수 없다.
	 * @param userID : 관리자 권한을 부여·회수할 계정의 ID(User 클래스의 id)
	 */
	public void admin(int userID) {
		if (findUserByID(userID) != null && findUserByID(userID).getId() == userID) {
			if (findUserByID(userID).isAdmin() == false && findUserByID(userID).isBlacklist() == false) {
				findUserByID(userID).setAdmin(true);
				System.out.printf("%s님께 관리자 권한을 부여하였습니다.%n", findUserByID(userID).getUserName());
			} else if (findUserByID(userID).isAdmin() == false && findUserByID(userID).isBlacklist() == true) {
				System.out.println("해당 계정은 블랙리스트입니다.");
			} else {
				findUserByID(userID).setAdmin(false);
				System.out.printf("%s님의 관리자 권한을 회수하였습니다.%n", findUserByID(userID).getUserName());
			}
		} else {
			System.out.println("ID를 확인해주세요.");
		}
	}

	/**
	 * 블랙리스트로 등록·해제하는 메서드. '블랙리스트'는 '관리자'와 중첩될 수 없다.
	 * @param userID : 블랙리스트로 등록·해제할 계정의 ID(User 클래스의 id)
	 */
	public void blackList(int userID) {
		if (findUserByID(userID) != null && findUserByID(userID).getId() == userID) {
			if (findUserByID(userID).isBlacklist() == false && findUserByID(userID).isAdmin() == false) {
				findUserByID(userID).setBlacklist(true);
				System.out.printf("%s님이 블랙리스트로 등록 되었습니다.%n", findUserByID(userID).getUserName());
			} else if (findUserByID(userID).isBlacklist() == false && findUserByID(userID).isAdmin() == true) {
				System.out.println("관리자 계정입니다.");
			} else {
				findUserByID(userID).setBlacklist(false);
				System.out.println("블랙리스트가 해제되었습니다.");
			}
		} else {
			System.out.println("ID를 확인해주세요.");
		}
	}
	
	/**
	 * 로그인을 담당하는 메서드. 해당 계정이 일반 계정인지 관리자 계정인지 구분해준다.
	 * @param userID : 로그인할 계정의 ID(User 클래스의 id)
	 * @param userPW : 로그인할 계정의 PW(User 클래스의 pw)
	 * @return 관리자 계정은 1, 일반 계정은 2을 반환. id 또는 pw가 틀리면 0을 반환
	 */
	public int logIn(int userID, String userPW) {
		if (findUserByID(userID) != null) {
			if (findUserByID(userID).getPw().equals(userPW)) {
				// 관리자 계정일 경우
				if (findUserByID(userID).isAdmin() == true) {
					System.out.println();
					System.out.println("관리자 계정입니다.");
					return 1;
				// 일반 계정일 경우
				} else {
					System.out.println();
					System.out.printf("%s님, 안녕하세요.%n", findUserByID(userID).getUserName());
					return 2;
				}
			// PW가 일치하지 않는 경우
			} else {
				System.out.println();
				System.out.println("ID 또는 pw를 확인해주세요.");
				return 0;
			}
		// ID가 존재하지 않는 경우
		} else {
			System.out.println();
			System.out.println("ID 또는 pw를 확인해주세요.");
			return 0;
		}
	}

	/**
	 * 로그아웃을 담당하는 메서드.
	 */
	public void logOut() {
		System.out.println("로그아웃이 성공적으로 이루어졌습니다.");
	}
	
	/**
	 * 계정 ID가 일치하는 계정정보를 찾는 메서드. 일치하는 값이 없을 경우, null을 반환한다.
	 * @param userID : 계정 ID(User 클래스의 id)
	 * @return user : 계정 ID가 일치하는 계정정보를 User 타입으로 반환
	 */
	private User findUserByID(int userID) { // UserID로 User 탐색
		User user = null;

		for (User u : users) {
			if (u.getId() == userID) {
				user = u;
			}
		}
		return user;
	}
	
	/**
	 * 계정정보의 이름을 축약하는 메서드. 이름이 6글자가 넘어가면, 5번째 글자 이후는 ... 으로 출력된다.
	 * @param user : 이름을 축약할 계정의 정보(User 타입)
	 * @return 반각문자, 전각문자 상관없이 최대 5글자까지 출력이 가능하다.
	 * 6글자가 넘어가면 ... 으로 출력된다.
	 */
	private String userNameLength(User user) {
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
	private String birthLength(String birth) {
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
	private String pnLength(String pn) {
		if (pn.length() == 11) {
			return pn.substring(0, 3) + "-" + pn.substring(3, 7) + "-" + pn.substring(7, 11);
		} else {
			throw new RuntimeException("11자리로 입력해주세요.[ex)01086940273]");
		}
	}
}
