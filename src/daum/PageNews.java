// 다음 뉴스 목록의 페이지단위로 읽어서  각 기사마다 제목과 본문을 수집 
// (1page: 기사 15건, 10page = 기사 150건)
package daum;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageNews {

	public static void main(String[] args) throws IOException {
		// 자료를 수집할 주소의 값을 base에 넣기
		String base = "https://news.daum.net/breakingnews/digital?page=";
		
		// 페이지는 1부터 시작하니까 1부터 넣어줌
		int page = 1;
		String url = base + page;
		System.out.println(url);
		int count = 0;
		
		System.out.println("■■■■■■■■■■■■■■■■■ START ■■■■■■■■■■■■■■■■■");
//		for(int i = 1; i <= 3; i++) { // 1,2,3 페이지 데이터 수집
		while(true) { 
			// 해당 페이지으 전체 내용(태그)
			Document doc = Jsoup.connect(url).get();
			// 해당 페이지의 기사 목록을 수집 (1Page당 15건)
			Elements headLine = doc.select("ul.list_allnews strong.tit_thumb > a.link_txt");
			
			// while 반복문을 돌면 페이지에 내용이 없어도 계속 데이터를 수집하기 때문에 빠져나가는  구간을 만들어줘야한다
			if(headLine.isEmpty()) {
				// isEmpty
				break;
			}
			
			
			// 1 페이지의 기사 목록에서 1건씩 수집해서 element에 담음
			for (Element element : headLine) {
				// 뉴스 전체 카운트 수 +1 증가
				count++;			
				
				// 단건 출력된 기사에서 href 속성값 수집
				String href = element.attr("href");
				// 단건 출력된 기사 전체 내용을 태그
				Document docNews = Jsoup.connect(href).get();
				
				// 단건 출력 된 기사의 제목(title)
				Elements title = docNews.select("h3.tit_view");  
				// 단건 출력된 기사의 본문(content)
				Elements content = docNews.select("div#harmonyContainer");
				
				// 다넉ㄴ 결과 출력(제목 + 본문)
				System.out.println("■■■■■■■■■■■■■■■■"+count+" 건 수집■■■■■■■■■■■■■■■■");
				//출력 : .text() 태그 및 속성은 지우고 content 내용만 추출
				System.out.println("제목 : "+title.text()); // 제목 출력
				System.out.println("내용: "+content.text()); // 내용 출력
			}			
				// 다음페이지 이동
				page = page +1;
				url = base + page;		
			}	
			// 수집완료, 전체 수집된 결과 출력
			System.out.println("■■■■■■■■■■■■■■■■■■END■■■■■■■■■■■■■■■■■■■■");
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
			System.out.println("■■■■■ Daum 수집한 총 뉴스 " + count +"건 입니다" );
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");		
		}		
	}


