<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
	<session-factory>
		<!-- -->
    	<property name="connection.driver_class">com.ibm.db2.jcc.DB2Driver</property>
    	<property name="connection.url">jdbc:db2://diva.deis.unibo.it:50000/tw_stud</property>
    
    	<property name="connection.username">user</property>
    	<property name="connection.password">pass</property>
   		<property name="connection.pool_size">1</property>
    
    	<property name="dialect">org.hibernate.dialect.DB2Dialect</property>
    	<!-- <property name="dialect">org.hibernate.dialect.HSQLDialect</property> -->
    	<!-- <property name="dialect">org.hibernate.dialect.MySQLDialect</property> -->
    
    	<property name="current_session_context_class">thread</property>
    	<property name="cache.provider_class">org.hibernate.cache.NoCacheProvider</property>
    	<property name="show_sql">true</property>
    	<property name="hbm2ddl.auto">create</property>
    	
<#list managers as classe>	
		<mapping resource="it/unibo/tw/${classe.nomeBean}.hbm.xml"/>
</#list>	

	</session-factory>
</hibernate-configuration>