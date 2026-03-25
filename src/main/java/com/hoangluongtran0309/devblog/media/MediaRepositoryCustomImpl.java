package com.hoangluongtran0309.devblog.media;

public class MediaRepositoryCustomImpl implements MediaRepositoryCustom {

    @Override
    public MediaId nextId() {
        return MediaId.generate();
    }
}
