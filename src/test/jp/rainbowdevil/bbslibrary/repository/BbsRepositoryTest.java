package jp.rainbowdevil.bbslibrary.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import jp.rainbowdevil.bbslibrary.model.Bbs;
import jp.rainbowdevil.bbslibrary.utils.IOUtils;

import org.junit.Before;
import org.junit.Test;

public class BbsRepositoryTest {
	
	private BbsRepository repository;
	private String TEST_REPOSITORY_PATH = "testrepo/log/";

	@Before
	public void setUp() throws Exception {
		repository = new BbsRepository();
		repository.setBbsRepositoryPath(TEST_REPOSITORY_PATH);
	}
	
	@Test
	public void 板一覧の保存() throws IOException{
		// Setup
		Bbs bbs = new Bbs();
		bbs.setId("abc");
		String html = "<html>hoge</html>\n";
		byte[] bytes = html.getBytes();
		
		// Exercise
		repository.init();
		repository.writeBoardList(bbs, bytes);
		
		// Verify
		File file = new File(TEST_REPOSITORY_PATH+bbs.getId()+"/"+BbsRepository.BOARD_LIST_FILE);
		assertThat(file.exists(), is(true));
		String readHtml = IOUtils.readFile(file, "shift-jis");
		assertThat(html, is(readHtml));
		
		// 後片付け
		file.delete();
	}

}
