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
	
	private List<User> users = DataUtil.initUsers();
	
	// 계정정보 목록조회
	public List<User> listUser() {
		return users;
	}
	
	// 계정정보 조회
	public User getUser(int id) { 
		for (User user : users) {
			if (user.getId() == id) {
				return user;
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
	public void modifyUser(User u) {
		User user = getUser(u.getId());
		
		user.setName(u.getName());
		user.setBirth(u.getBirth());
		user.setPhone(u.getPhone());
		
		saveUser();
	}
	
	// 계정정보 삭제
	public void removeUser(User user) {
		users.remove(user);
		saveUser();
	}
	
	// 계정권한 변경
	public void updateAuth(User u) {
		User user = getUser(u.getId());
		
		user.setAdmin(u.isAdmin());
		user.setBlacklist(u.isBlacklist());
		
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
