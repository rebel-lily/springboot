<template>
    <el-container style="min-height:100vh; ">
      <el-aside :width="sizeWidth +'px'" style="background-color: rgb(238, 241, 246);">
          <Aside :isCollapse="isCollapse" :logoTextShow="logoTextShow"></Aside>
      </el-aside>

      <el-container>
        <el-header style="font-size: 12px;align-items: center" >
           <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :pathName="pathName" :user="user"></Header>
        </el-header>

        <el-main>
          <!--表示当前页面的子路由会在router-view里面展示-->
          <router-view  @refreshUser="getUser"/>
        </el-main>
      </el-container>
    </el-container>

</template>

<style>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>

<script>
import Aside from "@/components/Aside";
import Header from "@/components/Header";
// @ is an alias to /src

export default {
  name: 'HomeView',
  components: {
    Header,
    Aside
  },
  data(){
    return {
      collapseBtnClass:'el-icon-s-fold',
      isCollapse:false,
      sizeWidth:200,
      logoTextShow:true,
      MRight:'5px',
      pathName:'',
      user:localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  comments: {
    Aside,
    Header
  },
  created() {
    //从后台获取最新的数据
    this.getUser()
  },
  methods:{
    collapse(){//点击收缩菜单
      this.isCollapse = !this.isCollapse
      if (this.isCollapse){
        this.sizeWidth=50
        this.collapseBtnClass='el-icon-s-unfold'
        this.MRight='0'
      }else {
        this.sizeWidth=200
        this.collapseBtnClass='el-icon-s-fold'
        this.MRight='5px'
      }
    },
    getUser(){
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
     //从后台获取数据
     this.request.get("/user/username/" + username).then(res =>{
        this.user = res.data
     })
    }
  }
}
</script>
