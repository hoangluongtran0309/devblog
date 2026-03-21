package com.hoangluongtran0309.devblog.content.tag;

public class TagRepositoryCustomImpl implements TagRepositoryCustom {

    @Override
    public TagId nextId() {
        return TagId.generate();
    }
}
