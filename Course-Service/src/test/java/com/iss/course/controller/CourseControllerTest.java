//package com.iss.course.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.iss.common.result.Result;
//import com.iss.course.domain.dto.CourseDTO;
//import com.iss.course.domain.po.Course;
//import com.iss.course.domain.vo.CourseVO;
//import com.iss.course.service.ICourseService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CourseController.class)
//class CourseControllerTest {
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ICourseService courseService;
//
//    @InjectMocks
//    private CourseController courseController;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
//    }
//
//    @Test
//    void testCreateCourse() throws Exception {
//        CourseDTO courseDTO = new CourseDTO();
//        courseDTO.setCourseName("Test Course");
//        // Add other fields as necessary
//
//        Course course = new Course();
//        course.setCourseName("Test Course");
//
//        // Add other fields as necessary
//
//        CourseVO courseVO = new CourseVO();
//        courseVO.setCourseName("Test Course");
//        // Add other fields as necessary
//
//        when(courseService.save(any(Course.class))).thenReturn(true);
//
//        mockMvc.perform(post("/courses/new")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(courseDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Course created successfully"))
//                .andExpect(jsonPath("$.data.name").value("Test Course"));
//
//        verify(courseService, times(1)).save(any(Course.class));
//    }
//
//    @Test
//    void testUpdateCourse() throws Exception {
//        Course course = new Course();
//        course.setId(1L);
//        course.setCourseName("Updated Course");
//        // Add other fields as necessary
//
//        when(courseService.updateById(any(Course.class))).thenReturn(true);
//
//        mockMvc.perform(put("/courses/update")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(course)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Course updated successfully"));
//
//        verify(courseService, times(1)).updateById(any(Course.class));
//    }
//
//    @Test
//    void testDeleteCourseById() throws Exception {
//        Long courseId = 1L;
//
//        when(courseService.removeById(courseId)).thenReturn(true);
//
//        mockMvc.perform(delete("/courses/delete/{id}", courseId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Course deleted successfully"));
//
//        verify(courseService, times(1)).removeById(courseId);
//    }
//
//    @Test
//    void testGetCourseById() throws Exception {
//        Long courseId = 1L;
//        Course course = new Course();
//        course.setId(courseId);
//        course.setCourseName("Test Course");
//        // Add other fields as necessary
//
//        CourseVO courseVO = new CourseVO();
//        courseVO.setId(courseId);
//        courseVO.setCourseName("Test Course");
//        // Add other fields as necessary
//
//        when(courseService.getById(courseId)).thenReturn(course);
//
//        mockMvc.perform(get("/courses/get/{id}", courseId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Course retrieved successfully"))
//                .andExpect(jsonPath("$.data.id").value(courseId))
//                .andExpect(jsonPath("$.data.name").value("Test Course"));
//
//        verify(courseService, times(1)).getById(courseId);
//    }
//
//    @Test
//    void testGetCoursesByIds() throws Exception {
//        Long courseId = 1L;
//        List<Long> ids = Collections.singletonList(courseId);
//        Course course = new Course();
//        course.setId(courseId);
//        course.setCourseName("Test Course");
//        // Add other fields as necessary
//
//        CourseVO courseVO = new CourseVO();
//        courseVO.setId(courseId);
//        courseVO.setCourseName("Test Course");
//        // Add other fields as necessary
//
//        when(courseService.listByIds(ids)).thenReturn(Collections.singletonList(course));
//
//        mockMvc.perform(get("/courses/get/lists")
//                        .param("ids", "1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value(200))
//                .andExpect(jsonPath("$.msg").value("Course retrieved successfully"))
//                .andExpect(jsonPath("$.data[0].id").value(courseId))
//                .andExpect(jsonPath("$.data[0].name").value("Test Course"));
//
//        verify(courseService, times(1)).listByIds(ids);
//    }
//}
