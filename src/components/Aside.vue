<template>
  <el-menu :default-openeds="opens" style="min-height: 100%; overflow-x: hidden"
           :collapse="isCollapse"  background-color="#0F2027" text-color="#fff" active-text-color="#ffd04b"
           collapse-transition="false"
           router
           @select="handleSelect"
  >
    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/logo.png" alt="" :style="{width: 20+'px',position: 'relative',top: 5 +'px',marginRight: MRight}">
      <b style="color: white" v-show="!isCollapse">后台管理系统</b>
    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <template slot="title">
            <i :class="item.icon"></i>
            <span>{{ item.name }}</span>
          </template>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span>{{ item.name }}</span>
          </template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <template slot="title">
                <i :class="subItem.icon"></i>
                <span>{{ subItem.name }}</span>
              </template>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>
  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props:{
    isCollapse:Boolean,
    logoTextShow:Boolean
  },
  data(){
    return{
      menus:localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : {},
      opens:localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : {}
    }
  },
  methods:{
    handleSelect(index){
      console.log(this.$router.options.routes);
    }
  }
}
</script>

<style scoped>

</style>