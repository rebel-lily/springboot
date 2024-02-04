import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  // {
  //   path: '/',
  //   component: () => import(/* webpackChunkName: "about" */ '../views/Manage.vue'),
  //   redirect:"/home",
  //   children:[
  //     {path: 'home', name: '主页',component:() => import("../views/Home")},
  //     {path: 'user', name: '用户中心',component:() => import("../views/User")},
  //     {path: 'role', name: '权限管理',component:() => import('../views/Role')},
  //     {path: 'menu', name: '菜单管理',component:() => import('../views/Menu')},
  //     {path: 'person', name: '个人信息',component:() => import('../views/Person')},
  //     {path: 'file', name: '文件管理',component:() => import('../views/File')}
  //   ]
  // },
  {
    path: '/login',
    name:'login',
    component:() => import('../views/login')
  },
  {
    path: '/register',
    name:'register',
    component:() => import('../views/Register')
  },
  {
    path: '*',
    name:'NotFound',
    component:() => import('../views/404')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

//提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode:'history',
    base:process.env.BASE_URL,
    routes
  })
}

//注意:刷新页面会导致页面路由重置
 export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus")
  if (storeMenus){
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')){//当路由name不包含Manage时
      //拼装动态路由
      const manageRoute = {path: '/',name:'Manage',component: () => import(/* webpackChunkName: "about" */ '../views/Manage.vue'), redirect:"/home", children:[]}
      const menus = JSON.parse(storeMenus)
      menus.forEach(item =>{
        if (item.path){//当前仅当path不为空的时候才去设置路由
          let itemMenu = {path: item.path.replace("/",''),name:item.name,component: () => import(/* webpackChunkName: "about" */ '../views/' + item.pagePath + '.vue')}
          manageRoute.children.push(itemMenu)
        }else if (item.children.length){
          item.children.forEach(item =>{
            if (item.path){
              let itemMenu = {path: item.path.replace("/",''),name:item.name,component: () => import(/* webpackChunkName: "about" */ '../views/' + item.pagePath + '.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      manageRoute.children.push({path: 'person', name: '个人信息',component:() => import('../views/Person')})
      //动态添加到现在路由对象中去
      router.addRoute(manageRoute)
    }
  }
}

//当刷新时再调用一遍
setRoutes()

router.beforeEach((to,from,next) => {
  localStorage.setItem("currentPathName",to.name)//设置当前路由名称
  store.commit("setPath")
  //未到找路由的情况
  if (!to.matched.length){
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus){
      next("/404")
    }else {
      //跳转到登录页
      next("/login")
    }
  }
 next()

})




export default router
