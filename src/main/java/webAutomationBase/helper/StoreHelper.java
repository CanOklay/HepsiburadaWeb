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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public enum StoreHelper {

    INSTANCE;
    Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DIRECTORY_PATH = "elementValues";
}
