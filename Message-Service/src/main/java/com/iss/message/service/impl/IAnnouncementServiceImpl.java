package com.iss.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iss.message.domain.po.Announcement;
import com.iss.message.mapper.AnnouncementMapper;
import com.iss.message.service.IAnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IAnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements IAnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Override
    public void publishAnnouncement(Announcement announcement) {
        announcement.setCreatedAt(LocalDateTime.now());
        announcementMapper.insert(announcement);
    }

    @Override
    public List<Announcement> getAnnouncementsForStudent(Long studentId) {
        return announcementMapper.selectList(new QueryWrapper<Announcement>().eq("student_id", studentId));
    }
}
