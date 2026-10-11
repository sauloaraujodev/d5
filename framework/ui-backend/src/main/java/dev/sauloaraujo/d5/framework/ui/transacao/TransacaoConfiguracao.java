package dev.sauloaraujo.d5.framework.ui.transacao;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.BeanFactoryTransactionAttributeSourceAdvisor;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransacaoConfiguracao {
	@Bean
	DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		var autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		autoProxyCreator.setProxyTargetClass(true);
		return autoProxyCreator;
	}

	@Bean
	TransactionAttributeSource postMappingTransactionAttributeSource() {
		return new PostMappingTransactionAttributeSource();
	}

	@Bean
	TransactionInterceptor postMappingTransactionInterceptor(PlatformTransactionManager gerenciadorTransacao,
			TransactionAttributeSource postMappingTransactionAttributeSource) {
		var interceptor = new TransactionInterceptor();
		interceptor.setTransactionManager(gerenciadorTransacao);
		interceptor.setTransactionAttributeSource(postMappingTransactionAttributeSource);
		return interceptor;
	}

	@Bean
	BeanFactoryTransactionAttributeSourceAdvisor postMappingTransactionAdvisor(
			TransactionAttributeSource postMappingTransactionAttributeSource,
			TransactionInterceptor postMappingTransactionInterceptor) {
		var advisor = new BeanFactoryTransactionAttributeSourceAdvisor();
		advisor.setTransactionAttributeSource(postMappingTransactionAttributeSource);
		advisor.setAdvice(postMappingTransactionInterceptor);
		return advisor;
	}
}
