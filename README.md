# springboot-cloud-run-lab-2022-0221

Lab for cloud run


## 待改善問題

1. 準備棄坑 springfox, 請參考 [swagger-starter3.0 not support springBoot2.6.x version](https://github.com/springfox/springfox/issues/3934)
    springfox 近年在技術跟進上並不太積極所以跟不上版本變化, 目前暫時解法是在設定檔中加上 ```spring.mvc.pathmatch.matching-strategy=ant-path-matcher```, 但是治標不治本, 可以更換成 [springdoc-openapi](https://github.com/springdoc/springdoc-openapi), 官方也提供 [Migrating from SpringFox](https://springdoc.org/migrating-from-springfox.html) 文件.

## 初始化外部開發資源

``` bash
docker-compose -f dev-resources/docker-compose.yml up -d
```

## 啟動服務

本機上服務啟動流程  

1. 預設是 所以請參考 ```config/application-dev.yml``` 配置.
2. 透過 [Liquibase](https://liquibase.org/) 進行 Database schema migration.
3. 啟動完成, 進入 OpenAPI 測試頁面.

