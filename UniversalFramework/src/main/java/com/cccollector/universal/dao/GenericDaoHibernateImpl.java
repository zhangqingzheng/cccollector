/**
 * 
 */
package com.cccollector.universal.dao;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.EntityType;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import com.cccollector.universal.dao.GenericDao;
import com.cccollector.universal.dao.QueryField.PredicateEnum;

/**
 * 泛型DAO的Hibernate实现类
 *
 * @author 谢朋
 * Copyright © 2016年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public abstract class GenericDaoHibernateImpl<K extends Serializable, T extends Serializable> implements GenericDao<K, T> {
	
	/**
	 * 实体类对应的Class对象
	 */
	private Class<T> _clazz;

	/**
	 * 带有实体类对应的Class对象的构造函数
	 * 
	 * @param clazz 实体类对应的Class对象
	 */
	public GenericDaoHibernateImpl(Class<T> clazz) {
		_clazz = clazz;
	}
	
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 子类获取SessionFactory对象
	 *
	 * @return SessionFactory对象
	 */
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Override
	public Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	@Override
	public EntityManager getEntityManager() {
		return getSession();
	}

	@Override
	public T loadById(K id, String... lazyAttributeNames) {
		Session session = getSession();
		T t = session.get(_clazz, id);
		if (t != null) {
			for (String attributeName : lazyAttributeNames) {
				try {
					PropertyDescriptor propertyDescriptor = new PropertyDescriptor(attributeName, _clazz);
					Object value = propertyDescriptor.getReadMethod().invoke(t);
					if (Collection.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
						Collection<?> collection = (Collection<?>) value;
						collection.iterator();
					} else if (Map.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
						Map<?, ?> map = (Map<?, ?>) value;
						map.entrySet().iterator();
					}
				} catch (Exception e) {}
			}
		}
		return t;
	}
	
	@Override
	public T loadNew(T t) {
		Session session = getSession();
		Integer idValue = (Integer) getSessionFactory().getPersistenceUnitUtil().getIdentifier(t);
		T nt = session.get(_clazz, idValue);
		return nt;
	}

	@Override
	public List<T> loadAll() {
		return this.loadPage(null, null, null, null, null, null);
	}
	
	@Override
	public List<T> loadAll(List<QueryField> orderQueryFieldList) {
		return this.loadPage(null, null, null, orderQueryFieldList, null, null);
	}

	@Override
	public List<T> loadAll(List<QueryField> predicateQueryFieldList, List<QueryField> orderQueryFieldList) {
		return this.loadPage(null, null, predicateQueryFieldList, orderQueryFieldList, null, null);
	}

	@Override
	public <X> List<X> loadAll(Class<X> resultClass, QueryField resultQueryField, List<QueryField> predicateQueryFieldList, List<QueryField> orderQueryFieldList) {
		return this.loadPage(resultClass, resultQueryField, predicateQueryFieldList, orderQueryFieldList, null, null);
	}

	@Override
	public List<T> loadPage(List<QueryField> orderQueryFieldList, Integer first, Integer pageSize) {
		return this.loadPage(null, null, null, orderQueryFieldList, first, pageSize);
	}
	
	@Override
	public List<T> loadPage(List<QueryField> predicateQueryFieldList, List<QueryField> orderQueryFieldList, Integer first, Integer pageSize) {
		return this.loadPage(null, null, predicateQueryFieldList, orderQueryFieldList, first, pageSize);
	}

	/**
	 * 创建Predicate
	 * 
	 * @param <X> 查询结果对象的泛型
	 * @param queryField 查询字段
	 * @param criteriaQuery 查询
	 * @param root 根
	 * @param predicateToOperate 用来运算的Predicate
	 * @return 对应的Predicate
	 */
	@SuppressWarnings("unchecked")
	private <X> Predicate createPredicate(QueryField queryField, CriteriaQuery<X> criteriaQuery, Root<T> root, Predicate predicateToOperate) {
		// 可能用到的子查询和子查询根
		Subquery<T> subquery = criteriaQuery.subquery(_clazz);
		Root<T> subRoot = subquery.from(_clazz);
		
		// 循环获取最后一个字段
		QueryField lastField = queryField;
		while (lastField.getValue() instanceof QueryField) {
			lastField = (QueryField) lastField.getValue();
		}
		
		// 获取到最后一个路径
		From<T, T> from = lastField.getPredicate() != PredicateEnum.NOT_IN ? root : subRoot;
		QueryField field = queryField;
		while (field.getValue() instanceof QueryField) {
			if (lastField.getPredicate() != PredicateEnum.NOT_IN) {
				// 如果不是NOT_IN（子查询），则需要在root的已有Join中查找，否则进行join操作
				boolean flag = true;
				for (Join<T, ?> join : root.getJoins()) {
					if (join.getAttribute().getName().equals(field.getName())) {
						from = (From<T, T>) join;
						flag = false;
						break;
					}
				}
				if (flag) {
					from = from.join(field.getName());
				}
			} else {
				// 如果是NOT_IN（子查询），则进行join操作
				from = from.join(field.getName());
			}
			field = (QueryField) field.getValue();
		}
		Path<T> path = from.get(lastField.getName());
		
		// 创建Predicate
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		Predicate predicate = null;
		switch(lastField.getPredicate()) {
		case EQUAL:
			predicate = criteriaBuilder.equal(path, lastField.getValue());
			break;
		case GREATER_THAN:
			predicate = criteriaBuilder.gt((Expression<? extends Number>) path, (Number) lastField.getValue());
			break;
		case GREATER_THAN_OR_EQUAL:
			predicate = criteriaBuilder.ge((Expression<? extends Number>) path, (Number) lastField.getValue());
			break;
		case LESS_THAN:
			predicate = criteriaBuilder.lt((Expression<? extends Number>) path, (Number) lastField.getValue());
			break;
		case LESS_THAN_OR_EQUAL:
			predicate = criteriaBuilder.le((Expression<? extends Number>) path, (Number) lastField.getValue());
			break;
		case LIKE:
			predicate = criteriaBuilder.like((Expression<String>) path, "%" + lastField.getValue() + "%");
			break;
		case NOT_EQUAL:
			predicate = criteriaBuilder.notEqual(path, lastField.getValue());
			break;
		case NOT_LIKE:
			predicate = criteriaBuilder.notLike((Expression<String>) path, "%" + lastField.getValue() + "%");
			break;
		case IS_NULL:
			predicate = criteriaBuilder.isNull(path);
			break;
		case IS_NOT_NULL:
			predicate = criteriaBuilder.isNotNull(path);
			break;
		case IN:
			if (lastField.getValue() instanceof List) {
				predicate = path.in((List<?>) lastField.getValue());
			} else {
				predicate = path.in(lastField.getValue());
			}
			break;
		case NOT_IN:
			if (lastField.getValue() instanceof List) {
				subquery.where(path.in((List<?>) lastField.getValue())).select(subRoot);
			} else {
				subquery.where(path.in(lastField.getValue())).select(subRoot);
			}
			predicate = criteriaBuilder.not(root.in(subquery));
			break;
		default:
			break;
		}

		if (predicateToOperate != null) {
			switch(lastField.getBooleanOperator()) {
			case AND:
				predicate = criteriaBuilder.and(predicateToOperate, predicate);
				break;
			case OR:
				predicate = criteriaBuilder.or(predicateToOperate, predicate);
				break;
			default:
				break;
			}
		}
		return predicate;
	}

	/**
	 * 创建Order
	 * 
	 * @param queryField 查询字段
	 * @param root 根
	 * @return 对应的Order
	 */
	@SuppressWarnings("unchecked")
	private Order createOrder(QueryField queryField, Root<T> root) {
		// 循环获取最后一个字段
		QueryField lastField = queryField;
		while (lastField.getValue() instanceof QueryField) {
			lastField = (QueryField) lastField.getValue();
		}
		
		// 获取到最后一个路径
		From<T, T> from = root;
		QueryField field = queryField;
		while (field.getValue() instanceof QueryField) {
			// 在root的已有Join中查找，否则进行join操作
			boolean flag = true;
			for (Join<T, ?> join : root.getJoins()) {
				if (join.getAttribute().getName().equals(field.getName())) {
					from = (From<T, T>) join;
					flag = false;
					break;
				}
			}
			if (flag) {
				from = from.join(field.getName());
			}
			field = (QueryField) field.getValue();
		}
		Path<T> path = from.get(lastField.getName());
		
		// 创建Order
		CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
		Order order = ((Boolean) field.getValue()) == true ? criteriaBuilder.asc(path) : criteriaBuilder.desc(path);
		return order;
	}
	
	/**
	 * 创建CriteriaQuery
	 * 
	 * @param <X> 查询结果对象的泛型
	 * @param resultClass 查询结果对象的类
	 * @param predicateQueryFieldList 谓词查询字段列表
	 * @param orderQueryFieldList 排序查询字段列表
	 * @return 对应的CriteriaQuery
	 */
	private <X> CriteriaQuery<X> createCriteriaQuery(Class<X> resultClass, List<QueryField> predicateQueryFieldList, List<QueryField> orderQueryFieldList) {
		CriteriaQuery<X> criteriaQuery = getSession().getCriteriaBuilder().createQuery(resultClass);
		Root<T> root = criteriaQuery.from(_clazz);
		if (predicateQueryFieldList != null) {
			Predicate predicate = null;
			for (QueryField queryField : predicateQueryFieldList) {
				predicate = createPredicate(queryField, criteriaQuery, root, predicate);
			}
			criteriaQuery.where(predicate);
		}
		
		if (orderQueryFieldList != null) {
			List<Order> orderList = new ArrayList<Order>();
			for (QueryField queryField : orderQueryFieldList) {
				Order order = createOrder(queryField, root);
				orderList.add(order);
			}
			criteriaQuery.orderBy(orderList);
		}
		
		return criteriaQuery;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <X> List<X> loadPage(Class<X> resultClass, QueryField resultQueryField, List<QueryField> predicateQueryFieldList, List<QueryField> orderQueryFieldList, Integer first, Integer pageSize) {
		if (resultClass == null) {
			resultClass = (Class<X>) _clazz;
		}
		CriteriaQuery<X> criteriaQuery = this.createCriteriaQuery(resultClass, predicateQueryFieldList, orderQueryFieldList);
		Root<T> root = (Root<T>) criteriaQuery.getRoots().iterator().next();
		
		// 设置root为查询结果类型
		From<T, T> from = root;
		// 如果查询结果对象的查询字段不为空
		if (resultQueryField != null) {
			QueryField field = resultQueryField;
			while (field != null) {
				// 在root的已有Join中查找，否则进行join操作
				boolean flag = true;
				for (Join<T, ?> join : from.getJoins()) {
					if (join.getAttribute().getName().equals(field.getName())) {
						from = (From<T, T>) join;
						flag = false;
						break;
					}
				}
				if (flag) {
					from = from.join(field.getName());
				}
				field = (QueryField) field.getValue();
			}
		}
		// 设置查询结果
		criteriaQuery.select((Selection<? extends X>) from);
		if (resultClass != null && resultQueryField != null) {
			criteriaQuery.distinct(true);
		}
		
		// 创建查询并返回结果
		Query<X> query = getSession().createQuery(criteriaQuery).setCacheable(true);
		if (first != null && pageSize != null) {
			query.setFirstResult(first).setMaxResults(pageSize);
		}
		List<X> result = query.getResultList();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long count(List<QueryField> predicateQueryFieldList) {
		Session session = getSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = this.createCriteriaQuery(Long.class, predicateQueryFieldList, null);
		Root<T> root = (Root<T>) criteriaQuery.getRoots().iterator().next();
		criteriaQuery.select(criteriaBuilder.count(root));
		Long count = session.createQuery(criteriaQuery).getSingleResult();
        return count;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer max(String attributeName, List<QueryField> predicateQueryFieldList) {
		Session session = getSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaQuery<Integer> criteriaQuery = this.createCriteriaQuery(Integer.class, predicateQueryFieldList, null);
		Root<T> root = (Root<T>) criteriaQuery.getRoots().iterator().next();
		criteriaQuery.select(criteriaBuilder.max(root.get(attributeName)));
		Integer max = session.createQuery(criteriaQuery).getSingleResult();
        return max;
	}
	
	@Override
	public void lock(T t) {
		Session session = getSession();
		session.lock(t, LockMode.NONE);
	}
	
	@Override
	public void evict(T t) {
		Session session = getSession();
		session.evict(t);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public K save(T t) {
		Session session = getSession();
		return (K) session.save(t);
	}

	@Override
	public void saveAll(Collection<T> ct) {
		Session session = getSession();
		for (T t : ct) {
			session.save(t);
		}
	}

	@Override
	public T update(T t) {
		Session session = getSession();
		session.update(t);
		return t;
	}
	
	@Override
	public void update(T t, String... attributeNames) {
		Session session = getSession();
		CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
		CriteriaUpdate<T> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(_clazz);
		Root<T> root = criteriaUpdate.from(_clazz);
		try {
			for (String attributeName : attributeNames) {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(attributeName, _clazz);
				Object value = propertyDescriptor.getReadMethod().invoke(t);
				criteriaUpdate.set(attributeName, value);
			}
		} catch (Exception e) {}
		EntityType<T> entityType = getSessionFactory().createEntityManager().getMetamodel().entity(_clazz);
		String idName = entityType.getId(Integer.class).getName();
		Integer idValue = (Integer) getSessionFactory().getPersistenceUnitUtil().getIdentifier(t);
		criteriaUpdate.where(criteriaBuilder.equal(root.get(idName), idValue));
		session.createQuery(criteriaUpdate).executeUpdate();
	}

	@Override
	public T saveOrUpdate(T t) {
		Session session = getSession();
		session.saveOrUpdate(t);
		return t;
	}

	@Override
	public void delete(T t) {
		Session session = getSession();
		session.delete(t);
	}

	@Override
	public boolean deleteById(K id) {
		Session session = getSession();
		T t = session.get(_clazz, id);
		if (t == null) {
			return false;
		}
		session.delete(t);
		return true;
	}

	@Override
	public void deleteAll(Collection<T> ct) {
		Session session = getSession();
		for (T t : ct) {
			session.delete(t);
		}
	}
}
