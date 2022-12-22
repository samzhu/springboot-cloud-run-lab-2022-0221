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

./gradlew --no-build-cache cleanTest test build 
./gradlew --no-build-cache cleanTest test
docker-compose -f loadtests/docker-compose.yml up

sdk install java 22.0.0.2.r17-nik
sdk default java 22.0.0.2.r17-nik

sdk default java 17.0.2-ms

sdk install java 22.0.0.2.r17-grl
sdk use java 22.0.0.2.r17-grl
gu install native-image


Native 有 logback-spring.xml 問題
***************************
loadtests-app-1       | APPLICATION FAILED TO START
loadtests-app-1       | ***************************
loadtests-app-1       | 
loadtests-app-1       | Description:
loadtests-app-1       | 
loadtests-app-1       | Native reflection configuration for liquibase.configuration.LiquibaseConfiguration.<init>() is missing.
loadtests-app-1       | 
loadtests-app-1       | Action:
loadtests-app-1       | 
loadtests-app-1       | Native configuration for a method accessed reflectively is likely missing.
loadtests-app-1       | You can try to configure native hints in order to specify it explicitly.
loadtests-app-1       | See https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#native-hints for more details.

https://github.com/spring-projects-experimental/spring-native/issues/1118
https://github.com/joshlong/spring-native-jhipster-research/blob/main/liquibase/src/main/java/jhipster/liquibase/LiquibaseApplication.java



## K6
https://editor.leonh.space/2022/k6/


docker run -it -v ${PWD}/loadtests:/scripts grafana/k6 bash

curl http://localhost:9000/actuator/health/readiness


export SPRING_CLOUD_GCP_SECRETMANAGER_ENABLED=false

gradle --no-daemon -x test clean bootBuildImage --imageName=${REGISTRY_NAME}/${BUILD_IMAGE_NAME}:${BUILD_IMAGE_TAG}

docker compose -f loadtests/docker-compose.yml down
docker compose -f loadtests/docker-compose.yml up postgres -d
docker compose -f loadtests/docker-compose.yml up redis -d
docker compose -f loadtests/docker-compose.yml up app -d
docker compose -f loadtests/docker-compose.yml up app-test -d
docker compose -f loadtests/docker-compose.yml up k6




docker run --rm -it -v ${PWD}/loadtests:/loadtests alpine /bin/sh
apk --no-cache add curl



docker pull alpine


http://127.0.0.1:8080/swagger-ui/

https://github.com/paketo-buildpacks/java

https://marketplace.visualstudio.com/items?itemName=ms-ossdata.vscode-postgresql

https://github.com/wagoodman/dive
brew install dive

./gradlew --no-daemon -x test clean bootBuildImage --imageName=test:1