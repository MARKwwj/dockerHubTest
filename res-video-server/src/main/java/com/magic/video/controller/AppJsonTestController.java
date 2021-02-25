package com.magic.video.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.common.utils.JacksonUtils;
import com.magic.framework.redis.JedisUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/appJson/create")
public class AppJsonTestController {
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

    @RequestMapping("/zip.zip")
    public byte[] zip(HttpServletResponse response) {
        byte[] zipEncryptedBytes = JedisUtil.get("ZIP".getBytes());
        encrypted(zipEncryptedBytes);
        return zipEncryptedBytes;
    }

    static final int[] keyBytes = {
            0x59, 0x3d, 0x94, 0x69, 0x28, 0xed, 0xff, 0xe3, 0xc3, 0x6f, 0xac, 0xc5, 0xec, 0x52, 0x2b, 0xa3,
            0x79, 0x4e, 0xf5, 0x32, 0x43, 0x75, 0x88, 0x03, 0x1a, 0x84, 0x34, 0xcc, 0xb6, 0x53, 0x0d, 0x92,
            0x15, 0xd7, 0x2f, 0x30, 0xbd, 0x60, 0xb5, 0x17, 0x01, 0x9e, 0xdb, 0xb8, 0x56, 0x70, 0x33, 0x54,
            0x2e, 0x3b, 0xbf, 0x6a, 0x8c, 0x04, 0x41, 0xad, 0x6d, 0xf8, 0x58, 0x35, 0x98, 0x99, 0x24, 0x73,
            0x25, 0xf2, 0xb1, 0x5a, 0xb3, 0xc4, 0x8f, 0xd9, 0xef, 0xfb, 0x45, 0xc1, 0x37, 0x2a, 0x93, 0x4c,
            0x86, 0xda, 0x09, 0xae, 0x8d, 0xd8, 0x8a, 0x81, 0x7c, 0x44, 0x6b, 0xea, 0xf9, 0x66, 0x40, 0xa9,
            0xb9, 0x7a, 0x38, 0xe5, 0x29, 0xfc, 0x7f, 0x12, 0x4f, 0xcd, 0xba, 0xc7, 0x6c, 0xd6, 0xa0, 0x10,
            0xe2, 0x5e, 0xf0, 0xd1, 0xfd, 0xbb, 0xa6, 0x63, 0x05, 0xd5, 0x22, 0x9b, 0x9a, 0x00, 0xd3, 0x61,
            0x48, 0xaf, 0xee, 0xa7, 0x46, 0x77, 0x1f, 0x71, 0xde, 0x02, 0x42, 0x9c, 0xa2, 0xc2, 0xcf, 0xce,
            0x9d, 0x64, 0xf6, 0xca, 0xab, 0x14, 0x36, 0x0b, 0xd0, 0xdc, 0x3e, 0x7e, 0x0e, 0x72, 0x5f, 0x20,
            0x49, 0xa5, 0xfe, 0x23, 0xf4, 0x51, 0x95, 0x89, 0x87, 0xb0, 0x5d, 0x0f, 0x2c, 0x39, 0xa4, 0x5c,
            0xf1, 0x13, 0xcb, 0x57, 0x06, 0xf3, 0xeb, 0x97, 0x0c, 0x18, 0xb7, 0x21, 0xbc, 0x90, 0xe0, 0xb2,
            0x96, 0x1b, 0x27, 0xe9, 0x74, 0x19, 0x67, 0x6e, 0xc9, 0x55, 0xc0, 0x9f, 0xe8, 0xbe, 0xd4, 0x7b,
            0x83, 0x16, 0xd2, 0x2d, 0x4a, 0x0a, 0x7d, 0xb4, 0x82, 0x4d, 0xdd, 0x85, 0x3c, 0xe6, 0x50, 0x4b,
            0xc6, 0x80, 0xf7, 0x1d, 0xe7, 0x76, 0x3f, 0xfa, 0xe1, 0x78, 0xa8, 0x68, 0xa1, 0x1c, 0x91, 0xdf,
            0xaa, 0x3a, 0x26, 0x08, 0x8e, 0x62, 0x47, 0x5b, 0x1e, 0x65, 0xc8, 0x07, 0x11, 0x8b, 0x31, 0xe4,
    };

    @RequestMapping
    public void createClassify(@RequestBody String url) {
        //String url = "http://192.168.100.51:8848/da_xiang/uncrypt.zip";
        ZipInputStream zipInputStream = getZipInputStream(url);
        //遍历zip目录下的所有文件，并保存成zip流
        byte[] zipBytes = handleZip(zipInputStream);
        //加密zip bytes
        //encrypted(zipBytes);
        JedisUtil.set("ZIP".getBytes(), zipBytes);
        zipInputStream = getZipInputStream(url);
        //解析app.json
        JSONObject appJsonObject = parseAppJson(zipInputStream);
        //重置app.json中的settings属性
        resetAppJsonSettings(appJsonObject);
        //获取以类别id为key,标签名以及模板名(对应settings下的属性)为value的Map
        Map<Integer, Category> categoryMap = categoryMap();
        //生成合集配置
        setCollectionSettings(appJsonObject, categoryMap);
        //生成特殊模块(排行榜和热门分类)和广告配置
        setModelSettings(appJsonObject, categoryMap);
        byte[] appJsonBytes = JacksonUtils.toJson(appJsonObject).getBytes();
        //加密appJson bytes
        //encrypted(appJsonBytes);
        JedisUtil.set("APP_JSON".getBytes(), appJsonBytes);
    }


    private void encrypted(byte[] bytes) {
        for (int index = 0; index < bytes.length; index++) {
            bytes[index] ^= keyBytes[index % keyBytes.length];
        }
    }

    private ZipInputStream getZipInputStream(String url) {
        HttpRequest get = HttpUtil.createGet(url);
        HttpResponse response = get.execute();
        InputStream inputStream = response.bodyStream();
        return new ZipInputStream(inputStream);
    }

    private byte[] handleZip(ZipInputStream zipInputStream) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bytes);
        ZipEntry zipEntry;
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().startsWith("zip/") && zipEntry.getSize() > 0) {
                    zos.putNextEntry(new ZipEntry(zipEntry.getName()));
                    int b;
                    while ((b = zipInputStream.read()) != -1) {
                        zos.write(b);
                    }
                    zos.flush();
                    zipInputStream.closeEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipInputStream != null) {
                    zipInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes.toByteArray();
    }


    private JSONObject parseAppJson(ZipInputStream zipInputStream) {
        InputStreamReader isr = new InputStreamReader(zipInputStream);
        BufferedReader br = new BufferedReader(isr);
        ZipEntry zipEntry;
        StringBuilder builder;
        Map<String, Map<String, Object>> map = new HashMap<>(200);
        try {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.getName().endsWith("json")) {
                    builder = new StringBuilder();
                    String len;
                    while ((len = br.readLine()) != null) {
                        builder.append(len);
                    }
                    map.put(zipEntry.getName(), JSONUtil.toBean(builder.toString(), Map.class));
                    zipInputStream.closeEntry();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map appJsonStr = map.get("app.json");
        JSONObject jsonObject = JSONUtil.parseObj(appJsonStr);
        JSONArray customsArray = jsonObject.getJSONArray("customs");
        Map<String, Object> customsJsonObject = new JSONObject();
        for (Object custom : customsArray) {
            JSONArray configs = jsonObject.getJSONArray((String) custom);
            for (Object config : configs) {
                String customName = "conf/" + custom + "/" + config + ".json";
                Object configJson = map.get(customName);
                customsJsonObject.put((String) config, configJson);
            }
            jsonObject.remove(custom);
        }
        jsonObject.set("customs", customsJsonObject);
        return jsonObject;
    }

    private void setModelSettings(JSONObject appJsonObject, Map<Integer, Category> categoryMap) {
        List<LongVideoModelInfoEntity> longVideoModelInfoEntityList = longVideoModelInfoMapper.selectList(null);
        JSONObject settings = appJsonObject.getJSONObject("settings");
        for (LongVideoModelInfoEntity entity : longVideoModelInfoEntityList) {
            Category categoryValue = categoryMap.get(entity.getCategoryId());
            JSONArray remarkArray = settings.getJSONArray(categoryValue.getCategoryRemark());
            JSONArray modelTemplateName = settings.getJSONArray(entity.getModelTemplateName());
            if (remarkArray != null) {
                remarkArray.add(entity.getModelIndex() - 1, new JSONObject().set("modelUid", entity.getModelUid()));
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
                    moreTemplateArray.add(generateMoreSetting(entity, categoryValue, iconPathMap));
                }
            }
        }
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
        return new CollectionSetting(
                entity.getCollectionName() == null ? "" : entity.getCollectionName(),
                iconPathMap.get(entity.getCollectionIconId()) == null ? "" : iconPathMap.get(entity.getCollectionIconId()),
                entity.getShowType() == null ? "" : entity.getShowType(),
                entity.getShowTypeTopItemBuilder() == null ? "" : entity.getShowTypeTopItemBuilder(),
                entity.getShowTypeBottomItemBuilder() == null ? "" : entity.getShowTypeBottomItemBuilder(),
                entity.getApiUrl() == null ? "" : entity.getApiUrl(),
                entity.getCollectionId() == null ? "" : String.valueOf(entity.getCollectionId()),
                entity.getAction() == null ? "" : entity.getAction(),
                categoryValue.getCategoryRemark() == null ? "" : categoryValue.getCategoryRemark()
        );
    }

    private CollectionSetting generateMoreSetting(LongVideoCollectionInfoEntity entity, Category categoryValue, Map<Integer, String> iconPathMap) {
        return new CollectionSetting(
                entity.getCollectionName() == null ? "" : entity.getCollectionName(),
                iconPathMap.get(entity.getCollectionIconId()) == null ? "" : iconPathMap.get(entity.getCollectionIconId()),
                entity.getShowType() == null ? "" : entity.getShowType(),
                entity.getMoreShowTypeTopItemBuilder() == null ? "" : entity.getMoreShowTypeTopItemBuilder(),
                entity.getMoreShowTypeBottomItemBuilder() == null ? "" : entity.getMoreShowTypeBottomItemBuilder(),
                entity.getApiUrl() == null ? "" : entity.getApiUrl(),
                entity.getCollectionId() == null ? "" : String.valueOf(entity.getCollectionId()),
                entity.getAction() == null ? "" : entity.getAction(),
                categoryValue.getCategoryRemark() == null ? "" : categoryValue.getCategoryRemark()
        );
    }
}

