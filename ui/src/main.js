import Vue from 'vue'
import App from './App'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import Paginate from 'vuejs-paginate'

Vue.component('paginate', Paginate)
Vue.use(BootstrapVue);
new Vue({
    render: h => h(App),
}).$mount('#app')
