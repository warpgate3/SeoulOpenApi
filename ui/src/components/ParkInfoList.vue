<template>
    <div class="offset">
        <div v-if="loading" style="position: absolute; left: 50%; top:30%;">
<!--            <div style="position: relative; left: -50%; border: dotted red 1px;">-->
                <ring-loader :loading="loading"></ring-loader>
<!--            </div>-->
        </div>
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
            <tr v-if="parkLots.length === 0">
                <td colspan="7" style="text-align: center">공영 주차장 정보 결과가 없습니다.</td>
            </tr>
            </tbody>
        </table>
        <div class="text-center">
            <paginate v-model="pageNumber" :page-count="pageCount" :page-range="pageScale" :margin-pages="3"
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
                pageCount: 0,
                loading: false,
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
            fetchData(init) {
                this.loading = true;
                if (init === 'Y') { //재 검색일 경우 페이지 세트번호를 1로 초기화
                    this.pageNumber = 1;
                }
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
                    this.pageCount = Math.floor(res.totalCount/this.pageScale);
                    if (res.totalCount % this.pageScale > 0) {
                        this.pageCount = this.pageCount + 1;
                    }
                }).finally(() => {
                    this.loading = false;
                })
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
