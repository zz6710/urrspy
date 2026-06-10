INSERT INTO sys_menu (moduleid,menuid,menuname,shortname,model,upperid,url,iconcls,loadorder,icon,status,remark,auth_server) VALUES
	 ('3','M10','工作流','流程',NULL,'','','setting',0,'workflow','N',NULL,NULL),
	 ('3','M1001','流程参数','',NULL,'M10','flowable/flowParam','setting',1,'flowContext','N',NULL,NULL),
	 ('3','M1002','表单配置','','','M10','flowable/flowFormField','setting',2,'flowCtx','N','',''),
	 ('3','M1003','流程模型','','','M10','flowable/flowModel','setting',3,'flowDesign','N','',''),
	 ('3','M1004','部署管理','','','M10','flowable/flowDeploy','setting',4,'flowSurrogateActiveTask','N','',''),
	 ('3','M1005','业务配置','','','M10','flowable/flowBusinessConfig','setting',5,'flowBusinessConfiguration','N','',''),
	 ('3','M1006','待审核任务','','','M10','flowable/flowUserToDoTask','setting',6,'flowActiveTask','N','',''),
	 ('3','M1007','转审批','','','M10','flowable/flowSurrogate','setting',7,'surrogate','N','',''),
	 ('3','M1008','流程追踪','',NULL,'M10','flowable/flowTrack','setting',8,'flowProcessInstanceList','N',NULL,NULL),
	 ('3','M1009','业务流程状态','','','M10','flowable/flowBusinessStatus','setting',9,'flowBusinessStatusList','N','','');
INSERT INTO sys_menu (moduleid,menuid,menuname,shortname,model,upperid,url,iconcls,loadorder,icon,status,remark,auth_server) VALUES
	 ('3','M1010','抄送','','','M10','flowable/flowCopy','setting',10,'flowBusinessStatusList','N','','');
