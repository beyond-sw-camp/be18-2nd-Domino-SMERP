INSERT INTO item_status (item_status_id, status)
VALUES (1, 'RAW_MATERIAL'),
       (2, 'AUXILIARY_MATERIAL'),
       (3, 'SEMI_FINISHED_PRODUCT'),
       (4, 'FINISHED_PRODUCT'),
       (5, 'MERCHANDISE'),
       (6, 'INTANGIBLE_GOODS');

-- K-pop 그룹 자산 데이터를 item 테이블에 삽입하는 스크립트

-- AUXILIARY_MATERIAL: 소속사/기획사 데이터 삽입
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (2, '빅히트 뮤직', '엔터테인먼트 기획사', '개', 100000000, 150000000, '2005-02-01', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-AGENCY-BIGHIT', '하이브', NULL, NULL, FALSE),
       (2, '플레디스 엔터테인먼트', '엔터테인먼트 기획사', '개', 100000000, 150000000, '2007-01-01', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-AGENCY-PLEDIS', '하이브', NULL, NULL, FALSE),
       (2, 'YG 엔터테인먼트', '엔터테인먼트 기획사', '개', 100000000, 150000000, '1996-02-24', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-AGENCY-YG', NULL, NULL, NULL, FALSE),
       (2, '어도어', '엔터테인먼트 기획사', '개', 100000000, 150000000, '2021-11-12', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-AGENCY-ADOR', '하이브', NULL, NULL, FALSE),
       (2, 'JYP 엔터테인먼트', '엔터테인먼트 기획사', '개', 100000000, 150000000, '1997-04-25', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-AGENCY-JYP', NULL, NULL, NULL, FALSE),
       (2, 'KQ엔터테인먼트', '엔터테인먼트 기획사', '개', 100000000, 150000000, '2013-01-01', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-AGENCY-KQ', NULL, '세븐시즌스', NULL, FALSE),
       (2, '쏘스뮤직', '엔터테인먼트 기획사', '개', 100000000, 150000000, '2009-11-17', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-AGENCY-SOURCEMUSIC', '하이브', NULL, NULL, FALSE),
       (2, 'SM 엔터테인먼트', '엔터테인먼트 기획사', '개', 100000000, 150000000, '1995-02-14', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-AGENCY-SM', NULL, NULL, NULL, FALSE),
       (2, '스타쉽엔터테인먼트', '엔터테인먼트 기획사', '개', 100000000, 150000000, '2008-01-28', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-AGENCY-STARSHIP', NULL, NULL, NULL, FALSE);


-- 방탄소년단 (BTS) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '방탄소년단', '보이그룹', '명', 4500, 4900, '2013-06-13', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-GROUP-01', '하이브', '빅히트 뮤직', '아미', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, 'RM', '남자', '세', 1000, 1200, '1994-09-12', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-RM-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE),
       (1, '진', '남자', '세', 1000, 1200, '1992-12-04', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-JIN-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE),
       (1, '슈가', '남자', '세', 1000, 1200, '1993-03-09', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-SUGA-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE),
       (1, '제이홉', '남자', '세', 1000, 1200, '1994-02-18', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-JHOPE-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE),
       (1, '지민', '남자', '세', 1000, 1200, '1995-10-13', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-JIMIN-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE),
       (1, '뷔', '남자', '세', 1000, 1200, '1995-12-30', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-V-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE),
       (1, '정국', '남자', '세', 1000, 1200, '1997-09-01', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-JUNGKOOK-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE);

-- SEMI_FINISHED_PRODUCT: 솔로 프로젝트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (3, '지민 솔로 앨범', '멤버 솔로 활동', '개', 2000, 3000, '2025-04-05', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BTS-SOLO-01', '방탄소년단', '빅히트 뮤직', '지민', FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'Proof', '앨범', '개', 15000, 20000, '2022-06-10', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BTS-ALBUM-01', '방탄소년단', '빅히트 뮤직', NULL, FALSE),
       (5, 'Map of the Soul: 7', '앨범', '개', 15000, 20000, '2020-02-21', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BTS-ALBUM-02', '방탄소년단', '빅히트 뮤직', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'Weverse (위버스)', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2013-06-13', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-BTS-SITE-01', '방탄소년단', '하이브', NULL, FALSE),
       (6, 'ibighit.com', '공식 홈페이지', '점', 10000000, 15000000, '2013-06-13', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-BTS-SITE-02', '방탄소년단', '빅히트 뮤직', NULL, FALSE);

-- 세븐틴 (SEVENTEEN) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '세븐틴', '보이그룹', '명', 4500, 4900, '2015-05-26', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-GROUP-01', '하이브', '플레디스 엔터테인먼트', '캐럿', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '에스쿱스', '남자', '세', 1000, 1200, '1995-08-08', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-SC-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '정한', '남자', '세', 1000, 1200, '1995-10-04', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-JH-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '조슈아', '남자', '세', 1000, 1200, '1995-12-30', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-JS-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '준', '남자', '세', 1000, 1200, '1996-06-10', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-JUN-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '호시', '남자', '세', 1000, 1200, '1996-06-15', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-HS-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '원우', '남자', '세', 1000, 1200, '1996-07-17', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-WW-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '우지', '남자', '세', 1000, 1200, '1996-11-22', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-WZ-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '도겸', '남자', '세', 1000, 1200, '1997-02-18', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-DK-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '민규', '남자', '세', 1000, 1200, '1997-04-06', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-MG-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '디에잇', '남자', '세', 1000, 1200, '1997-11-07', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-D8-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '승관', '남자', '세', 1000, 1200, '1998-01-16', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-SK-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '버논', '남자', '세', 1000, 1200, '1998-02-18', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-VN-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '디노', '남자', '세', 1000, 1200, '1999-02-11', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-DN-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE);

-- SEMI_FINISHED_PRODUCT: 유닛 그룹
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (3, '부석순(BSS)', '세븐틴 유닛', '명', 2000, 3000, '2018-03-21', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-SVT-BSS-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (3, '정한X원우', '세븐틴 유닛', '명', 2000, 3000, '2025-06-17', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-JHW-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'FML', '앨범', '개', 15000, 20000, '2023-04-24', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-SVT-ALBUM-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (5, 'Seventeenth Heaven', '앨범', '개', 15000, 20000, '2023-10-23', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-SVT-ALBUM-02', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'pledis.co.kr', '공식 홈페이지', '점', 10000000, 15000000, '2015-05-26', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-SVT-SITE-01', '세븐틴', '플레디스 엔터테인먼트', NULL, FALSE),
       (6, 'weverse.io', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2015-05-26', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-SVT-SITE-02', '세븐틴', '하이브', NULL, FALSE);

-- 블랙핑크 (BLACKPINK) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '블랙핑크', '걸그룹', '명', 4500, 4900, '2016-08-08', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKP-GROUP-01', '블랙핑크 멤버 개별 레이블', 'YG 엔터테인먼트', '블링크', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '지수', '여자', '세', 1000, 1200, '1995-01-03', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKP-JS-01', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE),
       (1, '제니', '여자', '세', 1000, 1200, '1996-01-16', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKP-JN-01', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE),
       (1, '로제', '여자', '세', 1000, 1200, '1997-02-11', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKP-RZ-01', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE),
       (1, '리사', '여자', '세', 1000, 1200, '1997-03-27', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKP-LS-01', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE);

-- SEMI_FINISHED_PRODUCT: 솔로 프로젝트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (3, '지수 솔로', '멤버 솔로 활동', '개', 2000, 3000, '2021-12-12', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKP-SOLO-01', '블랙핑크', '블랙핑크 멤버 개별 레이블', '지수', FALSE),
       (3, '제니 솔로', '멤버 솔로 활동', '개', 2000, 3000, '2020-03-21', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKP-SOLO-02', '블랙핑크', '블랙핑크 멤버 개별 레이블', '제니', FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'BORN PINK', '앨범', '개', 15000, 20000, '2022-09-16', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKP-ALBUM-01', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE),
       (5, 'THE ALBUM', '앨범', '개', 15000, 20000, '2020-10-02', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKP-ALBUM-02', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'ygfamily.com', '공식 홈페이지', '점', 10000000, 15000000, '2016-08-08', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-BKP-SITE-01', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE),
       (6, 'weverse.io', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2016-08-08', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-BKP-SITE-02', '블랙핑크', 'YG 엔터테인먼트', NULL, FALSE);

-- 뉴진스 (NewJeans) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '뉴진스', '걸그룹', '명', 4500, 4900, '2022-07-22', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NJN-GROUP-01', '하이브', '어도어', '버니즈', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '민지', '여자', '세', 1000, 1200, '2004-05-07', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NJN-MJ-01', '뉴진스', '어도어', NULL, FALSE),
       (1, '하니', '여자', '세', 1000, 1200, '2004-10-06', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NJN-HN-01', '뉴진스', '어도어', NULL, FALSE),
       (1, '다니엘', '여자', '세', 1000, 1200, '2005-04-11', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NJN-DL-01', '뉴진스', '어도어', NULL, FALSE),
       (1, '해린', '여자', '세', 1000, 1200, '2006-05-15', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NJN-HR-01', '뉴진스', '어도어', NULL, FALSE),
       (1, '혜인', '여자', '세', 1000, 1200, '2008-04-21', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NJN-HI-01', '뉴진스', '어도어', NULL, FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'How Sweet', '앨범', '개', 15000, 20000, '2024-05-24', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-NJN-ALBUM-01', '뉴진스', '어도어', NULL, FALSE),
       (5, 'Supernatural', '앨범', '개', 15000, 20000, '2024-06-21', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-NJN-ALBUM-02', '뉴진스', '어도어', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'newjeans.kr', '공식 홈페이지', '점', 10000000, 15000000, '2022-07-22', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-NJN-SITE-01', '뉴진스', '어도어', NULL, FALSE),
       (6, 'weverse.io', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2022-07-22', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-NJN-SITE-02', '뉴진스', '하이브', NULL, FALSE);

-- 프로미스나인 (fromis_9) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '프로미스나인', '걸그룹', '명', 4500, 4900, '2018-01-24', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-GROUP-01', '하이브', '플레디스 엔터테인먼트', '플로버', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '이새롬', '여자', '세', 1000, 1200, '1997-01-07', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-LSR-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '송하영', '여자', '세', 1000, 1200, '1997-09-29', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-SHY-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '박지원', '여자', '세', 1000, 1200, '1998-03-20', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-PJW-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '노지선', '여자', '세', 1000, 1200, '1998-11-23', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-NJS-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '이서연', '여자', '세', 1000, 1200, '2000-01-22', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-LSY-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '이채영', '여자', '세', 1000, 1200, '2000-05-14', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-LCY-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '이나경', '여자', '세', 1000, 1200, '2000-06-01', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-LNK-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (1, '백지헌', '여자', '세', 1000, 1200, '2003-04-17', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-FMN-BJH-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE);

-- SEMI_FINISHED_PRODUCT: 솔로 프로젝트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (3, '송하영 솔로곡', '멤버 솔로 활동', '개', 2000, 3000, '2024-10-02', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-FMN-SOLO-01', '프로미스나인', '플레디스 엔터테인먼트', '송하영', FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'Unlock My World', '앨범', '개', 15000, 20000, '2023-06-05', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-FMN-ALBUM-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (5, 'Supersonic', '앨범', '개', 15000, 20000, '2024-05-24', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-FMN-ALBUM-02', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'pledis.co.kr', '공식 홈페이지', '점', 10000000, 15000000, '2018-01-24', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-FMN-SITE-01', '프로미스나인', '플레디스 엔터테인먼트', NULL, FALSE),
       (6, 'weverse.io', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2018-01-24', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-FMN-SITE-02', '프로미스나인', '하이브', NULL, FALSE);

-- 엔믹스 (NMIXX) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '엔믹스', '걸그룹', '명', 4500, 4900, '2022-02-22', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-GROUP-01', 'JYP 엔터테인먼트', NULL, 'NSWER', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '해원', '여자', '세', 1000, 1200, '2003-02-25', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-HW-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE),
       (1, '설윤', '여자', '세', 1000, 1200, '2004-01-26', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-SY-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE),
       (1, '릴리', '여자', '세', 1000, 1200, '2002-10-17', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-LLY-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE),
       (1, '배이', '여자', '세', 1000, 1200, '2004-12-28', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-BAE-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE),
       (1, '지우', '여자', '세', 1000, 1200, '2005-04-13', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-JIU-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE),
       (1, '규진', '여자', '세', 1000, 1200, '2006-05-26', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-KYJ-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'Fe3O4: BREAK', '앨범', '개', 15000, 20000, '2024-01-15', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-NMX-ALBUM-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE),
       (5, 'expérgo', '앨범', '개', 15000, 20000, '2023-03-20', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-NMX-ALBUM-02', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'Weverse', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2022-02-22', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-NMX-SITE-01', '엔믹스', 'JYP 엔터테인먼트', NULL, FALSE);

-- 블락비 (Block B) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '블락비', '보이그룹', '명', 4500, 4900, '2011-04-15', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-GROUP-01', 'KQ엔터테인먼트', '세븐시즌스', 'BBC', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '태일', '남자', '세', 1000, 1200, '1990-09-24', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-TAEIL-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (1, '비범', '남자', '세', 1000, 1200, '1990-12-14', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-BB-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (1, '재효', '남자', '세', 1000, 1200, '1990-12-23', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-JH-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (1, '유권', '남자', '세', 1000, 1200, '1992-04-09', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-YKW-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (1, '박경', '남자', '세', 1000, 1200, '1992-07-08', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-PK-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (1, '지코', '남자', '세', 1000, 1200, '1992-09-14', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-ZICO-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (1, '피오', '남자', '세', 1000, 1200, '1993-02-02', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-BKB-PIO-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE);

-- SEMI_FINISHED_PRODUCT: 유닛/솔로 프로젝트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (3, '블락비 바스타즈', '유닛 그룹', '명', 2000, 3000, '2015-04-14', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKB-BASTARZ-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (3, '지코 솔로 앨범', '멤버 솔로 활동', '개', 2000, 3000, '2015-02-13', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKB-SOLO-01', '블락비', 'KQ엔터테인먼트', '지코', FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'Very Good', '앨범', '개', 15000, 20000, '2013-10-02', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKB-ALBUM-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (5, 'Blooming Period', '앨범', '개', 15000, 20000, '2016-04-11', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-BKB-ALBUM-02', '블락비', 'KQ엔터테인먼트', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, '블락비 공식 웹사이트', '공식 홈페이지', '점', 10000000, 15000000, '2011-04-15', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-BKB-SITE-01', '블락비', 'KQ엔터테인먼트', NULL, FALSE),
       (6, 'Weverse', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2011-04-15', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-BKB-SITE-02', '블락비', 'KQ엔터테인먼트', NULL, FALSE);

-- 르세라핌 (LE SSERAFIM) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '르세라핌', '걸그룹', '명', 4500, 4900, '2022-05-02', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-LSF-GROUP-01', '하이브', '쏘스뮤직', '피어나', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '김채원', '여자', '세', 1000, 1200, '2000-08-01', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-LSF-KCW-01', '르세라핌', '쏘스뮤직', NULL, FALSE),
       (1, '사쿠라', '여자', '세', 1000, 1200, '1998-03-19', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-LSF-SKR-01', '르세라핌', '쏘스뮤직', NULL, FALSE),
       (1, '허윤진', '여자', '세', 1000, 1200, '2001-10-08', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-LSF-HYJ-01', '르세라핌', '쏘스뮤직', NULL, FALSE),
       (1, '카즈하', '여자', '세', 1000, 1200, '2003-08-09', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-LSF-KZH-01', '르세라핌', '쏘스뮤직', NULL, FALSE),
       (1, '홍은채', '여자', '세', 1000, 1200, '2006-11-10', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-LSF-HEC-01', '르세라핌', '쏘스뮤직', NULL, FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'FEARLESS', '앨범', '개', 15000, 20000, '2022-05-02', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-LSF-ALBUM-01', '르세라핌', '쏘스뮤직', NULL, FALSE),
       (5, 'UNFORGIVEN', '앨범', '개', 15000, 20000, '2023-05-01', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-LSF-ALBUM-02', '르세라핌', '쏘스뮤직', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'Weverse', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2022-05-02', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-LSF-SITE-01', '르세라핌', '하이브', NULL, FALSE);

-- 에스파 (aespa) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '에스파', '걸그룹', '명', 4500, 4900, '2020-11-17', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-AESP-GROUP-01', 'SM 엔터테인먼트', NULL, 'MY', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '카리나', '여자', '세', 1000, 1200, '2000-04-11', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-AESP-KR-01', '에스파', 'SM 엔터테인먼트', NULL, FALSE),
       (1, '지젤', '여자', '세', 1000, 1200, '2000-10-30', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-AESP-GJ-01', '에스파', 'SM 엔터테인먼트', NULL, FALSE),
       (1, '윈터', '여자', '세', 1000, 1200, '2001-01-01', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-AESP-WT-01', '에스파', 'SM 엔터테인먼트', NULL, FALSE),
       (1, '닝닝', '여자', '세', 1000, 1200, '2002-10-23', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-AESP-NN-01', '에스파', 'SM 엔터테인먼트', NULL, FALSE);

-- SEMI_FINISHED_PRODUCT: 멤버 솔로곡
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (3, '멤버 솔로곡', '멤버 솔로 활동', '개', 2000, 3000, '2024-10-09', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-AESP-SOLO-01', '에스파', 'SM 엔터테인먼트', NULL, FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'Armageddon', '앨범', '개', 15000, 20000, '2024-05-27', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-AESP-ALBUM-01', '에스파', 'SM 엔터테인먼트', NULL, FALSE),
       (5, 'Savage', '앨범', '개', 15000, 20000, '2021-10-05', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-AESP-ALBUM-02', '에스파', 'SM 엔터테인먼트', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'aespa 공식 웹사이트', '공식 홈페이지', '점', 10000000, 15000000, '2020-11-17', '2025-08-22',
        'ACTIVE', 0, 'DISABLED', 'RFID-AESP-SITE-01', '에스파', 'SM 엔터테인먼트', NULL, FALSE),
       (6, 'Weverse', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2020-11-17', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-AESP-SITE-02', '에스파', 'SM 엔터테인먼트', NULL, FALSE);

-- 아이브 (IVE) 데이터 삽입
-- FINISHED_PRODUCT: 그룹명
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (4, '아이브', '걸그룹', '명', 4500, 4900, '2021-12-01', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-IVE-GROUP-01', '스타쉽엔터테인먼트', NULL, 'DIVE', FALSE);

-- RAW_MATERIAL: 멤버
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (1, '안유진', '여자', '세', 1000, 1200, '2003-09-01', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-IVE-AYJ-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE),
       (1, '가을', '여자', '세', 1000, 1200, '2002-09-24', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-IVE-GEUL-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE),
       (1, '레이', '여자', '세', 1000, 1200, '2004-02-03', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-IVE-REI-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE),
       (1, '장원영', '여자', '세', 1000, 1200, '2004-08-31', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-IVE-JWY-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE),
       (1, '리즈', '여자', '세', 1000, 1200, '2004-11-21', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-IVE-LIZ-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE),
       (1, '이서', '여자', '세', 1000, 1200, '2007-02-21', '2025-08-22', 'ACTIVE', 0, 'DISABLED',
        'RFID-IVE-LSR-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE);

-- MERCHANDISE: 앨범
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (5, 'I\'ve IVE', '앨범', '개', 15000, 20000, '2023-04-10', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-IVE-ALBUM-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE),
       (5, 'I\'VE MINE', '앨범', '개', 15000, 20000, '2023-10-13', '2025-08-22', 'ACTIVE', 0,
        'DISABLED', 'RFID-IVE-ALBUM-02', '아이브', '스타쉽엔터테인먼트', NULL, FALSE);

-- INTANGIBLE_GOODS: 공식 사이트
INSERT INTO item (item_status_id, name, specification, unit, inbound_unit_price,
                  outbound_unit_price, created_at, updated_at, item_act, safety_stock,
                  safety_stock_act, rfid, group_name1, group_name2, group_name3, is_deleted)
VALUES (6, 'Weverse', '팬 커뮤니티 플랫폼', '점', 10000000, 15000000, '2021-12-01', '2025-08-22', 'ACTIVE',
        0, 'DISABLED', 'RFID-IVE-SITE-01', '아이브', '스타쉽엔터테인먼트', NULL, FALSE);