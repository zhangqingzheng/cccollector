/**
 * 
 */
package com.cccollector.universal.dao;

/**
* 查询字段类
 *
 * @author 谢朋
 * Copyright © 2016年 北京华星成汇文化发展有限公司. All rights reserved.
 */
public class QueryField {

	/**
	 * 谓词枚举
	 */
	public static enum PredicateEnum {
		EQUAL,
		NOT_EQUAL,
		GREATER_THAN,
		GREATER_THAN_OR_EQUAL,
		LESS_THAN,
		LESS_THAN_OR_EQUAL,
		LIKE,
		NOT_LIKE,
		IS_NULL,
		IS_NOT_NULL,
		IN,
		NOT_IN
	}

	/**
	 * 布尔运算符枚举
	 */
	public static enum BooleanOperatorEnum {
		AND,
		OR
	}
	
    public QueryField(String name, Object value) {
    	_name = name;
    	_value = value;
    	_predicate = PredicateEnum.EQUAL;
    	_booleanOperator = BooleanOperatorEnum.AND;
    }
	
    public QueryField(String name, Object value, PredicateEnum predicate) {
    	_name = name;
    	_value = value;
    	_predicate = predicate;
    	_booleanOperator = BooleanOperatorEnum.AND;
    }	
    public QueryField(String name, Object value, PredicateEnum predicate, BooleanOperatorEnum booleanOperator) {
    	_name = name;
    	_value = value;
    	_predicate = predicate;
    	_booleanOperator = booleanOperator;
    }

	private String _name;

	/**
	 * 名称
	 */
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	private Object _value;

	/**
	 * 值
	 */
	public Object getValue() {
		return _value;
	}

	public void setValue(Object value) {
		_value = value;
	}

	private PredicateEnum _predicate;

	/**
	 * 谓词
	 */
	public PredicateEnum getPredicate() {
		return _predicate;
	}

	public void setPredicate(PredicateEnum predicate) {
		_predicate = predicate;
	}

	private BooleanOperatorEnum _booleanOperator;

	/**
	 * 布尔运算符
	 */
	public BooleanOperatorEnum getBooleanOperator() {
		return _booleanOperator;
	}

	public void setBooleanOperator(BooleanOperatorEnum booleanOperator) {
		_booleanOperator = booleanOperator;
	}
}
