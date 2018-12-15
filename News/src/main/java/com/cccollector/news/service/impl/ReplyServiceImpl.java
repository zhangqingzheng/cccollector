/**
 * 
 */
package com.cccollector.news.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cccollector.news.dao.ReplyDao;
import com.cccollector.news.model.Reply;
import com.cccollector.news.service.ReplyService;
import com.cccollector.universal.service.GenericServiceHibernateImpl;

/**
 * 回复服务实现类
 *
 * @author 谢朋
 * Copyright © 2015-2018年 北京华星成汇文化发展有限公司. All rights reserved.
 */
@Service("replyService")
public class ReplyServiceImpl extends GenericServiceHibernateImpl<Integer, Reply> implements ReplyService {

	@SuppressWarnings("unused")
	@Autowired
	private ReplyDao replyDao;
}
