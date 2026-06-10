package ${package}.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import ${package}.dao.${model}Dao;
import ${package}.model.${model};

@Service
@APIDefine(desc = "${dbTable.comment}服务", model = ${model}.class)
public class ${model}Service {

	@Autowired
	private ${model}Dao ${lowHeadModel}Dao;

	@API(desc = "查询${dbTable.comment}信息", auth = APIAuth.YES)
	public SqlResult<${model}> find${model}s(SqlParam<${model}> params) throws Exception {
		params.setMakeSql(true);
		return ${lowHeadModel}Dao.find${model}s(params);
	}

	@API(desc = "添加${dbTable.comment}", params = "${dbTable.fields}", auth = APIAuth.NO)
	public int add${model}(SqlParam<${model}> params) throws Exception {
		return ${lowHeadModel}Dao.add${model}(params).getEffect();
	}
	
	@API(desc = "修改${dbTable.comment}", params = "${dbTable.fields}", auth = APIAuth.NO)
	public int update${model}(SqlParam<${model}> params) throws Exception {
		return ${lowHeadModel}Dao.update${model}(params).getEffect();
	}
	
	@API(desc = "删除${dbTable.comment}", params = "${dbTable.fields}", auth = APIAuth.NO)
	public int delete${model}(SqlParam<${model}> params) throws Exception {
		return ${lowHeadModel}Dao.delete${model}(params).getEffect();
	}

}
