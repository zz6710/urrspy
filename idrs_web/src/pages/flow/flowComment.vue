<template>
<!--  审批流程 评论-->
  <div>
    <!-- 评论输入框 START   -->
    <div v-clickoutside="hideReplyBtn" @click="inputFocus" class="my-reply">
      <!--   头像   -->
<!--      <el-avatar class="header-img" :size="40" :src="headImg"></el-avatar>-->
      <Avatar :username="username" :src="username" :background-color="extractColorByName(username)"
              color="skyblue" style="border: 1px solid skyblue;" :inline="true" />
      <!--   评论输入框   -->
      <div class="reply-info" >
        <div tabindex="0" contenteditable="true" id="replyInput" spellcheck="false" placeholder="输入评论内容..."
             class="reply-input" @focus="showReplyBtn" @input="onDivInput($event)" >
        </div>
      </div>
      <!--   发表评论按钮   -->
      <div class="reply-btn-box" v-show="btnShow">
        <el-button class="reply-btn" size="medium" @click="sendComment" type="success">发表评论</el-button>
      </div>
    </div>
    <!-- 评论输入框 END   -->

    <!-- 评论内容展示 START   -->
    <div v-for="(item,i) in comments" :key="i" class="author-title reply-father">
      <!--  头像   -->
<!--      <el-avatar class="header-img" :size="40" :src="item.headImg"></el-avatar>-->
<!--      <el-avatar class="header-img" :size="40" :src="headImg"></el-avatar>-->
      <Avatar :username="item.username" :src="item.username" :background-color="extractColorByName(item.username)"
              color="skyblue" style="border: 1px solid skyblue;" :inline="true" />
      <!--   人员信息START   -->
      <div class="author-info">
        <span class="author-name">{{item.username}}</span>
        <span class="author-time">{{item.createTime}}</span>
      </div>
      <!--   人员信息END   -->
      <div class="icon-btn">
           <span @click="showReplyInput(i,item.userId,item.username,item.id,item.processInstanceId,item.processName,1)">
               <i class="iconfont el-icon-s-comment">回复</i></span>

           <span @click="removeComment(item.id,1)" v-show="item.userId === userId">
              <i class="iconfont el-icon-delete">删除</i>
           </span>
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
<!--          <el-avatar class="header-img" :size="40" :src="headImg" :style="`background:${extractColorByName('管理员')}`"></el-avatar>-->
          <Avatar :username="reply.from" :src="reply.from" :background-color="extractColorByName(reply.from)"
                  color="skyblue" style="border: 1px solid skyblue;" :inline="true" />
          <!--  人员信息   -->
          <div class="author-info">
            <span class="author-name">{{reply.from}}</span>
            <span class="author-time">{{reply.createTime}}</span>
          </div>
          <!--  回复按钮 -->
          <div class="icon-btn">
            <span @click="showReplyInput(i,reply.userId,reply.from,reply.id,reply.processInstanceId,reply.processName,2)">
              <i class="iconfont el-icon-s-comment">回复</i></span>

            <span @click="removeComment(reply.id,2)" v-show="reply.userId === userId">
              <i class="iconfont el-icon-delete">删除</i>
            </span>
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
      <!-- 回复内容列表展示 End -->
      <div  v-show="_inputShow(i)" class="my-reply my-comment-reply">
<!--        <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>-->
        <Avatar :username="username" :src="username" :background-color="extractColorByName(username)"
                color="skyblue" style="border: 1px solid skyblue;" :inline="true" />
        <div class="reply-info" >
          <div tabindex="0" contenteditable="true" spellcheck="false" placeholder="输入评论内容..."   @input="onDivInput($event)"  class="reply-input reply-comment-input"></div>
        </div>
        <div class=" reply-btn-box">
          <el-button class="reply-btn" size="medium" @click="sendCommentReply(i,j)" type="success">发表评论</el-button>
        </div>
      </div>
    </div>
    <!-- 评论内容展示 END   -->
  </div>
</template>

<script>
import Tools from "@/utils/tools";

const clickoutside = {
  // 初始化指令
  bind(el, binding, vnode) {
    function documentHandler(e) {
      // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false;
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
        // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e);
      }
    }
    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.vueClickOutside = documentHandler;
    document.addEventListener('click', documentHandler);
  },
  update() {},
  unbind(el, binding) {
    // 解除事件监听
    document.removeEventListener('click', el.vueClickOutside);
    delete el.vueClickOutside;
  },
};
import Avatar from 'vue-avatar'
export default {
  name:'flowComment',
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
      userId:localStorage.getItem('userid'),
      btnShow: true,
      index:'0',
      username:localStorage.getItem('username'),
      myHeader:'',
      to:'',
      toId:-1,
      processInstanceId:'',
      processName:'',
      content:'',
      createTime:'',
      headImg:'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
      parentCommentId:'',
      parentCommentUserId:'',
      replyCommentId:'',
      replyCommentUserId:'',
      comments:[
        {
          reply:[],
        }
      ],
    }
  },
  directives: {clickoutside},
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
    //控制删除按钮
    isShow(id){
      console.log('一级id',id);
      console.log('当前用户id')
    },
    //删除评论数据
    removeComment(id,type){
      console.log('评论id',id)
      this.$confirm('此操作将删除该条评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.httpUtil
          .ajax({
            url: "wf/wf/comment/removeComment.json",
            params: {
              id:id,
              type:type
            }
          }).then(res => {
          console.log('res返回的数据',res);
          if (res.status==='200'){
            //删除后重新加载评论数据
            this.getCommentList();
            Tools.alert("删除评论成功");
          }else {
            Tools.alert("删除评论失败，请检查网络是否正常！","danger")
          }
        });
      }).catch(() => {
        Tools.alert("取消删除评论成功");
      });
    },
    inputFocus(){
      let replyInput = document.getElementById('replyInput');
      replyInput.style.padding= "8px 8px"
      replyInput.style.border ="2px solid blue"
      replyInput.focus()
    },
    showReplyBtn(){
      this.btnShow = true
    },
    hideReplyBtn(){
      this.btnShow = false
      let replyInput = document.getElementById('replyInput');
      replyInput.style.padding= "10px"
      replyInput.style.border ="none"
    },
    showReplyInput(i,userId,username,id,processInstanceId,processName,type){
      console.log('点击回复按钮',i,userId,username,processInstanceId,processName)
      this.comments[this.index].inputShow = false
      this.index =i
      this.comments[i].inputShow = true
      //回复谁
      this.to = username
      this.toId = userId
      if (type===1){
        //回复的父级评论id
        this.parentCommentId = id;
        this.parentCommentUserId = userId;
        this.replyCommentId = id;
        this.replyCommentUserId = userId;
      }else {
        this.parentCommentId = this.comments[i].id;
        this.parentCommentUserId = this.comments[i].userId;
        this.replyCommentId = id;
        this.replyCommentUserId = userId;
        console.log('二级回复评论，设置父级id为一级id',this.parentCommentId)
      }
      //this.parentCommentUserId = userId;
      this.processInstanceId = processInstanceId;
      this.processName = processName;

    },
    _inputShow(i){
      return this.comments[i].inputShow
    },
    //提交新评论
    sendComment(){
      if(!this.content){
        this.$message({
          showClose: true,
          type:'warning',
          message:'评论不能为空'
        })
      }else{
        let commentData ={}
        let input =  document.getElementById('replyInput')
        this.createTime = this.getCurrentTime()
        this.processInstanceId = this.commentParams[0];
        this.processName = this.commentParams[1];

        commentData.userId = this.userId
        commentData.username= this.username
        commentData.headImg = this.myHeader
        commentData.processInstanceId = this.processInstanceId;
        commentData.processName = this.processName;
        commentData.content = this.content;
        commentData.createTime = this.createTime;
        commentData.status = '1';
        commentData.level = 1;

        this.comments.push(commentData)
        //新添加的数据
        console.log('comments==>',commentData)
        //保存评论数据到数据库
        this.httpUtil
          .ajax({
            url: "wf/wf/comment/saveComment.json",
            params: commentData
          }).then(res => {
            console.log('res返回的数据',res);
            if (res.status=='200'){
              this.getCommentList();
              Tools.alert("评论成功");
            }else {
              Tools.alert("评论失败，请检查网络是否正常！","danger")
            }
        });
        this.content = ''
        input.innerHTML = '';
      }
    },
    //回复评论提交
    sendCommentReply(i,j){
      if(!this.content){
        this.$message({
          showClose: true,
          type:'warning',
          message:'评论不能为空'
        })
      }else{
        let replyCommentData ={}
        this.createTime = this.getCurrentTime()
        //回复者
        replyCommentData.from= this.username
        //回复谁
        replyCommentData.to = this.to

        replyCommentData.fromHeadImg = this.myHeader

        this.processInstanceId = this.commentParams[0];
        this.processName = this.commentParams[1];
        //流程数据---->
        replyCommentData.processInstanceId = this.processInstanceId;
        replyCommentData.processName = this.processName;
        replyCommentData.content = this.content;
        replyCommentData.createTime = this.createTime;
        replyCommentData.status = '1';
        replyCommentData.level = '2';
        //父级评论id 用户id
        replyCommentData.parentCommentId = this.parentCommentId;
        replyCommentData.parentCommentUserId = this.toId;
        //被回复的评论id 用户id
        replyCommentData.replyCommentId = this.replyCommentId;
        replyCommentData.replyCommentUserId = this.replyCommentUserId;
        //操作者id
        replyCommentData.userId = this.userId;
        this.comments[i].reply.push(replyCommentData)
        //保存回复评论到数据库
        this.httpUtil
          .ajax({
            url: "wf/wf/comment/saveComment.json",
            params: replyCommentData
          }).then(res => {
          console.log('res返回的数据',res);
          if (res.status==='200'){
            this.getCommentList()
            Tools.alert("回复成功");
          }else {
            Tools.alert("回复失败，请检查网络是否正常！","danger")
          }
        });
        this.content = '';
        document.getElementsByClassName("reply-comment-input")[i].innerHTML = ""
      }
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
    //监听获取输入的值
    onDivInput: function(e) {
      //获取输入的值
      this.content = e.target.innerText;
    },

    //获取当前日期时间
    getCurrentTime() {
      var date = new Date();//当前时间
      var month = this.zeroFill(date.getMonth() + 1);//月
      var day = this.zeroFill(date.getDate());//日
      var hour = this.zeroFill(date.getHours());//时
      var minute = this.zeroFill(date.getMinutes());//分
      var second = this.zeroFill(date.getSeconds());//秒
      //当前时间
      var curTime = date.getFullYear() + "-" + month + "-" + day
        + " " + hour + ":" + minute + ":" + second;
      return curTime;
    },
    zeroFill(i){
      if (i >= 0 && i <= 9) {
        return "0" + i;
      } else {
        return i;
      }
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
