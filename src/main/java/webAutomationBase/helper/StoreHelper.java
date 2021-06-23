package webAutomationBase.helper;
import webAutomationBase.model.ElementInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public enum StoreHelper {

    INSTANCE;
    Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DIRECTORY_PATH = "elementValues";
    ArrayList<ElementInfo> elementInfoList;

    StoreHelper() {
        initMap(getFileList());
    }

    private void initMap(File[] fileList) {
        elementInfoList = new ArrayList<>();
        Type elementType = new TypeToken<List<ElementInfo>>(){}.getType();
        Gson gson = new Gson();
        for (File file : fileList) {
            try {
                elementInfoList = gson.fromJson(new FileReader(file), elementType);
            }
            catch (FileNotFoundException e) {
                logger.warn("{} not found", e);
            }
        }
    }

    private File[] getFileList() {
        File[] fileList = new File(
                this.getClass().getClassLoader().getResource(DIRECTORY_PATH).getFile())
                .listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
        if (fileList == null) {
            logger.warn(
                    "File Directory Is Not Found! Please Check Directory Location. Default Directory Path = {}",
                    DIRECTORY_PATH);
            throw new NullPointerException();
        }
        return fileList;
    }

    public void printAllValues() {
        elementInfoList.forEach(element-> logger.info("Key = {} value = {}", element.getKey(), element.getValue()));
    }

    public ElementInfo findElementInfoByKey(String key){
        List<ElementInfo> results =  elementInfoList.stream()
                .filter(elementInfo -> elementInfo.getKey()
                        .equals(key)).collect(Collectors.toList());
        try{
            return results.get(0);
        }catch (Exception e){
            return null;
        }
    }

    public void saveValue(int key, ElementInfo value) {
        elementInfoList.add(key, value);
    }

    public ElementInfo getValue(String key) {
        return elementInfoList.get(0);
    }

}
