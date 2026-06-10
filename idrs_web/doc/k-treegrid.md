---

>	Description: 树列表控件

>	Author: 代超

---



k-treegrid属性
data-checkbox：是否多选
data-checkbox-width：多选宽度
- data-fixed-header-height: 固定表头高度
data-checkbox-id：指定查询结果集的主键字段，多个字段用半角逗号分隔
  data-diffcondition: 指定树结构上下级的映射字段，如：id,upperid
data-autoload：是否自动加载数据
-	data-action：指定数据来源action
-	data-graphql：指定数据来源graphql
data-params：查询的固定参数值
data-row-select：绑定行选中事件
data-db-click：绑定行双击事件
data-before-load：绑定数据加载前执行事件，如果JS里返回false，则阻止数据加载
data-after-load：绑定数据加载完成后执行事件
data-fit:列的宽度是否自撑开

k-treegrid：方法
  load(params);//加载方法
  getSelected();//获取选中的列
  setSelected(arr);//设置选中的列 arr 为 id
  clearSelected();//清空选择项
  clearAll() // 清除所有
  selectAll() // 选择所有

k-treegrid-column:
data-align：指定该列数据对其方式 left/right/center
data-header：指定该列列表头显示的数据
data-name：指定该列的数据列表name，
data-type：指定该列的数据类型
          date
          time
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
data-dict：根据字典来回显该列的数据，需要data-type为string
  data-fixed: 固定列，left/right
