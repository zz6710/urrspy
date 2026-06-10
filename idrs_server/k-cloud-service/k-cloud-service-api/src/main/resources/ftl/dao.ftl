package ${package};

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class ${model}Dao extends ComnDao {

	public SqlResult<${model}> find${model}s(SqlParam<${model}> params) throws Exception {
		return super.findRows("SELECT ${dbTable.fields} FROM ${dbTable.name}", params);
	}

	public UpdateResult add${model}(SqlParam<${model}> params) throws Exception {
		return super.update("INSERT INTO ${dbTable.name}(${dbTable.fields}) VALUES(${dbTable.insertParams})",
				params.getModel());
	}
	
	public UpdateResult update${model}(SqlParam<${model}> params) throws Exception {
		return super.update("UPDATE ${dbTable.name} SET ${dbTable.updateParams} WHERE ${dbTable.keyParams}",
				params.getModel());
	}
	
	public UpdateResult delete${model}(SqlParam<${model}> params) throws Exception {
		return super.update("DELETE FROM ${dbTable.name} WHERE ${dbTable.keyParams}",
				params.getModel());
	}

}
