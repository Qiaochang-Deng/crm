package com.xxxx.crm.controller;

import com.xxxx.base.BaseController;
import com.xxxx.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;


    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.login(userName, userPwd);
        /**
         * 登录成功后
         *    1.将用户登录信息存入session
         *    2.将用户信息返回给客户端 有客户端(cookie)保存
         */
        resultInfo.setResult(userModel);
        return resultInfo;
    }

    @PostMapping("user/updatePassword")
    @ResponseBody
    public ResultInfo updatePassword(HttpServletRequest request, String oldPassword, String newPassword, String confirmPassword) {
        ResultInfo resultInfo = new ResultInfo();
        userService.updateUserPassword(LoginUserUtil.releaseUserIdFromCookie(request), oldPassword, newPassword, confirmPassword);
        return resultInfo;
    }


    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }


    @PostMapping("user/login02")
    @ResponseBody
    public ResultInfo login02(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = userService.login(userName, userPwd);
        /**
         * 登录成功后
         *    1.将用户登录信息存入session
         *    2.将用户信息返回给客户端 有客户端(cookie)保存
         */
        resultInfo.setResult(userModel);
        return resultInfo;
    }


    @RequestMapping("user/queryAllSales")
    @ResponseBody
    public List<Map<String,Object>> queryAllSales(){
        return userService.queryAllSales();
    }

}
