<template>
  <div :class="{'hidden':hidden === true || hidden === 'true'}" class="pagination-container">
    <k-pagination
      :background="background === true || background === 'true'"
      :current-page.sync="currentPage"
      :page-size.sync="pageSize"
      :layout="layout"
      :page-sizes="pageSizes"
      :total="total"
      v-bind="$attrs"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    ></k-pagination>
  </div>
</template>

<script>
import { scrollTo } from './scroll-to.js'
import KPagination from '@/components/k-element/k-grid/k-pagination/pagination.js';

export default {
  name: 'KGridPagination',
  components:{
    KPagination
  },
  props: {
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    limit: {
      type: Number,
      default: 20
    },
    pageSizes: {
      type: Array,
      default() {
        return [5,10, 20, 30, 50,100,200,500,1000]
      }
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    background: {
      type: [Boolean, String],
      default: true
    },
    autoScroll: {
      type: [Boolean, String],
      default: true
    },
    hidden: {
      type: [Boolean, String],
      default: false
    }
  },
  computed: {
    currentPage: {
      get() {
        return this.page
      },
      set(val) {
        this.$emit('update:page', val)
      }
    },
    pageSize: {
      get() {
        return this.limit
      },
      set(val) {
        this.$emit('update:limit', val)
      }
    }
  },
  methods: {
    handleSizeChange(val) {
      this.$emit('pagination', { page: this.currentPage, limit: val })
      if (this.autoScroll === true || this.autoScroll === 'true') {
        scrollTo(0, 800)
      }
    },
    handleCurrentChange(val) {
      this.$emit('pagination', { page: val, limit: this.pageSize })
      if (this.autoScroll === true || this.autoScroll === 'true') {
        scrollTo(0, 800)
      }
    }
  }
}
</script>

<style scoped>
.pagination-container {
  background: #fff;
  padding: 10px 16px;
  text-align: right;
}
.pagination-container.hidden {
  display: none;
}
</style>
