<template>
  <div>
    <el-form-item label="宽度">
      <el-input clearable v-model="element.style.width"></el-input>
    </el-form-item>
    <el-form-item label="高度">
      <el-input clearable v-model="element.style.height"></el-input>
    </el-form-item>
    <el-form-item label="布局方式">
      <el-select clearable v-model="element.style.display" placeholder="请选择">
        <el-option label="inline" value="inline"></el-option>
        <el-option label="flex" value="flex"></el-option>
        <el-option label="block" value="block"></el-option>
        <el-option label="inline-block" value="inline-block"></el-option>
      </el-select>
    </el-form-item>
    <template v-if="element.style.display == 'flex'">
      <el-form-item label="主轴方向">
        <el-select clearable v-model="element.style['flex-direction']" placeholder="请选择">
          <el-option label="row" value="row"></el-option>
          <el-option label="row-reverse" value="row-reverse"></el-option>
          <el-option label="column" value="column"></el-option>
          <el-option label="column-reverse" value="column-reverse"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="主轴对齐方式">
        <el-select clearable v-model="element.style['justify-content']" placeholder="请选择">
          <el-option label="左对齐(flex-start)" value="flex-start"></el-option>
          <el-option label="右对齐(flex-end)" value="flex-end"></el-option>
          <el-option label="居中(center)" value="center"></el-option>
          <el-option label="两端对齐(space-between)" value="space-between"></el-option>
          <el-option label="横向平分(space-around)" value="space-around"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="辅轴对齐">
        <el-select clearable v-model="element.style['align-items']" placeholder="请选择">
          <el-option label="起点对齐(flex-start)" value="flex-start"></el-option>
          <el-option label="终点对齐(flex-end)" value="flex-end"></el-option>
          <el-option label="居中(center)" value="center"></el-option>
          <el-option label="第一行文字的基线对齐(baseline)" value="baseline"></el-option>
          <el-option label="如果项目未设置高度或设为auto，将占满整个容器的高度stretch" value="stretch"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="换行">
        <el-select clearable v-model="element.style['flex-wrap']" placeholder="请选择">
          <el-option label="不换行(nowrap)" value="nowrap"></el-option>
          <el-option label="换行(wrap)" value="wrap"></el-option>
          <el-option label="换行，第一行在下方(wrap-reverse)" value="wrap-reverse"></el-option>
        </el-select>
      </el-form-item>
    </template>
    <el-form-item label="padding(px)">
      <div style="display:flex;flex-direction:column;justify-content:center;align-items:center;">
        <el-input-number placeholder="top" clearable size="mini" v-model="element.style['padding-top']"></el-input-number>
        <div style="display:flex;flex-direction:row;">
          <el-input-number placeholder="left" clearable size="mini" v-model="element.style['padding-left']"></el-input-number>
          <el-input-number placeholder="right" clearable size="mini" v-model="element.style['padding-right']"></el-input-number>
        </div>
        <el-input-number placeholder="bottom" clearable size="mini" v-model="element.style['padding-bottom']"></el-input-number>
      </div>
    </el-form-item>
    <el-form-item label="margin(px)">
      <div style="display:flex;flex-direction:column;justify-content:center;align-items:center;">
        <el-input-number placeholder="top" clearable size="mini" v-model="element.style['margin-top']"></el-input-number>
        <div style="display:flex;flex-direction:row;">
          <el-input-number placeholder="left" clearable size="mini" v-model="element.style['margin-left']"></el-input-number>
          <el-input-number placeholder="right" clearable size="mini" v-model="element.style['margin-right']"></el-input-number>
        </div>
        <el-input-number placeholder="bottom" clearable size="mini" v-model="element.style['margin-bottom']"></el-input-number>
      </div>
    </el-form-item>
    <el-form-item label="定位类型">
      <el-select clearable v-model="element.style.position" placeholder="请选择">
        <el-option label="relative" value="relative"></el-option>
        <el-option label="absolute" value="absolute"></el-option>
        <el-option label="fixed" value="fixed"></el-option>
        <el-option label="sticky" value="sticky"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="定位尺寸(px)" v-if="element.style.position">
      <div style="display:flex;flex-direction:column;justify-content:center;align-items:center;">
        <el-input-number placeholder="top" clearable size="mini" v-model="element.style['top']"></el-input-number>
        <div style="display:flex;flex-direction:row;">
          <el-input-number placeholder="left" clearable size="mini" v-model="element.style['left']"></el-input-number>
          <el-input-number placeholder="right" clearable size="mini" v-model="element.style['right']"></el-input-number>
        </div>
        <el-input-number placeholder="bottom" clearable size="mini" v-model="element.style['bottom']"></el-input-number>
      </div>
    </el-form-item>
    <el-form-item label="层叠顺序">
      <el-input-number clearable v-model="element.style['z-index']"></el-input-number>
    </el-form-item>
    <el-form-item label="背景色">
      <el-color-picker clearable v-model="element.style['background']"></el-color-picker>
    </el-form-item>
    <el-form-item label="背景图">
      <el-input clearable v-model="element.style['background-image']"></el-input>
    </el-form-item>
    <el-form-item label="背景图重复">
      <el-select clearable v-model="element.style['background-repeat']" placeholder="请选择">
        <el-option label="垂直方向和水平方向重复(repeat)" value="repeat"></el-option>
        <el-option label="水平方向重复(repeat-x)" value="repeat-x"></el-option>
        <el-option label="垂直方向重复(repeat-y)" value="repeat-y"></el-option>
        <el-option label="不重复(no-repeat)" value="no-repeat"></el-option>
        <el-option label="从父元素继承(inherit)" value="inherit"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="背景图定位">
      <el-autocomplete
        class="inline-input"
        v-model="element.style['background-position']"
        :fetch-suggestions="querySearch"
        placeholder="background-position"
      >
        <template slot-scope="{ item }">
          <div class="name">{{ item.value }}</div>
        </template>
      </el-autocomplete>
    </el-form-item>
    <el-form-item label="圆角">
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend" style="">
          <md-icon md-src="/static/svg/design/radius.svg" style="width:16px;height:16px;font-size:16px !important;"></md-icon>
        </div>
        <el-input-number placeholder="全局" clearable size="mini" v-model="element.style['border-radius']" controls-position="right">
        </el-input-number>
      </div>
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend" style="">
          <md-icon md-src="/static/svg/design/radiusTopLeft.svg" style="width:20px;height:20px;font-size:20px !important;"></md-icon>
        </div>
        <el-input-number placeholder="左上" clearable size="mini" v-model="element.style['border-top-left-radius']" controls-position="right">
        </el-input-number>
      </div>
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend" style="">
          <md-icon md-src="/static/svg/design/radiusTopRight.svg" style="width:20px;height:20px;font-size:20px !important;"></md-icon>
        </div>
        <el-input-number placeholder="右上" clearable size="mini" v-model="element.style['border-top-right-radius']" controls-position="right">
        </el-input-number>
      </div>
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend" style="">
          <md-icon md-src="/static/svg/design/radiusBottomLeft.svg" style="width:20px;height:20px;font-size:20px !important;"></md-icon>
        </div>
        <el-input-number placeholder="左下" clearable size="mini" v-model="element.style['border-bottom-left-radius']" controls-position="right">
        </el-input-number>
      </div>
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend" style="">
          <md-icon md-src="/static/svg/design/radiusBottomRight.svg" style="width:20px;height:20px;font-size:20px !important;"></md-icon>
        </div>
        <el-input-number placeholder="右下" clearable size="mini" v-model="element.style['border-bottom-right-radius']" controls-position="right">
        </el-input-number>
      </div>
    </el-form-item>
    <el-form-item label="边框">
      <div style="display:flex;flex-direction:column;justify-content:center;align-items:center;">
        <md-icon md-src="/static/svg/design/border_top.svg" @click.native="selectBorder('-top')"></md-icon>
        <div style="display:flex;flex-direction:row;justify-content:center;align-items:center;">
          <md-icon md-src="/static/svg/design/border_left.svg" @click.native="()=>{activeBorder = '-left'}"></md-icon>
          <md-icon md-src="/static/svg/design/border.svg" @click.native="()=>{activeBorder = ''}"></md-icon>
          <md-icon md-src="/static/svg/design/border_right.svg" @click.native="()=>{activeBorder = '-right'}"></md-icon>
        </div>
          <md-icon md-src="/static/svg/design/border_bottom.svg" @click.native="()=>{activeBorder = '-bottom'}"></md-icon>
      </div>
      <div style="display:flex;flex-direction:row;justify-content:center;align-items:center;">
        <template>
          <el-input-number placeholder="border-width" clearable size="mini" v-model="element.style['border' + activeBorder + '-width']" controls-position="right"></el-input-number>
          <el-color-picker clearable v-model="element.style['border' + activeBorder + '-color']"></el-color-picker>
          <el-select clearable v-model="element.style['border' + activeBorder + '-style']" placeholder="请选择">
            <el-option label="solid" value="solid"></el-option>
            <el-option label="dashed" value="dashed"></el-option>
            <el-option label="dotted" value="dotted"></el-option>
          </el-select>
        </template>
      </div>
    </el-form-item>
    <el-form-item label="文字">
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend">字号</div>
        <el-input-number placeholder="font-size" clearable size="mini" v-model="element.style['font-size']" controls-position="right">
        </el-input-number>
      </div>
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend">行高</div>
        <el-input-number placeholder="line-height" clearable size="mini" v-model="element.style['line-height']" controls-position="right">
        </el-input-number>
      </div>
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend">字宽</div>
        <el-input-number placeholder="font-widget" clearable size="mini" :step="100" v-model="element.style['font-widget']" controls-position="right">
        </el-input-number>
      </div>
      <el-input placeholder="font-size" clearable size="mini" v-model="element.style['color']">
        <template slot="prepend">颜色</template>
        <el-color-picker slot="append" clearable size="mini" v-model="element.style['color']"></el-color-picker>
      </el-input>
      <div class="el-input el-input--mini el-input-group el-input-group--prepend">
        <div class="el-input-group__prepend">对齐</div>
        <el-select clearable v-model="element.style['text-align']" placeholder="请选择">
          <el-option label="左对齐 left" value="left"></el-option>
          <el-option label="居中 center" value="center"></el-option>
          <el-option label="右对齐 right" value="right"></el-option>
          <el-option label="两端对齐 right" value="justify"></el-option>
        </el-select>
      </div>
    </el-form-item>
  </div>
</template>

<script>

export default {
  name: "StyleSetting",
  props: {
    element: {
      type: Object,
      default: {}
    }
  },
  inject: ["kFormDesign"],
  data () {
    return {
      activeBorder: '',
      backgroundPositionSuggest: [
        {value:"top left"},
        {value:"top center"},
        {value:"top right"},
        {value:"center left"},
        {value:"center center"},
        {value:"center right"},
        {value:"bottom left"},
        {value:"bottom center"},
        {value:"bottom right"},
        {value:"x% y%"}
      ]
    }
  },
  created (){
    
  },
  mounted () {

  },
  methods: {
    querySearch(queryString, cb) {
      var results = queryString ? this.backgroundPositionSuggest.filter(this.createFilter(queryString)) : this.backgroundPositionSuggest;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (row) => {
        return (row.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    selectBorder: function(border){
      console.log(" border ");
      this.activeBorder = border;
    }
  },
  watch: {
    
  }
}
</script>

<style lang="scss" scoped>   //必须要加上“<style lang="scss" scoped>，否则引入失败（提示保存）

</style>
