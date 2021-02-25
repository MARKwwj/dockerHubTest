package com.magic.video.common.consts;

import lombok.Data;

@Data
public class VideoStatus {

    public static final int DISABLE = 0;
    public static final int ENABLE = 1;
    public static final int NOT_UPLOAD = 2;
    public static final int UPLOADING = 3;
    public static final int NOT_EDITED = 4;

}
