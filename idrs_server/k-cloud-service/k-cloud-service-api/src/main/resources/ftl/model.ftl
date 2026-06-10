package ${package}.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "${lowHeadModel}Service",table = "${dbTable.name}")
@Data
public class ${model} {

   <#list dbTable.tableFields as field>
   @GraphQLField(<#if field.key>key = true ,</#if>kkhtml = "KFieldText", label = "${field.comment}", sql = "${field.dbField} = $S{${field.field}}" ,field = "${field.dbField}")
   private String ${field.field};
   </#list>

}