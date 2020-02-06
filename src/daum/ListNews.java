// 다음 뉴스 목록의 각 기사마다 제목과 본문을 수집 
// (1page내의 목록만 ex: 목록 10개 >> 10건의 기사)
package daum;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ListNews {
	public static void main(String[] args) throws IOException {
		
		// 데이터를 수집할 사이트 주소
		String url = "https://news.daum.net/breakingnews/digital";
		
		//URL 문서의 전체 태그를 Select함
		Document doc = Jsoup.connect(url).get();
		
		// 전체문서인 document에서 데이터를 수집 하려면  각 자료를 list로 만들어 주는게 elements
		Elements urls = doc.select("ul.list_allnews strong.tit_thumb > a.link_txt");
		
		//System.out.println(urls);
		
		int count = 0;
		// 전체 출력
		for (Element element : urls) {
			//→ urls 의 데이터를 하나씩 꺼내 element에 넣어줌
			count++;
			
			
			// attr()을 이용하여 원하는 속성값 추출
			String href = element.attr("href");
			// 전체문서인 document에서 데이터를 수집 하려면  각 자료를 list로 만들어 주는게 elements
			// 그 list의 각각 데이터를 수집 하는게 element
			
			Document docNews = Jsoup.connect(href).get();
			
			Elements title = docNews.select("h3.tit_view");  // 제목출력
			Elements content = docNews.select("div#harmonyContainer");
			System.out.println("■■■■■■■■■■■■■■■■"+count+" 건 수집■■■■■■■■■■■■■■■■");

			//출력 : .text() 태그 및 속성은 지우고 content 내용만 추출
			System.out.println("제목 : "+title.text()); // 제목 출력
			System.out.println("내용: "+content.text()); // 내용 출력
						
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
			System.out.println("■■■■■ Daum 수집한 총 뉴스 " + count +"건 입니다" );
			System.out.println("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
		}
		
		
	}

}
