package com.iss.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iss.message.domain.po.Announcement;

import java.util.List;

public interface IAnnouncementService extends IService<Announcement> {
    void publishAnnouncement(Announcement announcement);

    List<Announcement> getAnnouncementsForStudent(Long studentId);
}
