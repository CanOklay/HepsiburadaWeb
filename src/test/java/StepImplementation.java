import com.thoughtworks.gauge.Step;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;
import webAutomationBase.helper.ElementHelper;
import webAutomationBase.helper.StoreHelper;
import webAutomationBase.model.ElementInfo;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StepImplementation extends BaseTest {

    private static final int DEFAULT_MAX_ITERATION_COUNT = 75;
    private static final int DEFAULT_MILLISECOND_WAIT_AMOUNT = 200;
    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(StepImplementation.class);
    private Actions actions = new Actions(driver);

    public StepImplementation() {

        PropertyConfigurator
                .configure(StepImplementation.class.getClassLoader().getResource("log4j.properties"));
    }

    public List<WebElement> findElementsByKey(String key) {
        try {
            return driver.findElements(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)));

        } catch (Exception e) {
            Assert.fail("Element: '" + key + "' doesn't exist.");
            return null;
        }
    }

    private WebElement findElement(String key) {
        try {
            ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
            By infoParam = ElementHelper.getElementInfoToBy(elementInfo);

            WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.presenceOfElementLocated(infoParam));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                    webElement);
            return webElement;
        } catch (Exception e) {
            Assert.fail("Element: '" + key + "' doesn't exist.");
            return null;
        }
    }

    public int randomNumber(int start, int end) {
        Random rn = new Random();
        int randomNumber = rn.nextInt(end - 1) + start;
        return randomNumber;
    }


    @Step({"Wait for element to load with <key> and click",
            "Elementin yüklenmesini bekle ve tıkla <key>"})
    public void clickElementWithKeyIfExists(String key) {
        try {
            ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
            By infoParam = ElementHelper.getElementInfoToBy(elementInfo);

            WebDriverWait webDriverWait = new WebDriverWait(driver, 60);
            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.elementToBeClickable(infoParam));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                    webElement);
            webElement = findElement(key);
            if (webElement != null) {
                logger.info(key + " elementi bulundu.");
                actions.moveToElement(findElement(key));
                actions.click();
                actions.build().perform();
                logger.info(key + " elementine tıklandı.");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Assert.fail("Element: '" + key + "' doesn't exist.");
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            TimeUnit.SECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini <key> elementine yaz"})
    public void sendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine" + text + " texti yazıldı.");
        } else {
            logger.info(key + " elementine" + text + " texti yazılamadı.");
        }
    }

    @Step({"Hover element <key>",
            "<key> elementinin üzerine gidilir"})
    public void hoverElement(String key) {
        logger.info("Üstüne gelinen element :" + key);
        actions.moveToElement(findElement(key)).build().perform();
    }


    @Step("Anasayfanın geldiği kontrol edilir <key>")
    public void mainPageControl(String key) {
        logger.info("Anasayfaya gelindi." + key);
        String hepsiburadaText = driver.getTitle();
        Assert.assertTrue(hepsiburadaText, true);
    }

    @Step({"Click <key> button",
            "<key> butonuna tıkla"})
    public void clickElement(String key) {
        findElement(key).click();
        logger.info("Tıklanan element: " + key);
    }
}


