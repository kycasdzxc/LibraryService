package dao;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import domain.Rent;
import domain.User;
import util.DataUtil;

public class RentDao {
	
	// Singleton 패턴 적용
	private static RentDao rentDao = new RentDao();
	
	private RentDao() {}
	
	public static RentDao getInstance() {
		return rentDao;
	}
	
	private List<Rent> rents = DataUtil.initRents();
	
	// 대여정보 목록조회
	public List<Rent> listRent() {
		return rents;
	}
	
	// 대여정보 목록조회(계정정보)
	public List<Rent> listRent(User user) {
		List<Rent> rents = new ArrayList<Rent>();
		
		for (Rent rent : this.rents) {
			if (rent.getUserID() == user.getId()) {
				rents.add(rent);
			}
		}
		return rents;
	}
	
	// 대여정보 목록조회(소장도서 ID)
	public List<Rent> listRent(int libBookID) {
		List<Rent> rents = new ArrayList<Rent>();
		
		for (Rent rent : this.rents) {
			if (rent.getLibBookID() == libBookID) {
				rents.add(rent);
			}
		}
		return rents;
	}
	
	// 대여정보 조회
	public Rent getRent(int rentNum) { 
		for (Rent rent : rents) {
			if (rent.getRentNum() == rentNum) {
				return rent;
			}
		}
		return null;
	}
	
	// 대여정보 생성
	public void regRent(Rent rent) {
		rents.add(rent);
		saveRent();
	}
	
	// 대여정보 수정(도서반납)
	public void modifyRent(Rent r) {
		Rent rent = getRent(r.getRentNum());
		
		rent.setDateReturn(System.currentTimeMillis());
		saveRent();
	}
	
	// 대여정보 삭제
	public void removeRent(Rent rent) {
		rents.remove(rent);
		saveRent();
	}
	
	// Rent 데이터 저장
	private void saveRent() {
		ObjectOutputStream oos;
		try {
		oos = new ObjectOutputStream(new FileOutputStream("rents.ser"));
		oos.writeObject(rents);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
