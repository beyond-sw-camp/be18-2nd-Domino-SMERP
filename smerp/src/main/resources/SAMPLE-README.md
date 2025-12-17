# ⚠️ Note:
# 본 데이터는 공개 연예인 및 회사명을 예시로 사용한 테스트 데이터이며,
# 실제 회사 또는 인물과는 아무 관련이 없습니다.



### newman 샘플 데이터 사용법

```text
# backend/smerp에서 newman으로 디렉터리 위치 변경
cd src/main/resources/newman

# postman 실행 후 bom sample-data insert
newman run bom_collection.json -e bom_env.json