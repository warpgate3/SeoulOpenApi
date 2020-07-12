<template>
    <div class="offset">
        <table class="table table-bordered">
            <thead class="thead-dark">
            <tr style="text-align: center">
                <th>주차장코드</th>
                <th>주차장명</th>
                <th>전화번호</th>
                <th>주소</th>
                <th>사용가능</th>
                <th>기본주차요금</th>
                <th>기본주차시간(분)</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(parkLot, index) in parkLots" :key="index">
                <td>{{parkLot.parkingCode}}</td>
                <td>{{parkLot.parkingName}}</td>
                <td>{{parkLot.tel}}</td>
                <td>{{parkLot.addr}}</td>
                <td style="text-align: center ">
                    <button v-if="parkLot.availableParkingYn == 'Y'" type="button" class="btn btn-success">가능</button>
                    <button v-else type="button" class="btn btn-danger">불가</button>
                </td>
                <td>{{parkLot.rates}}</td>
                <td>{{parkLot.timeRate}}</td>
            </tr>
            </tbody>
        </table>
        <div class="text-center">
            <paginate v-model="pageNumber" :page-count="pageCount" :page-range="pageScale" :margin-pages="1"
                      :click-handler="pageChangeHandler"
                      :prev-text="'Prev'" :next-text="'Next'" :container-class="'pagination'"
                      :page-class="'page-item'"></paginate>
        </div>
    </div>
</template>

<script>
    import {parkInfoService} from '../api';

    export default {
        data() {
            return {
                parkLots: [],
                pageNumber: 1,
                pageScale: 10,
                totalCount: 0,
                pageCount: 0
            }
        },
        components: {},
        created() {
            this.fetchData();
        },
        props: {
            searchWardName: String,
            searchParkingName: String,
            searchTelNumber: String,
            searchByCoord: Boolean,
            searchMyLat: Number,
            searchMyLng: Number
        },
        methods: {
            fetchData() {
                parkInfoService.fetch({
                    wardName: this.searchWardName,
                    parkingName: this.searchParkingName,
                    telNumber: this.searchTelNumber,
                    pageNumber: this.pageNumber,
                    pageScale: this.pageScale,
                    byCoord: this.searchByCoord,
                    myLat: this.searchMyLat,
                    myLng: this.searchMyLng
                }).then(res => {
                    this.parkLots = res.parkInfoResponseList;
                    this.totalCount = res.totalCount;
                    this.pageCount = res.totalCount / this.pageScale;
                });
            },
            pageChangeHandler(selectedPage) {
                this.pageNumber = selectedPage;
                this.fetchData();
            }

        }
    }
</script>
<style>
    .offset {
        width: 80% !important;
        margin: 20px auto;
    }
</style>
