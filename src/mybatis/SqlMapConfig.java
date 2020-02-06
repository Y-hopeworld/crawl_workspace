package mybatis;


import java.io.IOException;
import java.io.Reader;

//마이바티스에서 만든것
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


//SqlSessionFactoryBuilder를 통해서 SqlSessionFactory를 만들어주는 역할

public class SqlMapConfig {

	private static SqlSessionFactory sqlSessionFactory;
	// 							최초 실행시에 Configuration.xml 공장이 만들어지고
	//						    아래에 있는 public static SqlSessionFactory getSqlSession()에 보내주기
	
	
	// static 블럭을 정적 블럭이라고 한다
	// 클래스 로딩시 1회만 실행되는 코드 
	
	// 1.최초 처음 실행시 
	static {
			// 변수의 값을 담아 넣음 (실행되는 건 없음)
			String resource = "mybatis/Configuration.xml";
			
			try {
				
				 Reader reader = Resources.getResourceAsReader(resource);
				 			// 		String resource을 향하는게 아님
				 			//   getResourceAsReader의 역할: 매개변수인 resource에 있는 경로로 가서 파일을 하나씩 읽어서 reader에 저장
				 			//    reader 안에는 Configuration.xml 파일이 들어있음
				 
				 if(sqlSessionFactory == null) {
					 // 최초 실행시 null임
					 sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
					 //				 	  빌드패턴:  		  생성자 메서드		
					 						// Builder에게 Configuration.xml 공장을 만들어줘					
					 						// 결과 : SqlSessionFactoryBuilder가 sqlSessionFactory 공장을 만들어주고, sqlSessionFactory가 생성됌
				 }
				 
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	public static SqlSessionFactory getSqlSession() {
		return sqlSessionFactory;
	}
	
}
