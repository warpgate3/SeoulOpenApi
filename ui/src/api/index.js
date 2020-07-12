import axios from 'axios'

// const DOMAIN = 'http://192.168.35.39:8080'; //dev TODO: deploy profiling
const DOMAIN = ''; //deploy

const request = (method, url, data) => {
    return axios({
        method,
        url: DOMAIN + url,
        data
    }).then(result => result.data).catch(result => {
        //TODO: error 처리
        alert(result.message);
        console.log(result);
    });
}

export const parkInfoService = {
    fetch(data) {
        console.log("request data=>", data);
        return request('get',`/api/getParkInfo?telNumber=${data.telNumber}&parkingName=${data.parkingName}&wardName=${data.wardName}&pageNumber=${data.pageNumber}&pageScale=${data.pageScale}&byCoord=${data.byCoord}&myLat=${data.myLat}&myLng=${data.myLng}`);
    }
}