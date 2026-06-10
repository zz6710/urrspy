<template>
  <div>
    <k-field-code>
      <textarea>

k-grid属性  (属性为 true| false | number 时, 需要在属性前面加冒号,  当为字符串时候, 不需要)
   data-operate-column：是否展示操作列，默认true
   data-operate-column-position：操作列位置 first/end  默认：end
   data-checkbox：是否多选 (:data-checkbox="true"), k-grid-tree 不支持
   data-checkbox-id：指定查询结果集的主键字段，多个字段用半角逗号分隔 (设置 data-checkbox 为true时, 必须设置。通过id来获取table row 数据 )
   data-checkbox-width：多选宽度 :data-checkbox-width="100"
   data-operate-width: 操作列的宽度
   data-height: 表格高度
   data-page-size：每页记录数，默认10，为0表示不分页，同时不显示分页组件 :data-page-size="10"
   data-autoload：是否自动加载数据 :data-autoload="true"
         -	data-action：指定数据来源action
         -	data-graphql：指定数据来源graphql
   data-params：查询的固定参数值 :data-params='{"k1":"v1"}'

k-tree-grid:
    data-tree-id(*): k-tree-grid 必填, 为 children 中的 id.   eg: data-tree-id="id"
	  data-expand-all: 是否默认展开全部, false  eg:  :data-expand-all="true"
    data-tree-props: 给 tree 的children 属性指定一个别名 非必填  eg: data-tree-props="{children: 'children'}"
事件
   data-row-select：绑定行选中事件 @data-row-select="fn(row, column, event)" row: 行数据  column: 行配置  event: 属性
   data-db-click：绑定行双击事件  @data-db-click
   data-before-load：绑定数据加载前执行事件，如果JS里返回false，则阻止数据加载, 返回 json 对象, 可以增加额外参数  data-before-load="fn()
   data-after-load：绑定数据加载完成后执行事件 data-after-load = "fn(tableData)"
   data-fit:列的宽度是否自撑开(todo)
     data-summary: 是否显示合计列
     data-summary-lable: 合计列的第一列标题，默认：合计
     data-summary-method: 指定列合计的方法
     data-span-method: span-method方法可以实现合并行或列，方法的参数是一个对象，里面包含当前行row、当前列column、当前行号rowIndex、当前列号columnIndex四个属性。该函数可以返回一个包含两个元素的数组，第一个元素代表rowspan，第二个元素代表colspan。 也可以返回一个键名为rowspan和colspan的对象。

k-grid：方法(统一调用方式  this.$kgrid.xxx)
   load(params);//加载方法
   getSelected();//获取选中的列
   setSelected(arr);//设置选中的列 arr 为 id, 注意字符串还是数字
   clearSelected();//清空选择项
   clearAll() // 清除所有
   selectAll() // 选择所有

k-grid-column: (统一配置方式, 全部为字符串, 属性前面不能加 ":" 冒号)
    data-sortable: 是否能根据该列排序，默认false
    data-default-sort: 默认排序顺序，仅当 data-sortable 设置为 true 的时候有效，DESC/ASC
    data-align：指定该列数据对其方式 left/right/center
    data-header：指定该列列表头显示的数据 (姓名)
    data-name：指定该列的数据列表name     (username)
    data-type：指定该列的数据类型
              - date
              - time
              - timestamp （时间戳转为 YYYY-MM-DD HH:mm:ss）
              - datetime
              - money
              - double
              - bigdecimal
              - Percent
              - long
              - int
              - string  默认为string类型，
    data-render：该列数据显示前的加载事件，返回值将作为显示内容，参数有：row（列数据所在行的数据），k（该列的data-name）,text（该字段的值或者td中的html）
    data-hidden：true/false，是否隐藏该列，默认为false
    data-dict：根据字典来回显该列的数据，需要data-type为string
  data-fixed: 固定列，left/right

        </textarea>
    </k-field-code>

    <!--     <k-field-code>
    </k-field-code> -->

    基本表格
    <k-grid
      class="k-grid"
      data-graphql='{queryUser(action:"findUsers") {rows{  userid, username},results}}'
      :data-checkbox="true"
      data-checkbox-id="userid"
      data-after-load="handleAfterLoad"
      @data-row-select="handleRowSelect"
      @init="(grid)=>{this.$kgrid = grid}"
    >
      <k-grid-column
        data-header="用户id"
        data-name="userid"
        data-width="100"
        data-fixed="left"
      ></k-grid-column>
      <k-grid-column
        data-header="name"
        data-name="username"
        data-fixed="right"
      ></k-grid-column>
      <template slot="operate" slot-scope="scope">
        {{ scope }}
        <el-button @click="handleClickRowBtn($event)">show</el-button>
      </template>
      <template slot="expand" slot-scope="scope">
        行数据：{{ scope.row }}
      </template>
    </k-grid>

    <el-button @click="setRow()">设置</el-button>
  </div>
</template>

<script>
import kayak from "@/frame/kayak.js";

export default {
  props: {
    dataContentType: {
      type: String
    }
  },
  data() {
    return {
      $kgrid: null
    };
  },
  methods: {
    handleClickRowBtn(ev) {
      console.log(this.$kgrid.getRowData(ev));
    },
    handleRowSelect(row, column, event) {
      /*  console.log("row: %o, column: %o,  event: %o", row, column, event); */
    },
    handleAfterLoad(data) {
      console.log(data);
    },
    setRow() {
      this.$kgrid.setSelected(["10267"]);
    }
  }
};
</script>
<style>
</style>
