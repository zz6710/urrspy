---

>	Description: 列表控件

>	Author: 代超

---

k-grid属性
data-display：是否启用详情功能，默认true，双击行弹出详情
data-operate-column：是否展示操作列，默认true
data-operate-column-position：操作列位置 first/end  默认：end
data-checkbox：是否多选
data-operate-width: 操作列的宽度
data-width: 表格宽度
data-checkbox-width：多选宽度
data-operate-width：操作宽度
- data-fixed-header-height: 固定表头高度
data-checkbox-id：指定查询结果集的主键字段，多个字段用半角逗号分隔
data-page-size：每页记录数，默认10，为0表示不分页，同时不显示分页组件
data-autoload：是否自动加载数据
-	data-action：指定数据来源action
-	data-graphql：指定数据来源graphql
-   data-url: 指定数据来源url
data-params：查询的固定参数值 data-params='{"k1":"v1"}'
data-row-select：绑定行选中事件
data-db-click：绑定行双击事件
data-before-load：绑定数据加载前执行事件，如果JS里返回false，则阻止数据加载
data-after-load：绑定数据加载完成后执行事件
data-fit:列的宽度是否自撑开
  data-summary: 是否显示合计列
  data-summary-lable: 合计列的第一列标题，默认：合计
  data-summary-method: 指定列合计的方法
  data-span-method: span-method方法可以实现合并行或列，方法的参数是一个对象，里面包含当前行row、当前列column、当前行号rowIndex、当前列号columnIndex四个属性。该函数可以返回一个包含两个元素的数组，第一个元素代表rowspan，第二个元素代表colspan。 也可以返回一个键名为rowspan和colspan的对象。

k-grid：方法
  load(params);//加载方法
  getSelected();//获取选中的列
  setSelected(arr);//设置选中的列 arr 为 id
  clearSelected();//清空选择项
  clearAll() // 清除所有
  selectAll() // 选择所有

k-grid-column:
data-sortable: 是否能根据该列排序，默认false
data-default-sort: 默认排序顺序 DESC/ASC，仅当 data-sortable 设置为 true 的时候有效，配置多个只生效第一个
data-align：指定该列数据对其方式 left/right/center
data-header：指定该列列表头显示的数据
data-name：指定该列的数据列表name，
data-type：指定该列的数据类型
          date
          time
          timestamp （时间戳转为 YYYY-MM-DD HH:mm:ss）
          datetime
          money
          double
          bigdecimal
          Percent
          long
          int
          string  默认为string类型，
data-render：该列数据显示前的加载事件，返回值将作为显示内容，参数有：row（列数据所在行的数据），k（该列的data-name）,text（该字段的值或者td中的html）
data-hidden：true/false，是否隐藏该列，默认为false
data-dict：根据字典来回显该列的数据，需要data-type为string (todo)
  data-fixed: 固定列，left/right
