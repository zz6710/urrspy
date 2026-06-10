import Tools from '@/utils/tools.js'

export default {
  methods: {
    getGraphqlUrl(dataGraphql,dataDiffcondition){
      let data_graphql = dataGraphql.trim()
      let queryName = data_graphql.substring(data_graphql.indexOf("{")+1,data_graphql.indexOf("("))
      data_graphql += "treeConfig{"
      data_graphql += queryName
      data_graphql += ":{"
      data_graphql += "diffcondition: '"+ dataDiffcondition + "'"
      data_graphql += "}"
      data_graphql += "}"
      return data_graphql
    },
    //处理数据
    handleData(rows) {
      const options = []
      rows.map(row => {
        // 获取值
        const valueField = row[this.dataValueField]
        // 获取显示值
        let displayField
        let disabled = false
        const dataDisplayFields = this.dataDisplayField.split(',')
        for (let i = 0; i < dataDisplayFields.length; i++) {
          if (i === 0) {
            displayField = row[dataDisplayFields[i]]
          } else {
            displayField += this.dataDisplaySeparator + row[dataDisplayFields[i]]
          }
        }
        if (this.dataValueDisabled && row.hasOwnProperty(this.dataValueDisabled)) {
          disabled = true
        }
        if (this.props.children && row.hasOwnProperty(this.props.children) && row[this.props.children] instanceof Array) {
          const childoptions = this.handleData(row[this.props.children])
          const _row = {}
          _row[this.props.label] = displayField
          _row[this.props.value] = valueField
          Tools.apply(_row, row)
          if (disabled) {
            this.setRowDisabld(_row)
          }
          _row[this.props.children] = childoptions
          options.push(_row)
        } else {
          const _row = {}
          _row[this.props.label] = displayField
          _row[this.props.value] = valueField
          Tools.apply(_row, row)
          if (disabled) {
            this.setRowDisabld(_row)
          }
          options.push(_row)
        }
      })
      return options
    },
    setRowDisabld(row) {
      if (row[this.dataValueDisabled] === true || row[this.dataValueDisabled] === 'true' || row[this.dataValueDisabled] === 'disabled' || (this.dataValueMethod !== undefined ? this.dataValueMethod(row) : false)) {
        row[this.props.disabled] = 'disabled'
      }
    },
    //异步加载
    loadActionData() {
      this.httpUtil.comnQuery({
        action: this.dataAction,
        params: this.params
      }).then(data => {
        this.options = this.handleData(data.rows);
      });
    },
    loadGraphqlData() {
      this.httpUtil.graphqlQurey({
        graphql: this.graphql,
        params: this.params
      }).then(data => {
        let graphqlFirst = this.graphql.substring(this.graphql.indexOf("{") + 1, this.graphql.indexOf("("));
        this.options = this.handleData(data[graphqlFirst].rows);
      });
    },
    loadActionDataTree() {
      this.httpUtil.comnQueryTree({
        action: this.dataAction,
        params:this.params
      }).then(data => {
        this.options = this.handleData(data.rows);
      });
    },
    showNode (node,resolve) {
      const { data } = node
      if (data === undefined || data.length === 0 || node.level === 0) {
        return
      }
      const params = {}
      if (this.params.hasOwnProperty(this.dataParentField)) {
        params[this.dataParentField] = data[this.props.value]
      }
      setTimeout(() => {
        if (this.dataAction) {
          this.httpUtil.comnQuery({
            action: this.dataAction,
            params: params
          }).then(data => {
            resolve(this.handleData(data.rows))
          })
        } else if (this.dataGraphql) {
          this.httpUtil.graphqlQurey({
            graphql: this.dataGraphql,
            params: params
          }).then(data => {
            // 获取请求头
            const graphqlFirst = this.dataGraphql.substring(this.dataGraphql.indexOf('{') + 1, this.dataGraphql.indexOf('('))
            resolve(this.handleData(data[graphqlFirst].rows))
          })
        }
      }, 1000)
    }
  }
}
