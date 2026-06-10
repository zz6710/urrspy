<script>
const code_example_3 = `
<template>
  <div>
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
        {{scope}}
        <el-button @click="handleClickRowBtn($event)">show</el-button>
      </template>
      <template slot="expand" slot-scope="scope">
       行数据：{{scope.row}}
      </template>
    </k-grid>
  </div>
</template>

<script>
export default {
	data() {
		return {
			$kgrid: null
		};
	},
  created(){
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
<\/script>

<style>


<\/style>

`;

export default {
  name: "preview",
  data() {
    return {
      className: ["page-container"], // page className
    };
  },
  mounted() {},
  methods: {},

  render() {
    const { className, style } = this;

    return (
      <div class={className} style={style}>
        <code-viewer source={code_example_3} showCode></code-viewer>
      </div>
    );
  },
};
</script>

<style lang="scss" scoped>
.page-container {
  // 最小尺寸
  // min-width: 1024px;
  // min-height: 768px;

  // width: 100%;
  // height: 100vh;

  // display: flex;
  // flex-direction: column;
  padding: 16px;
}
</style>
