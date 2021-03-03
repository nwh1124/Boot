# 데이터베이스 생성
DROP DATABASE IF EXISTS Boot;
CREATE DATABASE Boot;
USE Boot;

# 게시물 테이블 생성
CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    boardId SMALLINT(2) NOT NULL DEFAULT 1,
    memberId SMALLINT(2) NOT NULL DEFAULT 1,
    title CHAR(100) NOT NULL,
    `body` TEXT NOT NULL
);

# 게시물, 테스트 데이터 생성
INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목1 입니다.",
`body` = "내용1 입니다.",
memberId = 1;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목2 입니다.",
`body` = "내용2 입니다.",
memberId = 2;

INSERT INTO article
SET regDate = NOW(),
updateDate = NOW(),
title = "제목3 입니다.",
`body` = "내용3 입니다.",
memberId = 3;

 # 멤버 테이블 생성
 CREATE TABLE `member`(
id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
regDate DATETIME NOT NULL,
updateDate DATETIME NOT NULL,
loginId CHAR(50) NOT NULL UNIQUE,
loginPw CHAR(200) NOT NULL,
`name` CHAR(50) NOT NULL,
nickname CHAR(50) NOT NULL,
email CHAR(50) NOT NULL,
phoneNumber CHAR(50) NOT NULL
 );
 
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'user1',
loginPw = 'user1',
`name` = '유저1',
nickname = '유저1',
email = 'nwh0716@gmail.com',
phoneNumber = '010-0101-0101';
 
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'user2',
loginPw = 'user2',
`name` = '유저2',
nickname = '유저2',
email = 'nwh0716@gmail.com',
phoneNumber = '010-0101-0101';
 
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'user3',
loginPw = 'user3',
`name` = '유저3',
nickname = '유저3',
email = 'nwh0716@gmail.com',
phoneNumber = '010-0101-0101';

# 테스트 데이터 생성용 쿼리

INSERT INTO article
(regDate, updateDate, memberId, title, `body`)
SELECT NOW(), NOW(), FLOOR(RAND() * 2) + 1, CONCAT('제목_', FLOOR(RAND() * 1000) + 1), CONCAT('내용_', FLOOR(RAND() * 1000) + 1)
FROM article;

INSERT INTO article
(regDate, updateDate, memberId, title, `body`)
SELECT NOW(), NOW(), FLOOR(RAND() * 2) + 1, CONCAT('제목_', FLOOR(RAND() * 1000) + 1), CONCAT('내용_', FLOOR(RAND() * 1000) + 1)
FROM article;

INSERT INTO article
(regDate, updateDate, memberId, title, `body`)
SELECT NOW(), NOW(), FLOOR(RAND() * 2) + 1, CONCAT('제목_', FLOOR(RAND() * 1000) + 1), CONCAT('내용_', FLOOR(RAND() * 1000) + 1)
FROM article;

INSERT INTO article
(regDate, updateDate, memberId, title, `body`)
SELECT NOW(), NOW(), FLOOR(RAND() * 2) + 1, CONCAT('제목_', FLOOR(RAND() * 1000) + 1), CONCAT('내용_', FLOOR(RAND() * 1000) + 1)
FROM article;

# 게시판 테이블 추가
CREATE TABLE board (
  id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
  regDate DATETIME NOT NULL,
  updateDate DATETIME NOT NULL,
  `code` CHAR(20) UNIQUE NOT NULL,
  `name` CHAR(20) UNIQUE NOT NULL
);

# 공지사항 게시판 추가
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'notice',
`name` = '공지사항';

# 자유 게시판 추가
INSERT INTO board
SET regDate = NOW(),
updateDate = NOW(),
`code` = 'free',
`name` = '자유';

# 기존 데이터를 랜덤하게 게시판 지정
UPDATE article
SET boardId = FLOOR(RAND() * 2) + 1;


# authKey 칼럼 추가
ALTER TABLE `member` ADD COLUMN authKey CHAR(80) NOT NULL AFTER loginPw;

# 기존 회원의 authKey 데이터 채우기
UPDATE `member`
SET authKey = CONCAT("authKey1__", UUID(), "__", RAND())
WHERE authKey = '';

# USER1의 authKey 지정
UPDATE `member`
SET authKey = CONCAT("authKey1__1")
WHERE id = 1;

# authKey 칼럼에 유니크 인덱스 추가
ALTER TABLE `Boot`.`member` ADD UNIQUE INDEX(`authKey`);

CREATE TABLE getFile (
  id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, # 번호
  regDate DATETIME DEFAULT NULL, # 작성날짜
  updateDate DATETIME DEFAULT NULL, # 갱신날짜
  delDate DATETIME DEFAULT NULL, # 삭제날짜
  delStatus TINYINT(1) UNSIGNED NOT NULL DEFAULT 0, # 삭제상태(0:미삭제,1:삭제)
  relTypeCode CHAR(50) NOT NULL, # 관련 데이터 타입(article, member)
  relId INT(10) UNSIGNED NOT NULL, # 관련 데이터 번호
  originFileName VARCHAR(100) NOT NULL, # 업로드 당시의 파일이름
  fileExt CHAR(10) NOT NULL, # 확장자
  typeCode CHAR(20) NOT NULL, # 종류코드 (common)
  type2Code CHAR(20) NOT NULL, # 종류2코드 (attatchment)
  fileSize INT(10) UNSIGNED NOT NULL, # 파일의 사이즈
  fileExtTypeCode CHAR(10) NOT NULL, # 파일규격코드(img, video)
  fileExtType2Code CHAR(10) NOT NULL, # 파일규격2코드(jpg, mp4)
  fileNo SMALLINT(2) UNSIGNED NOT NULL, # 파일번호 (1)
  fileDir CHAR(20) NOT NULL, # 파일이 저장되는 폴더명
  PRIMARY KEY (id),
  KEY relId (relId,relTypeCode,typeCode,type2Code,fileNo)
); 

SELECT *
FROM `member`
ORDER BY id DESC;