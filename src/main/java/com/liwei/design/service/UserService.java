package com.liwei.design.service;

import com.liwei.design.model.User;
import com.liwei.design.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository uRepo;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getFriend(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        User user = uRepo.findAllByUsername(username);
        StringBuilder stringBuilder = new StringBuilder("select u.username as name from user u where ");
        int i = 0;
        if (user.getTiyu() != null) {
            stringBuilder.append("(u.tiyu = '1' ");
            ++i;
        }

        if (user.getLvyou() != null && i == 0) {
            stringBuilder.append("(u.lvyou = '1' ");
            ++i;
        } else if (user.getLvyou() != null && i != 0) {
            stringBuilder.append("or u.lvyou = '1' ");
        }

        if (user.getRenwen() != null && i == 0) {
            stringBuilder.append("(u.renwen = '1' ");
            ++i;
        } else if (user.getRenwen() != null && i != 0) {
            stringBuilder.append("or u.renwen = '1' ");
        }

        if (user.getYishu() != null && i == 0) {
            stringBuilder.append("(u.yishu = '1' ");
            ++i;
        } else if (user.getYishu() != null && i != 0) {
            stringBuilder.append("or u.yishu = '1' ");
        }

        stringBuilder.append(") and u.username <> " + "'" + username + "'");
        String sql = stringBuilder.toString();

        List<String> list = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("name");
            }
        });

        return list;
    }
}
