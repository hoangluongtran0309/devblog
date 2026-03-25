package com.hoangluongtran0309.devblog.media.web;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.hoangluongtran0309.devblog.infrastructure.config.TestConfig;
import com.hoangluongtran0309.devblog.infrastructure.exception.BusinessException;
import com.hoangluongtran0309.devblog.infrastructure.security.StubApplicationUserDetailsService;
import com.hoangluongtran0309.devblog.infrastructure.web.GlobalModelAttributeAdvice;
import com.hoangluongtran0309.devblog.media.Media;
import com.hoangluongtran0309.devblog.media.MediaFileName;
import com.hoangluongtran0309.devblog.media.MediaId;
import com.hoangluongtran0309.devblog.media.MediaPublicId;
import com.hoangluongtran0309.devblog.media.MediaService;
import com.hoangluongtran0309.devblog.media.MediaUrl;

@WebMvcTest(controllers = AdminMediaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GlobalModelAttributeAdvice.class))
@Import(TestConfig.class)
@TestPropertySource(properties = "devblog.remember-me-key=test-secret-key")
class AdminMediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MediaService mediaService;

    private MediaId mediaId;
    private Media media;

    @BeforeEach
    void setUp() {
        mediaId = MediaId.generate();
        media = new Media(
                mediaId,
                new MediaPublicId("devblog/media/image123"),
                new MediaUrl("https://res.cloudinary.com/demo/image/upload/sample.jpg"),
                new MediaFileName("sample.jpg"),
                com.hoangluongtran0309.devblog.media.MediaType.IMAGE,
                204800L);
    }

    @Test
    void listMediaRedirectsToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admin/media"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void uploadRedirectsToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(multipart("/admin/media/upload")
                .file(new MockMultipartFile("files", "img.jpg",
                        MediaType.IMAGE_JPEG_VALUE, new byte[1]))
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void deleteRedirectsToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/admin/media/" + mediaId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void listMediaDefaultReturnsAllMedia() throws Exception {
        Page<Media> page = new PageImpl<>(List.of(media));
        when(mediaService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/media"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/media/list"))
                .andExpect(model().attributeExists("page"))
                .andExpect(model().attribute("selectedType", "all"));

        verify(mediaService).getAll(any(Pageable.class));
        verify(mediaService, never()).getImages(any());
        verify(mediaService, never()).getVideos(any());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void listMediaWithTypeImageFiltersImages() throws Exception {
        Page<Media> page = new PageImpl<>(List.of(media));
        when(mediaService.getImages(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/media").param("type", "image"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/media/list"))
                .andExpect(model().attribute("selectedType", "image"));

        verify(mediaService).getImages(any(Pageable.class));
        verify(mediaService, never()).getAll(any());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void listMediaWithTypeVideoFiltersVideos() throws Exception {
        Page<Media> page = new PageImpl<>(List.of());
        when(mediaService.getVideos(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/media").param("type", "video"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/media/list"))
                .andExpect(model().attribute("selectedType", "video"));

        verify(mediaService).getVideos(any(Pageable.class));
        verify(mediaService, never()).getAll(any());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void listMediaWithUnknownTypeFallsBackToAll() throws Exception {
        Page<Media> page = new PageImpl<>(List.of(media));
        when(mediaService.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/media").param("type", "unknown"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("selectedType", "unknown"));

        verify(mediaService).getAll(any(Pageable.class));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void listMediaReturnsEmptyPage() throws Exception {
        when(mediaService.getAll(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/admin/media"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/media/list"))
                .andExpect(model().attributeExists("page"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void mediaPickerReturnsImagesOnly() throws Exception {
        Page<Media> page = new PageImpl<>(List.of(media));
        when(mediaService.getImages(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/admin/media/picker"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/media/picker"))
                .andExpect(model().attributeExists("pickerPage"));

        verify(mediaService).getImages(any(Pageable.class));
        verify(mediaService, never()).getAll(any());
        verify(mediaService, never()).getVideos(any());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void mediaPickerReturnsEmptyPage() throws Exception {
        when(mediaService.getImages(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/admin/media/picker"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/media/picker"))
                .andExpect(model().attributeExists("pickerPage"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void uploadSingleFileSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files", "photo.jpg", MediaType.IMAGE_JPEG_VALUE,
                "fake-image-content".getBytes());

        when(mediaService.upload(any())).thenReturn(media);

        mockMvc.perform(multipart("/admin/media/upload")
                .file(file)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("successMessage", "1 file(s) uploaded successfully."));

        verify(mediaService).upload(any());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void uploadMultipleFilesSuccess() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile(
                "files", "photo1.jpg", MediaType.IMAGE_JPEG_VALUE, "img1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile(
                "files", "photo2.png", MediaType.IMAGE_PNG_VALUE, "img2".getBytes());

        when(mediaService.upload(any())).thenReturn(media);

        mockMvc.perform(multipart("/admin/media/upload")
                .file(file1)
                .file(file2)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("successMessage", "2 file(s) uploaded successfully."));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void uploadSkipsEmptyFiles() throws Exception {
        MockMultipartFile nonEmpty = new MockMultipartFile(
                "files", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "data".getBytes());
        MockMultipartFile empty = new MockMultipartFile(
                "files", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        when(mediaService.upload(any())).thenReturn(media);

        mockMvc.perform(multipart("/admin/media/upload")
                .file(nonEmpty)
                .file(empty)
                .with(csrf()))
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("successMessage", "1 file(s) uploaded successfully."));
        verify(mediaService, org.mockito.Mockito.times(1)).upload(any());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void uploadFailsWithBusinessException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files", "bad.exe", "application/octet-stream", "data".getBytes());

        when(mediaService.upload(any()))
                .thenThrow(new BusinessException("Unsupported file type: application/octet-stream"));

        mockMvc.perform(multipart("/admin/media/upload")
                .file(file)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void uploadFailsWithUnexpectedException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "files", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "data".getBytes());

        when(mediaService.upload(any()))
                .thenThrow(new RuntimeException("Cloudinary unavailable"));

        mockMvc.perform(multipart("/admin/media/upload")
                .file(file)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("errorMessage",
                        "An unexpected error occurred. Please try again."));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void deleteMediaSuccess() throws Exception {
        when(mediaService.getById(eq(mediaId))).thenReturn(Optional.of(media));
        doNothing().when(mediaService).delete(eq(mediaId));

        mockMvc.perform(post("/admin/media/" + mediaId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("successMessage",
                        "\"sample.jpg\" deleted successfully."));

        verify(mediaService).delete(eq(mediaId));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void deleteMediaNotFound() throws Exception {
        when(mediaService.getById(eq(mediaId)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/media/" + mediaId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().isNotFound());

        verify(mediaService, never()).delete(any());
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void deleteMediaFailsWithBusinessException() throws Exception {
        when(mediaService.getById(eq(mediaId))).thenReturn(Optional.of(media));
        doThrow(new BusinessException("Storage provider error"))
                .when(mediaService).delete(eq(mediaId));

        mockMvc.perform(post("/admin/media/" + mediaId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    @WithUserDetails(StubApplicationUserDetailsService.USERNAME_ADMIN)
    void deleteMediaFailsWithUnexpectedException() throws Exception {
        when(mediaService.getById(eq(mediaId))).thenReturn(Optional.of(media));
        doThrow(new RuntimeException("Cloudinary timeout"))
                .when(mediaService).delete(eq(mediaId));

        mockMvc.perform(post("/admin/media/" + mediaId.asString() + "/delete")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/media"))
                .andExpect(flash().attribute("errorMessage",
                        "An unexpected error occurred. Please try again."));
    }
}