/* 
 * 创建日期 2019年4月29日
 *
 * 四川健康久远科技有限公司
 * 电话： 
 * 传真： 
 * 邮编： 
 * 地址：成都市武侯区
 * 版权所有
 */
package org.schic.common.persistence.dialect;

/**
 * 
 * @Description: Dialect for HSQLDB
 * @author Caiwb
 * @date 2019年4月29日 上午11:51:18
 */
public class HSQLDialect implements Dialect {
	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset),
				Integer.toString(limit));
	}

	/**
	 * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
	 * <pre>
	 * 如mysql
	 * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
	 * select * from user limit :offset,:limit
	 * </pre>
	 *
	 * @param sql               实际SQL语句
	 * @param offset            分页开始纪录条数
	 * @param offsetPlaceholder 分页开始纪录条数－占位符号
	 * @param limitPlaceholder  分页纪录条数占位符号
	 * @return 包含占位符的分页sql
	 */
	public String getLimitString(String sql, int offset,
			String offsetPlaceholder, String limitPlaceholder) {
		boolean hasOffset = offset > 0;
		return new StringBuffer(sql.length() + 10).append(sql)
				.insert(sql.toLowerCase().indexOf("select") + 6,
						hasOffset
								? " limit " + offsetPlaceholder + " "
										+ limitPlaceholder
								: " top " + limitPlaceholder)
				.toString();
	}

}
