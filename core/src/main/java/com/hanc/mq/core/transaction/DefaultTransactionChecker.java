package com.hanc.mq.core.transaction;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.hanc.mq.core.model.OnsTopic;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DefaultTransactionChecker implements LocalTransactionChecker{

	@Autowired
	private OnsTopic onsTopic;

	@Autowired(required=false)
	private List<TransactionCheckStrategy<?>> strategies;

	@Override
	public TransactionStatus check(Message message) {
		for (TransactionCheckStrategy<?> strategy : strategies) {
			if(strategy.support(message.getTag())){
				return strategy.check(message);
			}
		}
		return TransactionStatus.Unknow;
	}

}
