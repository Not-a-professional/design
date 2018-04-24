package com.liwei.design.service;

import com.liwei.design.model.Trash;
import com.liwei.design.model.User;
import com.liwei.design.model.ticketVolume;
import com.liwei.design.othermodel.hotShare;
import com.liwei.design.repo.DeleteRepository;
import com.liwei.design.repo.UserRepository;
import com.liwei.design.repo.ticketVolumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository uRepo;
    @Autowired
    private ticketVolumeRepository tvRepo;
    @Autowired
    private DeleteRepository dRepo;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String> getFriend(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        User user = uRepo.findAllByUsername(username);
        StringBuilder stringBuilder = new StringBuilder("select u.username as name from user u where ");

        getHobby(stringBuilder, user);

        stringBuilder.append(") and u.username <> " + "'" + username + "'");
        String sql = stringBuilder.toString();

        return jdbcTemplate.query(sql, (rs, rowsNum) -> rs.getString("name"));
    }

    public List<Trash> getTrashList(HttpServletRequest request, Pageable pageable) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        return dRepo.findAllByUser(username, pageable);
    }

    public List<hotShare> getHotShare(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
            .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        User user = uRepo.findAllByUsername(username);
        StringBuilder stringBuilder = new StringBuilder("select u.username userId, s.path url, "
                + "s.download hot from user u, share s where u.username = s.user and s.spath is"
                + " null and ");

        getHobby(stringBuilder, user);

        stringBuilder.append(") and u.username <> " + "'" + username + "' order by"
                + " s.download desc limit 8");

        String sql = stringBuilder.toString();

        return jdbcTemplate.query(sql, (rs, rowsNum) -> {
            hotShare hotShare = new hotShare();
            hotShare.setUserId(rs.getString("userId"));
            hotShare.setUrl(rs.getString("url"));
            hotShare.setHot(rs.getString("hot"));
            return hotShare;
        });
    }

    public Map<String, String> volumeReason(HttpServletRequest request, String reason) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
            .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        Map<String, String> res = new HashMap<String, String>();
        //有申请还在进行则不允许再次申请
        if (!tvRepo.findAllByUserAndStatus(username).isEmpty()) {
            res.put("fail1", "您的前一次申请还在审核中，请耐心等待！");
            return res;
        }
        ticketVolume ticketVolume = new ticketVolume();
        ticketVolume.setStatus("0");
        ticketVolume.setReason(reason);
        ticketVolume.setUser(username);

        if (tvRepo.saveAndFlush(ticketVolume) == null) {
            res.put("fail","fail");
            return res;
        } else {
            res.put("success", "success");
            return res;
        }
    }

    private void getHobby(StringBuilder stringBuilder, User user) {
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
        } else if (user.getYishu() != null && i != 0) {
            stringBuilder.append("or u.yishu = '1' ");
        }
    }
}
