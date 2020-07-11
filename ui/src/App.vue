<template>
    <div id="app">
        <section class="search-sec">
            <h1 class="muted p-2 align-content-center" style="text-align: center">서울시 공영주차장 안내 정보 서비스</h1>
            <div class="container">
                <form action="#" method="post" novalidate="novalidate">
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="row">
                                <div class="col-lg-3 col-md-3 col-sm-12 p-2">
                                    <input type="text" class="form-control search-slt" placeholder="동&구"
                                           v-model="wardName" @keyup.enter="doSearch()">
                                </div>
                                <div class="col-lg-3 col-md-3 col-sm-12 p-2">
                                    <input type="text" class="form-control search-slt" placeholder="주차장 시설명"
                                           v-model="parkingName" @keyup.enter="doSearch()">
                                </div>
                                <div class="col-lg-3 col-md-3 col-sm-12 p-2">
                                    <input type="text" class="form-control search-slt" placeholder="주차장 전화번호"
                                           v-model="telNumber" @keyup.enter="doSearch()">
                                </div>
                                <div class="col-lg-3 col-md-3 col-sm-12 p-2">
                                    <button type="button" class="btn btn-info wrn-btn" @click="doSearch()">검색</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </section>
        <ParkInfoList ref="list" :searchWardName="wardName"
                      :searchParkingName="parkingName"
                      :searchTelNumber="telNumber" />
    </div>
</template>

<script>
    import ParkInfoList from './components/ParkInfoList.vue';

    export default {
        name: 'App',
        components: {
            ParkInfoList
        },
        data() {
            return {
                wardName: "",
                parkingName: "",
                telNumber: ""
            }
        },
        methods: {
            doSearch() {
                console.log("do search!!");
                this.$refs.list.fetchData();
            }
        }
    }
</script>

