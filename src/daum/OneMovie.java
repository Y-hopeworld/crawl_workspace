package daum;
// JAVA 문자열 다루는 함수 
// substring(시작,끝); → 시작과 끝은 인덱스 값으로 설정, 시작값은 포함하지만 끝값은 포함하지 않음
//index번호 : 0부터 시작
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import domain.MovieDTO;
import persistence.MovieDAO;

public class OneMovie {

	public static void main(String[] args) throws IOException {
		
		// 1페이지의 평점 10건 수집
		String base = "https://movie.daum.net/moviedb/grade?movieId=134091&type=netizen&page=";
		//페이지 1부터 시작 
		int page = 1;
		String url = base + page;
		int count = 0; // 전체 댓글 수
		String title = ""; // 영화 제목
		int total = 0; // 총 평점
		double scoreAvg = 0.0; // 평균 평점
		int score , regdate = 0;
		
		// MovieDAO 객체 생성()
		MovieDAO mDao = new MovieDAO();

		while(true){
			// 1페이지으 ㅣ평점 10건 수집
			Document doc = Jsoup.connect(url).get();		
			Elements reply = doc.select("ul.list_review div.review_info");
							// 댓글전체를  싸고 있는 ul, 하나의 댓글을 싸고 있는 div.review_info
			// 마지막페이지면 수집 종료
			if(reply.isEmpty()) {
			break;
		}
		//영화제목 수집
		Elements movieName = doc.select("h2.tit_rel");
		title = movieName.text();
		
		String writer, content, basedate, subdate = "";
		
		for (Element one : reply) {
			count++;
			writer = one.select("em.link_profile").text();
			score = Integer.parseInt(one.select("em.emph_grade").text());
			content = one.select("p.desc_review").text();
			basedate = one.select("span.info_append").text();
			
			subdate= basedate.substring(0, 10);
			regdate = Integer.parseInt(subdate.replace(".","")); 
			
			// 누적 평점 계산 : total = total + score;
			total += score; 
			
			MovieDTO mDto = new MovieDTO(title, content, writer, score, "daum", regdate);
			
			//DB에 저장
			mDao.addMovie(mDto);
			System.out.println("■■"+count+"건" );
			System.out.println("영화 :"+ title);
			System.out.println("평점 : "+ score);
			System.out.println("작성자 :"+ writer);
			System.out.println("내용 : "+ content);
			System.out.println("작성일자 : "+ regdate);
					
		} // 한 페이지가 끝나는 구간
		// 다음페이지로 이동하기 위해 page +1 증가
		page = page + 1;
		// 다음 페이지로 이동할 URL 작성
		url = base + page;
	
		
	}
		// 평균 평점 계싼
		scoreAvg = (double)total/count;
		// 소수점 첫번째자리까지 출력
		double result = Math.floor(scoreAvg);
		
		// 수집 및 분석결과 출력
		System.out.println(title + "Daum 영화 평점 수집 결과");
		System.out.println((page-1) + "페이지에서");
		System.out.println("수집한 평점은 총"+count+"건 입니다.");
		System.out.println("평균 평점은 : " + result +"점 입니다.");	
		
	}

}
