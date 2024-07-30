package com.iss.message.controller;

import com.iss.common.constant.MessageConstant;
import com.iss.common.result.Result;
import com.iss.message.domain.po.Announcement;
import com.iss.message.service.IAnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Message Management Interface")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class AnnouncementController {

    private final IAnnouncementService announcementService;

    @ApiOperation("Publish Announcement")
    @PostMapping("/publish")
    public Result<String> publishAnnouncement(@RequestBody Announcement announcement) {
        try {
            //TODO
            //获取这个课下的学生
            announcementService.publishAnnouncement(announcement);
            return Result.success(MessageConstant.CREATE_MESSAGE_SUCCESS);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    @ApiOperation("Get Announcements for Student")
    @GetMapping("/student/{studentId}")
    public Result<List<Announcement>> getAnnouncementsForStudent(@PathVariable Long studentId) {
        try {
            List<Announcement> announcementsForStudent = announcementService.getAnnouncementsForStudent(studentId);
            return Result.success(MessageConstant.GET_MESSAGE_SUCCESS, announcementsForStudent);
        } catch (Exception e) {
            return Result.error(extractErrorCode(e.getMessage()), e.getMessage());
        }
    }

    private int extractErrorCode(String errorMessage) {
        String[] parts = errorMessage.split(" ");
        for (String part : parts) {
            try {
                return Integer.parseInt(part);
            } catch (NumberFormatException ignored) {
                // Ignore parts that are not numbers
            }
        }
        return 500; // Default error code if not found
    }
}
