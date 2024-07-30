package com.iss.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iss.auth.domain.po.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT username FROM user WHERE github_account LIKE CONCAT(#{githubAccount}, '%')")
    String getUsernameByGithubAccount(@RequestParam("githubAccount") String githubAccount);
}
