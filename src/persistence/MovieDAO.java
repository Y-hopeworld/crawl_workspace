package persistence;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import domain.MovieDTO;
import mybatis.SqlMapConfig;

public class MovieDAO {

	SqlSessionFactory sqlsessionFactory = SqlMapConfig.getSqlSession();
	SqlSession sqlSession;
	int result;
	
	// Oracle DB에 크롤링 데이터를 저장하는 기능
	// mDto : 평점 1건 
	public void addMovie(MovieDTO mDto) {
		sqlSession = sqlsessionFactory.openSession(true);
		
		try {
			result = sqlSession.insert("movie.addMovie",mDto);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}
}
