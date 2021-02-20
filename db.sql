# 데이터베이스 생성
DROP DATABASE IF EXISTS Boot;
CREATE DATABASE Boot;
USE Boot;

# 게시물 테이블 생성
CREATE TABLE article (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    updateDate DATETIME NOT NULL,
    title CHAR(100) NOT NULL,
    `body` TEXT NOT NULL,
    memberId SMALLINT(2) NOT NULL DEFAULT 1
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
