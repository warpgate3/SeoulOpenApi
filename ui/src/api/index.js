import axios from 'axios'

const DOMAIN = 'http://localhost:8080';

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
        return request('get',`/api/getParkInfo?telNumber=${data.telNumber}&parkingName=${data.parkingName}&wardName=${data.wardName}&pageNumber=${data.pageNumber}&pageScale=${data.pageScale}`);
    }
}