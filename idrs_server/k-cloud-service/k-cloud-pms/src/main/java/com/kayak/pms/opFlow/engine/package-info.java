/**
 * Created by daniel on 29/03/2017.
 * Service可以看做是一个对外暴露的服务接口  并且对特殊的对象进行转化,比如表单数据
 * Dao防止数据层的更改，在Dao层返回对象，防止connection关闭(应为现在的事务都在数据库手动关闭，而不是通过代理来进行关闭)
 * 在其它地方用workflowEngine来暴露对应的服务，便于统一管理
 * Controller层只用来接收前台传递过来的数据
 * 时间之类的数据设置应该在service层进行，这样如果对service进行调用。避免上层每次进行对应的设置
 *
 *
 *
 *
 *
 * 事物问题，
 * 要么直接多个service之间相互调用
 * 要么在一个service钟调用多个Dao
 * 禁止在service中即调用service又调用dao
 */
package com.kayak.pms.opFlow.engine;
