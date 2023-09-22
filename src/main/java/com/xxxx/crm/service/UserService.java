package com.xxxx.crm.service;

import com.xxxx.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {

    @Autowired
    private UserMapper userMapper;

    public UserModel login(String userName,String userPwd){
        /**
         * 1.参数校验
         *    用户名  非空
         *    密码    非空
         * 2.根据用户名  查询用户记录
         * 3.用户存在性校验
         *     不存在   -->记录不存在  方法结束
         * 4.用户存在
         *     校验密码
         *        密码错误 -->密码不正确   方法结束
         * 5.密码正确
         *     用户登录成功  返回用户信息
         */
        checkLoginParams(userName,userPwd);
        User user = userMapper.queryUserByUserName(userName);
        AssertUtil.isTrue(null == user,"用户不存在或已注销!");
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(userPwd))),"用户密码不正确，请重新输入!");
        return buildUserInfo(user);
    }

    private UserModel buildUserInfo(User user) {
        UserModel userModel=new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空!");
    }

    public void updateUserPassword(Integer userId,String oldPassword,String newPassword,String confirmPassword){
        /**
         * 1.参数校验
         *     userId  非空  记录必须存在
         *     oldPassword  非空 与数据库密文 密码保持一致
         *     newPassword  非空   与原始密码不能相同
         *     confirmPassword  非空 与新密码保持一致
         * 2.设置用户新密码
         *     新密码进行加密处理
         * 3.执行更新操作
         */
        checkParams(userId,oldPassword,newPassword,confirmPassword);
        User user =userMapper.selectByPrimaryKey(userId);
        user.setUserPwd(Md5Util.encode(newPassword));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户密码更新失败!");
    }

    private void checkParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User temp =userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null== userId || null==temp,"用户未登录或不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码!");
        AssertUtil.isTrue(StringUtils.isBlank(newPassword),"请输入新密码!");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"请输入确认密码!");
        AssertUtil.isTrue(!(temp.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确!");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码输入不一致!");
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码与原始密码不能相同!");
    }


    public List<Map<String,Object>>  queryAllSales(){
        return userMapper.queryAllSales();
    }


}
