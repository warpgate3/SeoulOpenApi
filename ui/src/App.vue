<template>
    <div id="app">
        <section class="search-sec">
            <h1 class="muted p-2 align-content-center" style="text-align: center">서울시 공영주차장 안내 정보 서비스</h1>
            <div class="container">
                <div class="row">
                    <div>
                        <div class="row">
                            <div class="col-xs-2 col-lg-2 col-md-2 col-sm-2 p-2">
                                <input type="text" class="form-control search-slt" placeholder="동&구"
                                       v-model="wardName" @keyup.enter="doSearch()">
                            </div>
                            <div class="col-xs-2 col-lg-2 col-md-2 col-sm-2 p-2">
                                <input type="text" class="form-control search-slt" placeholder="주차장 시설명"
                                       v-model="parkingName" @keyup.enter="doSearch()">
                            </div>
                            <div class="col-xs-2 col-lg-2 col-md-2 col-sm-2 p-2">
                                <input type="text" class="form-control search-slt w-25" placeholder="주차장 전화번호"
                                       v-model="telNumber" @keyup.enter="doSearch()">
                            </div>
                            <div class="col-xs-2 col-lg-2 col-md-2 col-sm-2 p-2">
                                <button type="button" class="btn btn-info wrn-btn" @click="doSearch()">검색</button>&nbsp;
                                <input type="checkbox" class="custom-control-input" id="myCoordChk" v-model="byCoord"
                                       @change="getMyCoordinates($event)">&nbsp;
                                <label class="custom-control-label" for="myCoordChk">내 위치 기준검색 </label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <ParkInfoList ref="list" :searchWardName="wardName"
                      :searchParkingName="parkingName"
                      :searchByCoord="byCoord"
                      :searchMyLat="myLat"
                      :searchMyLng="myLng"
                      :searchTelNumber="telNumber"/>
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
                telNumber: "",
                myLat: 0.0,
                myLng: 0.0,
                byCoord: false
            }
        },
        created() {
        },
        methods: {
            doSearch() {
                console.log("do search!!");
                this.$refs.list.fetchData();
            },
            getMyCoordinates() {
                if (this.byCoord) {
                    navigator.geolocation.getCurrentPosition(location => {
                        this.myLat = location.coords.latitude;
                        this.myLng = location.coords.longitude;
                        console.log(location.coords);
                        this.doSearch();
                    }, err => {
                        alert(err.message);
                    })
                }
            }

        }
    }
</script>

