package org.schic.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.schic.common.persistence.InterfaceBaseDao;
import org.schic.modules.sys.entity.User;

/**
 * 
 * @Description: 用户DAO接口
 * @author Caiwb
 * @date 2019年5月6日 上午10:39:33
 */
@Mapper
public interface UserDao extends InterfaceBaseDao<User> {

	/**
	 * 根据登录名称查询用户
	 * @param loginName
	 * @return
	 */
	User getByLoginName(User user);

	/**
	 * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
	 * @param user
	 * @return
	 */
	List<User> findUserByOfficeId(User user);

	/**
	 * 查询全部用户数目
	 * @return
	 */
	long findAllCount(User user);

	/**
	 * 更新用户密码
	 * @param user
	 * @return
	 */
	int updatePasswordById(User user);

	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param user
	 * @return
	 */
	int updateLoginInfo(User user);

	/**
	 * 删除用户角色关联数据
	 * @param user
	 * @return
	 */
	int deleteUserRole(User user);

	/**
	 * 插入用户角色关联数据
	 * @param user
	 * @return
	 */
	int insertUserRole(User user);

	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 */
	int updateUserInfo(User user);

	/**
	 * 插入好友
	 */
	int insertFriend(@Param("id") String id, @Param("userId") String userId,
			@Param("friendId") String friendId);

	/**
	 * 查找好友
	 */
	User findFriend(@Param("userId") String userId,
			@Param("friendId") String friendId);
	/**
	 * 删除好友
	 */
	void deleteFriend(@Param("userId") String userId,
			@Param("friendId") String friendId);

	/**
	 * 
	 * 获取我的好友列表
	 * 
	 */
	List<User> findFriends(User currentUser);

	/**
	 * 
	 * 查询用户-->用来添加到常用联系人
	 * 
	 */
	List<User> searchUsers(User user);

	/**
	 * 
	 */

	List<User> findListByOffice(User user);

}
