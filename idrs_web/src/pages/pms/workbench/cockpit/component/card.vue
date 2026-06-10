<template>
  <div class="card">
    <div class="card-title">
      <span>{{data.title}}</span>
    </div>
    <div class="card-container"
      :class="(data.status=='equal' || !data.status)
        ? 'equal-color'
        : data.status == 'up'
        ? 'up-color'
        : 'down-color'"
    >
      <span class="main-value" :class="!isSpecial(data) ? 'special' : ''">
        {{data.value}}{{data.unit}}
        <span class="total-amount" v-if="!isSpecial(data)">
          <b class="line">|</b>
          {{data.totalAmount}}{{data.unitMoney}}
        </span>
      </span>
    </div>
    <div class="card-tip" v-if="isSpecial(data)">
      <span class="card-tip-label">
        {{data.tip}}{{
          (data.status=='equal' || !data.status)
          ? '不变'
          :  data.status=='up'
          ? '增长'
          : '下跌'
        }}
      </span>
      <span class="card-tip-status"
        :class="(data.status=='equal' || !data.status)
          ?  'equal-color'
          : data.status == 'up'
          ? 'up-color'
          : 'down-color'"
        >
          {{data.percent}}<em v-if="['up', 'dowm'].includes(data.status)">%</em>
        </span>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    data: {
      type: Object,
      default: ()=>{
        return {
          title: '理财规模',
          unit: '亿元',
          value: "20,015",
          status: 'up',
          percent: '8',
          tip: '同比去年底'
        }
      }
    }
  },
  methods: {
    isSpecial(data) {
      return data.totalAmount == null || data.totalAmount == undefined || data.totalAmount == ''
    }
  }
}
</script>
<style lang="scss" scoped>
.card {
  padding: 15px;
  border-radius: 10px;
  background: #1a3b6a;
  .card-title {
    color: #fff;
    font-weight: bold;
    padding: 0 0 0 4px;
    font-size: 14px;
    line-height: 14px;
    position: relative;
    padding-left: 10px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 4px;
      bottom: 0;
      background: #60c9e5;
      border-radius: 2px;
    }
    img {
      width: 14px;
      height: 14px;
    }
  }
  .card-container {
    display: flex;
    justify-content: center;
    font-size: 30px;
    font-weight: bold;
    margin: 30px 0 15px;
    white-space: nowrap;
  }
  .card-tip {
    display: flex;
    justify-content: center;
    color: #fff;
    font-size: 12px;
    font-weight: bold;
    .card-tip-status {
      margin-left: 4px;
    }
  }
  .equal-color {
    color: #fff;
    cursor: pointer;
    transition: transform 0.3s;
    &:hover {
      transform: scale(1.2);
    }
  }
  .up-color {
    color: #ec6c3e;
    cursor: pointer;
    transition: transform 0.3s;
    &:hover {
      transform: scale(1.2);
    }
  }
  .down-color {
    cursor: pointer;
    color: #9bfa58;
    transition: transform 0.3s;
    &:hover {
      transform: scale(1.2);
    }
  }
  .main-value {
    line-height: 30px;
    display: flex;
    align-items: center;
    .line {
      margin: 0 5px;
    }
  }
  .total-amount {
    display: flex;
    align-items: center;
  }
  .special {
    font-size: 20px;
  }
}
</style>
