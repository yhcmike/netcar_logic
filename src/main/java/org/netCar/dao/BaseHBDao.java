package org.netCar.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.Id;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.netCar.util.Assert;
import org.netCar.util.Page;
import org.netCar.util.PageUtil;

public abstract class BaseHBDao<M extends Serializable, PK extends Serializable> {

	private String pkName = "id";

	private Class<M> entityClass;

	private final String listAllHql;

	private final String countAllHql;

	@SuppressWarnings("unchecked")
	public BaseHBDao() {
		entityClass = (Class<M>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		Field[] fields = entityClass.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Id.class)) {
				pkName = f.getName();
			}
		}
		Assert.notNull(pkName);
		listAllHql = "FROM " + entityClass.getSimpleName() + " ORDER BY " + pkName;
		countAllHql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName();
	}

	public BaseHBDao(Class<M> c, String idName) {
		entityClass = c;
		pkName = idName;
		Assert.notNull(pkName);
		listAllHql = "FROM " + entityClass.getSimpleName() + " ORDER BY " + pkName;
		countAllHql = "SELECT COUNT(*) FROM " + entityClass.getSimpleName();
	}

	@Resource
	private SessionFactory sessionFactory;

	/** 
	 * 获取当前线程的session 
	 */
	public Session getCurrentSession() {
		// 事务必须是开启的（REQUIRED），否则获取不到
		return sessionFactory.getCurrentSession();
	}

	/**
	 * 添加
	 * @param model 添加的对象
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	public PK save(M model) {
		try {
			return (PK) getCurrentSession().save(model);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 添加或者修改
	 * @param model 添加、修改的对象
	 * @since    JDK 1.7
	 */
	public void saveOrUpdate(M model) {
		try {
			getCurrentSession().saveOrUpdate(model);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 修改
	 * @param model 修改的对象
	 * @since    JDK 1.7
	 */
	public void update(M model) {
		try {
			getCurrentSession().update(model);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	public void merge(M model) {
		try {
			getCurrentSession().merge(model);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 根据id删除
	 * @param id 删除对象的主键
	 * @since    JDK 1.7
	 */
	public void delete(PK id) {
		try {
			getCurrentSession().delete(get(id));
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 根据对象删除
	 * @param model 删除对象
	 * @since    JDK 1.7
	 */
	public void deleteObject(M model) {
		try {
			getCurrentSession().delete(model);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 根据id获取对象
	 * @param id 对象的id
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	public M get(PK id) {
		try {
			return (M) getCurrentSession().get(entityClass, id);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 获取总数（hql）
	 * @since    JDK 1.7
	 */
	public int countAll() {
		try {
			Long total = unique(countAllHql);
			return total.intValue();
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * 获取查询总数
	 * @param sql 查询sql，分页查询中调用，sql中不含有count函数
	 * @since    JDK 1.7
	 */
	public int countAll(String sql) {
		try {
			Query query = getCurrentSession().createSQLQuery(getCountSQL(sql));
			return Integer.valueOf(query.list().get(0).toString());
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * 获取查询总数
	 * @param sql 查询sql，sql中含有count函数
	 * @since    JDK 1.7
	 */
	public int countSqlQuery(String sql) {
		try {
			
			SQLQuery  query = getCurrentSession().createSQLQuery(sql);
		    return Integer.parseInt(query.uniqueResult().toString()); 
		    
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * 获取所有数据
	 * @since    JDK 1.7
	 */
	public List<M> listAll() {
		try {
			return list(listAllHql, -1, -1);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 获取所有数据，用于查询条件中参数个数确定的查询
	 * @param hql 查询hql
	 * @param paramList 查询参数
	 * @since    JDK 1.7
	 */
	public List<M> listAll(final String hql, final Object... paramList) {
		try {
			return list(hql, -1, -1, paramList);
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * 获取所有数据，用于查询条件中参数个数不确定的查询
	 * @param hql 查询hql
	 * @param params 查询参数，map形式
	 * @since    JDK 1.7
	 */
	public List<M> listAll(final String hql, final Map<String,Object> params) {
		try {
			return list(hql, -1, -1, params);
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	/**
	 * 分页查询
	 * @param pageNumber 当前页数
	 * @param pageSize 页大小
	 * @since    JDK 1.7
	 */
	public List<M> listAll(int pageNumber, int pageSize) {
		try {
			return list(listAllHql, pageNumber, pageSize);
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @since    JDK 1.7
	 */
	public Page<M> listPageAll(Page<M> page) {
		try {
			 Long total = unique(countAllHql);
			 page.setTotal(total);
			 List<M> l = list(listAllHql, page.getPage(), page.getRows());
			 page.setResult(l);
		} catch (RuntimeException e) {
			throw e;
		}
		return page;
	}
	
	/**
	 * hql分页查询，用于查询条件中参数个数确定的查询
	 * @param page 分页对象
	 * @param hql 查询hql
	 * @param paramList 查询参数
	 * @since    JDK 1.7
	 */
	public Page<M> listPageAll(Page<M> page, String hql, final Object... paramList) {
		try {
			 Long total = unique(getCountHQL(hql), paramList);
			 page.setTotal(total);
			 List<M> l = list(hql, page.getPage(), page.getRows(), paramList);
			 page.setResult(l);
		} catch (RuntimeException e) {
			throw e;
		}
		return page;
	}
	
	/**
	 * hql分页查询，用于查询条件中参数个数不确定的查询
	 * @param page 分页对象
	 * @param hql 查询hql
	 * @param params 查询参数，map形式
	 * @since    JDK 1.7
	 */
	public Page<M> listPageAll(Page<M> page, String hql, Map<String, Object> params) {
		try {
			 Long total = unique(getCountHQL(hql), params);
			 page.setTotal(total);
			 List<M> l = list(hql, page.getPage(), page.getRows(), params);
			 page.setResult(l);
		} catch (RuntimeException e) {
			throw e;
		}
		return page;
	}
	
	/**
	 * sql分页查询 
	 * @param page 分页参数
	 * @param sql 查询sql
	 * @param tClass 查询结果封装
	 * @param params 查询条件
	 */
	public <T> Page<T> listPageAllBySql(Page<T> page, String sql, Class<T> tClass, Map<String, Object> params) {
		try {
			if(sql != null && sql.length() > 0){
				String contSql = getCountSQL(sql);
				BigInteger total = uniqueSQL(contSql, params);
				page.setTotal(total.longValue());
				List<T> l = listBySql(sql, page.getPage(), page.getRows(), tClass, params);
				page.setResult(l);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		return page;
	}
	
	public <T> Page<T> listPageAllBySql(Page<T> page, String sql, Class<T> tClass, final Object... paramList) {
		try {
			if(sql != null && sql.length() > 0){
				String contSql = getCountSQL(sql);
				BigInteger total = uniqueSQL(contSql, paramList);
				page.setTotal(total.longValue());
				List<T> l = listBySql(sql, page.getPage(), page.getRows(), tClass, paramList);
				page.setResult(l);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
		return page;
	}
	
	/**
	 * id对应的对象是否存在
	 * @param id 对象主键
	 */
	public boolean exits(PK id) {
		try {
			return get(id) != null;
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 将session的缓存中的数据与数据库同步，清除session中的缓存数据
	 */
	public void flush() {
		try {
			getCurrentSession().flush();
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 清除session中的缓存数据
	 */
	public void clear() {
		try {
			getCurrentSession().clear();
		} catch (RuntimeException e) {
			throw e;
		}
	}

	/**
	 * 自定义hql修改 
	 * @param hql 修改hql
	 * @param paramList 查询sql
	 */
	public int executeUpdate(String hql, final Object... paramList) {
		Query query = getCurrentSession().createQuery(hql);
		if (paramList != null) {
			setParameters(query, paramList);
		}
		return query.executeUpdate();
	}
	
	/**
	 * 自定义sql修改 
	 * @param sql 修改sql
	 * @param paramList 查询sql
	 */
	public int executeUpdateSQL(String sql, final Object... paramList) {
		Query query = getCurrentSession().createSQLQuery(sql);
		if (paramList != null) {
			setParameters(query, paramList);
		}
		return query.executeUpdate();
	}
	
	/**
	 *  hql分页查询，用于查询条件中参数个数确定的查询
	 * @param hql 查询hql
	 * @param pageNumber 当前页数
	 * @param pageSize 页大小
	 * @param paramList 查询参数
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> list(final String hql, final int pageNumber, final int pageSize,
			final Object... paramList) {
		Query query = getCurrentSession().createQuery(hql);
		if (paramList != null) {
			setParameters(query, paramList);
		}
		if (pageSize > -1) {
			query.setMaxResults(pageSize);
		}
		if (pageSize > 0) {
			query.setFirstResult(PageUtil.getPageStart(pageNumber, pageSize));
		} else {
			query.setFirstResult(0);
		}
		return (List<T>) query.list();
	}
	
	/**
	 * hql分页查询，用于查询条件中参数个数不确定的查询
	 * @param hql 查询hql
	 * @param pageNumber 当前页数
	 * @param pageSize 页大小
	 * @param params 查询参数，map形式
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<T> list(final String hql, final int pageNumber, final int pageSize, Map<String,Object> params) {
		Query query = getCurrentSession().createQuery(hql);
		if (params != null) {
			setParameters(query, params);
		}
		if (pageSize > -1) {
			query.setMaxResults(pageSize);
		}
		if (pageSize > 0) {
			query.setFirstResult(PageUtil.getPageStart(pageNumber, pageSize));
		} else {
			query.setFirstResult(0);
		}
		return (List<T>) query.list();
	}
	
	/**
	 * 获取hql唯一结果，用于查询条件中参数个数不确定的查询
	 * @param hql 查询hql
	 * @param map 查询参数
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	protected <T> T unique(final String hql, final Map<String, Collection<?>> map,
			final Object... paramList) {
		Query query = getCurrentSession().createQuery(hql);
		if (paramList != null) {
			setParameters(query, paramList);
			if (map != null) {
				for (Entry<String, Collection<?>> entry : map.entrySet()) {
					query.setParameterList(entry.getKey(), entry.getValue());
				}
			}
		}
		return (T) query.uniqueResult();
	}

	/**
	 * 获取hql唯一结果，用于查询条件中参数个数确定的查询
	 * @param hql 查询hql
	 * @param paramList 查询参数
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	protected <T> T unique(final String hql, final Object... paramList) {
		Query query = getCurrentSession().createQuery(hql);
		if (paramList != null) {
			setParameters(query, paramList);
		}
		return (T) query.uniqueResult();
	}
	
	/**
	 * 获取hql唯一结果，用于查询条件中参数个数不确定的查询
	 * @param hql 查询hql
	 * @param params 查询参数
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	protected <T> T unique(final String hql, Map<String,Object> params) {
		Query query = getCurrentSession().createQuery(hql);
		if (params != null) {
			setParameters(query, params);
		}
		return (T) query.uniqueResult();
	}
	
	/**
	 * 获取sql唯一结果，用于查询条件中参数个数不确定的查询
	 * @param sql 查询sql
	 * @param params 查询参数
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	protected <T> T uniqueSQL(final String sql, Map<String, Object> params) {
		Query query = getCurrentSession().createSQLQuery(sql);
		if (params != null) {
			setParameters(query, params);
		}
		return (T) query.uniqueResult();
	}
	
	/**
	 * 获得唯一结果，sql语句，确定参数
	 * @param sql
	 * @param paramList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T uniqueSQL(final String sql, final Object... paramList) {
		Query query = getCurrentSession().createSQLQuery(sql);
		if (paramList != null) {
			setParameters(query, paramList);
		}
		return (T) query.uniqueResult();
	}
	
	/**
	 * 获取sql唯一结果，用于查询条件中参数个数不确定的查询
	 * @param sql 查询sql 
	 * @param tClass 将查询结果封装为指定对象的class
	 * @param params 查询参数
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	protected <T> T uniqueSQL(final String sql, Class<T> tClass, final Object... paramList) {
		Query query = getCurrentSession().createSQLQuery(sql);
		if (paramList != null) {
			setParameters(query, paramList);
		}
		//设置查询
		if(tClass != null){
			query.setResultTransformer(Transformers.aliasToBean(tClass));
		}
		return (T) query.uniqueResult();
	}
	
	/**
	 * 获取查询总数的sql,分页查询中调用(多个from,主大写)
	 * @param sql 查询sql
	 * @since    JDK 1.7
	 */
	public String getCountSQL(String sql){
		int index = sql.indexOf("From"); 
		sql = sql.substring(index);
		return "select count(*) " + sql;
	}
	
	/**
	 * 获取查询总数的hql,分页查询中调用
	 * @param hql 查询hql
	 * @since    JDK 1.7
	 */
	public String getCountHQL(String hql){
		int index = hql.toLowerCase().indexOf("from");
		hql = hql.substring(index);
		return "select count(*) " + hql;
	}

	/**
	 * sql分页查询，用于查询条件中参数个数不确定的查询
	 * @param sql 查询sql
	 * @param pageNumber 当前页数
	 * @param pageSize 页大小
	 * @param tClass 将查询结果封装为指定对象的class
	 * @param params 查询参数
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> listBySql(final String sql, final int pageNumber, final int pageSize, Class<T> tClass, Map<String, Object> params) {
		
		Query query = getCurrentSession().createSQLQuery(sql);
		
		//设置查询条件
		if(params != null && params.size()>0){
			setParameters(query, params);
		}
		//设置查询
		if(tClass != null){
			query.setResultTransformer(Transformers.aliasToBean(tClass));
		}
		if (pageSize > -1) {
			query.setMaxResults(pageSize);
		}
		if (pageSize > 0) {
			query.setFirstResult(PageUtil.getPageStart(pageNumber, pageSize));
		} else {
			query.setFirstResult(0);
		}
		return query.list();
	}
	
	/**
	 * sql分页查询，用于查询条件中参数个数确定的查询
	 * @param sql 查询sql
	 * @param pageNumber 当前页数
	 * @param pageSize 页大小
	 * @param tClass 将查询结果封装为指定对象的class
	 * @param params 查询参数
	 * @since    JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> listBySql(final String sql, final int pageNumber, final int pageSize, Class<T> tClass, Object... params) {
		
		Query query = getCurrentSession().createSQLQuery(sql);
		
		//设置查询条件
		if(params != null){
			setParameters(query, params);
		}
		//设置查询
		if(tClass != null){
			query.setResultTransformer(Transformers.aliasToBean(tClass));
		}
		if (pageSize > -1) {
			query.setMaxResults(pageSize);
		}
		if (pageSize > 0) {
			query.setFirstResult(PageUtil.getPageStart(pageNumber, pageSize));
		} else {
			query.setFirstResult(0);
		}
		return query.list();
	}
	
	/**
	 * 设置查询条件
	 * @param query
	 * @param params
	 */
	protected void setParameters(Query query, Map<String, Object> params) {
		if (params != null) {
			for (String key:params.keySet()) {
				Object val = params.get(key);
				//如有特殊的以后再添加 
				if(val.getClass().isArray()){
					//in 关键字查询
					query.setParameterList(key, (Object[])val);
				}else {
					query.setParameter(key, val);
				}
			}
		}
	}
	/**
	 * 为Query自动设置参数列表
	 */
	protected void setParameters(Query query, Object[] paramList) {
		if (paramList != null) {
			for (int i = 0; i < paramList.length; i++) {
				if (paramList[i] instanceof Date) {
					query.setTimestamp(i, (Date) paramList[i]);
				} else {
					query.setParameter(i, paramList[i]);
				}
			}
		}
	}
	
	/**
	 * sql获取所有数据 
	 * @param sql
	 * @param tClass
	 */
	public <T> List<T> listAll(final String sql, Class<T> tClass, Map<String, Object> params) {
		try {
			return listBySql(sql, -1, -1, tClass, params);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}
}