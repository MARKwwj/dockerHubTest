package com.magic.video.test;

import com.magic.ResVideoApplication;
import com.magic.video.mapper.*;
import com.magic.video.pojo.entity.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ResVideoApplication.class})
public class Test {

    @Autowired
    private LongVideoClassifyInfoMapper longVideoClassifyInfoMapper;
    @Autowired
    private LongVideoVideoInfoMapper longVideoVideoInfoMapper;
    @Autowired
    private LongVideoCollectionInfoMapper longVideoCollectionInfoMapper;
    @Autowired
    private LongVideoCategoryRelationMapper longVideoCategoryRelationMapper;
    @Autowired
    private LongVideoClassifyRelationMapper longVideoClassifyRelationMapper;
    @Autowired
    private LongVideoCollectionRelationMapper longVideoCollectionRelationMapper;

    @org.junit.Test
    public void createClassify() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 10; j++) {
                longVideoClassifyInfoMapper.insert(new LongVideoClassifyInfoEntity()
                        .setClassifyId(null)
                        .setCategoryId(i)
                        .setClassifyName("分类" + j)
                        .setClassifySort(j)
                );
            }
        }
    }

    @org.junit.Test
    public void createVideoInfo() {
        for (int i = 55; i <= 10000; i++) {
            longVideoVideoInfoMapper.insert(new LongVideoVideoInfoEntity()
                    .setVideoId(null)
                    .setVideoTitle("视频" + i)
                    .setVideoIntro("简介" + i)
                    .setVideoDuration(3000)
                    .setVideoSerialNum("番号" + i)
                    .setVideoTags("[{\"tagId\": 1, \"tagName\": \"1\", \"categoryId\": 1}, {\"tagId\": 1, \"tagName\": \"1\", \"categoryId\": 1}]")
                    .setVideoMachineId(1)
                    .setVideoMachineName("资源服务器1")
                    .setVideoPayType(1)
                    .setVideoPayCoin(10 * (i % 5))
                    .setVideoScore((Math.random() + 8) * 10)
                    .setVideoPlayCount(841441L * i)
                    .setVideoPraiseCount(241441L * i)
                    .setVideoFavoriteCount(341441L * i)
                    .setVideoStatus(1)
                    .setVideoCreator("Auto")
                    .setVideoCreateTime(new Date())
                    .setVideoUpdater("Auto")
                    .setVideoUpdateTime(new Date())
                    .setVideoByteSize(1234L * i)
            );
        }
    }

    @org.junit.Test
    public void createCollectionInfo() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                longVideoCollectionInfoMapper.insert(new LongVideoCollectionInfoEntity()
                        .setCollectionId(null)
                        .setCategoryId(i)
                        .setCollectionName("合集" + j)
                        .setCollectionSort(j)
                        .setShowType("1")
                        .setAction("all")
                );
            }
        }
    }

    @org.junit.Test
    public void createCateGoryRelation() {
        for (int i = 1; i <= 7; i++) {
            for (int j = 1; j <= 50; j++) {
                longVideoCategoryRelationMapper.insert(new LongVideoCategoryRelationEntity()
                        .setRelationId(null)
                        .setCategoryId(i)
                        .setVideoId(j)
                );
            }
        }
    }

    @org.junit.Test
    public void createClassifyRelation() {
        for (int i = 1; i <= 50; i++) {
            for (int j = 1; j <= 50; j++) {
                longVideoClassifyRelationMapper.insert(new LongVideoClassifyRelationEntity()
                        .setRelationId(null)
                        .setClassifyId(i)
                        .setVideoId(j)
                );
            }
        }
    }

    @org.junit.Test
    public void createCollectionRelation() {
        for (int i = 1; i <= 25; i++) {
            for (int j = 1; j <= 50; j++) {
                longVideoCollectionRelationMapper.insert(new LongVideoCollectionRelationEntity()
                        .setRelationId(null)
                        .setCollectionId(i)
                        .setVideoId(j)
                );
            }
        }
    }

    @org.junit.Test
    public void createTag() {
        for (int i = 1; i <= 25; i++) {
            for (int j = 1; j <= 50; j++) {
                longVideoCollectionRelationMapper.insert(new LongVideoCollectionRelationEntity()
                        .setRelationId(null)
                        .setCollectionId(i)
                        .setVideoId(j)
                );
            }
        }
    }

}
