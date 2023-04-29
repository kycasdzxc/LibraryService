package service.user;

import static util.LibUtil.convert;
import static util.LibUtil.convertLeft;
import static util.LibUtil.nextInt;
import static util.LibUtil.nextLine;

import java.util.List;

import dao.RentDao;
import dao.UserDao;
import domain.Rent;
import domain.User;
import util.LibUtil;
import util.StringUtil;

public class UserServiceImpl implements UserService {
	
	private UserDao userDao = UserDao.getInstance();
	private RentDao rentDao = RentDao.getInstance();
	
	List<Rent> rents = rentDao.listRent();
	
	@Override
	public void regUser() {
		User user = new User();
		user.setName(nextLine("이름 > ", true, false));
		user.setBirth(StringUtil.birthLength(nextLine("생년월일[ex)921024] > ", false, true)));
		user.setPhone(StringUtil.pnLength(nextLine("전화번호[ex)01086940273] > ", false, true)));
		user.setId(nextInt("ID > "));
		user.setPw(nextLine("PW > "));
		
		userDao.regUser(user);

		System.out.println("계정생성이 완료되었습니다.");
	}

	@Override
	public void listUser() {
		List<User> users = userDao.listUser();
		
		LibUtil.userIndex();
		for (User user : users) {
			System.out.print("│  " + convertLeft(StringUtil.userNameLength(user), 13) + "   │    ");
			System.out.print(convert(user.getBirth(), 6) + "    │ ");
			System.out.print(convert(user.getPhone(), 13) + " │  ");
			System.out.print(convertLeft(userState(user), 10) + "  │");
			System.out.printf("%n──────────────────────────────────────────────────────────────────%n");
		}
	}

	private String userState(User user) {
		if ( user.isAdmin() ) {
			return "  관리자";
		} else if ( user.isBlacklist() ) {
			return "블랙리스트";
		} else {
			return "";
		}
	}
	
	@Override
	public void modifyUser() {
		User user = userDao.getUser(nextInt("수정할 계정의 ID > "));
		
		if ( user != null ) {
			System.out.println(user);

			user.setName(nextLine("Enter the name to be replaced. > ", true, false));
			user.setBirth(nextLine("Enter the birth to be replaced. > ", false, true));
			user.setPhone(StringUtil.pnLength(nextLine("Enter the P.N to be replaced. >", false, true)));
			
			userDao.modifyUser(user);
			System.out.println("수정 완료 되었습니다.");
			
		} else {
			System.out.println("등록된 적 없는 ID입니다.");
		}
	}

	@Override
	public void removeUser() {
		int id = nextInt("삭제할 회원의 ID를 입력하세요 > ");
		User user = userDao.getUser(id);
		int userID = 0;
		for (Rent r : rents) {
			if ( user.getId() == r.getUserID() ) {
				userID = r.getUserID();
				break;
			}
		}
		// userID가 없는 아이디일 경우
		if ( user == null ) {
			System.out.println("잘못된 ID입니다.");
		}
		// 대여 중인 도서가 있는 경우
		else if ( userID != 0 ) {
			System.out.println("대여 중인 도서가 있습니다.");
		}
		// 관리자 계정일 경우
		else if ( user.isAdmin() ) {
			System.out.println("해당 계정은 관리자 계정입니다.");
		} else {
			userDao.removeUser(user);
			System.out.println("**삭제완료**");
		}
	}

	@Override
	public void updateAdmin(int id) {
		User user = userDao.getUser(id);
		
		if ( user != null ) {
			if( user.isBlacklist() ) {
				System.out.println("해당 계정은 블랙리스트입니다.");
			} else if ( !user.isAdmin() ) {
				user.setAdmin(true);
				System.out.printf("%s님께 관리자 권한을 부여하였습니다.%n", user.getName());
			} else {
				user.setAdmin(false);
				System.out.printf("%s님의 관리자 권한을 회수하였습니다.%n", user.getName());
			}
			userDao.updateAuth(user);
		} else {
			System.out.println("ID를 확인해주세요.");
		}
	}

	@Override
	public void updateBlackList(int id) {
		User user = userDao.getUser(id);
		
		if ( user != null ) {
			if( user.isAdmin() ) {
				System.out.println("관리자 계정입니다.");
			} else if ( !user.isBlacklist() ) {
				user.setBlacklist(true);
				System.out.printf("%s님이 블랙리스트로 등록 되었습니다.%n", user.getName());
			} else {
				user.setBlacklist(false);
				System.out.println("블랙리스트가 해제되었습니다.");
			}
			userDao.updateAuth(user);
		} else {
			System.out.println("ID를 확인해주세요.");
		}
	}

	@Override
	public int login(int id, String pw) {
		User user = userDao.getUser(id);
		
		if ( user != null ) {
			if ( user.getPw().equals(pw) ) {
				// 관리자 계정일 경우
				if ( user.isAdmin() ) {
					System.out.printf("%n관리자 계정입니다.%n");
					return 1;
				}
				// 일반 계정일 경우
				else {
					System.out.printf("%n%s님, 안녕하세요.%n", user.getName());
					return 2;
				}
			}
		}
		// ID가 존재하지 않는 경우, PW가 일치하지 않는 경우
		System.out.printf("%nID 또는 pw를 확인해주세요.%n");
		return 0;
	}

}
