package com.iss.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iss.message.domain.po.Announcement;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
}
