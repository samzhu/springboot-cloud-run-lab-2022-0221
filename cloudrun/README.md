# Cloud Run 環境準備

## 帳號授權

先清除以免用到別的帳號

``` bash
gcloud auth revoke --all
gcloud auth application-default revoke
gcloud components update --quiet
```

在本機環境授權

``` bash
gcloud auth login
```

認證完可使用指令查看

``` bash
gcloud auth list
```

配置預設屬性

``` bash
export PROJECT_ID=test-cloud-run-0324-1
export REGION=asia-east1
export ZONE=asia-east1-c

gcloud config set project $PROJECT_ID

gcloud config list project
```

新專案要啟動

``` bash
gcloud services enable artifactregistry.googleapis.com
gcloud services enable secretmanager.googleapis.com
gcloud services enable run.googleapis.com
gcloud services enable sqladmin.googleapis.com
gcloud services enable compute.googleapis.com
gcloud services enable servicenetworking.googleapis.com
gcloud services enable redis.googleapis.com
gcloud services enable vpcaccess.googleapis.com
```

## 需要三個階段的 Service Account

CI / CD / Runtime

## CI Service Account

在包版過程需要用到 artifacts repositories

### 建立儲存庫

``` bash
export REPOSITORY_NAME=quickstart-docker-repo

gcloud artifacts repositories create $REPOSITORY_NAME --repository-format=docker \
--location=$REGION --description="Demo docker repository"
```

(Example output)

``` bash
Create request issued for: [quickstart-docker-repo]
Waiting for operation [projects/cloud-run-0322/locations/asia-east1/operations/066e84bd-c4eb-4852-adbd-c48f59c1a2ce] to complete...done.               
Created repository [quickstart-docker-repo].
```

``` bash
gcloud artifacts repositories list
```

(Example output)

``` bash
Listing items under project cloud-run-0322, across all locations.

                                                           ARTIFACT_REGISTRY
REPOSITORY              FORMAT  DESCRIPTION             LOCATION    LABELS  ENCRYPTION          CREATE_TIME          UPDATE_TIME
quickstart-docker-repo  DOCKER  Demo docker repository  asia-east1          Google-managed key  2022-03-22T11:31:33  2022-03-22T11:31:33
```

建立可寫入 repository 的 Service Account, 需要綁定此角色 ```roles/artifactregistry.writer``

``` bash
export SERVICE_ACCOUNT_ID=serviceaccount-ci

gcloud iam service-accounts create $SERVICE_ACCOUNT_ID \
    --description="Artifact Registry Writer" \
    --display-name="SA CI"
```

(Example output)

``` bash
Created service account [serviceaccount-ci].
```

Add policy binding to service account

``` bash
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/artifactregistry.writer"
```

(Example output)

``` bash
Updated IAM policy for project [cloud-run-0322].
bindings:
- members:
  - serviceAccount:service-880896405107@gcp-sa-artifactregistry.iam.gserviceaccount.com
  role: roles/artifactregistry.serviceAgent
- members:
  - serviceAccount:serviceaccount-ci@cloud-run-0322.iam.gserviceaccount.com
  role: roles/artifactregistry.writer
.
.
.
etag: BwXax4aJOXE=
version: 1
```

Creating service account keys

``` bash
export KEY_FILE=./sa-ci.json

gcloud iam service-accounts keys create $KEY_FILE \
    --iam-account=$SERVICE_ACCOUNT_ID@$PROJECT_ID.iam.gserviceaccount.com
```

(Example output)

``` bash
created key [0f7468727956db8aafab962847541068e40ab7e2] of type [json] as [./sa-ci.json] for [serviceaccount-ci@cloud-run-0322.iam.gserviceaccount.com]
```

Service account authorization

``` bash
gcloud auth activate-service-account $SERVICE_ACCOUNT_ID@$PROJECT_ID.iam.gserviceaccount.com --key-file=$KEY_FILE
```

(Example output)

``` bash
Activated service account credentials for: [serviceaccount-ci@cloud-run-0322.iam.gserviceaccount.com]
```

Check account

``` bash
gcloud auth list
```

(Example output)

``` bash
                     Credentialed Accounts
ACTIVE  ACCOUNT
        samzhu@gmail.com
*       serviceaccount-ci@cloud-run-0322.iam.gserviceaccount.com

To set the active account, run:
    $ gcloud config set account `ACCOUNT`
```

授權給 docker

``` bash
gcloud auth configure-docker $REGION-docker.pkg.dev --quiet
```

(Example output)

``` bash
WARNING: Your config file at [/Users/samzhu/.docker/config.json] contains these credential helper entries:

{
  "credHelpers": {
    "asia-east1-docker.pkg.dev": "gcloud"
  }
}
Adding credentials for: asia-east1-docker.pkg.dev
gcloud credential helpers already registered correctly.
```

包版與推版

``` bash
export REGISTRY_URI=$REGION-docker.pkg.dev/$PROJECT_ID/$REPOSITORY_NAME
export BUILD_IMAGE_NAME=book
export BUILD_IMAGE_TAG=0.0.3

./gradlew --no-daemon -x test clean bootBuildImage --imageName=$REGISTRY_URI/$BUILD_IMAGE_NAME:$BUILD_IMAGE_TAG
```

(Example output)

``` bash
To honour the JVM settings for this build a single-use Daemon process will be forked. See https://docs.gradle.org/7.4/userguide/gradle_daemon.html#sec:disabling_the_daemon.
Daemon will be stopped at the end of the build 
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

> Task :compileJava
Note: /Users/samzhu/workspace/github-spike/springboot-cloud-run-lab-2022-0221/src/main/java/com/example/demo/configuration/OpenAPIConfig.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.

> Task :bootBuildImage
Building image 'asia-east1-docker.pkg.dev/cloud-run-0322/quickstart-docker-repo/book:0.0.1'
.
.
.
    [creator]     ===> EXPORTING
    [creator]     Adding layer 'paketo-buildpacks/ca-certificates:helper'
    [creator]     Adding layer 'paketo-buildpacks/bellsoft-liberica:helper'
    [creator]     Adding layer 'paketo-buildpacks/bellsoft-liberica:java-security-properties'
    [creator]     Adding layer 'paketo-buildpacks/bellsoft-liberica:jre'
    [creator]     Adding layer 'paketo-buildpacks/executable-jar:classpath'
    [creator]     Adding layer 'paketo-buildpacks/spring-boot:helper'
    [creator]     Adding layer 'paketo-buildpacks/spring-boot:spring-cloud-bindings'
    [creator]     Adding layer 'paketo-buildpacks/spring-boot:web-application-type'
    [creator]     Adding 5/5 app layer(s)
    [creator]     Adding layer 'launcher'
    [creator]     Adding layer 'config'
    [creator]     Adding layer 'process-types'
    [creator]     Adding label 'io.buildpacks.lifecycle.metadata'
    [creator]     Adding label 'io.buildpacks.build.metadata'
    [creator]     Adding label 'io.buildpacks.project.metadata'
    [creator]     Adding label 'org.springframework.boot.version'
    [creator]     Setting default process type 'web'
    [creator]     Saving asia-east1-docker.pkg.dev/cloud-run-0322/quickstart-docker-repo/book:0.0.1...
    [creator]     *** Images (5b3cdd528b00):
    [creator]           asia-east1-docker.pkg.dev/cloud-run-0322/quickstart-docker-repo/book:0.0.1
    [creator]     Adding cache layer 'paketo-buildpacks/syft:syft'

Successfully built image 'asia-east1-docker.pkg.dev/cloud-run-0322/quickstart-docker-repo/book:0.0.1'


BUILD SUCCESSFUL in 3m 6s
7 actionable tasks: 7 executed
```

推版

``` bash
docker push $REGISTRY_URI/$BUILD_IMAGE_NAME:$BUILD_IMAGE_TAG
```

(Example output)

``` bash
The push refers to repository [asia-east1-docker.pkg.dev/cloud-run-0322/quickstart-docker-repo/book]
1dc94a70dbaa: Preparing 
326174e2e5d8: Pushed 
b28e272ad893: Pushed 
5f70bf18a086: Layer already exists 
801e59c541f0: Pushed
.
.
.
8a7c16d608bf: Pushed 
a4e25480be6b: Pushed 
0.0.1: digest: sha256:5778b5a80d1fababa9a93fc601d6e7dbd0e9e2ec6702540495649e23eaafe49d size: 4500
```

## Create service account for cloud run runtime

``` bash
export SERVICE_ACCOUNT_ID_RUNTIME=serviceaccount-cloudrun

gcloud iam service-accounts create $SERVICE_ACCOUNT_ID_RUNTIME \
    --description="To cloud run runtime" \
    --display-name="cloud run-service SA" \
    --project=$PROJECT_ID
```

(Example output)

``` bash
Created service account [serviceaccount-cloudrun].
```

角色綁定

``` bash
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID_RUNTIME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/cloudsql.admin"
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID_RUNTIME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/cloudsql.instanceUser"
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID_RUNTIME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/cloudtrace.agent"
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID_RUNTIME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/logging.serviceAgent"
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID_RUNTIME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/monitoring.metricWriter"
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID_RUNTIME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/secretmanager.secretAccessor"
gcloud projects add-iam-policy-binding $PROJECT_ID \
    --member="serviceAccount:$SERVICE_ACCOUNT_ID_RUNTIME@$PROJECT_ID.iam.gserviceaccount.com" \
    --role="roles/iam.serviceAccountUser"
```

// roles/cloudsql.client

(Example output)

``` bash
Updated IAM policy for project [cloud-run-0322].
bindings:
- members:
  - serviceAccount:service-880896405107@gcp-sa-artifactregistry.iam.gserviceaccount.com
  role: roles/artifactregistry.serviceAgent
- members:
  - serviceAccount:serviceaccount-ci@cloud-run-0322.iam.gserviceaccount.com
  role: roles/artifactregistry.writer
- members:
  - serviceAccount:serviceaccount-cloudrun@cloud-run-0322.iam.gserviceaccount.com
  role: roles/cloudsql.admin
.
.
.
```

## Create database

Ensure your project is configured for private services access.

为 Cloud SQL 配置专用服务访问通道
[Configure private services access for Cloud SQL](https://cloud.google.com/sql/docs/postgres/configure-private-services-access#configure-access)

要僅指定前綴長度（子網掩碼），請使用 prefix-length 標誌。當您省略地址範圍時，Google Cloud 會自動在您的 VPC 網絡中選擇一個未使用的地址範圍  

以下示例分配了一個 IP 範圍，允許 VPC 網絡 default 中的資源使用私有 IP 連接到 Cloud SQL 實例

``` bash
export VPC_NETWORK_NAME=default

gcloud compute addresses create google-managed-services-$VPC_NETWORK_NAME \
  --global \
  --purpose=VPC_PEERING \
  --prefix-length=16 \
  --network=projects/$PROJECT_ID/global/networks/$VPC_NETWORK_NAME \
  --project=$PROJECT_ID
```

(Example output)

``` bash
Created [https://www.googleapis.com/compute/v1/projects/test-cloud-run-0323/global/addresses/google-managed-services-default].
```

``` bash
gcloud compute addresses create default-ip-range --global --purpose=VPC_PEERING --prefix-length=24 --network=default
```

(Example output)

``` bash
Created [https://www.googleapis.com/compute/v1/projects/test-cloud-run-0323/global/addresses/default-ip-range].
```

Create a private connection

``` bash
gcloud services vpc-peerings update --service=servicenetworking.googleapis.com --network=default --ranges=default-ip-range --force
```

(Example output)

``` bash
Operation "operations/pssn.p24-880896405107-ffe9500c-cf31-4ccb-9c78-807110b3b91a" finished successfully.
```

For tier

``` bash
export DB_INSTANCE_ID=quickstart-instance
export API_TIER_STRING=db-g1-small
export REGION=asia-east1
export DB_ROOT_PASSWORD=pw1234

gcloud sql instances create $DB_INSTANCE_ID \
  --database-version=POSTGRES_13 \
  --tier=$API_TIER_STRING \
  --region=$REGION \
  --root-password=$DB_ROOT_PASSWORD \
  --no-assign-ip \
  --network=default
```

(Example output)

``` bash
Creating Cloud SQL instance...done.                                                                                                                    
Created [https://sqladmin.googleapis.com/sql/v1beta4/projects/cloud-run-0322/instances/loadtests-sql].
NAME           DATABASE_VERSION  LOCATION      TIER         PRIMARY_ADDRESS  PRIVATE_ADDRESS  STATUS
loadtests-sql  POSTGRES_13       asia-east1-c  db-g1-small  -                10.17.250.3      RUNNABLE
```

連線名稱 ```$PROJECT_ID:$REGION:$DB_INSTANCE_ID``` 例如 ```cloud-run-0322:asia-east1:loadtests-sql```

创建数据库
``` bash
export DB_INSTANCE_ID=quickstart-instance
export DB_DATABASE_NAME=testdb

gcloud sql databases create $DB_DATABASE_NAME --instance=$DB_INSTANCE_ID
```

创建用户

``` bash
export DB_INSTANCE_ID=quickstart-instance
export DB_USERNAME=dbuser
export DB_PASSWORD=pw1234

gcloud sql users create $DB_USERNAME \
  --instance=$DB_INSTANCE_ID \
  --password=$DB_PASSWORD
```



https://cloud.google.com/sql/docs/postgres/create-instance
https://codelabs.developers.google.com/codelabs/cloud-sql-connectivity-gce-private#3

## Create Redis

``` bash
export INSTANCE_ID_REDIS=loadtests-redis

gcloud redis instances create $INSTANCE_ID_REDIS --size=1 --region=$REGION --tier=basic --project=$PROJECT_ID
```

(Example output)

``` bash
Create request issued for: [loadtests-redis]
Waiting for operation [projects/cloud-run-0322/locations/asia-east1/operations/operation-1647932522316-5dac9309b42c4-8931314e-0d4d81ae] to complete...d
one.
Created instance [loadtests-redis].
```

Cheak

``` bash
gcloud redis instances list --region=$REGION
```

(Example output)

``` bash
INSTANCE_NAME    VERSION    REGION      TIER   SIZE_GB  HOST          PORT  NETWORK  RESERVED_IP      STATUS  CREATE_TIME
loadtests-redis  REDIS_6_X  asia-east1  BASIC  1        10.50.73.211  6379  default  10.50.73.208/29  READY   2022-03-22T07:02:03
```

https://cloud.google.com/memorystore/docs/redis/creating-managing-instances  
https://codelabs.developers.google.com/codelabs/cloud-spring-cache-memorystore

## Create secrets for config

``` bash
export SECRET_CONFIG_NAME=ct-mid-cl-book
export LOCAL_CONFIG_FILE_PATH=config/application-gcp.yml

gcloud secrets create $SECRET_CONFIG_NAME --quiet --data-file=$LOCAL_CONFIG_FILE_PATH --project=$PROJECT_ID || \
gcloud secrets versions add $SECRET_CONFIG_NAME --data-file=$LOCAL_CONFIG_FILE_PATH --project=$PROJECT_ID
```

(Example output)

``` bash
Created version [1] of the secret [ct-mid-cl-book].
```

Describe secret

``` bash
gcloud secrets describe $SECRET_CONFIG_NAME
```

(Example output)

``` bash
createTime: '2022-03-22T07:16:32.018182Z'
etag: '"15dac96471db06"'
name: projects/880896405107/secrets/ct-mid-cl-book
replication:
  automatic: {}
```

檢視內容

``` bash
export VERSION_ID=1

gcloud secrets versions access $VERSION_ID --secret=$SECRET_CONFIG_NAME
# or
gcloud secrets versions access $VERSION_ID --secret=$SECRET_CONFIG_NAME --format='get(payload.data)' | tr '_-' '/+' | base64 -d
```

(Example output)

``` bash
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    username: dbuser
    password: ${sm://db_password}
    hikari:
      minimum-idle: 5
      maximum-pool-size: 5
      connection-timeout: 10000
      idle-timeout: 600000
      max-lifetime: 1800000
.
.
.
```

https://cloud.google.com/secret-manager/docs/creating-and-accessing-secrets

## Create secrets

``` bash
export SECRET_NAME=db_password

echo -n "pw1234" | \
    gcloud secrets create $SECRET_NAME --data-file=-
```

OR

``` bash
export SECRET_NAME=db_password

echo -n "pw1234" | \
    gcloud secrets versions add $SECRET_NAME --data-file=-
```

(Example output)

``` bash
Created version [1] of the secret [lab_0221_db_password].
```

## 建立 Serverless VPC access

``` bash
export VPC_ACCESS=quickstart-connector

gcloud compute networks vpc-access connectors create $VPC_ACCESS \
  --region=asia-east1 \
  --network=default \
  --range=10.61.3.0/28 \
  --min-instances=2 \
  --max-instances=10 \
  --machine-type=e2-micro
```

## Create service account to deploy




## Deploy service

https://cloud.google.com/sql/docs/postgres/connect-instance-cloud-run

``` bash
export REGISTRY_URI=$REGION-docker.pkg.dev/$PROJECT_ID/$REPOSITORY_NAME
export BUILD_IMAGE_NAME=book
export BUILD_IMAGE_TAG=0.0.3

export SERVICE_ACCOUNT_ID=serviceaccount-cloudrun
export MEMORY=1024M
export CPU=1
export SERVICE_NAME=book
export SECRET_CONFIG_NAME=ct-mid-cl-book
export SECRET_CONFIG_PATH=/workspace/config/application-gcp.yml
export MIN_INSTANCES=1
export MAX_INSTANCES=1
export INSTANCE_CONNECTION_NAME=test-cloud-run-0324-1:asia-east1:quickstart-instance
export DB_DATABASE_NAME=testdb
export DB_USERNAME=dbuser
export REDIS_HOST=10.180.61.83

gcloud run deploy $SERVICE_NAME \
  --quiet \
  --region=$REGION \
  --image=$REGISTRY_URI/$BUILD_IMAGE_NAME:$BUILD_IMAGE_TAG \
  --cpu=$CPU \
  --memory=$MEMORY \
  --service-account=$SERVICE_ACCOUNT_ID \
  --set-env-vars="^@^spring.profiles.active=gcp,chaos-monkey" \
  --set-env-vars="spring.cloud.gcp.secretmanager.enabled=true" \
  --set-env-vars="management.server.port=8080" \
  --set-env-vars="db_username=${DB_USERNAME}" \
  --set-env-vars="sql_instance_connection_name=${INSTANCE_CONNECTION_NAME}" \
  --set-env-vars="sql_database_name=${DB_DATABASE_NAME}" \
  --set-env-vars="redis_host=${REDIS_HOST}" \
  --set-secrets=$SECRET_CONFIG_PATH=$SECRET_CONFIG_NAME:latest \
  --min-instances=$MIN_INSTANCES \
  --max-instances=$MAX_INSTANCES \
  --project=$PROJECT_ID \
  --vpc-connector="quickstart-connector" \
  --allow-unauthenticated
```

(Example output)

``` bash
```

gcloud run services delete $SERVICE_NAME --region=$REGION


https://cloud.google.com/run/docs/configuring/environment-variables#command-line
https://codelabs.developers.google.com/codelabs/cloud-run-deploy#0



https://cloud.google.com/sql/docs/postgres/connect-instance-cloud-run


zh-TW

https://cloud.google.com/run/docs/tips/java

https://cloud.google.com/run/docs/tips#optimizing_performance