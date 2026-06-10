package com.kayak.graphql.autoconfigure;

import com.kayak.auth.dao.ServerDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.Tools;
import com.kayak.graphql.service.GraphqlService;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.*;
import graphql.schema.GraphQLObjectType.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static graphql.schema.GraphQLObjectType.newObject;

@Service
public class GraphQLAnnotationImpl {

	private static Logger log = LoggerFactory.getLogger(GraphQLAnnotationImpl.class);

	@Autowired
	private ServerDao serverService;
	@Autowired
	private GraphqlService graphqlService;

	private Map<String, String> serverMap = new HashMap<>();

	boolean isInit = false;

	private GraphQL graphQL;

	private GraphQLObjectType mutationType;

	public static final Map<String, SqlRow> authMap = new ConcurrentHashMap<>();

	public static final Map<String, List<SqlRow>> authOpCheckMap = new ConcurrentHashMap<>();

	public static final Map<String, List<SqlRow>> authRoleCheckMap = new ConcurrentHashMap<>();

	public final Map<String, SqlRow> modelMap = new HashMap<String, SqlRow>();;

	public static final Map<String, GraphQLScalarType> ScalarsMap = new HashMap<String, GraphQLScalarType>() {
		private static final long serialVersionUID = 1L;

		{
			put("int", Scalars.GraphQLInt);
			put("java.lang.Integer", Scalars.GraphQLInt);
			put("java.lang.String", Scalars.GraphQLString);
			put("short", ExtendedScalars.GraphQLShort);
			put("java.lang.Short", ExtendedScalars.GraphQLShort);
			put("long", ExtendedScalars.GraphQLLong);
			put("java.lang.Long", ExtendedScalars.GraphQLLong);
			put("double", ExtendedScalars.GraphQLBigDecimal);
			put("java.lang.Double", ExtendedScalars.GraphQLBigDecimal);
			put("java.math.BigDecimal", ExtendedScalars.GraphQLBigDecimal);
			put("float", Scalars.GraphQLFloat);
			put("char", ExtendedScalars.GraphQLChar);
			put("byte", ExtendedScalars.GraphQLByte);
			put("boolean", Scalars.GraphQLBoolean);
			put("java.lang.Boolean", Scalars.GraphQLBoolean);
		}
	};

	@PostConstruct
	public void init() {
		synchronized (GraphQLAnnotationImpl.class) {
			if (!isInit) {
				try {
					// 定义GraphQL类型
					mutationType = newObject().name("mutation")
							.field(GraphQLFieldDefinition.newFieldDefinition().name("update").type(Scalars.GraphQLInt))
							.field(GraphQLFieldDefinition.newFieldDefinition().name("data").type(Scalars.GraphQLString))
							.build();
					loadConfig();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("初始化配置异常");
				}
			}

			isInit = true;

		}

	}

	public void loadConfig() throws Exception {
		this.loadAnnotation();
		this.loadAuths();
		this.loadModeMap();
		this.loadAuthOpChecks();
		log.info("网关缓存刷新成功");
	}

	private void loadAnnotation() throws Exception {

		List<SqlRow> serverModels = serverService.findServerModels();

		graphql.schema.GraphQLSchema.Builder schemaBuilder = GraphQLSchema.newSchema();

		Builder queryTypeBuilder = newObject().name("kayakquery");
		Builder mutationTypeBuilder = newObject().name("kayakmutation");

		for (SqlRow serverModel : serverModels) {
			String modelName = serverModel.getString("model_name");
			String appName = serverModel.getString("app_name");
			String modelFullName = serverModel.getString("model_full_name");
			String modelField = serverModel.getString("model_field");
			String serverName = serverModel.getString("server_name");

			serverMap.put(modelName, appName);

			// 定义GraphQL类型
			graphql.schema.GraphQLObjectType.Builder graphQLObjectTypeBuilder = newObject().name(modelName);

			List<GraphQLArgument> graphQLArguments = new ArrayList<GraphQLArgument>();

			graphQLArguments
					.add(GraphQLArgument.newArgument().name("action").type(ScalarsMap.get("java.lang.String")).build());
			graphQLArguments.add(GraphQLArgument.newArgument().name("start").type(Scalars.GraphQLInt).build());
			graphQLArguments.add(GraphQLArgument.newArgument().name("limit").type(Scalars.GraphQLInt).build());
			graphQLArguments.add(GraphQLArgument.newArgument().name("sort").type(Scalars.GraphQLString).build());
			graphQLArguments.add(GraphQLArgument.newArgument().name("dir").type(Scalars.GraphQLString).build());
			graphQLArguments.add(GraphQLArgument.newArgument().name("ip").type(Scalars.GraphQLString).build());
			if (modelName.equals("Test")) {
				System.out.println("..........");
			}

			if (!Tools.strIsEmpty(modelField)) {
				String[] modelFields = modelField.split(",");
				for (String _modelField : modelFields) {
					String[] __modelFields = _modelField.split(":");
					String fieldName = __modelFields[0];
					String fieldtype = __modelFields[1];

					try {
						graphQLObjectTypeBuilder.field(GraphQLFieldDefinition.newFieldDefinition().name(fieldName)
								.type(ScalarsMap.get(fieldtype)));
					} catch (Exception e) {
						log.error("实体类【" + modelName + "】字段映射错误, 字段名为:" + fieldName + ", 字段类型为:" + fieldtype);
					}

					graphQLArguments
							.add(GraphQLArgument.newArgument().name(fieldName).type(ScalarsMap.get(fieldtype)).build());

				}
			}

			GraphQLObjectType modelType = graphQLObjectTypeBuilder.build();

			// 定义查询
			graphql.schema.GraphQLObjectType.Builder dataBuilder = newObject().name(modelName + "s");
			dataBuilder
					.field(GraphQLFieldDefinition.newFieldDefinition().type(new GraphQLList(modelType)).name("rows"));
			dataBuilder.field(GraphQLFieldDefinition.newFieldDefinition().type(Scalars.GraphQLInt).name("results"));
			dataBuilder.field(GraphQLFieldDefinition.newFieldDefinition().type(Scalars.GraphQLString).name("treeData"));

			queryTypeBuilder.field(GraphQLFieldDefinition.newFieldDefinition().type(dataBuilder.build())
					.name("query" + modelName).argument(graphQLArguments)
					.dataFetcher(graphqlService.queryFetcher(appName, serverName, modelFullName)));

			// 定义更新
			mutationTypeBuilder.field(GraphQLFieldDefinition.newFieldDefinition().type(mutationType)
					.name("mutation" + modelName).argument(graphQLArguments)
					.dataFetcher(graphqlService.mutationFetcher(appName, serverName, modelFullName)));
		}

		schemaBuilder.query(queryTypeBuilder.build()).mutation(mutationTypeBuilder.build());

		graphQL = GraphQL.newGraphQL(schemaBuilder.build()).build();

	}

	/**
	 * 加载服务配置
	 * 
	 * @throws Exception
	 */
	private void loadAuths() throws Exception {
		List<SqlRow> servers = serverService.findServers();

		authMap.clear();
		for (SqlRow server : servers) {
			authMap.put(server.getString("server"), server);
		}
	}

	/**
	 * 加载授权审批
	 * @throws Exception
	 */
	private void loadAuthOpChecks()throws Exception{
		List<SqlRow> authOpChecks = serverService.findAuthOpChecks();
		List<SqlRow> authRoleChecks = serverService.findAuthRoleChecks();
		authOpCheckMap.clear();
		authRoleCheckMap.clear();
		for (SqlRow authOpcheck : authOpChecks) {
			if(authOpCheckMap.containsKey(authOpcheck.getString("server"))){
				authOpCheckMap.get(authOpcheck.getString("server")).add(authOpcheck);
			}else{
				List<SqlRow> sqlRowList=new LinkedList<>();
				sqlRowList.add(authOpcheck);
				authOpCheckMap.put(authOpcheck.getString("server"), sqlRowList);
			}
		}

		for (SqlRow authRolecheck : authRoleChecks) {
			String key=authRolecheck.getString("roleid")+"-"+authRolecheck.getString("server");
			if(authRoleCheckMap.containsKey(key)){
				authRoleCheckMap.get(key).add(authRolecheck);
			}else{
				List<SqlRow> sqlRowList=new LinkedList<>();
				sqlRowList.add(authRolecheck);
				authRoleCheckMap.put(key, sqlRowList);
			}
		}
	}

	public GraphQL getGraphQL() {
		return graphQL;
	}

	public Map<String, String> getServerMap() {
		return serverMap;
	}

	public void loadModeMap() throws Exception {
		List<SqlRow> serverModels = serverService.findServerModels();
		for (SqlRow serverModel : serverModels) {
			modelMap.put(serverModel.getString("model_name"), serverModel);
		}
		
	}

}
