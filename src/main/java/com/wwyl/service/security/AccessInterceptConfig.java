package com.wwyl.service.security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wwyl.entity.security.Operator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

/**
 * @author fyunli
 */
@Service
public class AccessInterceptConfig {

    private static Logger logger = LoggerFactory.getLogger(AccessInterceptConfig.class);
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final String DEFAULT_CONFIG_FILE = "/access-intercepts.xml";
    private Map<String, String> accessInterceptMap = new LinkedHashMap<String, String>();

    public AccessInterceptConfig() {
        this(DEFAULT_CONFIG_FILE);
    }

    @SuppressWarnings("unchecked")
    public AccessInterceptConfig(String config) {
        Assert.notNull(config);
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream(config);
            Document document = new SAXReader().read(in);
            Element rootElement = document.getRootElement();
            if (rootElement == null || !rootElement.getName().equals("url-intercepts")) {
                throw new RuntimeException("invalid config file.");
            }

            for (Iterator<Element> iterator = rootElement.elementIterator("url-intercept"); iterator.hasNext(); ) {
                parseMapping((Element) iterator.next());
            }
        } catch (DocumentException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseMapping(Element element) {
        String path = element.attributeValue("pattern");
        String permission = element.attributeValue("permission");
        accessInterceptMap.put(path, permission);
    }

    public String getPermission(String path) {
        String permission = null;
        for (String key : accessInterceptMap.keySet()) {
            if (ANT_PATH_MATCHER.match(key, path)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("{} matches the pattern {}.", path, key);
                }
                permission = accessInterceptMap.get(key);
            }
        }
        return permission;
    }

    public boolean checkPermission(String path, Operator operator) throws Exception, IOException {
        String permCode = getPermission(path);
        if (permCode == null || operator.hasPermission(permCode)) {
            return true;
        } else {
            if (operator.getId() == 1L) {// 超级管理员 id=1('admin')
                return true;
            } else {
                return false;
            }
        }
    }
}
