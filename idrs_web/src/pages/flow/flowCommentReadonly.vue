<template>
  <div>
    <h4>评论信息</h4>
    <!-- 评论内容展示 START   -->
    <div v-for="(item,i) in comments" :key="i" class="author-title reply-father">
      <!--  头像   -->
      <Avatar :username="item.username" :src="item.username" :background-color="extractColorByName(item.username)"
              color="skyblue" style="border: 1px solid skyblue;" :inline="true" />
      <!--   人员信息START   -->
      <div class="author-info">
        <span class="author-name">{{item.username}}</span>
        <span class="author-time">{{item.createTime}}</span>
      </div>
      <!-- 一级评论内容 Start  -->
      <div class="talk-box">
        <p>
          <span class="reply">{{item.content}}</span>
        </p>
      </div>
      <!-- 一级评论内容 End -->
      <!-- 回复内容列表展示 Start -->
      <div class="reply-box">
        <div v-for="(reply,j) in item.reply" :key="j" class="author-title">
          <!--  头像 -->
          <Avatar :username="reply.from" :src="reply.from" :background-color="extractColorByName(reply.from)"
                  color="skyblue" style="border: 1px solid skyblue;" :inline="true" />
          <!--  人员信息   -->
          <div class="author-info">
            <span class="author-name">{{reply.from}}</span>
            <span class="author-time">{{reply.createTime}}</span>
          </div>
          <div class="talk-box">
            <p>
              <span>回复 {{reply.to}}:</span>
              <span class="reply">{{reply.content}}</span>
            </p>
          </div>
          <div class="reply-box">

          </div>
        </div>
      </div>
    </div>
    <!-- 评论内容展示 END   -->
  </div>
</template>

<script>

import Avatar from 'vue-avatar'
export default {
  name: "flow-comment-readonly",
  props:{
    commentParams:{
      type:Array,
      required:true
    }
  },
  components:{
    Avatar
  },
  data(){
    return{
      index:'0',
      to:'',
      processInstanceId:'',
      content:'',
      createTime:'',
      comments:[
        {
          reply:[],
        }
      ],
    }
  },
  created() {
    this.getCommentList();
  },
  methods: {
    extractColorByName(username){
      let temp = [];
      temp.push("#");
      for (let index = 0; index < name.length; index++){
        temp.push(parseInt(name[index].charCodeAt(0),10).toString(16));
      }
      return temp.slice(0,5).join('').slice(0,4);
    },
    //获取评论数据
    getCommentList(){
      let processInstanceId = this.commentParams[0];
      console.log('this.processInstanceId',processInstanceId)
      this.httpUtil
        .ajax({
          url: "wf/wf/comment/getComment.json",
          params: {
            processInstanceId: processInstanceId
          }
        })
        .then(res => {
          this.comments = res.rows;
          console.log('评论页面：评论数据',this.comments)
        });
    },
  },
  watch:{
    commentParams:function (value) {
      console.log("commentParams:watch===",value);
    }
  }
}
</script>

<style lang="scss" scoped>
.my-reply {
  padding: 10px;
  background-color: #fafbfc;
  .header-img {
    display: inline-block;
    vertical-align: top;
  }

  .reply-info {
    display: inline-block;
    margin-left: 5px;
    width: 90%;
    @media screen and (max-width: 1200px) {
      width: 80%;
    }
    .reply-input {
      min-height: 80px;
      line-height: 22px;
      padding: 10px 10px;
      //color: #ccc;
      color: black;
      background-color: #fff;
      border-radius: 5px;
      &:empty:before {
        content: attr(placeholder);
      }
      &:focus:before {
        content: none;
      }
      &:focus {
        padding: 8px 8px;
        border: 2px solid blue;
        box-shadow: none;
        outline: none;
      }
    }
  }
  .reply-btn-box {
    height: 25px;
    margin: 10px 0;
    .reply-btn {
      position: relative;
      float: right;
      margin-right: 15px;
    }
  }
}
.my-comment-reply {
  margin-left: 50px;
  .reply-input {
    width: flex;
  }
}

.author-title:not(:last-child) {
  border-bottom: 1px solid rgba(178, 186, 194, 0.3);
}

.author-title {
  padding: 10px;
  .header-img {
    display: inline-block;
    vertical-align: top;
  }
  .author-info {
    display: inline-block;
    margin-left: 5px;
    width: 60%;
    height: 40px;
    line-height: 20px;
    > span {
      display: block;
      cursor: pointer;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
    .author-name {
      color: #000;
      font-size: 14px;
      font-weight: bold;
    }

    .author-time {
      font-size: 14px;
    }
  }
  .icon-btn {
    width: 30%;
    padding: 0 !important ;
    float: right;
    @media screen and (max-width: 1200px) {
      width: 20%;
      padding: 7px;
    }
    > span {
      cursor: pointer;
    }
    .iconfont {
      margin: 0 5px;
    }
  }
  .talk-box {
    margin: 0 50px;
    > p {
      margin: 0;
    }
    .reply {
      font-size: 14px;
      color: #000;
    }
  }
  .reply-box {
    margin: 10px 0 0 50px;
    background-color: #efefef;
  }
}
</style>
