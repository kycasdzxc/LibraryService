package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import domain.Book;
import domain.LibBook;
import domain.Rent;
import domain.User;

@SuppressWarnings("unchecked")
public class DataUtil {
	
	public static List<User> initUsers() {
		List<User> users = new ArrayList<User>();
		
		try {
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.ser"));
	        users = (List<User>) ois.readObject();
	        System.out.println("User 데이터 불러오기 완료");
	        ois.close();
	        
	    } catch (FileNotFoundException e) {
	        // User 데이터
			users.add(new User("혁오", "981111", "010-1111-1111", 1001, "1234", true)); // 관리자
			users.add(new User("너드커넥션", "981112", "010-1111-1112", 1002, "1234", false));
			users.add(new User("카더가든", "981113", "010-1111-1113", 1003, "1234", false));
			users.add(new User("오존", "981114", "010-1111-1114", 1004, "1234", false));
			users.add(new User("설", "981115", "010-1111-1115", 1005, "1234", false));
			users.add(new User("다섯", "981116", "010-1111-1116", 1006, "1234", true));
			
	        System.out.println("User 임시 데이터 초기화 완료");

	    } catch (IOException e) {
	        e.printStackTrace();
	        System.exit(0);
	        
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
		return users;
	}
	
	public static List<Book> initBooks() {
		List<Book> books = new ArrayList<Book>();
		
		try {
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("books.ser"));
	        books = (List<Book>) ois.readObject();
	        System.out.println("Book 데이터 불러오기 완료");
	        ois.close();
	        
	    } catch (FileNotFoundException e) {
	        // Book 데이터
	    	books.add(new Book(101, "카이사르의 마지막 숨", "샘 킨", "해나무", "9791164051298"));
			books.get(0).setAmount(3);

			books.add(new Book(102, "주목받는 카메라연기 레슨", "안지은", "한권의책", "9791185237053")); // 제목 '카' 중복
			books.get(1).setAmount(2);

			books.add(new Book(103, "숙명", "히가시노 게이고", "창해", "9788979197662"));
			books.get(2).setAmount(1);

			books.add(new Book(104, "조선의 위기 대응 노트", "김준태", "민음사", "9788937444654"));
			books.get(3).setAmount(1);

			books.add(new Book(105, "형제", "김준태", "지식을 만드는 지식", "9788964062920")); // 저자 중복
			books.get(4).setAmount(1);

			books.add(new Book(106, "Java performance", "Scott Oaks", "Reilly", "9781492056119")); // 출판사 중복
			books.get(5).setAmount(3);

			books.add(new Book(107, "definitive guide", "Eric A. Meyer", "Reilly", "9780596527334"));
			books.get(6).setAmount(1);

			books.add(new Book(108, "Treatment of disorders", "Eric A. Youngstrom", "Guilford Press", "9781462547715")); // 해외저자
			books.get(7).setAmount(2);

			books.add(new Book(109, "다크 데이터", "데이비드 핸드", "더퀘스트", "9791165217099"));
			books.get(8).setAmount(3);
			
	        System.out.println("Book 임시 데이터 초기화 완료");
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.exit(0);
	        
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
		return books;
	}
	
	public static List<LibBook> initLibBooks() {
		List<LibBook> libBooks = new ArrayList<LibBook>();
		
		try {
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("libBooks.ser"));
	        libBooks = (List<LibBook>) ois.readObject();
	        System.out.println("LibBook 데이터 불러오기 완료");
	        ois.close();
	        
	    } catch (FileNotFoundException e) {
	        // LibBook 데이터
	    	libBooks.add(new LibBook(10001, 101)); // 카이사르의 마지막 숨(대여)
	    	libBooks.get(0).setRent(true);
			libBooks.add(new LibBook(10002, 101)); // 카이사르의 마지막 숨
			libBooks.get(1).setRent(true);
			libBooks.add(new LibBook(10003, 101)); // 카이사르의 마지막 숨
			libBooks.get(2).setRent(true);
			libBooks.add(new LibBook(10004, 102)); // 주목받는 카메라연기 레슨
			libBooks.get(3).setRent(true);
			libBooks.add(new LibBook(10005, 102)); // 주목받는 카메라연기 레슨
			libBooks.get(4).setRent(true);
			libBooks.add(new LibBook(10006, 103)); // 숙명
			libBooks.get(5).setRent(true);
			libBooks.add(new LibBook(10007, 104)); // 조선의 위기 대응 노트
			libBooks.add(new LibBook(10008, 105)); // 형제
			libBooks.add(new LibBook(10009, 106)); // Java performance
			libBooks.add(new LibBook(10010, 106)); // Java performance
			libBooks.add(new LibBook(10011, 106)); // Java performance
			libBooks.add(new LibBook(10012, 107)); // definitive guide
			libBooks.add(new LibBook(10013, 108)); // Treatment of disorders
			libBooks.add(new LibBook(10014, 108)); // Treatment of disorders
			libBooks.add(new LibBook(10015, 109)); // 다크 데이터
			libBooks.add(new LibBook(10016, 109)); // 다크 데이터
			libBooks.add(new LibBook(10017, 109)); // 다크 데이터
			
	        System.out.println("LibBook 임시 데이터 초기화 완료");
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.exit(0);
	        
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
		return libBooks;
	}
	
	public static List<Rent> initRents() {
		List<Rent> rents = new ArrayList<Rent>();
		
		try {
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("rents.ser"));
	        rents = (List<Rent>) ois.readObject();
	        System.out.println("Rent 데이터 불러오기 완료");
	        ois.close();
	        
	    } catch (FileNotFoundException e) {
	        // Rent 데이터
	    	rents.add(new Rent(1001, 1001, 10001)); // 연체
			rents.get(0).setDateRent(1643718644381l);
			
			rents.add(new Rent(1002, 1001, 10002));
			rents.get(1).setDateRent(1642938620000l);
			
			rents.add(new Rent(1003, 1001, 10003));
			rents.add(new Rent(1004, 1002, 10004));
			rents.add(new Rent(1005, 1003, 10005));
			rents.add(new Rent(1006, 1004, 10006));
			
	        System.out.println("Rent 임시 데이터 초기화 완료");
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.exit(0);
	        
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
		return rents;
	}
	
}
