import com.thoughtworks.gauge.Step;
import com.thoughtworks.gauge.Table;
import com.thoughtworks.gauge.TableRow;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import webAutomationBase.helper.ElementHelper;
import webAutomationBase.helper.StoreHelper;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StepImplementation extends BaseTest{

    public List<WebElement> findElementsByKey(String key) {
        try{
            return driver.findElements(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)));

        }catch (Exception e){
            Assert.fail("Element: '" + key + "' doesn't exist.");
            return null;
        }
    }

    @Step("Elementin yüklenmesini bekle ve tıkla <aramaKutusu>")
    public void getElementWithKeyIfExists(String key) {
        findElementsByKey(key);
    }
}
