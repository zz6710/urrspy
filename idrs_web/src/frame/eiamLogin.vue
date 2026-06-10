<template>

</template>

<script>
import auth from "@/utils/auth.js"
    export default {
        name: "eiamLogin",
        data(){
          return{
            tableData:[]
          }
        },
        methods:{
          setTab(){
            sessionStorage.removeItem("kk-tab")
            sessionStorage.removeItem("kk-tab2")
            this.$store.commit("system/setTab", [
              {
                name: "首页",
                path: "/main/desktop",
                query: {},
                active: true
              },]);
            this.$store.commit("system/setTab2", []);
          },
        },
        created() {
           localStorage.setItem("systemName", "产品管理系统");
           document.title = "产品管理系统"
           console.log(document.title);
          var ticket ="";//iam登录票据
                var location = window.location.href;
                var index=location.indexOf("iamcaspticket=")
                if (index != -1) {
                    ticket=location.substring(index+14);

                }
          console.log(ticket);
          this.httpUtil.query({
            url: "jwt/login.json",
            params: {
              ticket: ticket
            }
          })
          .then(data => {
            if(data.success){
              auth.setToken(data.returndata.token);
              localStorage.setItem("token", data.returndata.token);
              localStorage.setItem("username", data.returndata.username);
              localStorage.setItem("userid", data.returndata.userid);
              localStorage.setItem("servers", data.returndata.servers);
              this.$store.commit("system/setGridMaxHeight", data.returndata.gridMaxHeight);
              this.global.getProdUser(data.returndata.userid);
             // this.global.getRoleAndProd(data.returndata.userid);
              this.global.getRoleAndProd(data.returndata.userid);
              this.setTab()
              this.$router.push({
                path: '/main'
              });
            }
          });

        }
    }
</script>

<style scoped>

</style>
