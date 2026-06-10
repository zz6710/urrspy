package com.kayak.pms.opFlow.sql;

import com.kayak.core.util.FileUtil;
import com.kayak.core.util.Tools;
import com.kayak.core.util.XmlUtil;
import com.sun.org.apache.xerces.internal.impl.Constants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SqlXmlServer {

    protected static final Logger log = LoggerFactory.getLogger(SqlXmlServer.class);

    public static Map<String, Map<String, SqlConfig>> sqlCache = new ConcurrentHashMap<String, Map<String, SqlConfig>>();

    /**
     * 已读取的配置文件与sqlid对应关系<br />
     * 以每个页面配置文件的名称作为key，保存此配配置文件中有哪些sqlid定义在这里
     */
    private static List<SqlConfigFile> sqlinfoFiles = new ArrayList<SqlConfigFile>();

    public static void startAutoLoad() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                        loadSqlInfo();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    public static void loadSqlInfo() {
        Resource[] resources = FileUtil.getResources("classpath*:sql/**/**.xml");

        if (resources != null && resources.length > 0) {
            for (Resource resource : resources) {
                try {
                    loadSqlInfoFile(resource);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 加载sql-info配置文件，根据每个sqlinfo配置生成SqlConfig
     *
     * @throws IOException
     */
    public static String loadSqlInfoFile(Resource resource) throws Exception {
        String filename = resource.getFilename();
        String filepath = resource.getURI().getPath();

        String model = filename.replaceAll(".xml", "");

        SqlConfigFile confFile = SqlConfigFile.find(sqlinfoFiles, resource);
        if (confFile == null) {
            confFile = new SqlConfigFile(filename, resource.lastModified());
            sqlinfoFiles.add(confFile);
        } else {
            long lastModified = resource.lastModified();
            if (lastModified == confFile.getLastModified()) {
                // 文件没有更新，不需要重新加载
                return "";
            }

        }

        String res;

        Element root = null;// 引用读出的配置文件的根节点
        try {
            // 读取XML配置文件
            SAXReader saxReader = new SAXReader();// 创建读取xml文件的对象
            saxReader.setFeature(Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE, false);
            Document doc = saxReader.read(resource.getInputStream());// 读取xml文件
            root = doc.getRootElement();// 取根节点
        } catch (DocumentException e) {
            res = "打开配置文件失败：" + resource.getFilename();
            log.error(res, e);
            return res;
        }

        confFile.setLastModified(resource.lastModified());

        if (root == null) {// 没有根节点，直接退出
            res = "配置文件没有配置信息：" + resource.getFilename();
            log.info(res);
            return res;
        }

        // 取配置节点集合
        @SuppressWarnings("rawtypes")
        Iterator configs = root.elementIterator();
        if (configs == null) {// 没有配置节点，直接退出
            res = "配置文件没有配置信息：" + resource.getFilename();
            log.info(res);
            return res;
        }

        Map<String, SqlConfig> actionMap = new ConcurrentHashMap<String, SqlConfig>();

        int n = 0;// sqlinfo节点数累计变量
        while (configs.hasNext()) {
            n++;// sqlinfo节点数累计，用以获取当前加载第几个节点
            // 取得sqlinfo节点对象
            Element sqlinfoEl = (Element) configs.next();


            // 定义构造SqlConfig对象所需变量
            String action = XmlUtil.getAttributeString(sqlinfoEl, "action");
            String desc = XmlUtil.getAttributeString(sqlinfoEl, "desc");
            String makeSearchSql = XmlUtil.getAttributeString(sqlinfoEl, "makeSearchSql");
            String logConfig = XmlUtil.getAttributeString(sqlinfoEl, "log");
            String datasource = XmlUtil.getAttributeString(sqlinfoEl, "datasource");
            String interceptor = XmlUtil.getAttributeString(sqlinfoEl, "interceptor");
            String repeat = XmlUtil.getAttributeString(sqlinfoEl, "repeat");

            if (Tools.strIsEmpty(action)) {
                log.error("加载SQL配置文件" + filename + "时出错：第" + n + "个sqlinfo节点的action属性为空或未指定");
                continue;
            }

            boolean islog = true;

            if ("false".equals(logConfig)) {
                islog = false;
            }

            SqlConfig conf = new SqlConfig(action, desc, action, makeSearchSql, islog, Tools.str2Int(datasource), interceptor, repeat);
            // sql结点可出现多个，实现不同数据库的sql语句
            List<Element> sqlList = sqlinfoEl.elements("sql");

            if (sqlList == null || sqlList.size() < 1) {
                log.error("加载SQL配置文件" + filename + "时出错：action=" + action + "的sqlinfo > sql节点未指定或值为空");
                continue;
            }
            if (sqlList != null) {
                for (Element elSql : sqlList) {
                    String sql = XmlUtil.getElementTextTrim(elSql);
                    String dialect = XmlUtil.getAttributeString(elSql, "dialect");
                    conf.addSql(sql, dialect);
                }
            }

            List<Element> actionRefList = sqlinfoEl.elements("actionref");

            if (actionRefList != null && actionRefList.size() > 0) {
                for (Element actionRefEl : actionRefList) {
                    String refModel = XmlUtil.getAttributeString(actionRefEl, "model");
                    String refAction = XmlUtil.getAttributeString(actionRefEl, "action");
                    ActionRef actionRef = new ActionRef();

                    actionRef.setModel(refModel);
                    actionRef.setAction(refAction);
                    conf.addActionRef(actionRef);
                }
            }

            actionMap.put(action, conf);

            //check
            Element checksEl = sqlinfoEl.element("checks");
            if (checksEl != null) {
                Iterator checkIt = checksEl.elementIterator("check");
                if (checkIt != null) {
                    List<SqlCheck> checks = new ArrayList<SqlCheck>();// 用于保存读取到的配置信息
                    conf.setChecks(checks);
                    int nc = 0;// 用于计数，表示读取到第几个check节点配置
                    while (checkIt.hasNext()) {
                        nc++;
                        Element checkEl = (Element) checkIt.next();
                        String checkname = null, checksql = null, checkstring = null, compareval = null, comparesign = null, errtext = null;
                        Boolean exitall = null;
                        int checkDatasource = XmlUtil.getAttributeInt(checkEl,
                                "datasource", 0);

                        checkname = XmlUtil.getElementTextTrim(checkEl
                                .element("checkname"));
                        checksql = XmlUtil.getElementTextTrim(checkEl
                                .element("checksql"));
                        List<Element> sqlEls = checkEl.elements("checksql");
                        checkstring = XmlUtil.getElementTextTrim(checkEl
                                .element("checkstring"));
                        if (Tools.strIsEmpty(checksql)
                                && Tools.strIsEmpty(checkstring)) {
                            log.error("加载SQL配置文件"
                                    + filename
                                    + "时出错：model="
                                    + model
                                    + "的sqlinfo > checks的第"
                                    + nc
                                    + "个check节点中必须指定checksql/checksqlid/checkstring其中一个作为子节点");
                            continue;
                        }
                        compareval = XmlUtil.getElementTextTrim(checkEl
                                .element("compareval"));
                        if (Tools.strIsEmpty(compareval)) {
                            log.error("加载SQL配置文件" + filename + "时出错：model="
                                    + model + "的sqlinfo > checks的第" + nc
                                    + "个check > compareval节点未指定或值为空");
                            continue;
                        }
                        comparesign = XmlUtil.getAttributeString(checkEl,
                                "comparesign", "eql");
                        if (Tools.strIsEmpty(comparesign)) {
                            log.error("加载SQL配置文件" + filename + "时出错：model="
                                    + model + "的sqlinfo > checks的第" + nc
                                    + "个check节点的comparesign属性为空或未指");
                            continue;
                        }
                        errtext = XmlUtil.getElementTextTrim(checkEl
                                .element("errtext"));
                        if (Tools.strIsEmpty(errtext)) {
                            log.error("加载SQL配置文件" + filename + "时出错：model="
                                    + model + "的sqlinfo > checks的第" + nc
                                    + "个check > errtext节点未指定或值为空");
                            continue;
                        }
                        exitall = XmlUtil.getAttributeBoolean(checkEl,
                                "exitall", true);
                        SqlCheck chk = new SqlCheck(checkname, checksql,
                                checkstring, compareval,
                                comparesign, errtext, exitall, checkDatasource);
                        if (sqlEls != null) {
                            for (Element e : sqlEls) {
                                String dialect = XmlUtil.getAttributeString(e,
                                        "dialect");
                                chk.addCheckSql(XmlUtil.getElementTextTrim(e),
                                        dialect);
                            }
                        }
                        checks.add(chk);
                    }
                }
            }
        }

        sqlCache.put(model, actionMap);

        res = "加载配置文件完成：" + filepath;
        log.info(res);
        return res;
    }

}
