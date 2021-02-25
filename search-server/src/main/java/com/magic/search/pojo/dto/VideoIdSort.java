package com.magic.search.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class VideoIdSort implements Comparable<VideoIdSort> {
    private Integer videoId;
    private Integer sort;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VideoIdSort that = (VideoIdSort) o;
        return Objects.equals(videoId, that.videoId) &&
                Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, sort);
    }

    @Override
    public int compareTo(VideoIdSort o) {
        if (this.getSort() > o.getSort()) {
            return -1;
        }
        if (this.getVideoId().equals(o.getVideoId()) && this.getSort().equals(o.getSort())) {
            return 0;
        }
        return 1;
    }
}
