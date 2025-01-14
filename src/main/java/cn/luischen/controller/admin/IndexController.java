package cn.luischen.controller.admin;

import cn.luischen.constant.LogActions;
import cn.luischen.constant.WebConst;
import cn.luischen.controller.BaseController;
import cn.luischen.dto.StatisticsDto;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.Comment;
import cn.luischen.model.Content;
import cn.luischen.model.Log;
import cn.luischen.model.User;
import cn.luischen.service.log.LogService;
import cn.luischen.service.site.SiteService;
import cn.luischen.service.user.UserService;
import cn.luischen.utils.APIResponse;
import cn.luischen.utils.GsonUtils;
import cn.luischen.utils.TaleUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by winterchen on 2018/4/30.
 */
@Api("后台首页")
@Controller("adminIndexController")
@RequestMapping(value = "/admin")
@Slf4j
public class IndexController extends BaseController{

    @Autowired
    private SiteService siteService;

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;



    @ApiOperation("进入首页")
    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request){
        log.info("Enter admin index method");
        List<Comment> comments = siteService.getComments(5);
        List<Content> contents = siteService.getNewArticles(5);
        StatisticsDto statistics = siteService.getStatistics();
        // 取最新的20条日志
        Page<Log> logs = logService.getLogs(1, 5);
        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statistics);
        request.setAttribute("logs", logs);
        log.info("Exit admin index method");
        return "admin/index";
    }

    /**
     * 个人设置页面
     */
    @GetMapping(value = "/profile")
    public String profile() {
        return "admin/profile";
    }


    /**
     * 保存个人信息
     */
    @PostMapping(value = "/profile")
    @ResponseBody
    public APIResponse saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        User users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            User temp = new User();
            temp.setUid(users.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            User original= (User) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
        }
        return APIResponse.success();
    }

    /**
     * 修改密码
     */
    @PostMapping(value = "/password")
    @ResponseBody
    public APIResponse upPwd(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request,HttpSession session) {
        User users = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return APIResponse.fail("请确认信息输入完整");
        }

        if (!users.getPassword().equals(TaleUtils.MD5encode(users.getUsername() + oldPassword))) {
            return APIResponse.fail("旧密码错误");
        }
        if (password.length() < 6 || password.length() > 14) {
            return APIResponse.fail("请输入6-14位密码");
        }

        try {
            User temp = new User();
            temp.setUid(users.getUid());
            String pwd = TaleUtils.MD5encode(users.getUsername() + password);
            temp.setPassword(pwd);
            userService.updateUserInfo(temp);
            logService.addLog(LogActions.UP_PWD.getAction(), null, request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            User original= (User)session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY,original);
            return APIResponse.success();
        } catch (Exception e){
            String msg = "密码修改失败";
            if (e instanceof BusinessException) {
                msg = e.getMessage();
            } else {
                log.error(msg, e);
            }
            return APIResponse.fail(msg);
        }
    }




}
