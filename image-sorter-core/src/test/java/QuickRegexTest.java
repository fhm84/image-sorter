import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class QuickRegexTest {

	String arguments = "-r \"D:\\\\2012.09.09 Grillplatzeinweihung\"";
	String[] expected = { "-r", "D:\\\\2012.09.09 Grillplatzeinweihung" };

	@Test
	public void test() {
		String[] args = arguments.split("(?<=\").*(?=\")");
		// args = arguments.split("([^\"]\\S*|\".*\")|\\s*");

		for (String s : args) {
			System.out.println(s);
		}

		assertThat(args.length, is(equalTo(expected.length)));

		for (int i = 0; i < args.length; i++) {
			assertThat(args[i], is(equalTo(expected[i])));
		}
	}
}
