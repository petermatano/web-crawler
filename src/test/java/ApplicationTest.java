import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;

public class ApplicationTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void nullArgs() throws Exception {
        exit.expectSystemExitWithStatus(1);
        String[] args = null;
        Application.main(args);
    }

    @Test
    public void emptyArgs() throws Exception {
        exit.expectSystemExitWithStatus(1);
        String[] args = new String[0];
        Application.main(args);
    }

    @Test
    public void tooManyArgs() throws Exception {
        exit.expectSystemExitWithStatus(1);
        String[] args = {"1", "2", "3"};
        Application.main(args);
    }

}
