package com.hoangluongtran0309.devblog.content.tag;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoangluongtran0309.devblog.content.shared.Slug;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private static final Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsBySlug(Slug tagSlug) {
        return tagRepository.existsByTagSlug(tagSlug);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(TagName tagName) {
        return tagRepository.existsByTagName(tagName);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> getBySlug(Slug tagSlug) {
        return tagRepository.findByTagSlug(tagSlug);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> getById(TagId tagId) {
        return tagRepository.findById(tagId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Tag> getAll(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Tag> getAllById(Iterable<TagId> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    @Override
    public Tag create(TagParameters parameters) {
        logger.info("Creating tag with name: {}", parameters.getTagName());

        validateForCreate(parameters);

        Tag tag = new Tag(
                tagRepository.nextId(),
                parameters.getTagName(),
                parameters.getTagSlug());

        logger.info("Tag created with ID: {}", tag.getId().asString());

        return tagRepository.save(tag);
    }

    @Override
    public Tag update(TagId tagId, TagParameters parameters) {
        logger.info("Updating tag with ID: {}", tagId.asString());

        Tag tag = getById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        validateForUpdate(tag, parameters);

        tag.setTagName(parameters.getTagName());
        tag.setTagSlug(parameters.getTagSlug());

        logger.info("Tag updated with ID: {}", tagId.asString());

        return tagRepository.save(tag);
    }

    @Override
    public void delete(TagId tagId) {
        logger.info("Deleting tag with ID: {}", tagId.asString());

        Tag tag = getById(tagId)
                .orElseThrow(() -> new TagNotFoundException(tagId));

        tagRepository.delete(tag);

        logger.info("Tag deleted with ID: {}", tagId.asString());
    }

    private void validateForCreate(TagParameters parameters) {
        if (existsBySlug(parameters.getTagSlug())) {
            throw new TagAlreadyExistsException(parameters.getTagSlug());
        }

        if (existsByName(parameters.getTagName())) {
            throw new TagAlreadyExistsException(parameters.getTagName());
        }
    }

    private void validateForUpdate(Tag tag, TagParameters parameters) {
        if (!tag.getTagSlug().equals(parameters.getTagSlug())
                && existsBySlug(parameters.getTagSlug())) {
            throw new TagAlreadyExistsException(parameters.getTagSlug());
        }

        if (!tag.getTagName().equals(parameters.getTagName())
                && existsByName(parameters.getTagName())) {
            throw new TagAlreadyExistsException(parameters.getTagName());
        }
    }
}