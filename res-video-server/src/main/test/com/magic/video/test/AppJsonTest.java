package com.magic.video.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.magic.ResVideoApplication;
import com.magic.video.mapper.LongVideoCategoryInfoMapper;
import com.magic.video.mapper.LongVideoCollectionInfoMapper;
import com.magic.video.mapper.LongVideoIconInfoMapper;
import com.magic.video.mapper.LongVideoModelInfoMapper;
import com.magic.video.pojo.entity.LongVideoCategoryInfoEntity;
import com.magic.video.pojo.entity.LongVideoCollectionInfoEntity;
import com.magic.video.pojo.entity.LongVideoIconInfoEntity;
import com.magic.video.pojo.entity.LongVideoModelInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ResVideoApplication.class})
public class AppJsonTest {

    @Autowired
    private LongVideoCollectionInfoMapper longVideoCollectionInfoMapper;
    @Autowired
    private LongVideoCategoryInfoMapper longVideoCategoryInfoMapper;
    @Autowired
    private LongVideoModelInfoMapper longVideoModelInfoMapper;
    @Autowired
    private LongVideoIconInfoMapper longVideoIconInfoMapper;

    @Data
    @AllArgsConstructor
    class CollectionSetting {
        private String collectionName;
        private String iconPath;
        private String showType;
        private String topItemBuilder;
        private String bottomItemBuilder;
        private String apiUrl;
        private String modelUid;
        private String action;
        private String tabKey;
    }

    @Data
    @AllArgsConstructor
    class Category {
        private String categoryRemark;
        private String templateName;
    }

    @org.junit.Test
    public void createClassify() throws Exception {
        String url = "http://192.168.100.51:8848/da_xiang/uncrypt.zip";
        ZipInputStream zipInputStream = getZipInputStream(url);
        //遍历zip目录下的所有文件，并保存成zip流
        handleZip(zipInputStream);
//        //解析app.json
//        JSONObject appJsonObject = parseAppJson(url);
//        //重置app.json中的settings属性
//        resetAppJsonSettings(appJsonObject);
//        //获取以类别id为key,标签名以及模板名(对应settings下的属性)为value的Map
//        Map<Integer, Category> categoryMap = categoryMap();
//        //生成合集配置
//        setCollectionSettings(appJsonObject, categoryMap);
//        //生成特殊模块(排行榜和热门分类)和广告配置
//        setModelSettings(appJsonObject, categoryMap);
//        JedisUtil.setStr("APP_JSON_1", appJsonObject.toString());
//        System.out.println(appJsonObject);

    }

    private ZipInputStream getZipInputStream(String url) {
        HttpRequest get = HttpUtil.createGet(url);
        HttpResponse response = get.execute();
        InputStream inputStream = response.bodyStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        return zipInputStream;
    }

    private byte[] zipBytes;

    private void handleZip(ZipInputStream zis) throws IOException {
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(byteOS);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            if (zipEntry.getName().startsWith("zip/") && zipEntry.getSize() > 0) {
                zos.putNextEntry(new ZipEntry(zipEntry.getName().substring("zip/".length())));
                int b;
                while ((b = zis.read()) != -1) {
                    zos.write(b);
                }
                zos.flush();
            }
        }
        zis.close();
        zos.close();
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\1.zip");
        fileOutputStream.write(byteOS.toByteArray());
        fileOutputStream.close();
    }

    private void setModelSettings(JSONObject appJsonObject, Map<Integer, Category> categoryMap) {
        List<LongVideoModelInfoEntity> longVideoModelInfoEntityList = longVideoModelInfoMapper.selectList(null);
        JSONObject settings = appJsonObject.getJSONObject("settings");
        for (LongVideoModelInfoEntity entity : longVideoModelInfoEntityList) {
            Category categoryValue = categoryMap.get(entity.getCategoryId());
            JSONArray remarkArray = settings.getJSONArray(categoryValue.getCategoryRemark());
            JSONArray modelTemplateName = settings.getJSONArray(entity.getModelTemplateName());
            if (remarkArray != null) {
                remarkArray.add(entity.getModelIndex(), new JSONObject().set("modelUid", entity.getModelUid()));
            }
            if (modelTemplateName != null) {
                modelTemplateName.add(new JSONObject().set("modelUid", entity.getModelUid()));
            }
        }
    }

    private void setCollectionSettings(JSONObject appJsonObject, Map<Integer, Category> categoryMap) {
        //生成以模板名为key的合集配置
        List<LongVideoCollectionInfoEntity> longVideoCollectionInfoEntityList = longVideoCollectionInfoMapper.selectList(null);
        List<LongVideoIconInfoEntity> longVideoIconInfoEntityList = longVideoIconInfoMapper.selectList(null);
        Map<Integer, String> iconPathMap = new HashMap<>(longVideoIconInfoEntityList.size());
        for (LongVideoIconInfoEntity longVideoIconInfoEntity : longVideoIconInfoEntityList) {
            iconPathMap.put(longVideoIconInfoEntity.getIconId(), longVideoIconInfoEntity.getIconRealPath());
        }
        JSONObject settings = appJsonObject.getJSONObject("settings");
        for (LongVideoCollectionInfoEntity entity : longVideoCollectionInfoEntityList) {
            Category categoryValue = categoryMap.get(entity.getCategoryId());
            //通过类别名获取settings下的类别数组
            JSONArray remarkArray = settings.getJSONArray(categoryValue.getCategoryRemark());
            //通过模板名获取settings下的模板数组
            JSONArray templateArray = settings.getJSONArray(categoryValue.getTemplateName());
            //通过更多模板名获取settings下的更多模板数组
            JSONArray moreTemplateArray = settings.getJSONArray(entity.getMoreTemplateName());
            if (remarkArray != null) {
                remarkArray.add(new JSONObject().set("modelUid", entity.getCollectionId()));
            }
            if (templateArray != null) {
                templateArray.add(generateCollectionSetting(entity, categoryValue, iconPathMap));
            }
            if (moreTemplateArray != null) {
                if ("all".equals(entity.getAction()) || "more".equals(entity.getAction())) {
                    moreTemplateArray.add(generateCollectionSetting(entity, categoryValue, iconPathMap));
                }
            }
        }
    }

    private JSONObject parseAppJson(String url) throws Exception {
        HttpRequest get = HttpUtil.createGet(url);
        HttpResponse response = get.execute();
        InputStream inputStream = response.bodyStream();
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        InputStreamReader isr = new InputStreamReader(zipInputStream);
        BufferedReader br = new BufferedReader(isr);
        ZipEntry zipEntry;
        StringBuilder builder;
        Map<String, JSONObject> map = new HashMap<>(200);
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().endsWith("json")) {
                builder = new StringBuilder();
                String len;
                while ((len = br.readLine()) != null) {
                    builder.append(len);
                }
                map.put(zipEntry.getName(), JSONUtil.parseObj(builder.toString()));
            }
        }
        JSONObject jsonObject = map.get("app.json");
        JSONArray customs = jsonObject.getJSONArray("customs");
        for (Object custom : customs) {
            JSONArray configs = jsonObject.getJSONArray((String) custom);
            for (int index = 0; index < configs.size(); index++) {
                Object config = configs.get(index);
                JSONObject configJson = map.get("conf/" + custom + "/" + config + ".json");
                configs.set(index, configJson);
            }
        }
        return jsonObject;
    }

    private void resetAppJsonSettings(JSONObject jsonObject) {
        JSONObject settings = jsonObject.getJSONObject("settings");
        for (Map.Entry<String, Object> entry : settings.entrySet()) {
            JSONArray jsonArray = settings.getJSONArray(entry.getKey());
            jsonArray.clear();
        }
    }

    private Map<Integer, Category> categoryMap() {
        List<LongVideoCategoryInfoEntity> longVideoCategoryInfoEntityList = longVideoCategoryInfoMapper.selectList(null);
        Map<Integer, Category> categoryMap = new HashMap<>(longVideoCategoryInfoEntityList.size());
        for (LongVideoCategoryInfoEntity entity : longVideoCategoryInfoEntityList) {
            categoryMap.put(entity.getCategoryId(), new Category(entity.getCategoryRemark(), entity.getTemplateName()));
        }
        return categoryMap;
    }

    private CollectionSetting generateCollectionSetting(LongVideoCollectionInfoEntity entity, Category categoryValue, Map<Integer, String> iconPathMap) {
        CollectionSetting collectionSetting = new CollectionSetting(
                entity.getCollectionName(),
                iconPathMap.get(entity.getCollectionIconId()),
                entity.getShowType(),
                entity.getShowTypeTopItemBuilder() == null ? "" : entity.getShowTypeTopItemBuilder(),
                entity.getShowTypeBottomItemBuilder(),
                entity.getApiUrl(),
                String.valueOf(entity.getCollectionId()),
                entity.getAction(),
                categoryValue.getCategoryRemark()
        );
        return collectionSetting;
    }

}
