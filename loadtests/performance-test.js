// 引用必要套件
import { check, sleep } from 'k6';
import http from 'k6/http';

export default function () {

  // 可以定義變數供後續使用
  const url = 'http://app:9000/actuator/health/readiness';

  sleep(30 * 1000);
  
  // 直接就對目標 url 發動測試
  const result = http.get(url);

  // 同時要驗證 Response 是否吻合期待,以及是否含有特定的文字
  check(result, {
    'http code = 200': (r) => r.status === 200
  });

}

// 設定要實行多大規模的 Load testing
export const options = {
  //
  duration: '1m', // 測試總共要跑多久時間
  vus: 10, // 同時要模擬的人數
  
  // 更進階的測試成功與失敗條件,這比較複雜,請參閱官網文件。
  thresholds: {
    // 這裡直接引用官方教學的範例。
    // 95 percent of response times must be below 500ms
    http_req_duration: ['p(95)<500'], 
  },
};