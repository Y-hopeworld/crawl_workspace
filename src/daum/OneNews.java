// 다음 뉴스 1건에서 제목과 내용 본문을 수집

package daum;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class OneNews {
	public static void main(String[] args) throws IOException{
		// 데이터를 수집 할 사이트의 주소
		String url = "https://entertain.v.daum.net/v/20200205143853187";
		
		//URL 문서에 전체 태그를 Select함
		Document doc = Jsoup.connect(url).get();
					// jsoup을 쓸거야 그리고  url을 연결 할거야
		
		
		// 전체태그에서 원하는 항목만 Select함
		// 1. 제목수집 
		Elements title = doc.select("h3.tit_view");  // 제목추출
						// h3중에서 tit_view를 긁어와줘
		
		Elements content = doc.select("div#harmonyContainer");
		
		
		//출력 : .text() 태그 및 속성은 지우고 content 내용만 추출
		System.out.println(title.text()); // 제목 출력
		System.out.println(content.text()); // 내용 출력
		
		
		
		
		
	}
}
