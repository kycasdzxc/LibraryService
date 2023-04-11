package dao;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import domain.Rent;
import util.DataUtil;

public class RentDao {
	
	// Singleton 패턴 적용
	private static RentDao rentDao = new RentDao();
	
	private RentDao() {}
	
	public static RentDao getInstance() {
		return rentDao;
	}
	
	private static List<Rent> rents;
	
	// Rent 데이터 초기화
	{ rents = DataUtil.initRents(); }
	
	public List<Rent> listRent() {
		return rents;
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
