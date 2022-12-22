// 引用必要套件
import http from 'k6/http';
import { check, group, sleep, fail } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
// https://github.com/benc-uk/k6-reporter

const BASE_URL = 'http://app:8080';

// 設定要實行多大規模的 Load testing
export const options = {
  //
  duration: '10s', // 測試總共要跑多久時間
  vus: 10, // 同時要模擬的人數

  // 更進階的測試成功與失敗條件,這比較複雜,請參閱官網文件。
  thresholds: {
    // 這裡直接引用官方教學的範例。
    // 95 percent of response times must be below 500ms
    http_req_duration: ['p(95)<500'],
  },
};

function randomString(length) {
  const charset = 'abcdefghijklmnopqrstuvwxyz';
  let res = '';
  while (length--) res += charset[(Math.random() * charset.length) | 0];
  return res;
}

function randomNumber(length) {
  const charset = '1234567890';
  let res = '';
  while (length--) res += charset[(Math.random() * charset.length) | 0];
  return res;
}

const headersJson = {
  headers: {
    'Content-Type': 'application/json',
  },
};

export default function () {

  let requestBody = {
    id: randomNumber(5),
    title: randomString(10),
    description: randomString(30)
  };

  const resultCreate = http.post(`${BASE_URL}/v1/books`,
    JSON.stringify(requestBody), headersJson);

  check(resultCreate, {
    'created book': (r) => r.status === 201
  });

  // 直接就對目標 url 發動測試
  const resultGetBooks = http.get(`${BASE_URL}/v1/books`);

  check(resultGetBooks, {
    'http code = 200': (r) => r.status === 200
  });

}



export function handleSummary(data) {
  return {
    "/report/summary.html": htmlReport(data),
  };
}

/*
https://k6.io/docs/examples/advanced-api-flow/
https://k6.io/docs/using-k6/http-requests/
https://betterprogramming.pub/an-introduction-to-k6-an-api-load-testing-tool-132a0d87827d
https://k6.io/blog/integrating-load-testing-with-gitlab/
https://dev.to/k6/intro-to-testing-asp-net-apis-with-k6-when-unit-tests-meet-load-testing-5b5h
https://github.com/grafana/k6-example-azure-pipelines/blob/master/azure-pipelines.docker.yml
https://medium.com/microsoftazure/load-testing-with-azure-devops-and-k6-839be039b68a
https://editor.leonh.space/2022/k6/


*/