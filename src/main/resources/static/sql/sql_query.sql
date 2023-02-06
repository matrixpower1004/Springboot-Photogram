create user 'matrix'@'%' identified by 'matrix87';
GRANT ALL PRIVILEGES ON *.* TO 'matrix'@'%';
create database photogram;
desc user;
select * from photogram;
use photogram;

-- 구독수
SELECT COUNT(*) FROM subscribe WHERE fromUserId = 2;

-- 구독 여부 (ssar(id 1)로 로그인, cos 페이지로 이동)
SELECT COUNT(*) FROM subscribe WHERE fromUserId = 1 AND toUserId = 2;
-- 1 이상이 나오면 구독을 했다는 의미

-- 로그인(id:1 ssar) -- 구독정보(id:2 cos)
-- 1번과 3번의 정보(toUserId)가 구독 모달에 출력
SELECT * FROM subscribe;
 
SELECT * FROM user;
SELECT * FROM subscribe WHERE fromUserId = 2;
SELECT * FROM user WHERE id = 1 or id = 3;

-- 조인 (user.id = subscribe.toUserId) 이너조인
SELECT u.id, u.username, u.profileImageUrl
FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;

-- 로그인(1), 화면(1, 3) 1번이 3번을 구독했는지 여부
SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 1;
SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 3;

-- 가상 컬럼을 추가
SELECT u.id, u.username, u.profileImageUrl, 1 subscribeState
FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;

-- 스칼라 서브쿼리(단일행을 리턴)
SELECT u.id, u.username, u.profileImageUrl,
(SELECT COUNT(*) FROM user) subscribeState
FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;

-- 구독여부 완성 쿼리
SELECT u.id, u.username, u.profileImageUrl,
(SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id) subscribeState
FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;

SELECT true FROM subscribe WHERE fromUserId = 1 AND toUserId = 3;

-- 동일 유저인지 판단하는 쿼리
SELECT u.id, u.username, u.profileImageUrl,
if ((SELECT 1 FROM subscribe WHERE fromUserId = 1 AND toUserId = u.id), 1, 0) subscribeState,
if ((1 = u.id), 1, 0) equalUserState
FROM user u INNER JOIN subscribe s
ON u.id = s.toUserId
WHERE s.fromUserId = 2;