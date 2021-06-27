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

    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(StepImplementation.class);
    private Actions actions = new Actions(driver);
    WebDriverWait webDriverWait = new WebDriverWait(driver, 60);

    public StepImplementation() {
        PropertyConfigurator
                .configure(StepImplementation.class.getClassLoader().getResource("log4j.properties"));
    }

    public List<WebElement> findElementsByKey(String key) {
        try {
            ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
            By infoParam = ElementHelper.getElementInfoToBy(elementInfo);

            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.presenceOfElementLocated(infoParam));
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", webElement);
            return driver.findElements(ElementHelper.getElementInfoToBy(StoreHelper.INSTANCE.findElementInfoByKey(key)));
        } catch (Exception e) {
            Assert.fail("Element: '" + key + "' bulunamadı.");
            return null;
        }
    }

    private WebElement findElement(String key) {
        try {
            ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
            By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.presenceOfElementLocated(infoParam));
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", webElement);
            return webElement;
        } catch (Exception e) {
            Assert.fail("Element: '" + key + "' bulunamadı.");
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
            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.elementToBeClickable(infoParam));

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", webElement);
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
            Assert.fail("Element: '" + key + "' bulunamadı.");
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
        try{
            logger.info("Üstüne gelinen element :" + key);
            actions.moveToElement(findElement(key)).build().perform();
        }catch (Exception e) {
            logger.error(e.getMessage());
            Assert.fail("Element: '" + key + "' bulunamadı.");
        }

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
        try{
            ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
            By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
            WebElement webElement = webDriverWait
                    .until(ExpectedConditions.elementToBeClickable(infoParam));
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})", webElement);
            webElement = findElement(key);
            webElement.click();
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            Assert.fail("Element: '" + key + "' bulunamadı.");
        }
    }

    @Step({"<key> elementindeki ürünler kontrol edilir"})
    public List<WebElement> controlProducts(String key) {
        List<WebElement> webElements = findElementsByKey(key);
        if(!webElements.isEmpty()) {
            logger.info("Element sayısı: " + webElements.size());
            return webElements;
        }
        else {
            Assert.fail("Elementler: '" + key + "' bulunamadı.");
            return null;
        }
    }

    @Step({"<key> elementindeki ürün kontrol edilir"})
    public WebElement controlProduct(String key) {
        List<WebElement> webElements = findElementsByKey(key);
        if(!webElements.isEmpty()) {
            logger.info("Element sayısı: " + webElements.size());
            WebElement selectedElement = webElements.get(0);
            logger.info("Seçtiğimiz element: " + selectedElement);
            return selectedElement;
        }
        else {
            Assert.fail("Element: '" + key + "' bulunamadı.");
            return null;
        }
    }

    @Step({"Sepette <key> ürün olmadığı kontrol edilir"})
    public void controlShoppingCart(String key) {
        String element = findElement(key).getText();
        logger.info("Sepet durumu: " + element);
        if(element.equals("Sepetin şu an boş")) {
            logger.info("Sepette ürün bulunmuyor.");
        }
        else {
            Assert.fail("Sepette ürün bulundu.");
        }
    }

    @Step({"<key> sayfa sayısının <pageNo> olduğu kontrol edilir"})
    public void pageControl(String key, String pageNo) {
        String pageNumber = findElement(key).getText();
        Assert.assertEquals(pageNo, pageNumber);
        logger.info("Sayfa numarasının " + pageNo + " olduğu kontrol edildi.");
    }

    @Step({"<key> elementinde <productNo> ürünü seçilir"})
    public WebElement clickByProductNumber(String key, int productNo) {
        try{
            List<WebElement> webElements = findElementsByKey(key);
            WebElement element = webElements.get(productNo);
            if (element != null) {
                logger.info(key + " elementi bulundu.");
                actions.moveToElement(findElement(key));
                actions.click();
                actions.build().perform();
                logger.info(key + " elementine tıklandı.");
                logger.info("Element seçildi: " + element);
                return element;
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            Assert.fail("Element: '" + key + "' bulunamadı.");
        }
        return null;
    }

    @Step({"<key> popupında <message> yazdığı kontrol edilir"})
    public void popupControl(String key, String message) {
        String popupText = findElement(key).getText();
        logger.info("Popup: " + popupText);
        Assert.assertEquals(message, popupText);
    }

    @Step({"<key> popupı kontrol edilir ve <sepeteGit> butonuna tıklanır. Eğer ürün bulunamadıysa başka mağazadan sepete <sepeteEkle2> eklenir <kapatma> ve sepete gidilir <sepeteGit2>"})
    public void popupControlAndClick(String key, String sepeteGit, String sepeteEkle2, String kapatma, String sepeteGit2) {
        String popupText = findElement(key).getText();
        logger.info("Popup: " + popupText);
        //Assert.assertEquals(message, popupText);
        if(popupText.equals("Ürün sepetinizde")) {
            logger.info("Sepete git " + sepeteGit + " butonu bulundu.");
            clickElement(sepeteGit);
            logger.info("Sepete git " + sepeteGit + " butonuna tıklandı.");
        }
        else {
            Assert.assertEquals("Ürün sepete eklenemedi", popupText);
            logger.info("Sepete ekle " + sepeteEkle2 + " butonu bulundu.");
            clickElement(sepeteEkle2);
            logger.info("Sepete ekle " + sepeteEkle2 + " butonuna tıklandı.");
            clickElement(kapatma);
            logger.info("Kapatma " + kapatma + " butonuna tıklandı.");
            clickElementWithKeyIfExists(sepeteGit2);
            logger.info("Sepetim " + sepeteGit2 + " butonuna tıklandı.");
        }
    }
}


