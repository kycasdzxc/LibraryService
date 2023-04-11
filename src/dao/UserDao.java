package dao;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import domain.User;
import util.DataUtil;

public class UserDao {
	
	// Singleton 패턴 적용
	private static UserDao userDao = new UserDao();
	
	private UserDao() {}
	
	public static UserDao getInstance() {
		return userDao;
	}
	
	private static List<User> users = DataUtil.initUsers();;
	
	// 계정정보 목록조회
	public List<User> listUser() {
		return users;
	}
	
	// 계정정보 조회
	public User getUser(int id) { 
		for (User u : users) {
			if (u.getId() == id) {
				return u;
			}
		}
		return null;
	}
	
	// 계정정보 생성
	public void regUser(User user) {
		users.add(user);
		saveUser();
	}
	
	// 계정정보 수정
	public void modifyUser(User user) {
		User u = getUser(user.getId());
		u.setName(user.getName());
		u.setBirth(user.getBirth());
		u.setPhone(user.getPhone());
		saveUser();
	}
	
	// 계정정보 삭제
	public void removeUser(User user) {
		users.remove(user);
		saveUser();
	}
	
	// 계정권한 변경
	public void updateAuth(User user) {
		User u = getUser(user.getId());
		u.setAdmin(user.isAdmin());
		u.setBlacklist(user.isBlacklist());
		saveUser();
	}
	
	// User 데이터 저장
	private void saveUser() {
		ObjectOutputStream oos;
		try {
		oos = new ObjectOutputStream(new FileOutputStream("users.ser"));
		oos.writeObject(users);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
