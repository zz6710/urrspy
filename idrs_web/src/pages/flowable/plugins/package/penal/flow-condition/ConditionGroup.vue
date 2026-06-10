<template>
  <div>
    <el-form-item style="margin-top:8px" label="条件组关系">
      <el-switch v-model="conditionConfig.groupsType" active-color="#409EFF" inactive-color="#c1c1c1" active-value="&&" inactive-value="||" active-text="且" inactive-text="或" />
    </el-form-item>
    <div class="element-drawer__button">
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="addGroup">添加条件组</el-button>
    </div>
    <div class="element-drawer__button" style="margin-bottom: 8px;">
      <el-input v-model="conditionExpressionContent" disabled type="textarea" autosize />
    </div>
    <div v-for="(group, index) in conditionConfig.conditionGroups" :key="index + '_g'" class="group">
      <div class="group-header">
        <span class="group-name">条件组 {{ groupNames[index] }}</span>
        <div class="group-cp">
          <span>组内条件关系：</span>
          <el-switch v-model="group.groupType" active-color="#409EFF" inactive-color="#c1c1c1" active-value="&&" inactive-value="||" active-text="且" inactive-text="或" />
        </div>
        <div class="group-operation">
          <i class="el-icon-plus" @click="addCondition(group)"></i>
          <i class="el-icon-delete" @click="delGroup(index)"></i>
        </div>
      </div>
      <div class="group-content">
        <p v-if="group.conditions.length === 0">点击右上角 + 为本条件组添加条件 ☝</p>
        <div v-else>
          <el-form ref="condition-form" label-width="80px">
            <el-form-item v-for="(condition, cindex) in group.conditions" :key="condition.id + '_' + cindex">
              <!--流程参数-->
              <el-tooltip slot="label" placement="top-start" @click.stop.prevent>
                <template slot="content">
                  {{condition.conditionAndFunctionContent}}
                </template>
                <el-cascader :options="conditionAndFunctionOptions" v-model="condition.conditionAndFunction" @change="conditionChange(condition)" style="width: 106px;" :props="{checkStrictly: true }"></el-cascader>
              </el-tooltip>
              <!--操作符-->
              <el-cascader :options="compareOptions" v-model="condition.typeAndCompare" style="width: 86px;margin-left: 30px" size="small" placeholder="判断符" :show-all-levels="false"></el-cascader>
              <!--值类型-->
              <el-select v-model="condition.valueType" style="width: 85px;" @change="condition.value = ''" placeholder="请选择值类型">
                <el-option label="输入值" value="input"></el-option>
                <el-option label="参数" value="param"></el-option>
              </el-select>
              <!--值-->
              <el-input v-if="condition.valueType==='input'&&condition.typeAndCompare[0]=='String'" style="width: 85px;" v-model="condition.value" clearable>
              </el-input>
              <el-input v-else-if="condition.valueType==='input'&&condition.typeAndCompare[0]=='Number'" oninput="value=value.replace(/[^0-9.]/g,'')" style="width: 85px;" v-model="condition.value" clearable>
              </el-input>
              <el-select v-else-if="condition.valueType==='param'" style="width: 85px;" size="small" v-model="condition.value" clearable>
                <el-option v-for="item in conditionList" :key="item.id" :label="item.title" :value="item.title" />
              </el-select>
              <i class="el-icon-delete" @click="rmSubCondition(group, cindex)"></i>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import { getEnvItems } from "../../utils";
export default {
	name: "ConditionGroup",
	props: {},
	inject: {
		prefix: "prefix",
		width: "width",
	},
	data() {
		return {
			groupNames: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"],
			conditionConfig: {
				groupsType: "||",
				conditionGroups: [],
			},
			conditionTooltip: "",
			conditionList: [],
			conditionAndFunctionOptions: [],
			conditionAndFunction: [],
			functions: [{ value: "length", label: "长度" }],
			conditionExpressionContent: "",
			compareOptions: [
				{
					value: "String",
					label: "字符串",
					children: [
						{ value: "==", label: "等于" },
						{ value: "!=", label: "不等于" },
					],
				},
				{
					value: "Number",
					label: "数字",
					children: [
						{ label: "等于", value: "==" },
						{ label: "不等于", value: "!=" },
						{ label: "大于", value: ">" },
						{ label: "大于等于", value: ">=" },
						{ label: "小于", value: "<" },
						{ label: "小于等于", value: "<=" },
					],
				},
			],
		};
	},
	watch: {
		conditionConfig: {
			deep: true,
			handler(newValue) {
				this.updateConditionExpression(newValue);
			},
		},
	},
	created() {
		let conditionConfig = window.bpmnInstances.bpmnElement.businessObject.conditionConfig;
		if (conditionConfig) {
			this.conditionConfig = JSON.parse(conditionConfig);
		}
		this.getConditionList();
	},
	methods: {
		conditionChange(condition) {
			condition.conditionAndFunctionContent = "";
			if (condition) {
				let cond = condition.conditionAndFunction[0];
				let fun = condition.conditionAndFunction[1];
				if (cond) {
					condition.conditionAndFunctionContent += cond;
				}
				if (fun) {
					condition.conditionAndFunctionContent += `/${this.getFunctionLabel(fun)}`;
				}
			}
		},
		//更新表达式
		updateConditionExpression(newValue) {
			//保存条件组对象
			window.bpmnInstances.modeling.updateProperties(window.bpmnInstances.bpmnElement, { conditionConfig: JSON.stringify(newValue) });
			console.log("conditionConfig", newValue);
			//将条件组对象解析为表达式
			//表达式
			let conditionExpression = "";
			//表达式的中文内容
			this.conditionExpressionContent = "";
			newValue.conditionGroups.forEach((group, index) => {
				if (index == 0) {
					conditionExpression += "(";
					this.conditionExpressionContent += "(";
				} else {
					conditionExpression += ` ${this.conditionConfig.groupsType} (`;
					this.conditionExpressionContent += ` ${this.conditionConfig.groupsType == "&&" ? "且" : "或"} (`;
				}
				group.conditions.forEach((condition, conditionIndex) => {
					if (!condition.conditionAndFunction) {
						return;
					}
					//获取流程参数和函数
					let cond = condition.conditionAndFunction[0];
					let fun = condition.conditionAndFunction[1];
					if (fun) {
						conditionExpression += ` var:${fun}(param.${cond}) `;
						this.conditionExpressionContent += ` ${this.getFunctionLabel(fun)}(${cond}) `;
					} else {
						conditionExpression += ` param.${cond} `;
						this.conditionExpressionContent += ` ${cond} `;
					}

					if (!condition.typeAndCompare) {
						return;
					}
					//获取值类型和比较符
					let type = condition.typeAndCompare[0];
					let compare = condition.typeAndCompare[1];
					conditionExpression += compare + " ";
					this.conditionExpressionContent += this.getCompareLabel(compare) + " ";
					if (condition.valueType == "param") {
						conditionExpression += "var:parse('" + condition.value + "')";
						this.conditionExpressionContent += condition.value;
					} else if (condition.valueType == "input") {
						if (type === "String") {
							conditionExpression += "'" + condition.value + "'";
							this.conditionExpressionContent += "'" + condition.value + "'";
						} else if (type === "Number") {
							conditionExpression += condition.value;
							this.conditionExpressionContent += condition.value;
						}
					}
					if (group.conditions.length != conditionIndex + 1) {
						conditionExpression += " " + group.groupType;
						this.conditionExpressionContent += " " + (group.groupType == "&&" ? "且" : "或");
					}
				});
				conditionExpression += " )";
				this.conditionExpressionContent += " )";
			});
			console.log("conditionExpression", conditionExpression);
			//保存解析完毕的表达式
			conditionExpression = "${" + conditionExpression + "}";
			let condition = window.bpmnInstances.moddle.create("bpmn:FormalExpression", { body: conditionExpression });
			window.bpmnInstances.modeling.updateProperties(window.bpmnInstances.bpmnElement, { conditionExpression: condition });
			this.$emit("updateConditionExpression", conditionExpression);
		},
		getCompareLabel(value) {
			return this.compareOptions[1].children.filter((t) => t.value == value)[0]?.label;
		},
		getFunctionLabel(value) {
			return this.functions.filter((t) => t.value == value)[0]?.label;
		},
		getConditionList() {
			let functions = [];
			functions.push({ value: "length", label: "长度" });
			getEnvItems().then((rows) => {
				rows.forEach((item) => {
					this.conditionList.push({ title: item.itemKey, id: item.envItemId });
					this.conditionAndFunctionOptions.push({ label: item.itemKey, value: item.itemKey, children: this.functions });
				});
			});
		},
		delGroup(index) {
			this.conditionConfig.conditionGroups.splice(index, 1);
		},
		addGroup() {
			this.conditionConfig.conditionGroups.push({
				groupType: "OR",
				conditions: [],
			});
		},
		rmSubCondition(group, index) {
			group.conditions.splice(index, 1);
		},
		// 新增条件
		addCondition(group) {
			group.conditions.push({ value: "", valueType: "", conditionAndFunctionContent: "" });
		},
	},
};
</script>
  
<style lang="scss" scoped>
.el-dropdown-link {
	cursor: pointer;
	color: #409eff;
}
.el-icon-arrow-down {
	font-size: 12px;
}
.group {
	margin-bottom: 20px;
	color: #5e5e5e;
	overflow: hidden;
	border-radius: 6px;
	border: 1px solid #e3e3e3;

	.group-header {
		padding: 5px 10px;
		background: #e3e3e3;
		position: relative;

		div {
			display: inline-block;
		}

		.group-name {
			font-size: small;
		}

		.group-cp {
			font-size: small;
			position: absolute;
			left: 100px;
			display: flex;
			top: 5px;
			justify-content: center;
			align-items: center;
		}

		.group-operation {
			position: absolute;
			right: 10px;

			i {
				padding: 0 10px;

				&:hover {
					cursor: pointer;
				}
			}
		}
	}

	.group-content {
		padding: 10px 5px;
		p {
			text-align: center;
			font-size: small;
		}
		.el-icon-delete {
			position: absolute;
			cursor: pointer;
			top: 12px;
			right: 0;
		}
	}

	.condition-title {
		display: block;
		width: 100px;
	}
}
</style>
  